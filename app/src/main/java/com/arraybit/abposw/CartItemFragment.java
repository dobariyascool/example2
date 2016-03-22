package com.arraybit.abposw;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.global.Globals;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

@SuppressWarnings("ConstantConditions")
public class CartItemFragment extends Fragment implements View.OnClickListener,CartItemAdapter.CartItemOnClickListener{


    CartItemChangeListener objCartItemChangeListener;
    RecyclerView rvCartItem;
    CartItemAdapter adapter;
    Button btnAddMore,btnConfirmOrder;
    TextView txtMsg;
    CompoundButton cbMenu;
    LinearLayout headerLayout;

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

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        cbMenu = (CompoundButton) view.findViewById(R.id.cbMenu);

        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        setHasOptionsMenu(true);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);

        SetRecyclerView();

        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            //Globals.counter = Globals.counter + 1;
            //objCartItemChangeListener = (CartItemChangeListener)getActivity();
            //objCartItemChangeListener.CartItemChangeResponse();

           // getActivity().setResult(Activity.RESULT_OK);
            //getActivity().getSupportFragmentManager().popBackStack();
            //MenuActivity menuActivity = (MenuActivity)getActivity();
            //menuActivity.SetCartItemResponse();
            //getActivity().finish();
        }else if(v.getId()==R.id.btnConfirmOrder){

        }else if(v.getId()==R.id.cbMenu){
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
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
        if (item.getItemId() == android.R.id.home) {
            //Globals.counter = 0;
            //objCartItemChangeListener = (CartItemChangeListener)getActivity();
            //objCartItemChangeListener.CartItemChangeResponse();
            //getActivity().getSupportFragmentManager().popBackStack();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ImageViewOnClick(int position) {
        adapter.RemoveData(position);
        if (Globals.alOrderItemTran.size() == 0) {
            SetRecyclerView();
        }
    }

    private void SetRecyclerView(){
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
            SetVisibility();

        } else {
            SetVisibility();
            rvCartItem.setVisibility(View.VISIBLE);
            adapter = new CartItemAdapter(getActivity(), Globals.alOrderItemTran,this,false);
            rvCartItem.setAdapter(adapter);
            rvCartItem.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCartItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Globals.HideKeyBoard(getActivity(), recyclerView);
                    if (!adapter.isItemAnimate) {
                        adapter.isItemAnimate = true;
                        adapter.isModifierChanged = false;
                    }
                }
            });
        }
    }

    private void SetVisibility() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);
            cbMenu.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            btnAddMore.setVisibility(View.GONE);
            btnConfirmOrder.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            cbMenu.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnConfirmOrder.setVisibility(View.VISIBLE);
        }
    }

    interface CartItemChangeListener {
        void CartItemChangeResponse();
    }

}
