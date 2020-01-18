package com.fetchrewards.contacts;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Map;

public interface EmployeeModel {
    List<Repository.Employee> getEmployeeList();
    List<Repository.FetchNameList> getNameList();
    Map<String,Repository.Employee> getEmployeeMap();
    Map<String,Drawable> getEmployeeIconMap();
    void setEmployeeList();
    Repository.Employee getEmployee(String employeeId);
    void setContext(Context context);
}
