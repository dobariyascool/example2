package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;

public class BookingActivity extends AppCompatActivity {

    FrameLayout addBookingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        addBookingFragment = (FrameLayout) findViewById(R.id.addBookingFragment);

        Intent intent = getIntent();
        boolean isBookingFromMenu = intent.getBooleanExtra("IsBookingFromMenu", false);
        if (isBookingFromMenu) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IsShowMessage", intent.getBooleanExtra("IsShowMessage", false));
            YourBookingFragment yourBookingFragment = new YourBookingFragment();
            yourBookingFragment.setArguments(bundle);
            Globals.ReplaceFragment(yourBookingFragment, getSupportFragmentManager(), getResources().getString(R.string.title_fragment_your_booking), R.id.addBookingFragment);
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
            Intent returnIntent = new Intent();
            returnIntent.putExtra("IsLogin", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    public void BookingDateOnClick(View view) {
        AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
        addBookingFragment.ShowDateTimePicker(view.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    if (data.getBooleanExtra("IsShowMessage", false)) {
                        Globals.ShowSnackBar(addBookingFragment, getResources().getString(R.string.siLoginSuccessMsg), BookingActivity.this, 2000);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
