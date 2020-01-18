package com.fetchrewards.contacts;

import android.content.Context;
import org.json.JSONException;

import java.util.List;

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
    void setContext(Context context);
    Context getContext();
    List<Repository.Employee> getEmployeeList() throws JSONException;
    List<Repository.FetchNameList> getNameList() throws JSONException;
    void setEmployeeList();
    void setModel(Repository model);
    Repository getModel();
 }