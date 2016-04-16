package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.BookingMaster;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingMasterViewHolder> {
    public boolean isItemAnimate = false;
    Context context;
    LayoutInflater inflater;
    View ConvertView;
    ArrayList<BookingMaster> alBookingMaster;
    BookingOnClickListener objBookingOnClickListener;
    int position;
    Date toDate, toTime, currentTime, currentDate;
    String today, strCurrentTime;
    SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    SimpleDateFormat sdfTime = new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US);

    public BookingAdapter(Context context, ArrayList<BookingMaster> result, BookingOnClickListener objBookingOnClickListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.alBookingMaster = result;
        this.objBookingOnClickListener = objBookingOnClickListener;
    }

    @Override
    public BookingMasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConvertView = inflater.inflate(R.layout.row_booking, parent, false);
        return new BookingMasterViewHolder(ConvertView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BookingMasterViewHolder holder, int position) {
        BookingMaster objBookingMaster = alBookingMaster.get(position);

        holder.txtDate.setText(String.valueOf(objBookingMaster.getToDate()));
        holder.txtTime.setText(String.valueOf(objBookingMaster.getFromTime()) + " To " + String.valueOf(objBookingMaster.getToTime()));
        holder.txtPersonName.setText(objBookingMaster.getBookingPersonName());
        if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.New.getValue()) {
            holder.txtBookingStatus.setText("(" + Globals.BookingStatus.New.toString() + ")");
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Modified.getValue()) {
            holder.txtBookingStatus.setText("(" + Globals.BookingStatus.Modified.toString() + ")");
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Confirmed.getValue()) {
            holder.txtBookingStatus.setText("(" + Globals.BookingStatus.Confirmed.toString() + ")");
        } else if (objBookingMaster.getBookingStatus() == Globals.BookingStatus.Cancelled.getValue()) {
            holder.txtBookingStatus.setText("(" + Globals.BookingStatus.Cancelled.toString() + ")");
            holder.ibCancelBooking.setVisibility(View.INVISIBLE);
        }

        if (objBookingMaster.getToDate() != null) {
            Calendar calendar = Calendar.getInstance();
            try {
                toDate = sdfDate.parse(objBookingMaster.getToDate());
                toTime = sdfTime.parse(objBookingMaster.getToTime());
                today = sdfDate.format(new Date());
                currentDate = sdfDate.parse(today);

                if (objBookingMaster.getBookingStatus() != Globals.BookingStatus.Cancelled.getValue()) {
                    if (toDate.compareTo(currentDate) < 0) {
                        holder.ibCancelBooking.setVisibility(View.INVISIBLE);
                    } else {
                        strCurrentTime = sdfTime.format(calendar.getTime());
                        currentTime = sdfTime.parse(strCurrentTime);
                        if (toDate.compareTo(currentDate) > 0) {
                            holder.ibCancelBooking.setVisibility(View.VISIBLE);
                        }else {
                            if (toTime.getTime() > currentTime.getTime()) {
                                holder.ibCancelBooking.setVisibility(View.VISIBLE);
                            } else {
                                holder.ibCancelBooking.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.alBookingMaster.size();
    }

    public void BookingDataChanged(ArrayList<BookingMaster> result) {
        alBookingMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void BookingDataChanged(BookingMaster objBookingMaster) {
        alBookingMaster.add(0,objBookingMaster);
        notifyDataSetChanged();
    }

    public void UpdateBookingStatus(int position) {
        alBookingMaster.get(position).setBookingStatus((short) Globals.BookingStatus.Cancelled.getValue());
        notifyDataSetChanged();
    }

    public interface BookingOnClickListener {
        void CancelClickListener(BookingMaster objBookingMaster, int position);
    }

    class BookingMasterViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtTime;
        TextView txtPersonName;
        TextView txtBookingStatus;
        ImageButton ibCancelBooking;

        public BookingMasterViewHolder(View view) {
            super(view);

            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtPersonName = (TextView) view.findViewById(R.id.txtPersonName);
            txtBookingStatus = (TextView) view.findViewById(R.id.txtStatus);
            ibCancelBooking = (ImageButton) view.findViewById(R.id.ibCancelBooking);

            ibCancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objBookingOnClickListener.CancelClickListener(alBookingMaster.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}

