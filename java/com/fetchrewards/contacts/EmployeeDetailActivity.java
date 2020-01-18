package com.fetchrewards.contacts;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeDetailActivity extends AppCompatActivity {

    private MainPresenter presenter;
    private static final String packageName = "com.com.fetchrewards.com.fetchrewards.com.fetchrewards.contacts";
    private ImageView contactImage;

    private MyApplication myApplication;

    private static boolean mIsFavorite;
    static private Drawable contactImageDrawable;
    static private String contactImageURL;
    static private Resources mResources;

    private static final int START_DOWNLOAD_MSG = 1001;
    private static final int UPDATE_PROGRESS_MSG = 2002;
    private static final int TOGGLE_START_BTN_MSG = 3003;
    private static final int DOWNLOAD_COMPLETED = 3004;
    private static final int ALL_DONE = 3005;

    static private Handler mBackgroundHandler;
    static private Handler mForegroundHandler;

    private MyCallback mCallback = new MyCallback();

    public static final String ID = packageName + ".EmployeeDetailActivity.ID";
    private static final String TAG = "EmployeeDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResources = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        TextView name = this.findViewById(R.id.name);
        contactImage = this.findViewById(R.id.contact_image);
        TextView company = this.findViewById(R.id.company);

        TextView phone = this.findViewById(R.id.phone);
        TextView phoneType = this.findViewById(R.id.phoneType);
        TextView  phoneLiteral= findViewById(R.id.phoneLiteral);

        TextView phone2 = this.findViewById(R.id.phone2);
        TextView phoneType2 = this.findViewById(R.id.phoneType2);
        TextView phoneLiteral2 = findViewById(R.id.phoneLiteral2);

        TextView phone3 = this.findViewById(R.id.phone3);
        TextView phoneType3 = this.findViewById(R.id.phoneType3);
        TextView phoneLiteral3 = findViewById(R.id.phoneLiteral3);

        TextView address1 = findViewById(R.id.addressLine1);
        TextView address2 = findViewById(R.id.addressLine2);
        TextView addressLiteral = findViewById(R.id.addressLiteral);

        TextView birthday = findViewById(R.id.birthday);
        TextView birthdayLiteral = findViewById(R.id.birthdayLiteral);

        TextView email = findViewById(R.id.email);
        TextView emailLiteral = findViewById(R.id.emailLiteral);
        myApplication = ((MyApplication) this.getApplication());

        String positionSelected = getIntent().getStringExtra(ID);
        presenter = ((MyApplication)this.getApplication()).getPresenter();
        String employeeId = myApplication.getEmployeeIdByPosition(positionSelected);
        Repository.Employee employeeData = presenter.getModel().getEmployee(employeeId);
        contactImageURL = employeeData.largeImageURL;

        //these parms are in pixels
        int imageHeight = contactImage.getHeight();

        ConstraintLayout.LayoutParams parms = (ConstraintLayout.LayoutParams)contactImage.getLayoutParams();

        parms = (ConstraintLayout.LayoutParams)name.getLayoutParams();
        int nameTopMargin = parms.topMargin;
        name.setText(employeeData.name);

        parms = (ConstraintLayout.LayoutParams)company.getLayoutParams();
        int companyTopMargin = parms.topMargin;
        company.setText(employeeData.companyName);

        new DownloadContactImageTask(this).execute("one", "two", "three");

        parms = (ConstraintLayout.LayoutParams)phone.getLayoutParams();
        int phoneTopMargin = parms.topMargin;
        int verticalIncrement = 70;
        int verticalIncrement2 = 30;
        int verticalIncrement3 = 50;
        parms = (ConstraintLayout.LayoutParams)phoneType.getLayoutParams();
        int phoneTypeTopMargin = parms.topMargin;
        parms = (ConstraintLayout.LayoutParams)phoneLiteral.getLayoutParams();
        int phoneLiteralTopMargin = parms.topMargin;

        if (employeeData.phone.home != null && employeeData.phone.home.length() > 0) {
            phone.setText(employeeData.phone.home);
            phoneType.setText("Home");
            phone.setVisibility(View.VISIBLE);
            phoneType.setVisibility(View.VISIBLE);
            phoneLiteral.setText("PHONE");
            phoneLiteral.setVisibility(View.VISIBLE);
            phoneTopMargin += verticalIncrement;
            phoneTypeTopMargin += verticalIncrement;
            phoneLiteralTopMargin += verticalIncrement;
        }

        if (employeeData.phone.work != null && employeeData.phone.work.length() > 0) {
            phone2.setText(employeeData.phone.work);
            phoneType2.setText("Work");
            phone2.setVisibility(View.VISIBLE);
            phoneType2.setVisibility(View.VISIBLE);
            phoneLiteral2.setText("PHONE");
            phoneLiteral2.setVisibility(View.VISIBLE);

            parms = (ConstraintLayout.LayoutParams)phone2.getLayoutParams();
            parms.topMargin = phoneTopMargin;
            parms = (ConstraintLayout.LayoutParams)phoneType2.getLayoutParams();
            parms.topMargin = phoneTypeTopMargin;
            parms = (ConstraintLayout.LayoutParams)phoneLiteral2.getLayoutParams();
            parms.topMargin = phoneLiteralTopMargin;

            phoneTopMargin += verticalIncrement;
            phoneTypeTopMargin += verticalIncrement;
            phoneLiteralTopMargin += verticalIncrement;
        }

        if (employeeData.phone.mobile != null && employeeData.phone.mobile.length() > 0) {
            phone3.setText(employeeData.phone.mobile);
            phoneType3.setText("Mobile");
            phone3.setVisibility(View.VISIBLE);
            phoneType3.setVisibility(View.VISIBLE);
            phoneLiteral3.setText("PHONE");
            phoneLiteral3.setVisibility(View.VISIBLE);

            parms = (ConstraintLayout.LayoutParams)phone3.getLayoutParams();
            parms.topMargin = phoneTopMargin;
            parms = (ConstraintLayout.LayoutParams)phoneType3.getLayoutParams();
            parms.topMargin = phoneTypeTopMargin;
            parms = (ConstraintLayout.LayoutParams)phoneLiteral3.getLayoutParams();
            parms.topMargin = phoneLiteralTopMargin;

            phoneTopMargin += verticalIncrement;
        }

        if (employeeData.address.street != null && employeeData.address.street.length() > 0) {
            address1.setText(employeeData.address.street);
            addressLiteral.setText("ADDRESS");

            String addressLine2 = employeeData.address.city + ", " + employeeData.address.state + " " + employeeData.address.zipCode + ", " + employeeData.address.country;
            address2.setText(addressLine2);
            parms = (ConstraintLayout.LayoutParams)addressLiteral.getLayoutParams();
            parms.topMargin = phoneTopMargin;
            parms = (ConstraintLayout.LayoutParams)address1.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement2;
            parms = (ConstraintLayout.LayoutParams)address2.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement2;
        }

        if (employeeData.birthdate != null && employeeData.birthdate.length() > 0) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date formattedDate = fmt.parse(employeeData.birthdate);
                SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM d, yyyy");
                String formattedBirthday = fmtOut.format(formattedDate);
                birthday.setText(formattedBirthday);
            } catch (Exception e) {
                birthday.setText(employeeData.birthdate);
            }
            birthdayLiteral.setText("BIRTHDATE");

            parms = (ConstraintLayout.LayoutParams)birthdayLiteral.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement3;
            parms = (ConstraintLayout.LayoutParams)birthday.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement2;
        }

        if (employeeData.emailAddress != null && employeeData.emailAddress.length() > 0) {
            email.setText(employeeData.emailAddress);
            emailLiteral.setText("EMAIL");

            parms = (ConstraintLayout.LayoutParams)emailLiteral.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement3;
            parms = (ConstraintLayout.LayoutParams)email.getLayoutParams();
            parms.topMargin = phoneTopMargin += verticalIncrement2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            contactImage.setImageDrawable(contactImageDrawable);
        } catch (Exception e) {
            Log.e(TAG,"OnResume", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        String employeeSelected = myApplication.getEmployeeId();
        String employeeId  = myApplication.getEmployeeIdByPosition(employeeSelected);
        String isFavorite = presenter.getModel().getEmployee(employeeId).isFavorite;
        if (isFavorite.equals("true")) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.favorite_icon_true));
            mIsFavorite = true;
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.favorite_icon_false_outline));
            mIsFavorite = false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            String employeeSelected = myApplication.getEmployeeId();
            String employeeId  = myApplication.getEmployeeIdByPosition(employeeSelected);
            if (mIsFavorite) {
                presenter.getModel().getEmployee(employeeId).isFavorite = "false";
                item.setIcon(getResources().getDrawable(R.drawable.favorite_icon_false_outline));
            } else {
                presenter.getModel().getEmployee(employeeId).isFavorite = "true";
                item.setIcon(getResources().getDrawable(R.drawable.favorite_icon_true));
            }
            mIsFavorite = !mIsFavorite;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable bitmapDrawable = Drawable.createFromStream(is, "src name");
            Drawable bitmap = bitmapDrawable.getCurrent();
            return bitmapDrawable;
        } catch (Exception e) {
            Log.e(TAG,"LoadImageFromWeb", e);
            return mResources.getDrawable( R.drawable.user_icon_large );
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("back button pressed");
        presenter.getModel().setEmployeeList();
        MainActivity mainActivity = (MainActivity)myApplication.getContext();
        mainActivity.loadListView2(presenter.getModel().getEmployeeList());
        super.onBackPressed();
    }

    private class DownloadContactImageTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;
        private MyCallback mCallback = new MyCallback();

        public void handleMessage(Message message) {
            System.out.println("handle message reached");
        }

        public DownloadContactImageTask(EmployeeDetailActivity activity) {
            Log.i(TAG,"DownloadContactImage");
        }

        @Override
        protected void onPreExecute() {
            //dialog.setMessage("Loading data, please wait.");
            //dialog.show();
            HandlerThread handlerThread = new HandlerThread("BackgroundThread");
            handlerThread.start();
            contactImageDrawable = null;
            mForegroundHandler = new Handler(handlerThread.getLooper(), mCallback);
            mBackgroundHandler  = new Handler(mCallback);
            mBackgroundHandler.obtainMessage(START_DOWNLOAD_MSG, this).sendToTarget();
            Log.i(TAG,"onPreExecute called");
        }

        @Override
        protected Void doInBackground(String... params) {
            for (int i = 0; i < 10; i++) {
                try {
                        while (contactImageDrawable == null) {
                            contactImageDrawable = LoadImageFromWebOperations(contactImageURL);
                            Log.i(TAG, "doInBackground called");
                            if (contactImageDrawable != null) {
                                mBackgroundHandler.obtainMessage(DOWNLOAD_COMPLETED, this).sendToTarget();
                                return null;
                            }
                        }
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return null;
        }
    }

    class MyCallback implements Handler.Callback {

        public MyCallback() {
            System.out.println("constructor called!");
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case START_DOWNLOAD_MSG:
                    break;
                case UPDATE_PROGRESS_MSG:
                    break;
                case TOGGLE_START_BTN_MSG:
                    break;
                case DOWNLOAD_COMPLETED:
                    contactImage.setImageDrawable(contactImageDrawable);
                    break;
            }
            return true;
        }
    }
}
