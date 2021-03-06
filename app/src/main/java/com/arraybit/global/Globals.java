package com.arraybit.global;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.arraybit.abposw.R;
import com.arraybit.modal.ItemMaster;
import com.facebook.FacebookRequestError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rey.material.widget.EditText;

import java.io.File;
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
    public static short CustomerType = 2;
    public static short linktoSourceMasterId = 4;
    public static short linktoOrderTypeMasterId = 0;
    public static DecimalFormat dfWithPrecision = new DecimalFormat("0.00");
    public static DecimalFormat dfWithOnePrecision = new DecimalFormat("0.0");
    public static DecimalFormat dfWithoutPrecision = new DecimalFormat("0");
    public static int counter = 0;
    public static ArrayList<ItemMaster> alOrderItemTran = new ArrayList<>();
    static int y, M, d, H, m;
    static GoogleApiClient googleApiClient;

    public static void ShowDatePickerDialog(final EditText txtView, Context context, final boolean IsPreventPreviousDateRequest) {
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

                        Calendar calendar = Calendar.getInstance();

                        Date date = new Date(view.getMinDate());
                        calendar.setTime(date);

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
                        if (IsPreventPreviousDateRequest) {
                            if (d >= calendar.get(Calendar.DAY_OF_MONTH) || M >= calendar.get(Calendar.MONTH) || y >= calendar.get(Calendar.YEAR)) {
                                txtView.setText(sdfControl.format(cal.getTime()));
                            } else {
                                txtView.setText(sdfControl.format(new Date()));
                            }
                        } else {
                            txtView.setText(sdfControl.format(cal.getTime()));
                        }
                    }

                }, y, M, d);
        if (IsPreventPreviousDateRequest) {
            dp.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
        }
        dp.hide();
        dp.show();
    }

    public static double CalculateAverageRating(double totalReview) {
        //Sum of (weight * number of reviews at that weight) / total number of reviews
        //double review = (5 * 252 + 4 * 124 + 3 * 40 + 2 * 29 + 1 * 33)/totalReview;
        return (5 * 252 + 4 * 124 + 3 * 40 + 2 * 29 + 33) / totalReview;
    }

    public static void ShowTimePickerDialog(final EditText txtView, final Context context, final boolean IsPreventPastTimeRequest) {
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
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, 0);
                        cal.set(Calendar.MONTH, 0);
                        cal.set(Calendar.DAY_OF_MONTH, 0);
                        if (IsPreventPastTimeRequest) {
                            if (hourOfDay >= H && minute >= m) {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                            } else if (hourOfDay > H && minute < m) {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                            }
                        } else {
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal.set(Calendar.MINUTE, minute);
                        }
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
        Globals.linktoOrderTypeMasterId = 0;
    }

    public static void ClearUserPreference(final Context context, final Activity activity) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();

        objSharePreferenceManage.RemovePreference("LoginPreference", "CustomerMasterId", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserName", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "UserPassword", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "CustomerName", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "Phone", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "CustomerProfileUrl", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "BusinessMasterId", context);
        objSharePreferenceManage.RemovePreference("LoginPreference", "IntegrationId", context);
        objSharePreferenceManage.ClearPreference("LoginPreference", context);

        ClearCartData();
    }


    public static boolean IsValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String GetCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy/h/m/s", Locale.US);
        return simpleDateFormat.format(calendar.getTime());
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


    public static void SelectImage(final Context context, final int requestCodeCamera, final int requestCodeGallery) {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("ADD PHOTO");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    ((Activity) context).startActivityForResult(intent, requestCodeCamera);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((Activity) context).startActivityForResult(
                            Intent.createChooser(intent, "Select File"), requestCodeGallery);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void SetErrorLayout(LinearLayout layout, boolean isShow, String errorMsg, RecyclerView recyclerView, int errorIcon) {
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtMsg);
        ImageView ivErrorIcon = (ImageView) layout.findViewById(R.id.ivErrorIcon);
        if (errorIcon != 0) {
            ivErrorIcon.setImageResource(errorIcon);
        }
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

    public static void SetScaleImageBackground(final Context context, final LinearLayout linearLayout, final RelativeLayout relativeLayout, final FrameLayout frameLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbackground);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        if (relativeLayout != null) {
            relativeLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (frameLayout != null) {
            frameLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (linearLayout != null) {
            linearLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        }
    }

    public static void SetScaleImageBackground(final Context context, final CoordinatorLayout coordinatorLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mainbackground);
//        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        coordinatorLayout.setBackground(new BitmapDrawable(context.getResources(), originalBitmap));
    }

    public static void SetHomePageBackground(final Context context, final DrawerLayout drawerLayout, final LinearLayout linearLayout, final RelativeLayout relativeLayout, final FrameLayout frameLayout) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels);

        if (relativeLayout != null) {
            relativeLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (frameLayout != null) {
            frameLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (linearLayout != null) {
            linearLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
        } else if (drawerLayout != null) {
            drawerLayout.setBackground(new BitmapDrawable(context.getResources(), resizeBitmap));
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
        Veg(4),
        NonVeg(5),
        Jain(6),
        Spicy(1),
        Sweet(3),
        DoubleSpicy(2);


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
        New(1),
        Modified(2),
        Confirmed(3),
        Cancelled(4);

        private int intValue;

        BookingStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum OrderStatus {
        Cooking(1),
        Ready(2),
        Served(3),
        Cancelled(4),
        Left(5),
        Delivered(6);

        private int intValue;

        OrderStatus(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum AddressType {
        Home(1),
        Office(2);

        private int intValue;

        AddressType(int value) {
            intValue = value;
        }

        public int getValue() {
            return intValue;
        }
    }

    public enum BannerType {
        Item(1),
        Offer(2),
        Category(3),
        General(4);

        private int intValue;

        BannerType(int value) {
            intValue = value;
        }

        public int getValue() {
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

    //    public static void ShowDatePickerDialog(final EditText txtView, Context context, final boolean IsPreventPreviousDateRequest) {
//        final Calendar c = Calendar.getInstance();
//
//        if (!txtView.getText().toString().equals("")) {
//            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
//            try {
//                Date dt = sdfControl.parse(String.valueOf(txtView.getText()));
//                c.setTime(dt);
//            } catch (ParseException ignored) {
//            }
//        }
//
//        y = c.get(Calendar.YEAR);
//        M = c.get(Calendar.MONTH);
//        d = c.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog dp = new DatePickerDialog(context,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        Calendar calendar = Calendar.getInstance();
//
//                        Date date = new Date(view.getMinDate());
//                        calendar.setTime(date);
//
//                        if (dayOfMonth >= calendar.get(Calendar.DAY_OF_MONTH)) {
//
//                            y = year;
//                            M = monthOfYear;
//                            d = dayOfMonth;
//
//                            System.out.println("view.getDayOfMonth();" + d);
//                            System.out.println("view.getDayOfMonth();" + calendar.get(Calendar.DAY_OF_MONTH));
//
//                            Calendar cal = Calendar.getInstance();
//                            cal.set(Calendar.YEAR, year);
//                            cal.set(Calendar.MONTH, monthOfYear);
//                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                            cal.set(Calendar.HOUR_OF_DAY, 0);
//                            cal.set(Calendar.MINUTE, 0);
//                            cal.set(Calendar.SECOND, 0);
//                            cal.set(Calendar.MILLISECOND, 0);
//
//                            SimpleDateFormat sdfControl = new SimpleDateFormat(DateFormat, Locale.US);
//                            if (IsPreventPreviousDateRequest) {
//                                if (d >= calendar.get(Calendar.DAY_OF_MONTH)) {
//                                    txtView.setText(sdfControl.format(cal.getTime()));
//                                } else {
//                                    txtView.setText(sdfControl.format(new Date()));
//                                }
////                            if(calendar.get(Calendar.DAY_OF_MONTH) >= d){
////                                txtView.setText(sdfControl.format(new Date()));
////                            }else{
////                                txtView.setText(sdfControl.format(cal.getTime()));
////                            }
//                            } else {
//                                txtView.setText(sdfControl.format(cal.getTime()));
//                            }
//                        } else {
//
//
//                            //view.setCalendarViewShown(false);
//
//
//                            Calendar cal1 = Calendar.getInstance();
//
//                            Date date1 = new Date(view.getMinDate());
//                            cal1.setTime(date1);
//
//                            view.updateDate(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DAY_OF_MONTH));
////
////
////                            cal1.set(Calendar.YEAR, 0);
////                            cal1.set(Calendar.MONTH, 0);
////                            cal1.set(Calendar.DAY_OF_MONTH, 0);
////                            cal1.set(Calendar.HOUR_OF_DAY, 0);
////                            cal1.set(Calendar.MINUTE, 0);
////                            cal1.set(Calendar.SECOND, 0);
////                            cal1.set(Calendar.MILLISECOND, 0);
////
////                            cal1.setTime(date1);
////
//                            txtView.setText(new SimpleDateFormat(DateFormat, Locale.US).format(new Date()));
//                        }
//                    }
//
//                }, y, M, d);
//        if (IsPreventPreviousDateRequest) {
//            dp.getDatePicker().setMinDate(c.getTimeInMillis() - 10000);
//        }
//        dp.hide();
//        dp.show();
//    }

//    simpleItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
//            position = viewHolder.getAdapterPosition();
//            final CustomerAddressTran objCustomerAddressTran = alCustomerAddressTran.get(viewHolder.getAdapterPosition());
//            ShowSnackBarWithAction(position, objCustomerAddressTran);
//            adapter.DeleteCustomerAddress(position);
//        }
//
//        @Override
//        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
//                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
//                    .equals(getActivity().getResources().getString(R.string.title_fragment_your_address))) {
//                return super.getSwipeDirs(recyclerView, viewHolder);
//            } else {
//                return 0;
//            }
//        }
//    };
//    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
//    itemTouchHelper.attachToRecyclerView(rvAddress);
    //endregion
}
