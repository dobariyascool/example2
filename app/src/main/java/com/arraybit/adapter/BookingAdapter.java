package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.BookingMaster;
import com.rey.material.widget.Button;
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
            holder.btnCancelBooking.setVisibility(View.GONE);
        }

        if (objBookingMaster.getToDate() != null) {
            SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
            SimpleDateFormat sdfTime = new SimpleDateFormat(Globals.TimeFormat, Locale.US);
            Calendar calendar = Calendar.getInstance();
            try {
                toDate = sdfDate.parse(objBookingMaster.getToDate());
                toTime = sdfTime.parse(objBookingMaster.getToTime());
                today = sdfDate.format(new Date());
                currentDate = sdfDate.parse(today);

                if(objBookingMaster.getBookingStatus() != Globals.BookingStatus.Cancelled.getValue()) {
                    if (toDate.compareTo(currentDate) < 0) {
                        holder.btnCancelBooking.setVisibility(View.GONE);
                    } else {
                        strCurrentTime = sdfTime.format(calendar.getTime());
                        currentTime = sdfTime.parse(strCurrentTime);
                        if (toTime.getTime() > currentTime.getTime()) {
                            holder.btnCancelBooking.setVisibility(View.VISIBLE);
                        } else {
                            holder.btnCancelBooking.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        try {
//
//            objBookingMaster.setFromTime(new SimpleDateFormat("HH:mm:ss", Locale.US).format(time));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        if (position > previousPosition) {
//            AnimationUtils.animate(holder, true);
//        } else {
//            AnimationUtils.animate(holder, false);
//        }
//        previousPosition = position;
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
        Button btnCancelBooking;

        public BookingMasterViewHolder(View view) {
            super(view);

            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtPersonName = (TextView) view.findViewById(R.id.txtPersonName);
            txtBookingStatus = (TextView) view.findViewById(R.id.txtStatus);
            btnCancelBooking = (Button) view.findViewById(R.id.btnCancelBooking);
//            ConvertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context.getApplicationContext(), BookingActivity.class);
//                    intent.putExtra("BookingMasterId", txtBookingMasterId.getText().toString());
//                    ((Activity) context).startActivityForResult(intent, ADDEDIT);
//                }
//            });

            //btnDelete = (ImageButton)view.findViewById(R.id.btnDelete);

//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    position = (int)btnDelete.getTag();
//                    ConfirmDeleteBookingMaster();
//                }
//            });
            btnCancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objBookingOnClickListener.CancelClickListener(alBookingMaster.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

//        public void ConfirmDeleteBookingMaster()
//        {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//            alertDialogBuilder
//                    .setMessage(context.getResources().getString(R.string.MsgConfirmDelete))
//                    .setCancelable(false)
//                    .setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    if (Service.CheckNet(context.getApplicationContext())) {
//                                        new DeleteBookingMasterAsyncTask().execute();
//                                    } else {
//                                        Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.MsgCheckConnection), Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            })
//                    .setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//
//            alertDialog.show();
//        }

//        private class DeleteBookingMasterAsyncTask extends AsyncTask<String, Integer, String> {
//            ProgressDialog pDialog;
//
//            @Override
//            protected void onPreExecute() {
//                pDialog = new ProgressDialog(context);
//                pDialog.setMessage(context.getResources().getString(R.string.MsgLoading));
//                pDialog.setIndeterminate(false);
//                pDialog.setCancelable(false);
//                pDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(String...arg) {
//                BookingJSONParser jsonParser = new BookingJSONParser();
//                //return jsonParser.DeleteBookingMaster(btnDelete.getId());
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                if (result != null && result.equals("0")) {
//                    Toast.makeText(context, context.getResources().getString(R.string.MsgDeleteSuccess), Toast.LENGTH_LONG).show();
//                    lstBookingMaster.remove(position);
//                    notifyDataSetChanged();
//                } else {
//                    Toast.makeText(context, context.getResources().getString(R.string.MsgDeleteFail), Toast.LENGTH_LONG).show();
//                }
//                pDialog.dismiss();
//            }
//        }
    }
}

