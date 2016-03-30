package com.arraybit.abposw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;

public class CartItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        FrameLayout fragmentLayout = (FrameLayout)findViewById(R.id.fragmentLayout);

        Globals.ReplaceFragment(new CartItemFragment(), getSupportFragmentManager(), null, R.id.fragmentLayout);

    }

}
