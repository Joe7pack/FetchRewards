package com.fetchrewards.contacts;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application {
    private MainPresenter mainPresenter;
    private Map<String, String> employeeIdsByPosition = new HashMap<String, String>();
    private String mEmployeeId;
    private ListView mListView;
    private List<Repository.Employee> employeeList;
    private CustomList mAdapter;
    private Context mContext;
    private List<View> mCustomList;
    private Map<String, Bitmap> mSmallIconBitmapMap;

    public int getEmployeeIdsByPositionSize() {
        return employeeIdsByPosition.size();
    }

    public Bitmap getSmallIconBitmapMap(String employeeId) {
        return mSmallIconBitmapMap.get(employeeId);
    }

    public void setSmallIconBitmapMap(Map<String, Bitmap> mSmallIconBitmapMap) {
        this.mSmallIconBitmapMap = mSmallIconBitmapMap;
    }

    private List<Bitmap> mSmallIconBitmapList;

    public MainPresenter getPresenter() {
        return mainPresenter;
    }

    public void setPresenter(MainPresenter presenter) {
        this.mainPresenter = presenter;
    }

    public String getEmployeeIdByPosition(String position) {
        return employeeIdsByPosition.get(position);
    }

    public void setEmployeeIdByPosition(String position, String employeeId) {
        employeeIdsByPosition.put(position, employeeId);
    }

    public String getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        mEmployeeId = employeeId;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    public void setListViewAdapter(CustomList adapter) {
        mAdapter = adapter;
    }

    public CustomList getmAdapter() {
        return mAdapter;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setCustomList(List<View> customList) {
        mCustomList = customList;
    }

    public View getCustomList(int position) {
        return mCustomList.get(position);
    }
}
