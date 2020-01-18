package com.fetchrewards.contacts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MvpView, View.OnClickListener {

    private MainPresenter presenter;
    private ListView mListView;
    private int mEmployeeSelected;
    private static boolean listViewLoaded;
    private MyApplication myApplication;
    static private Map<String, Drawable> employeeSmallIcons;
    static private LayoutInflater inflater;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setClickable(true);

        myApplication = ((MyApplication) this.getApplication());
        myApplication.setListView(mListView);
        myApplication.setContext(this);
        mContext = this;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = mListView.getItemAtPosition(position);
                mEmployeeSelected = position;
                myApplication.setEmployeeId(Integer.toString(position));
                showEmployeeDetail();
            }
        });

        presenter = new MainPresenter(this);
        presenter.setContext(this);
        employeeSmallIcons = new HashMap<String, Drawable>();
        inflater = this.getLayoutInflater();

        ((MyApplication) this.getApplication()).setPresenter(presenter);
        new DownloadFilesTask(this).execute("one", "two", "three");
    }

    private void showEmployeeDetail() {
        Intent i = new Intent(this, EmployeeDetailActivity.class);
        i.putExtra(EmployeeDetailActivity.ID, Integer.toString(mEmployeeSelected));
        startActivity(i);
    }

    static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable bitmapDrawable = Drawable.createFromStream(is, "src name");
            Drawable bitmap = bitmapDrawable.getCurrent();
            return bitmapDrawable;
        } catch (Exception e) {
            System.out.println("error in loadImageFromWebOperations: " +e);
            return mContext.getResources().getDrawable(R.drawable.user_icon_small);
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        private List<Repository.FetchNameList> fetchNameList;

        public DownloadFilesTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading data, please wait.");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            for (int i = 0; i < 30; i++) {
                try {
                    if (presenter.getDataLoaded()) {
                        fetchNameList = presenter.getNameList();
                        if (listViewLoaded) {
                            return null;
                        }
                        loadListView(fetchNameList);
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            mListView.setAdapter(myApplication.getmAdapter());
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        private void loadListView(List<Repository.FetchNameList> employeeList) {
            String[] listName = new String[employeeList.size()];
            String[] listId = new String[employeeList.size()];
            String[] listItem = new String[employeeList.size()];
            List<View> customList = new ArrayList<View>();

            for (int x = 0; x < employeeList.size(); x++) {
                Repository.FetchNameList employee = employeeList.get(x);
                listName[x] = employee.name;
                listId[x] = employee.listId;
                listItem[x] = employee.name;
            }

            for (int x=0; x<employeeList.size(); x++) {
                View rowView = inflater.inflate(R.layout.list_single, null, false);
                customList.add(rowView);
                String employeeId = myApplication.getEmployeeIdByPosition(Integer.toString(x));
            }

            myApplication.setCustomList(customList);
            CustomList adapter = new CustomList(myApplication,MainActivity.this, listName, listId, listItem);
            myApplication.setListViewAdapter(adapter);
            listViewLoaded = true;
        }
    }

    public void loadListView2(List<Repository.Employee> employeeList) {
        String[] listNames = new String[employeeList.size()];
        String[] listCompany = new String[employeeList.size()];
        Integer[] listImages = new Integer[employeeList.size()];
        String[] listFavorites = new String[employeeList.size()];
        List<View> customList = new ArrayList<View>();

        int imageId = getResources().getIdentifier("user_icon_small", "drawable", getPackageName());

        for (int x=0; x<employeeList.size(); x++) {
            Repository.Employee employee = employeeList.get(x);
            listNames[x] = employee.name;
            listCompany[x] = employee.companyName;

            listImages[x] = imageId;
            listFavorites[x] = employee.isFavorite;
            myApplication.setEmployeeIdByPosition(Integer.toString(x), employee.id);

            View rowView = inflater.inflate(R.layout.list_single, null, false);
            customList.add(rowView);
        }
        myApplication.setCustomList(customList);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.name)
            showEmployeeDetail();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
