package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class MyAccountActivity extends AppCompatActivity implements MyAccountAdapter.OptionClickListener {

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
            getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_your_account));
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

    //region Private Methods
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
    //end

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
            Globals.Logout(MyAccountActivity.this, this);
            getSupportFragmentManager().popBackStack();
        }

    }
}
