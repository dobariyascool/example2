package com.arraybit.global;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.arraybit.abposw.R;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static short linktoBusinessTypeMasterId = 1;
    public static short CustomerType = 3;
    public static short linktoSourceMasterId = 4;
    public static short linktoOrderTypeMasterId = 0;
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    public static int counter = 0;
    public static ArrayList<ItemMaster> alOrderItemTran = new ArrayList<>();
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


    public static void ShowTimePickerDialog(final EditText txtView, final Context context) {
        final Calendar c = Calendar.getInstance();

        if (!txtView.getText().toString().equals("")) {
            SimpleDateFormat sdfControl = new SimpleDateFormat(TimeFormat, Locale.US);
            try {
                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
                c.setTime(dt);
            } catch (ParseException ignored) {
            }
        }

        H = c.get(Calendar.HOUR_OF_DAY);
        m = c.get(Calendar.MINUTE);

        TimePickerDialog dp = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        H = hourOfDay;
                        m = minute;

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, 0);
                        cal.set(Calendar.MONTH, 0);
                        cal.set(Calendar.DAY_OF_MONTH, 0);
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        SimpleDateFormat sdfControl = new SimpleDateFormat(TimeFormat, Locale.US);
                        txtView.setText(sdfControl.format(cal.getTime()));

                    }

                }, H, m, true);

        dp.hide();
        dp.show();
    }

    public static void HideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void ClearCartData() {
        counter = 0;
        alOrderItemTran = new ArrayList<>();
    }

    public static void ClearUserPreference(Context context, Activity activity) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("LoginPreference", "CustomerMasterId", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserName", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserPassword", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "CustomerName", context);
        objSharePreferenceManage.ClearPreference("LoginPreference", context);
        ClearCartData();
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

    public static void ReplaceFragment(Fragment fragment, FragmentManager fragmentManager, String fragmentName, int layoutId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    public static void SetNavigationDrawer(ActionBarDrawerToggle actionBarDrawerToggle, final Context context, final DrawerLayout drawerLayout, Toolbar app_bar) {
        actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) context, drawerLayout, app_bar,
                R.string.navOpen, R.string.navClose) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final ActionBarDrawerToggle finalActionBarDrawerToggle = actionBarDrawerToggle;
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                finalActionBarDrawerToggle.syncState();
            }
        });
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

    public enum OptionValue {
        Veg(7),
        NonVeg(6),
        Jain(5);

        private int intValue;

        OptionValue(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum QuestionType {
        Input(1),
        Rating(2),
        SingleSelect(3),
        MultiSelect(4),
        Simple_Feedback(5),
        Null_Group(6);

        private int intValue;

        QuestionType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum FeedbackType {
        OtherQuery(1),
        BugReport(2),
        Suggestion(3);

        private int intValue;

        FeedbackType(int value) {
            intValue = value;

        }

        public int getValue() {
            return intValue;
        }

    }

    public enum OrderType {
        DineIn(1),
        TakeAway(2),
        HomeDelivery(3);

        private int intValue;

        OrderType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum BookingStatus {
        New (1),
        Modified (2),
        Confirmed (3),
        Canceled (4);

        private int intValue;

        BookingStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }
    //endregion

    //region Rating
    //    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
//    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//    try {
//        context.startActivity(goToMarket);
//    } catch (ActivityNotFoundException e) {
//        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
    //endregion

    //region Share
    //    Uri imageUri = Uri.parse("android.resource://com.arraybit.abposw/drawable/" + R.mipmap.app_logo);
//    Intent i = new Intent(Intent.ACTION_SEND);
//    i.setType("image/*");
//    i.putExtra(Intent.EXTRA_STREAM, imageUri);
//    i.putExtra(Intent.EXTRA_TEXT, "This is the very good app");
//    Intent chooser = Intent.createChooser(i, "Tell a Friend");
//    startActivity(chooser);
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
