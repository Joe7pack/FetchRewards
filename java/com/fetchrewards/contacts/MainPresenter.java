package com.fetchrewards.contacts;

import android.content.Context;
import java.util.List;

public class MainPresenter implements Presenter<MvpView> {

    private Context context;
    private Repository model;
    private MvpView mvpView;
    private List<Repository.Employee> employeeList;
    private List<Repository.FetchNameList> fetchNameList;

    public MainPresenter(Context context) {
        model = new Repository(context);
        setModel(model);
    }

    @Override
    public void attachView(MvpView view) {
        this.mvpView = view;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
    }

    @Override
    public List<Repository.Employee> getEmployeeList() {
        employeeList = getModel().getEmployeeList();
        return employeeList;
    }

    @Override
    public List<Repository.FetchNameList> getNameList() {
        fetchNameList = getModel().getNameList();
        return fetchNameList;
    }

    @Override
    public void setEmployeeList() {
        getModel().setEmployeeList();
    }

    public boolean getDataLoaded() {
        return getModel().getDataLoaded();
    }

    @Override
    public void setModel(Repository model) {
        this.model = model;
    }

    @Override
    public Repository getModel() {
        return this.model;
    }

}

