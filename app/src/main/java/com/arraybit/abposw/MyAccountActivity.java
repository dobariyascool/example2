package com.arraybit.abposw;

import android.content.Intent;
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

import com.arraybit.adapter.MyAccountAdapter;
import com.arraybit.global.Globals;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity implements MyAccountAdapter.OptionClickListener {

    ArrayList<String> alString;
    RecyclerView rvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Your Account");
        }

        GetData();
        rvOptions = (RecyclerView) findViewById(R.id.rvOptions);
        MyAccountAdapter accountAdapter = new MyAccountAdapter(alString, MyAccountActivity.this, this);
        rvOptions.setAdapter(accountAdapter);
        rvOptions.setLayoutManager(new LinearLayoutManager(MyAccountActivity.this));

    }

    private void GetData() {
        alString = new ArrayList<>();
        for (int i = 0; i < getResources().getStringArray(R.array.Option).length; i++) {
            alString.add(getResources().getStringArray(R.array.Option)[i]);
        }
    }

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
        if (item.getItemId() == R.id.home) {
            Intent i = new Intent(MyAccountActivity.this, HomeActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OptionClick(int id) {
        if (id == 0) {
            ReplaceFragment(new YourOrderFragment(), MyAccountActivity.this.getResources().getString(R.string.title_fragment_your_order));
        } else if (id == 1) {
            ReplaceFragment(new YourBookingFragment(), MyAccountActivity.this.getResources().getString(R.string.title_fragment_your_booking));
        } else if (id == 2) {
            ReplaceFragment(new ChangePasswordFragment(), MyAccountActivity.this.getResources().getString(R.string.title_fragment_change_password));
        } else if (id == 3) {
            Globals.Logout(MyAccountActivity.this, this);
            MyAccountActivity.this.getSupportFragmentManager().popBackStack();
        }

    }
}
