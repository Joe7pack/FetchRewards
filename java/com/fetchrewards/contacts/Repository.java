package com.fetchrewards.contacts;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.CompareToBuilder;
import java.util.Iterator;
import java.util.ArrayList;

public class Repository implements EmployeeModel {

    private JSONObject employeeObject;
    private String firstName;
    private String lastName;
    private String phone;
    static private String url = "https://api.jsonbin.io/b/5e0f707f56e18149ebbebf5f";
    static private List<Employee> employeeList;
    static private List<FetchNameList> nameList;
    static private Map<String, Employee> employeeMap;
    static private Map<String, Drawable> employeeIconMap;
    static private Context mContext;
    static private boolean mDataLoaded = false;
    static Map<String, Drawable> employeeSmallIcons;

    public Repository(Context context) {
        mContext = context;
        loadJSONData();
    }

    private void loadJSONData() {
        try {
            new JsonTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } catch (Exception e) {
            System.out.println("Error creating JSON Employee list: " + e);
        }
    }

    public boolean getDataLoaded() {
        return mDataLoaded;
    }

    private static void setDataLoaded(boolean dataLoaded) {
        mDataLoaded = dataLoaded;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    @Override
    public List<FetchNameList> getNameList() {
        return nameList;
    }

    @Override
    public Employee getEmployee(String employeeId) {
        if (employeeMap.containsKey(employeeId)) {
            return employeeMap.get(employeeId);
        } else {
            return null;
        }
    }

    static private void setEmployeeList(String jsonData) {
        try {
            Type collectionType = new TypeToken<List<Employee>>() {
            }.getType();
            employeeList = new Gson().fromJson(jsonData, collectionType);
            Collections.sort(employeeList);
            setEmployeeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEmployeeList() {
        Collections.sort(employeeList);
        setEmployeeMap();
    }

    static private void setEmployeeMap() {
        employeeMap = new HashMap<String, Employee>();
        for (Employee i : employeeList) employeeMap.put(i.getKey(), i);
        setDataLoaded(true);
    }

    @Override
    public Map<String, Employee> getEmployeeMap() {
        return employeeMap;
    }

    @Override
    public Map<String, Drawable> getEmployeeIconMap() {
        return employeeIconMap;
    }

    static private void setNameList(String jsonData) {
        try {
            Type collectionType = new TypeToken<List<FetchNameList>>() {
            }.getType();
            nameList = new Gson().fromJson(jsonData, collectionType);
            Collections.sort(nameList);
            List<FetchNameList> cloned_list = new ArrayList<FetchNameList>();
            cloned_list.addAll(nameList);

            for (Iterator<FetchNameList> iterator = cloned_list.iterator(); iterator.hasNext();) {
                FetchNameList workList = iterator.next();
                if(workList.name == null || workList.name.isEmpty()) {
                    iterator.remove();
                }
            }

            nameList = cloned_list;
            setDataLoaded(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load the Json list from jsonbin
    static private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                setNameList(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("GetEmployeesFromURL", "That didn't work!");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("secret-key", "$2b$10$Vr2RAD3mpzFZ6o8bPZNlgOOM0LmFLvN24IoxlELo3arTgNszX7otS");
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
        public class FetchNameList implements Comparable<FetchNameList> {
            String id;
            String listId;
            String name;

            @Override
            public int compareTo(FetchNameList otherNameList) {
                FetchNameList myClass = otherNameList;
                return new CompareToBuilder()
                        .append(this.listId, myClass.listId)
                        .append(this.name, myClass.name)
                        .toComparison();
            }
        }

        public class Employee implements Comparable<Employee> {
            String name;
            String id;
            String companyName;
            String isFavorite;
            String smallImageURL;
            String largeImageURL;
            String emailAddress;
            String birthdate;
            Phone phone;
            Address address;

            String getKey() {
                return id;
            }

            @Override
            public int compareTo(Employee otherEmployee) {
                StringBuilder thisNamePrefix = new StringBuilder();
                StringBuilder otherNamePrefix = new StringBuilder();

                if (this.isFavorite.equals("true")) {
                    thisNamePrefix.insert(0, "a");
                } else {
                    thisNamePrefix.insert(0, "b");
                }

                if (otherEmployee.isFavorite.equals("true")) {
                    otherNamePrefix.insert(0, "a");
                } else {
                    otherNamePrefix.insert(0, "b");
                }

                String valueHere = thisNamePrefix.toString() + this.name;
                String valueThere = otherNamePrefix.toString() + otherEmployee.name;
                return valueHere.compareTo(valueThere);
            }
        }

        public class Address {
            String street;
            String state;
            String city;
            String country;
            String zipCode;
        }

        public class Phone {
            String work;
            String home;
            String mobile;
        }
    }




