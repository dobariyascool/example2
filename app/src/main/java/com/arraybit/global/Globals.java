package com.arraybit.global;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.abposw.R;
import com.rey.material.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Globals {

    public static String DateFormat = "d/M/yyyy";
    public static String TimeFormat = "hh:mm";
    public static String DisplayTimeFormat = "hh:mm a";
    public static short linktoStateMasterId = 1;
    public static short linktoBusinessMasterId = 1;
    public static short linktoSourceMasterId = 2;
    static int y, M, d, H, m;

    public static void ShowDatePickerDialog(final EditText txtView, Context context) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
            }
        }

        y = c.get(Calendar.YEAR);
        M = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        y = year;
                        M = monthOfYear;
                        d = dayOfMonth;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
                        txtView.setText(sdfControl.format(cal.getTime()));
                    }

                }, y, M, d);

        dp.hide();
        dp.show();
    }


    public static boolean IsValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @SuppressLint("SimpleDateFormat")
    public static String GetCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h/m");
        return sdf.format(calendar.getTime());
    }

    public static void ShowSnackBar(View view, String message, Context context, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        TextView txt = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey));
        snackbar.show();
    }

    public static void SetItemAnimator(RecyclerView.ViewHolder holder) {
        //slide from bottom
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 200, 0);
        animatorTranslateY.setDuration(500);
        animatorTranslateY.start();
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg, RecyclerView recyclerView) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (isShow) {
            layout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            layout.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    //region Enum
    public enum Days {
        Day0("Sunday"),
        Day1("Monday"),
        Day2("Tuesday"),
        Day3("Wednesday"),
        Day4("Thursday"),
        Day5("Friday"),
        Day6("Saturday");

        private String intValue;

        Days(String value) {
            intValue = value;

        }

        public String getValue() {
            return intValue;
        }
    }
    //endregion

    //region Commented Code
    //    Picasso.with(holder.ivOffer.getContext()).load(objOfferMaster.getImagePhysicalName()).into(new Target() {
//        @Override
//        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                public void onGenerated(Palette p) {
//                    // Use generated instance
//                    holder.ivOffer.setImageBitmap(bitmap);
//                    holder.titleLayout.setBackgroundColor(p.getDarkMutedColor(0));
//                }
//            });
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//        }
//    });

    //        visibleItemCount = recyclerView.getChildCount();
//        totalItemCount = mLinearLayoutManager.getItemCount();
//        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }
//        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//            // End has been reached
//            if (firstVisibleItem == 0 && visibleItemCount >= 10 && totalItemCount >= 10) {
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            } else if(visibleItemCount == totalItemCount){
//                loading = false;
//            }
//            else{
//                current_page++;
//                onLoadMore(current_page);
//                loading = true;
//            }
//        }
    //endregion
}
