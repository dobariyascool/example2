package com.arraybit.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.arraybit.global.SpinnerItem;
import com.arraybit.abposw.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    LayoutInflater L_Inflater;

    ArrayList<SpinnerItem> lstSpinnerItem;
    boolean isAlignCenter;

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> result, boolean isAlignCenter) {
        this.context = context;
        this.lstSpinnerItem = result;
        this.L_Inflater = LayoutInflater.from(context);
        this.isAlignCenter = isAlignCenter;
    }

    @Override
    public int getCount() {
        return lstSpinnerItem.size();
    }

    @Override
    public Object getItem(int position) {
        return lstSpinnerItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = view;

        if (view == null) {
            view1 = L_Inflater.inflate(R.layout.row_spinner, viewGroup, false);
        }
        view1.setId(lstSpinnerItem.get(i).getValue());
        TextView txtview = (TextView) view1.findViewById(R.id.txtSpinnerItem);
        if(!isAlignCenter){
            txtview.setGravity(Gravity.START | Gravity.CENTER);
        }
        txtview.setText(lstSpinnerItem.get(i).getText());
        return view1;
    }

}