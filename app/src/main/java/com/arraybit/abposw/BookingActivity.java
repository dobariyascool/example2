package com.arraybit.abposw;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arraybit.global.Globals;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            Globals.ReplaceFragment(new YourBookingFragment(), getSupportFragmentManager(), getResources().getString(R.string.title_fragment_your_booking), R.id.addBookingFragment);
        } else {
            Globals.ReplaceFragment(new AddBookingFragment(this), getSupportFragmentManager(), getResources().getString(R.string.title_add_booking_fragment), R.id.addBookingFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_add_booking_fragment))) {
                AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
                addBookingFragment.objAddNewBookingListener = (AddBookingFragment.AddNewBookingListener) addBookingFragment.getTargetFragment();
                addBookingFragment.objAddNewBookingListener.AddNewBooking(null);
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_booking_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public void BookingDateOnClick(View view) {
        AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
        addBookingFragment.ShowDateTimePicker(view.getId());
    }
}
