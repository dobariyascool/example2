package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessHoursTran;
import com.arraybit.abposw.R;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WorkingHoursAdapter extends RecyclerView.Adapter<WorkingHoursAdapter.WorkingHoursViewHolder> {

    ArrayList<BusinessHoursTran> alBusinessHoursTran;
    Context context;
    View view;
    Date dt = null;
    SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH", Locale.US);
    private LayoutInflater inflater;

    public WorkingHoursAdapter(Context context, ArrayList<BusinessHoursTran> result) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alBusinessHoursTran = result;
    }

    @Override
    public WorkingHoursViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_workinghours, parent, false);
        return new WorkingHoursViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WorkingHoursViewHolder holder, int position) {

        BusinessHoursTran current = alBusinessHoursTran.get(position);

        try {
            dt = sdfTimeFormat.parse(current.getOpeningTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (position == 0) {
            holder.ivTimings.setVisibility(View.VISIBLE);
            holder.txtHeader.setVisibility(View.VISIBLE);
        } else {
            holder.ivTimings.setVisibility(View.GONE);
            holder.txtHeader.setVisibility(View.GONE);
        }

        //holder.txtDayOfWeek.setText(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue().charAt(0)).toUpperCase());
        holder.txtDayOfWeek.setText(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue().substring(0,3)).toUpperCase());
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            if (Globals.Days.valueOf("Day" + 0).getValue().equals(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue()))) {
                TextColorChange(holder,true);
            } else {
                TextColorChange(holder, false);
            }
        } else {
            if (Globals.Days.valueOf("Day" + calendar.get(Calendar.DAY_OF_WEEK)).getValue().equals(String.valueOf(Globals.Days.valueOf("Day" + current.getDayOfWeek()).getValue()))) {
                TextColorChange(holder,true);
            } else {
                TextColorChange(holder,false);
            }
        }

        if (current.getOpeningTime().equals("12:00 AM")) {
            holder.txtStartTime.setText("Close");
            holder.txtEndTime.setVisibility(View.GONE);
        } else {
            if (current.getBreakStartTime() == null && current.getBreakEndTime() == null) {
                holder.txtStartTime.setText(current.getOpeningTime() + " " + "To" + " " + current.getClosingTime());
                holder.txtEndTime.setVisibility(View.GONE);
            } else {
                holder.txtStartTime.setText(current.getOpeningTime() + " " + "To" + " " + current.getBreakStartTime());
                holder.txtEndTime.setText(current.getBreakEndTime() + " " + "To" + " " + current.getClosingTime());
            }
        }

    }

    @Override
    public int getItemCount() {
        return alBusinessHoursTran.size();
    }

    class WorkingHoursViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayOfWeek, txtStartTime, txtEndTime, txtHeader;
        LinearLayout layoutChild;
        ImageView ivTimings;

        public WorkingHoursViewHolder(View itemView) {
            super(itemView);

            ivTimings = (ImageView)itemView.findViewById(R.id.ivTimings);

            layoutChild = (LinearLayout) itemView.findViewById(R.id.layoutChild);
            txtDayOfWeek = (TextView) itemView.findViewById(R.id.txtDayOfWeek);
            txtStartTime = (TextView) itemView.findViewById(R.id.txtStartTime);
            txtEndTime = (TextView) itemView.findViewById(R.id.txtEndTime);
            txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);

        }
    }

    private void TextColorChange(WorkingHoursViewHolder holder,boolean isMatch){
        if(isMatch){
            holder.txtStartTime.setSelected(true);
            holder.txtEndTime.setSelected(true);
            holder.txtDayOfWeek.setSelected(true);
        }else{
            holder.txtStartTime.setSelected(false);
            holder.txtEndTime.setSelected(false);
            holder.txtDayOfWeek.setSelected(false);
        }
    }
}
