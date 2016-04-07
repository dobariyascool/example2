package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.arraybit.global.Globals;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Globals.ReplaceFragment(new AddBookingFragment(this), getSupportFragmentManager(),getResources().getString(R.string.title_add_booking_fragment), R.id.addBookingFragment);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void BookingDateOnClick(View view) {
        AddBookingFragment addBookingFragment = (AddBookingFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
        addBookingFragment.ShowDateTimePicker(view.getId());
    }
}
