package com.arraybit.abposw;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrderAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OrderJSONParser;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class YourOrderFragment extends Fragment implements ItemJSONParser.ItemMasterRequestListener, OrderAdapter.OrderOnClickListener, OrderJSONParser.OrderMasterRequestListener, ConfirmDialog.ConfirmationResponseListener {

    RecyclerView rvOrder;
    LinearLayout errorLayout;
    LinearLayoutManager linearLayoutManager;
    OrderAdapter adapter;
    ProgressDialog progressDialog = new ProgressDialog();
    int currentPage = 1, position;
    ArrayList<ItemMaster> alItemMaster;
    ArrayList<OrderMaster> alOrderMaster;
    int customerMasterId;
    SharePreferenceManage objSharePreferenceManage;
    OrderMaster objOrder;

    public YourOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_order, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_your_order));
        setHasOptionsMenu(true);

        rvOrder = (RecyclerView) view.findViewById(R.id.rvOrder);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            RequestOrderMasterOrderItem();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgCheckConnection), rvOrder, R.drawable.wifi_drawable);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!adapter.isItemAnimate) {
                    adapter.isItemAnimate = true;
                }
            }
        });

        rvOrder.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!adapter.isItemAnimate) {
                    adapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestOrderMasterOrderItem();
                    } else {
                        Globals.ShowSnackBar(rvOrder, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Globals.HideKeyBoard(getActivity(), getView());
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_my_order))) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IsLogin", true);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            } else {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster, boolean isFilter) {
        progressDialog.dismiss();
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }

    @Override
    public void CancelOnClick(OrderMaster objOrderMaster, int position) {
        this.position = position;
        this.objOrder = objOrderMaster;
        ConfirmDialog confirmDialog = new ConfirmDialog(null, true, String.format(getActivity().getResources().getString(R.string.cdfCancelMsg),"order no ("+ objOrder.getOrderNumber()+")"));
        confirmDialog.setTargetFragment(this, 0);
        confirmDialog.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void ConfirmResponse() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        OrderJSONParser orderJSONParser = new OrderJSONParser();
        orderJSONParser.UpdateOrderMasterStatus(String.valueOf(objOrder.getOrderMasterId()), getActivity(), this);
    }

    @Override
    public void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    //region Private Method
    private void RequestOrderMasterOrderItem() {
        progressDialog.show(getFragmentManager(), "");

        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }
        if (customerMasterId != 0) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            objItemJSONParser.SelectAllOrderMasterOrderItem(this, getActivity(), String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(customerMasterId));
        } else {
            progressDialog.dismiss();
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvOrder, 0);
        }
    }

    private void SetRecyclerView() {
        SetOrderItemList();
        if (alOrderMaster == null) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgSelectFail), rvOrder, 0);
            }
        } else if (alOrderMaster.size() == 0) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgNoRecord), rvOrder, 0);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvOrder, 0);
            if (currentPage > 1) {
                adapter.OrderDataChanged(alOrderMaster);
                return;
            } else if (alOrderMaster.size() < 10) {
                currentPage += 1;
            }
            adapter = new OrderAdapter(getActivity(), alOrderMaster, this);
            rvOrder.setAdapter(adapter);
            rvOrder.setLayoutManager(linearLayoutManager);
        }
    }

    private void SetOrderItemList() {
        alOrderMaster = new ArrayList<>();
        ArrayList<ItemMaster> alOrderItem = new ArrayList<>();
        OrderMaster objOrderMaster = new OrderMaster();
        int orderId = -1;
        int cnt = 0;
        for (ItemMaster objItemMaster : alItemMaster) {
            if (orderId == -1) {
                orderId = objItemMaster.getLinktoOrderMasterId();
                objOrderMaster.setOrderMasterId(objItemMaster.getLinktoOrderMasterId());
                objOrderMaster.setOrderNumber(objItemMaster.getOrderNumber());
                objOrderMaster.setTotalAmount(objItemMaster.getTotalAmount());
                objOrderMaster.setTotalTax(objItemMaster.getTotalTax());
                objOrderMaster.setOrderDateTime(objItemMaster.getCreateDateTime());
                objOrderMaster.setIsPreOrder(objItemMaster.getPaymentStatus());
                objOrderMaster.setlinktoOrderStatusMasterId(objItemMaster.getLinktoOrderStatusMasterId());
                alOrderItem.add(objItemMaster);
                if (cnt == alItemMaster.size() - 1) {
                    objOrderMaster.setAlOrderItemTran(alOrderItem);
                    alOrderMaster.add(objOrderMaster);
                }
            } else {
                if (orderId == objItemMaster.getLinktoOrderMasterId()) {
                    orderId = objItemMaster.getLinktoOrderMasterId();
                    alOrderItem.add(objItemMaster);
                    if (cnt == alItemMaster.size() - 1) {
                        objOrderMaster.setAlOrderItemTran(alOrderItem);
                        alOrderMaster.add(objOrderMaster);
                    }
                } else {
                    objOrderMaster.setAlOrderItemTran(alOrderItem);
                    alOrderMaster.add(objOrderMaster);
                    orderId = objItemMaster.getLinktoOrderMasterId();
                    alOrderItem = new ArrayList<>();
                    objOrderMaster = new OrderMaster();
                    objOrderMaster.setOrderMasterId(objItemMaster.getLinktoOrderMasterId());
                    objOrderMaster.setOrderNumber(objItemMaster.getOrderNumber());
                    objOrderMaster.setTotalAmount(objItemMaster.getTotalAmount());
                    objOrderMaster.setTotalTax(objItemMaster.getTotalTax());
                    objOrderMaster.setOrderDateTime(objItemMaster.getCreateDateTime());
                    objOrderMaster.setIsPreOrder(objItemMaster.getPaymentStatus());
                    objOrderMaster.setlinktoOrderStatusMasterId(objItemMaster.getLinktoOrderStatusMasterId());
                    alOrderItem.add(objItemMaster);
                    if (cnt == alItemMaster.size() - 1) {
                        objOrderMaster.setAlOrderItemTran(alOrderItem);
                        alOrderMaster.add(objOrderMaster);
                    }
                }
            }
            cnt++;
        }
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "1":
                Globals.ShowSnackBar(rvOrder, getActivity().getResources().getString(R.string.MsgCancelOrder), getActivity(), 1000);
                break;
            case "-1":
                Globals.ShowSnackBar(rvOrder, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                adapter.UpdateOrderData(position);
                break;
        }

    }

    //endregion
}