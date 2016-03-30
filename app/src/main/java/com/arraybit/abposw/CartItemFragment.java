package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
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
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.OrderJSONParser;
import com.arraybit.parser.TaxJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class CartItemFragment extends Fragment implements View.OnClickListener, CartItemAdapter.CartItemOnClickListener, TaxJSONParser.TaxMasterRequestListener, OrderJSONParser.OrderMasterRequestListener {

    RecyclerView rvCartItem;
    CartItemAdapter adapter;
    Button btnAddMore, btnConfirmOrder;
    TextView txtMsg;
    CompoundButton cbMenu;
    LinearLayout headerLayout;
    double totalAmount, totalTax;
    ArrayList<TaxMaster> alTaxMaster;
    ProgressDialog progressDialog = new ProgressDialog();
    int customerMasterId;
    SharePreferenceManage objSharePreferenceManage;

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

        if (Service.CheckNet(getActivity())) {
            RequestTaxMaster();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }


        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        } else if (v.getId() == R.id.btnConfirmOrder) {
            //RequestOrderMaster();
            Globals.ReplaceFragment(new OrderSummaryFragment(),getActivity().getSupportFragmentManager(),getActivity().getResources().getString(R.string.title_order_summary_fragment),R.id.fragmentLayout);
        } else if (v.getId() == R.id.cbMenu) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
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

    @Override
    public void TaxMasterResponse(ArrayList<TaxMaster> alTaxMaster) {
        progressDialog.dismiss();
        this.alTaxMaster = alTaxMaster;
    }

    @Override
    public void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }


    //region Private Methods
    private void RequestTaxMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        TaxJSONParser objTaxJSONParser = new TaxJSONParser();
        objTaxJSONParser.SelectAllTaxMaster(String.valueOf(Globals.linktoBusinessMasterId), getActivity(), this);
    }

    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
            SetVisibility();

        } else {
            SetVisibility();
            rvCartItem.setVisibility(View.VISIBLE);
            adapter = new CartItemAdapter(getActivity(), Globals.alOrderItemTran, this, false);
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
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }
    }

    private void RequestOrderMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        if (Globals.alOrderItemTran.size() != 0) {
            for (ItemMaster objOrderItemTran : Globals.alOrderItemTran) {
                totalAmount = totalAmount + objOrderItemTran.getTotalAmount();
                totalTax = totalTax + objOrderItemTran.getTotalTax();
            }
        }

        OrderMaster objOrderMaster = new OrderMaster();
        objOrderMaster.setLinktoBusinessMasterId((short) Globals.linktoBusinessMasterId);
        objOrderMaster.setlinktoOrderTypeMasterId((short) Globals.OrderType.TakeAway.getValue());
        objOrderMaster.setlinktoCustomerMasterId(customerMasterId);
        objOrderMaster.setTotalAmount(totalAmount);
        objOrderMaster.setTotalTax(totalTax);
        objOrderMaster.setNetAmount(totalAmount + totalTax);
        objOrderMaster.setPaidAmount(totalAmount + totalTax);
        objOrderMaster.setDiscount(0.00);
        objOrderMaster.setExtraAmount(0.00);
        objOrderMaster.setTotalItemPoint((short) 0);
        objOrderMaster.setTotalDeductedPoint((short) 0);
        objOrderMaster.setIsPreOrder(true);

        OrderJSONParser orderJSONParser = new OrderJSONParser();
        orderJSONParser.InsertOrderMaster(objOrderMaster, Globals.alOrderItemTran, alTaxMaster, getActivity(), this);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(rvCartItem, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "0":
                Globals.ShowSnackBar(rvCartItem, getResources().getString(R.string.MsgConfirmOrder), getActivity(), 1000);
                Globals.ClearCartData();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ShowMessage", false);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
                break;
        }

    }
    //endregion

}
