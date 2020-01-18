package com.fetchrewards.contacts;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {

    private Activity context;
    private final String[] names;
    private final String[] company;
    private final String[] favorite;
    private MainPresenter presenter;
    private MyApplication mMyApplication;
    private int mOtherContactStartingPosition = 0;

    public CustomList(MyApplication myApplication, Activity context, String[] names, String[] company, String[] favorite) {
        super(context, R.layout.list_single, names);
        this.context = context;
        this.names = names;
        this.company = company;
        this.favorite = favorite;
        mMyApplication = myApplication;
        presenter = mMyApplication.getPresenter();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = mMyApplication.getCustomList(position);
        TextView txtName = (TextView) rowView.findViewById(R.id.contact_name);
        TextView txtCompany = (TextView) rowView.findViewById(R.id.contact_company);
        txtName.setText(names[position]);
        txtCompany.setText(company[position]);
        return rowView;
    }
}
