package com.arraybit.abposw;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CheckOut;
import com.arraybit.modal.CustomerAddressTran;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OfferMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.CustomerAddressJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.arraybit.parser.OrderJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, AddressSelectorBottomDialog.AddressSelectorResponseListener,
                                                                   OfferJSONParser.OfferRequestListener, ConfirmDialog.ConfirmationResponseListener, OrderJSONParser.OrderMasterRequestListener,AddAddressFragment.AddNewAddressListener {

    public static CheckOut objCheckOut;
    LinearLayout textLayout;
    TextView txtCity, txtArea, txtAddress, txtPhone, txtName, txtPay;
    CompoundButton cbGetPromoCode;
    ToggleButton tbHomeDelivery, tbTakeAway;
    EditText etOfferCode, etOrderDate, etOrderTime;
    Button btnApply, btnViewMore, btnAdd, btnViewOrder, btnPlaceOrder;
    ProgressDialog progressDialog = new ProgressDialog();
    String customerMasterId, activityName;
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    ArrayList<TaxMaster> alTaxMaster;
    OrderMaster objOrderMaster;
    View snackFocus;
    FrameLayout checkOutMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        checkOutMainLayout = (FrameLayout) findViewById(R.id.checkOutMainLayout);

        tbTakeAway = (ToggleButton) findViewById(R.id.tbTakeAway);
        tbHomeDelivery = (ToggleButton) findViewById(R.id.tbHomeDelivery);

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPay = (TextView) findViewById(R.id.txtPay);

        textLayout = (LinearLayout) findViewById(R.id.textLayout);

        cbGetPromoCode = (CompoundButton) findViewById(R.id.cbGetPromoCode);

        etOfferCode = (EditText) findViewById(R.id.etOfferCode);
        etOrderDate = (EditText) findViewById(R.id.etOrderDate);
        etOrderTime = (EditText) findViewById(R.id.etOrderTime);

        btnApply = (Button) findViewById(R.id.btnApply);
        btnViewMore = (Button) findViewById(R.id.btnViewMore);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnViewOrder = (Button) findViewById(R.id.btnViewOrder);

        cbGetPromoCode.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        btnViewMore.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        btnViewOrder.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("OrderMaster") != null) {
            objOrderMaster = intent.getParcelableExtra("OrderMaster");
            txtPay.setText(String.format(getResources().getString(R.string.coaYouPay), Globals.dfWithPrecision.format(objOrderMaster.getNetAmount())));
        }
        if (intent.getParcelableArrayListExtra("TaxMaster") != null) {
            alTaxMaster = intent.getParcelableArrayListExtra("TaxMaster");
        }
        if (intent.getStringExtra("ParentActivity") != null) {
            activityName = intent.getStringExtra("ParentActivity");
        }

        etOfferCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    if (etOfferCode.getText().toString().equals("")) {
                        btnApply.setText(getResources().getString(R.string.coaCancel));
                        OfferMaster objOfferMaster = new OfferMaster();
                        objOfferMaster.setOfferCode("Remove");
                        SaveCheckOutData(null, objOfferMaster);
                    } else {
                        btnApply.setText(getResources().getString(R.string.coaApply));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tbHomeDelivery.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    if (isChecked) {
                        if (tbTakeAway.isChecked()) {
                            if (objCheckOut != null && objCheckOut.getObjOfferMaster() != null && objCheckOut.getOrderType() != Globals.OrderType.HomeDelivery.getValue()) {
                                cbGetPromoCode.setVisibility(View.VISIBLE);
                                etOfferCode.setVisibility(View.GONE);
                                btnApply.setVisibility(View.GONE);
                                OfferMaster objOfferMaster = new OfferMaster();
                                objOfferMaster.setOfferCode("Remove");
                                SaveCheckOutData(null, objOfferMaster);
                            }
                            tbTakeAway.setChecked(false);
                        } else {
                            buttonView.setChecked(true);
                        }
                    }              
                }
            }
        });
        tbTakeAway.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    if (isChecked) {
                        if (tbHomeDelivery.isChecked()) {
                            if (objCheckOut != null && objCheckOut.getObjOfferMaster() != null && objCheckOut.getOrderType() != Globals.OrderType.TakeAway.getValue()) {
                                cbGetPromoCode.setVisibility(View.VISIBLE);
                                etOfferCode.setVisibility(View.GONE);
                                btnApply.setVisibility(View.GONE);
                                OfferMaster objOfferMaster = new OfferMaster();
                                objOfferMaster.setOfferCode("Remove");
                                SaveCheckOutData(null, objOfferMaster);
                            }
                            tbHomeDelivery.setChecked(false);
                        } else {
                            buttonView.setChecked(true);
                        }
                    }
                }
            }
        });

        if (Service.CheckNet(this)) {
            RequestCustomerMaster();
        } else {
            Globals.ShowSnackBar(getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(getSupportFragmentManager().getBackStackEntryCount()!=1) {
                SaveCheckOutData(null, null);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(getSupportFragmentManager().getBackStackEntryCount()==0) {
            if (v.getId() == R.id.cbGetPromoCode) {
                cbGetPromoCode.setVisibility(View.GONE);
                etOfferCode.setVisibility(View.VISIBLE);
                btnApply.setVisibility(View.VISIBLE);
            } else if (v.getId() == R.id.btnApply) {
                snackFocus = v;
                if (btnApply.getText().equals(getResources().getString(R.string.coaCancel))) {
                    cbGetPromoCode.setVisibility(View.VISIBLE);
                    etOfferCode.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                } else {
                    RequestVerifyOfferCode();
                }
            } else if (v.getId() == R.id.btnViewMore) {
                AddressSelectorBottomDialog addressSelectorBottomDialog = new AddressSelectorBottomDialog(alCustomerAddressTran);
                addressSelectorBottomDialog.show(getSupportFragmentManager(), "");
            } else if (v.getId() == R.id.btnViewOrder) {
                SaveCheckOutData(null, null);
                finish();
            } else if (v.getId() == R.id.btnPlaceOrder) {
                ConfirmDialog confirmDialog = new ConfirmDialog(objCheckOut);
                confirmDialog.show(getSupportFragmentManager(), "");
            } else if (v.getId() == R.id.btnAdd) {
                Globals.ReplaceFragment(new AddAddressFragment(CheckOutActivity.this, null), getSupportFragmentManager(), getResources().getString(R.string.title_add_address_fragment), R.id.checkOutMainLayout);
            }
        }
    }

    public void OrderDateOnClick(View view) {
        if(getSupportFragmentManager().getBackStackEntryCount()==0) {
            Globals.ShowDatePickerDialog(etOrderDate, CheckOutActivity.this, true);
        }
    }

    public void OrderTimeOnClick(View view) {
        if(getSupportFragmentManager().getBackStackEntryCount()==0) {
            Globals.ShowTimePickerDialog(etOrderTime, CheckOutActivity.this,true);
        }
    }

    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        this.alCustomerAddressTran = alCustomerAddressTran;
        SetPrimaryAddress();
    }

    @Override
    public void AddressSelectorResponse(CustomerAddressTran objCustomerAddressTran) {
        SaveCheckOutData(objCustomerAddressTran, null);
        SetPrimaryAddress();
    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster) {
        progressDialog.dismiss();
        SaveCheckOutData(null, objOfferMaster);
        SetOffer(objOfferMaster);
    }

    @Override
    public void OrderMasterResponse(String errorCode, OrderMaster objOrderMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void ConfirmResponse() {
        RequestOrderMaster();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_add_address_fragment))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_address_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }else {
            SaveCheckOutData(null, null);
            finish();
        }
    }

    @Override
    public void AddNewAddress(CustomerAddressTran objCustomerAddressTran) {
        SaveCheckOutData(objCustomerAddressTran,null);
        SetCheckOutData(null);
        alCustomerAddressTran.add(0,objCustomerAddressTran);
    }

    //region Private Methods
    private void RequestCustomerMaster() {
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this) != null) {
            customerMasterId = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this);
        }
        objCustomerAddressJSONParser.SelectAllCustomerAddressTran(CheckOutActivity.this, null, customerMasterId);
    }

    private void SetPrimaryAddress() {
        if (objCheckOut == null) {
            for (CustomerAddressTran objCustomerAddressTran : alCustomerAddressTran) {
                if (objCustomerAddressTran.getIsPrimary()) {
                    SetCheckOutData(objCustomerAddressTran);
                    SaveCheckOutData(objCustomerAddressTran, null);
                    break;
                }
            }
        } else {
            SetCheckOutData(null);
        }
    }

    private void RequestVerifyOfferCode() {
        progressDialog.show(getSupportFragmentManager(), "");
        OfferJSONParser offerJSONParser = new OfferJSONParser();
        OfferMaster objOfferMaster = new OfferMaster();
        objOfferMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objOfferMaster.setLinktoCustomerMasterId(Short.parseShort(customerMasterId));
        objOfferMaster.setOfferCode(etOfferCode.getText().toString());
        if (tbHomeDelivery.isChecked()) {
            objOfferMaster.setlinktoOrderTypeMasterId((short) Globals.OrderType.HomeDelivery.getValue());
        } else if (tbTakeAway.isChecked()) {
            objOfferMaster.setlinktoOrderTypeMasterId((short) Globals.OrderType.TakeAway.getValue());
        }
        objOfferMaster.setMinimumBillAmount(objOrderMaster.getNetAmount());
        offerJSONParser.SelectOfferCodeVerification(CheckOutActivity.this, objOfferMaster);
    }

    private void SetOffer(OfferMaster objOfferMaster) {
        if (objOfferMaster == null) {
            Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeFailed), CheckOutActivity.this, 2000);
        } else {
            if (objOfferMaster.getOfferCode() == null) {
                Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeFailed), CheckOutActivity.this, 2000);
            } else {
                Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgOfferCodeSuccess), CheckOutActivity.this, 2000);
            }
        }
    }

    private void SetCheckOutData(CustomerAddressTran objCustomerAddress) {
        if (objCustomerAddress == null) {
            txtName.setText(objCheckOut.getObjCustomerAddressTran().getCustomerName());
            if (objCheckOut.getObjCustomerAddressTran().getMobileNum() != null) {
                txtPhone.setText(objCheckOut.getObjCustomerAddressTran().getMobileNum());
            }
            if (objCheckOut.getObjCustomerAddressTran().getCity() != null) {
                txtCity.setText(objCheckOut.getObjCustomerAddressTran().getCity());
            }
            if (objCheckOut.getObjCustomerAddressTran().getArea() != null) {
                txtArea.setText(objCheckOut.getObjCustomerAddressTran().getArea());
            }
            if (objCheckOut.getObjCustomerAddressTran().getAddress() != null) {
                txtAddress.setText(objCheckOut.getObjCustomerAddressTran().getAddress());
            }
            if (objCheckOut.getOrderType() == Globals.OrderType.HomeDelivery.getValue()) {
                tbHomeDelivery.setChecked(true);
            } else if (objCheckOut.getOrderType() == Globals.OrderType.TakeAway.getValue()) {
                tbTakeAway.setChecked(true);
            }
            etOrderDate.setText(objCheckOut.getOrderDate());
            etOrderTime.setText(objCheckOut.getOrderTime());
            if (objCheckOut.getObjOfferMaster() != null) {
                cbGetPromoCode.setVisibility(View.GONE);
                etOfferCode.setVisibility(View.VISIBLE);
                btnApply.setVisibility(View.VISIBLE);
                etOfferCode.setText(objCheckOut.getObjOfferMaster().getOfferCode());
            } else {
                cbGetPromoCode.setVisibility(View.VISIBLE);
                etOfferCode.setVisibility(View.GONE);
                btnApply.setVisibility(View.GONE);
            }
        } else {
            txtName.setText(objCustomerAddress.getCustomerName());
            if (objCustomerAddress.getMobileNum() != null) {
                txtPhone.setText(objCustomerAddress.getMobileNum());
            }
            if (objCustomerAddress.getCity() != null) {
                txtCity.setText(objCustomerAddress.getCity());
            }
            if (objCustomerAddress.getArea() != null) {
                txtArea.setText(objCustomerAddress.getArea());
            }
            if (objCustomerAddress.getAddress() != null) {
                txtAddress.setText(objCustomerAddress.getAddress());
            }
            if (Globals.linktoOrderTypeMasterId == Globals.OrderType.HomeDelivery.getValue()) {
                tbHomeDelivery.setChecked(true);
            } else if (Globals.linktoOrderTypeMasterId == Globals.OrderType.TakeAway.getValue()) {
                tbTakeAway.setChecked(true);
            }
            etOrderDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(new Date()));
            etOrderTime.setText(new SimpleDateFormat(Globals.TimeFormat, Locale.US).format(new Date()));

            cbGetPromoCode.setVisibility(View.VISIBLE);
            etOfferCode.setVisibility(View.GONE);
            btnApply.setVisibility(View.GONE);

        }

    }

    private void SaveCheckOutData(CustomerAddressTran objCustomerAddress, OfferMaster objOffer) {
        if (objCheckOut == null) {
            objCheckOut = new CheckOut();
        }
        if (tbHomeDelivery.isChecked()) {
            objCheckOut.setOrderType(Globals.OrderType.HomeDelivery.getValue());
        } else if (tbTakeAway.isChecked()) {
            objCheckOut.setOrderType(Globals.OrderType.TakeAway.getValue());
        }
        objCheckOut.setOrderDate(etOrderDate.getText().toString());
        objCheckOut.setOrderTime(etOrderTime.getText().toString());
        if (objCustomerAddress != null) {
            objCheckOut.setObjCustomerAddressTran(objCustomerAddress);
        }
        if (objOffer != null) {
            if (objOffer.getOfferCode().equals("Remove")) {
                objCheckOut.setObjOfferMaster(null);
            } else {
                objCheckOut.setObjOfferMaster(objOffer);
            }
        }
    }

    private void RequestOrderMaster() {
        progressDialog.show(getSupportFragmentManager(), "");

        objOrderMaster.setLinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        objOrderMaster.setlinktoOrderTypeMasterId((short) objCheckOut.getOrderType());
        objOrderMaster.setlinktoCustomerAddressTranId(objCheckOut.getObjCustomerAddressTran().getCustomerAddressTranId());
        objOrderMaster.setOfferCode(objCheckOut.getObjOfferMaster() != null ? objCheckOut.getObjOfferMaster().getOfferCode() : null);
        objOrderMaster.setlinktoOfferMasterId(objCheckOut.getObjOfferMaster() != null ? objCheckOut.getObjOfferMaster().getOfferMasterId() : 0);

        OrderJSONParser orderJSONParser = new OrderJSONParser();
        orderJSONParser.InsertOrderMaster(objOrderMaster, Globals.alOrderItemTran, alTaxMaster, CheckOutActivity.this, null);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgServerNotResponding), CheckOutActivity.this, 1000);
                break;
            case "0":
                Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgConfirmOrder), CheckOutActivity.this, 1000);
                if (activityName != null && activityName.equals(getResources().getString(R.string.title_activity_wish_list))) {
                    RemoveWishListFromSharePreference();
                }
                Globals.ClearCartData();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ShowMessage", false);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }
    }

    private void RemoveWishListFromSharePreference() {
        ArrayList<String> alNewString = new ArrayList<>();
        boolean isDuplicate;
        ItemAdapter.alWishItemMaster = new ArrayList<>();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", CheckOutActivity.this) != null) {
            ArrayList<String> alOldString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", CheckOutActivity.this);
            for (String strItemMasterId : alOldString) {
                isDuplicate = false;
                for (ItemMaster objItemMaster : Globals.alOrderItemTran) {
                    if (strItemMasterId.equals(String.valueOf(objItemMaster.getItemMasterId()))) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    alNewString.add(strItemMasterId);
                    ItemMaster objWishItemMaster = new ItemMaster();
                    objWishItemMaster.setItemMasterId(Integer.parseInt(strItemMasterId));
                    objWishItemMaster.setIsChecked((short) 1);
                    ItemAdapter.alWishItemMaster.add(objWishItemMaster);
                }
            }
            objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alNewString, CheckOutActivity.this);
        }
    }
    //endregion
}
