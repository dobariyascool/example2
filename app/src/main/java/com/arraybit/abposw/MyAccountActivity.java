package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arraybit.adapter.MyAccountAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.SharePreferenceManage;
import com.github.clans.fab.FloatingActionButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity implements MyAccountAdapter.OptionClickListener, UserProfileFragment.UpdateResponseListener {

    ArrayList<String> alString;
    RecyclerView rvOptions;
    FloatingActionButton fabEdit;
    TextView txtLoginChar, txtFullName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Text View start
        txtLoginChar = (TextView) findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        //end

        SetUserName();

        //RecyclerView start
        GetData();
        rvOptions = (RecyclerView) findViewById(R.id.rvOptions);
        MyAccountAdapter accountAdapter = new MyAccountAdapter(alString, MyAccountActivity.this, this);
        rvOptions.setAdapter(accountAdapter);
        rvOptions.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));
        //end

        //FloatingActionButton start
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new UserProfileFragment(), getResources().getString(R.string.title_fragment_your_profile));
            }
        });
        //end

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OptionClick(int id) {
        if (id == 0) {
            ReplaceFragment(new YourOrderFragment(), getResources().getString(R.string.title_fragment_your_order));
        } else if (id == 1) {
            ReplaceFragment(new YourBookingFragment(), getResources().getString(R.string.title_fragment_your_booking));
        } else if (id == 2) {
            ReplaceFragment(new ChangePasswordFragment(), getResources().getString(R.string.title_fragment_change_password));
        } else if (id == 3) {
            Globals.ClearUserPreference(MyAccountActivity.this, this);
            getSupportFragmentManager().popBackStack();
        }
    }

    public void EditTextOnClick(View view) {
        UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_fragment_your_profile));
        userProfileFragment.EditTextOnClick();
    }

    @Override
    public void UpdateResponse() {
        SetUserName();
    }

    public void BookingDateOnClick(View view) {
        AddBookingFragment addBookingFragment = (AddBookingFragment) getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
        addBookingFragment.ShowDateTimePicker(view.getId());
    }

    @Override
    public void onBackPressed() {
       if(getSupportFragmentManager().getBackStackEntryCount() > 0 ){
           if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                   && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                   .equals(getResources().getString(R.string.title_fragment_your_booking))) {
               getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_fragment_your_booking), FragmentManager.POP_BACK_STACK_INCLUSIVE);
           }else if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                       && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                       .equals(getResources().getString(R.string.title_add_booking_fragment))) {
                    AddBookingFragment addBookingFragment = (AddBookingFragment)getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.title_add_booking_fragment));
                    addBookingFragment.objAddNewBookingListener = (AddBookingFragment.AddNewBookingListener)addBookingFragment.getTargetFragment();
                    addBookingFragment.objAddNewBookingListener.AddNewBooking(null);
                    getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_booking_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
           }
       }
       else{
           super.onBackPressed();
       }
    }

    //region Private Method
    private void GetData() {
        alString = new ArrayList<>();
        for (int i = 0; i < getResources().getStringArray(R.array.Option).length; i++) {
            alString.add(getResources().getStringArray(R.array.Option)[i]);
        }
    }

    private void SetUserName() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this) != null) {
            txtEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this));
            txtLoginChar.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", MyAccountActivity.this).substring(0, 1).toUpperCase());
        }
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", MyAccountActivity.this) != null) {
            txtFullName.setVisibility(View.VISIBLE);
            txtFullName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", MyAccountActivity.this));
        } else {
            txtFullName.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RtlHardcoded")
    private void ReplaceFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction fragmentTransaction = MyAccountActivity.this.getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.RIGHT);
            slideTransition.setDuration(350);
            fragment.setEnterTransition(slideTransition);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, 0, R.anim.right_exit);
        }
        fragmentTransaction.replace(R.id.myaccount, fragment, fragmentName);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }
    //endregion

}
