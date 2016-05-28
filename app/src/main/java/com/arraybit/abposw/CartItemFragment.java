package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.adapter.CartItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.TaxJSONParser;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "ResourceType"})
@SuppressLint("ValidFragment")
public class CartItemFragment extends Fragment implements View.OnClickListener, CartItemAdapter.CartItemOnClickListener, TaxJSONParser.TaxMasterRequestListener, RemarkDialogFragment.RemarkResponseListener ,AddQtyRemarkDialogFragment.AddQtyRemarkDialogListener{

    RecyclerView rvCartItem;
    CartItemAdapter adapter;
    Button btnAddMore, btnConfirmOrder,btnDisableConfirmOrder,btnRemark;
    TextView txtMsg, txtRemark, txtTotalAmount, txtHeaderTotalAmount, txtHeaderDiscount, txtTotalDiscount, txtHeaderRounding, txtRoundingOff, txtHeaderNetAmount, txtNetAmount, txtHeaderRemark,txtMinOrder,txtEditMessage;
    ImageView ivRemark;
    CompoundButton cbMenu;
    LinearLayout headerLayout, taxLayout, errorLayout;
    double totalAmount, totalTax, netAmount, tax1, tax2, tax3, tax4, tax5;
    String strNetAmount;
    ArrayList<TaxMaster> alTaxMaster = new ArrayList<>();
    ProgressDialog progressDialog = new ProgressDialog();
    int customerMasterId;
    SharePreferenceManage objSharePreferenceManage;
    String activityName;
    OrderMaster objOrderMaster;
    int position;
    FrameLayout cartItemFragment;
    boolean isPause;


    public CartItemFragment(String activityName) {
        this.activityName = activityName;
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

        cartItemFragment = (FrameLayout)view.findViewById(R.id.cartItemFragment);
        Globals.SetScaleImageBackground(getActivity(),null,null,cartItemFragment);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);
        txtRemark = (TextView) view.findViewById(R.id.txtRemark);
        txtTotalAmount = (TextView) view.findViewById(R.id.txtTotalAmount);
        txtHeaderTotalAmount = (TextView) view.findViewById(R.id.txtHeaderTotalAmount);
        txtTotalDiscount = (TextView) view.findViewById(R.id.txtTotalDiscount);
        txtHeaderDiscount = (TextView) view.findViewById(R.id.txtHeaderDiscount);
        txtRoundingOff = (TextView) view.findViewById(R.id.txtRoundingOff);
        txtHeaderRounding = (TextView) view.findViewById(R.id.txtHeaderRounding);
        txtNetAmount = (TextView) view.findViewById(R.id.txtNetAmount);
        txtHeaderNetAmount = (TextView) view.findViewById(R.id.txtHeaderNetAmount);
        txtMinOrder = (TextView) view.findViewById(R.id.txtMinOrder);
        txtEditMessage = (TextView) view.findViewById(R.id.txtEditMessage);

        cbMenu = (CompoundButton) view.findViewById(R.id.cbMenu);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
        taxLayout = (LinearLayout) view.findViewById(R.id.taxLayout);

        rvCartItem = (RecyclerView) view.findViewById(R.id.rvCartItem);

        setHasOptionsMenu(true);

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        btnConfirmOrder = (Button) view.findViewById(R.id.btnConfirmOrder);
        btnDisableConfirmOrder = (Button) view.findViewById(R.id.btnDisableConfirmOrder);
        btnRemark = (Button) view.findViewById(R.id.btnRemark);

        SetRecyclerView();

