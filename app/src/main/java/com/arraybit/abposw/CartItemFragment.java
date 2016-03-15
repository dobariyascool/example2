package com.arraybit.abposw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;
import com.rey.material.widget.Button;

@SuppressWarnings("ConstantConditions")
public class CartItemFragment extends Fragment implements View.OnClickListener{


    CartItemChangeListener objCartItemChangeListener;

    public CartItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_item, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_cart_item_fragment));
        }
        //end

        setHasOptionsMenu(true);

        Button btnAddMore = (Button)view.findViewById(R.id.btnAddMore);
        Button btnConfirmOrder = (Button)view.findViewById(R.id.btnConfirmOrder);

        btnAddMore.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAddMore){
            Globals.counter = Globals.counter+1;
            //objCartItemChangeListener = (CartItemChangeListener)getActivity();
            //objCartItemChangeListener.CartItemChangeResponse();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //menu.findItem(R.id.viewChange).setVisible(false);
       // menu.findItem(R.id.cart_layout).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Globals.counter = 0;
            objCartItemChangeListener = (CartItemChangeListener)getActivity();
            objCartItemChangeListener.CartItemChangeResponse();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    interface CartItemChangeListener{
        void CartItemChangeResponse();
    }
}