        if (Service.CheckNet(getActivity())) {
            RequestTaxMaster();
        } else {
            AllControlVisibility();
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgCheckConnection), rvCartItem, R.drawable.wifi_drawable);
        }

        cbMenu.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnConfirmOrder.setOnClickListener(this);
        btnRemark.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddMore) {
            if(activityName!=null && activityName.equals(getResources().getString(R.string.title_home))){
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "OrderRemark", RemarkDialogFragment.strRemark, getActivity());
                if(objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType",getActivity())!=null){
                    String str = objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType", getActivity());
                    if(str!=null && !str.equals("")) {
                        Globals.linktoOrderTypeMasterId = Short.parseShort(str);
                    }
                }
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                getActivity().startActivityForResult(intent,0);
            }else {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "OrderRemark", RemarkDialogFragment.strRemark, getActivity());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ShowMessage", false);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
        } else if (v.getId() == R.id.btnConfirmOrder) {
            if(activityName!=null && activityName.equals(getResources().getString(R.string.title_home))){
                if(objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType",getActivity())!=null){
                    String str = objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType", getActivity());
                    if(str!=null && !str.equals("")) {
                        Globals.linktoOrderTypeMasterId = Short.parseShort(str);
                    }
                }
            }
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) == null) {
                isPause = true;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent, 0);
            } else {
                RequestOrderMaster();
                objSharePreferenceManage.RemovePreference("OrderTypePreference", "OrderType", getActivity());
                objSharePreferenceManage.ClearPreference("OrderTypePreference", getActivity());
                Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                intent.putExtra("OrderMaster", objOrderMaster);
                intent.putParcelableArrayListExtra("TaxMaster", alTaxMaster);
                intent.putExtra("ParentActivity", activityName);
                getActivity().startActivityForResult(intent, 0);
                //startActivity(intent);
                //customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
                //RequestOrderMaster();
            }
        } else if (v.getId() == R.id.cbMenu) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        } else if (v.getId() == R.id.btnRemark) {
            RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment();
            remarkDialogFragment.setTargetFragment(this, 0);
            remarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            objSharePreferenceManage = new SharePreferenceManage();
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
                RequestOrderMaster();
                objSharePreferenceManage.RemovePreference("OrderTypePreference", "OrderType", getActivity());
                objSharePreferenceManage.ClearPreference("OrderTypePreference", getActivity());
                Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                intent.putExtra("OrderMaster", objOrderMaster);
                intent.putParcelableArrayListExtra("TaxMaster", alTaxMaster);
                intent.putExtra("ParentActivity", activityName);
                getActivity().startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            if(errorLayout.isShown()){
//                CheckOutActivity.objCheckOut = null;
//            }
            if(activityName !=null && activityName.equals(getResources().getString(R.string.title_home))){
                CheckOutActivity.isBackPressed = false;
            }
            SaveCartDataInSharePreference();
            Intent returnIntent = new Intent();
            if(activityName!=null && activityName.equals(getResources().getString(R.string.title_home))){
                if(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                    returnIntent.putExtra("IsLogin", true);
                }
            }
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ImageViewOnClick(int position, ItemMaster objOrderItemMaster,boolean isDeleteClick) {
        if(isDeleteClick) {
            ShowSnackBarWithAction(position, objOrderItemMaster);
            adapter.RemoveData(position);
            if (Globals.alOrderItemTran.size() == 0) {
                RemovePreference();
                RemarkDialogFragment.strRemark = "";
                SetRecyclerView();
            }
            if (adapter.changeAmount) {
                totalAmount = 0;
                netAmount = 0;
                totalTax = 0;
                tax1 = 0;
                tax2 = 0;
                tax3 = 0;
                tax4 = 0;
                tax5 = 0;
                SetRecyclerView();
                if (alTaxMaster != null && alTaxMaster.size() != 0) {
                    SetTextLayout();
                }
            }
        }else{
            this.position = position;
            AddQtyRemarkDialogFragment addQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(objOrderItemMaster);
            addQtyRemarkDialogFragment.setTargetFragment(this, 0);
            addQtyRemarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void TaxMasterResponse(ArrayList<TaxMaster> alTaxMaster) {
        progressDialog.dismiss();
        this.alTaxMaster = alTaxMaster;
        if (alTaxMaster != null && alTaxMaster.size() != 0) {
            SetTextLayout();
        }
    }

    @Override
    public void RemarkResponse(String strRemark) {
        if (RemarkDialogFragment.strRemark != null && !RemarkDialogFragment.strRemark.equals("")) {
            txtRemark.setVisibility(View.VISIBLE);
            txtRemark.setText(RemarkDialogFragment.strRemark);
        } else {
            txtRemark.setVisibility(View.GONE);
            txtRemark.setText("");
        }
    }

    @Override
    public void AddQtyRemarkResponse(ItemMaster objItemMaster) {
        adapter.UpdateData(position, objItemMaster);
        totalAmount = 0;
        netAmount = 0;
        totalTax = 0;
        tax1 = 0;
        tax2 = 0;
        tax3 = 0;
        tax4 = 0;
        tax5 = 0;
        CountAmount();
        if (alTaxMaster != null && alTaxMaster.size() != 0) {
            SetTextLayout();
        }
    }

    //region Private Methods
    private void RequestTaxMaster() {
        progressDialog.show(getFragmentManager(), "");
        TaxJSONParser objTaxJSONParser = new TaxJSONParser();
        objTaxJSONParser.SelectAllTaxMaster(String.valueOf(Globals.linktoBusinessMasterId), getActivity(), this);
    }

    private void SetRecyclerView() {
        if (Globals.alOrderItemTran.size() == 0) {
            //txtMsg.setText(getActivity().getResources().getString(R.string.MsgCart));
            Globals.SetErrorLayout(errorLayout, true, getActivity().getResources().getString(R.string.MsgCart), rvCartItem, R.drawable.cart_drawable);
            RemarkDialogFragment.strRemark = null;
            SetVisibility();
        } else {
            SetVisibility();
            CountAmount();
            Globals.SetErrorLayout(errorLayout,false,null,rvCartItem,0);
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

    private void AllControlVisibility(){
        txtMsg.setVisibility(View.VISIBLE);
        cbMenu.setVisibility(View.GONE);
        rvCartItem.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);
        txtHeaderRemark.setVisibility(View.GONE);
        ivRemark.setVisibility(View.GONE);
        txtHeaderTotalAmount.setVisibility(View.GONE);
        txtTotalAmount.setVisibility(View.GONE);
        txtHeaderDiscount.setVisibility(View.GONE);
        txtTotalDiscount.setVisibility(View.GONE);
        txtHeaderRounding.setVisibility(View.GONE);
        txtRoundingOff.setVisibility(View.GONE);
        txtHeaderNetAmount.setVisibility(View.GONE);
        txtNetAmount.setVisibility(View.GONE);
        if(activityName!=null && activityName.equals(getActivity().getResources().getString(R.string.title_home))){
            btnAddMore.setVisibility(View.GONE);
        }else{
            btnAddMore.setVisibility(View.GONE);
        }
        btnConfirmOrder.setVisibility(View.GONE);
        taxLayout.setVisibility(View.GONE);
        txtRemark.setVisibility(View.GONE);
        txtMinOrder.setVisibility(View.GONE);
        txtEditMessage.setVisibility(View.GONE);
        btnDisableConfirmOrder.setVisibility(View.GONE);
    }

    private void SetVisibility() {
        if (Globals.alOrderItemTran.size() == 0) {
            txtMsg.setVisibility(View.VISIBLE);
            cbMenu.setVisibility(View.GONE);
            rvCartItem.setVisibility(View.GONE);
            headerLayout.setVisibility(View.GONE);
            btnRemark.setVisibility(View.GONE);
            txtHeaderTotalAmount.setVisibility(View.GONE);
            txtTotalAmount.setVisibility(View.GONE);
            txtHeaderDiscount.setVisibility(View.GONE);
            txtTotalDiscount.setVisibility(View.GONE);
            txtHeaderRounding.setVisibility(View.GONE);
            txtRoundingOff.setVisibility(View.GONE);
            txtHeaderNetAmount.setVisibility(View.GONE);
            txtNetAmount.setVisibility(View.GONE);
            if(activityName!=null && activityName.equals(getActivity().getResources().getString(R.string.title_home))){
                btnAddMore.setVisibility(View.GONE);
            }else{
                btnAddMore.setVisibility(View.GONE);
            }
            btnConfirmOrder.setVisibility(View.GONE);
            taxLayout.setVisibility(View.GONE);
            txtRemark.setVisibility(View.GONE);
            txtMinOrder.setVisibility(View.GONE);
            txtEditMessage.setVisibility(View.GONE);
            btnDisableConfirmOrder.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.GONE);
            cbMenu.setVisibility(View.GONE);
            txtEditMessage.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            btnRemark.setVisibility(View.VISIBLE);
            rvCartItem.setVisibility(View.VISIBLE);
            txtHeaderTotalAmount.setVisibility(View.VISIBLE);
            txtTotalAmount.setVisibility(View.VISIBLE);
            txtHeaderDiscount.setVisibility(View.VISIBLE);
            txtTotalDiscount.setVisibility(View.VISIBLE);
            txtHeaderRounding.setVisibility(View.VISIBLE);
            txtRoundingOff.setVisibility(View.VISIBLE);
            txtHeaderNetAmount.setVisibility(View.VISIBLE);
            txtNetAmount.setVisibility(View.VISIBLE);
            btnAddMore.setVisibility(View.VISIBLE);
            btnConfirmOrder.setVisibility(View.VISIBLE);
            taxLayout.setVisibility(View.VISIBLE);
            txtMinOrder.setVisibility(View.VISIBLE);
            if (RemarkDialogFragment.strRemark != null) {
                if (!RemarkDialogFragment.strRemark.equals("")) {
                    txtRemark.setVisibility(View.VISIBLE);
                    txtRemark.setText(RemarkDialogFragment.strRemark);
                }
            }
        }
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }
    }

    private void RequestOrderMaster() {
        objOrderMaster = new OrderMaster();
        objOrderMaster.setLinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objOrderMaster.setlinktoOrderTypeMasterId(Globals.linktoOrderTypeMasterId);
        objOrderMaster.setlinktoCustomerMasterId(customerMasterId);
        objOrderMaster.setTotalAmount(totalAmount);
        objOrderMaster.setTotalTax(totalTax);
        objOrderMaster.setNetAmount(netAmount);
        objOrderMaster.setPaidAmount(0.00);
        objOrderMaster.setDiscount(0.00);
        objOrderMaster.setBalanceAmount(netAmount);
        objOrderMaster.setExtraAmount(0.00);
        objOrderMaster.setTotalItemPoint((short) 0);
        objOrderMaster.setTotalDeductedPoint((short) 0);
        objOrderMaster.setIsPreOrder(true);
    }

    @SuppressLint("SetTextI18n")
    private void CountAmount() {
        if (Globals.alOrderItemTran.size() != 0) {
            for (ItemMaster objOrderItemTran : Globals.alOrderItemTran) {
                totalAmount = totalAmount + objOrderItemTran.getTotalAmount();
                totalTax = totalTax + objOrderItemTran.getTotalTax();
                netAmount = totalAmount + totalTax;
                tax1 = tax1 + objOrderItemTran.getTax1();
                tax2 = tax2 + objOrderItemTran.getTax2();
                tax3 = tax3 + objOrderItemTran.getTax3();
                tax4 = tax4 + objOrderItemTran.getTax4();
                tax5 = tax5 + objOrderItemTran.getTax5();
            }
            strNetAmount = Globals.dfWithPrecision.format(netAmount);
            txtTotalAmount.setText(Globals.dfWithPrecision.format(totalAmount));
            txtNetAmount.setText(Globals.dfWithPrecision.format(Math.round(netAmount)));
            txtRoundingOff.setText("- 0." + strNetAmount.substring(strNetAmount.lastIndexOf(".") + 1, strNetAmount.length()));
            if(totalAmount >= 300){
                btnConfirmOrder.setVisibility(View.VISIBLE);
                btnDisableConfirmOrder.setVisibility(View.GONE);
                txtMinOrder.setVisibility(View.INVISIBLE);
            }else{
                if(activityName!=null && activityName.equals(getActivity().getResources().getString(R.string.title_home))){
                    if(objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType",getActivity())!=null){
                        Globals.linktoOrderTypeMasterId = Short.parseShort(objSharePreferenceManage.GetPreference("OrderTypePreference","OrderType",getActivity()));
                    }
                }
                if(Globals.linktoOrderTypeMasterId == Globals.OrderType.HomeDelivery.getValue()){
                    btnConfirmOrder.setVisibility(View.GONE);
                    btnDisableConfirmOrder.setVisibility(View.VISIBLE);
                    txtMinOrder.setVisibility(View.VISIBLE);
                }else if(Globals.linktoOrderTypeMasterId == Globals.OrderType.TakeAway.getValue()){
                    btnConfirmOrder.setVisibility(View.VISIBLE);
                    btnDisableConfirmOrder.setVisibility(View.GONE);
                    txtMinOrder.setVisibility(View.GONE);
                }else{
                    btnConfirmOrder.setVisibility(View.VISIBLE);
                    btnDisableConfirmOrder.setVisibility(View.GONE);
                    txtMinOrder.setVisibility(View.GONE);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetTextLayout() {
        taxLayout.removeAllViewsInLayout();
        LinearLayout[] linearLayout = new LinearLayout[alTaxMaster.size()];
        TextView[] txtTaxName = new TextView[alTaxMaster.size()];
        TextView[] txtTaxRate = new TextView[alTaxMaster.size()];

        for (int i = 0; i < alTaxMaster.size(); i++) {

            linearLayout[i] = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            linearLayout[i].setLayoutParams(layoutParams);
            linearLayout[i].setPadding(8, 0, 8, 0);

            txtTaxName[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxNameParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxNameParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxNameParams);
            txtTaxName[i].setGravity(Gravity.START);
            txtTaxName[i].setTextSize(9f);
            txtTaxName[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

            txtTaxRate[i] = new TextView(getActivity());
            LinearLayout.LayoutParams txtTaxRateParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtTaxRateParams.weight = 0.5f;
            txtTaxName[i].setLayoutParams(txtTaxRateParams);
            txtTaxRate[i].setGravity(Gravity.END);
            txtTaxRate[i].setTextSize(9f);
            txtTaxRate[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));


            if (alTaxMaster.get(i).getIsPercentage()) {
                String str = String.valueOf(alTaxMaster.get(i).getTaxRate());
                String precision = str.substring(str.lastIndexOf(".") + 1);

                if (Integer.valueOf(precision) > 0) {
                    txtTaxName[i].setText(alTaxMaster.get(i).getTaxCaption() + "  [" + " " + str + " % ]");
                } else {
                    txtTaxName[i].setText(alTaxMaster.get(i).getTaxCaption() + "  [" + " " + str.substring(0, str.lastIndexOf(".")) + " % ]");
                }
                if (i == 0) {
                    txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax1));
                } else if (i == 1) {
                    txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax2));
                } else if (i == 2) {
                    txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax3));
                } else if (i == 3) {
                    txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax4));
                } else if (i == 4) {
                    txtTaxRate[i].setText(Globals.dfWithPrecision.format(tax5));
                }
            } else {
                txtTaxName[i].setText(alTaxMaster.get(i).getTaxCaption());
                txtTaxRate[i].setText(Globals.dfWithPrecision.format(alTaxMaster.get(i).getTaxRate()));
            }

            linearLayout[i].addView(txtTaxName[i]);
            linearLayout[i].addView(txtTaxRate[i]);
            taxLayout.addView(linearLayout[i]);
        }
    }

    private void ShowSnackBarWithAction(final int position, final ItemMaster objItemMaster) {
        Snackbar snackbar = Snackbar
                .make(rvCartItem, String.format(getActivity().getResources().getString(R.string.MsgCartItemDelete), objItemMaster.getItemName()), 8000)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
//                        if (event == DISMISS_EVENT_TIMEOUT) {
//                            //will be true if user not click on Action button (for example: manual dismiss, dismiss by swipe
//                        }
                    }
                })
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.UndoData(position, objItemMaster);
                        if (Globals.alOrderItemTran.size() == 0) {
                            RemarkDialogFragment.strRemark = "";
                            SetRecyclerView();
                        }
                        if (adapter.changeAmount) {
                            totalAmount = 0;
                            netAmount = 0;
                            totalTax = 0;
                            tax1 = 0;
                            tax2 = 0;
                            tax3 = 0;
                            tax4 = 0;
                            tax5 = 0;
                            SetRecyclerView();
                            if (alTaxMaster != null && alTaxMaster.size() != 0) {
                                SetTextLayout();
                            }
                        }
                        Globals.ShowSnackBar(rvCartItem, String.format(getActivity().getResources().getString(R.string.MsgCartItem), objItemMaster.getItemName()), getActivity(), 1000);
                    }
                });

        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.snackBarActionColor));

        View snackView = snackbar.getView();
        if (Build.VERSION.SDK_INT >= 21) {
            snackView.setElevation(R.dimen.snackbar_elevation);
        }
        android.widget.TextView txt = (android.widget.TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_grey));
        snackbar.show();
    }

    private void RemovePreference() {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", getActivity());
        objSharePreferenceManage.ClearPreference("CheckOutDataPreference", getActivity());
        objSharePreferenceManage.RemovePreference("CartItemListPreference", "OrderRemark", getActivity());
        objSharePreferenceManage.ClearPreference("CartItemListPreference", getActivity());
        objSharePreferenceManage.RemovePreference("OrderTypePreference","OrderType", getActivity());
        objSharePreferenceManage.ClearPreference("OrderTypePreference", getActivity());
    }

    private void SaveCartDataInSharePreference() {
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        try {
            if (Globals.alOrderItemTran.size() == 0) {
                objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", null, getActivity());
            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = gson.toJson(Globals.alOrderItemTran);
                objSharePreferenceManage.CreatePreference("CartItemListPreference", "CartItemList", string, getActivity());
                objSharePreferenceManage.CreatePreference("CartItemListPreference","OrderRemark",RemarkDialogFragment.strRemark,getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
