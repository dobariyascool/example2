package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.adapter.SpinnerAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.global.SpinnerItem;
import com.arraybit.modal.BookingMaster;
import com.arraybit.modal.BusinessMaster;
import com.arraybit.modal.CheckOut;
import com.arraybit.modal.CustomerAddressTran;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OfferMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.BookingJSONParser;
import com.arraybit.parser.BusinessJSONParser;
import com.arraybit.parser.CustomerAddressJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.arraybit.parser.OrderJSONParser;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@SuppressWarnings("ConstantConditions")
public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, AddressSelectorBottomDialog.AddressSelectorResponseListener,
        OfferJSONParser.OfferRequestListener, ConfirmDialog.ConfirmationResponseListener, OrderJSONParser.OrderMasterRequestListener, AddAddressFragment.AddNewAddressListener, BookingJSONParser.BookingRequestListener, BusinessJSONParser.BusinessRequestListener {

    public static boolean isBackPressed = false;
    CheckOut objCheckOut;
    LinearLayout textLayout;
    TextView txtCity, txtArea, txtAddress, txtPhone, txtName, txtPay, txtBusinessAddress;
    CompoundButton cbGetPromoCode;
    ToggleButton tbHomeDelivery, tbTakeAway;
    EditText etOfferCode, etOrderDate, etName, etPhone, etBusinessName;
    Button btnApply, btnViewOrder, btnPlaceOrder;
    ImageButton ibAdd, ibViewMore, ibBusinessViewMore;
    ProgressDialog progressDialog = new ProgressDialog();
    String customerMasterId, activityName;
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    ArrayList<TaxMaster> alTaxMaster;
    OrderMaster objOrderMaster;
    View snackFocus;
    FrameLayout checkOutMainLayout;
    ArrayList<SpinnerItem> alOrderTime;
    AppCompatSpinner spOrderTime, spOrderCity;
    boolean isDateChange = false, isDataFilter, isCityFilter, isSelected, isDataLoad, isCityLoad, isGroup;
    CardView cvEditName, cvCityArea, cvName, cvDateTime, cvAddress, cvOfferCode, cvPayment;
    BusinessMaster objBusinessMaster;
    ArrayList<BusinessMaster> alBusinessMaster;


    @SuppressLint("SetTextI18n")
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

        //hide keyboard focus when activity open
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this) != null) {
            customerMasterId = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this);
        }

        checkOutMainLayout = (FrameLayout) findViewById(R.id.checkOutMainLayout);
        Globals.SetScaleImageBackground(CheckOutActivity.this,null,null,checkOutMainLayout);

        cvEditName = (CardView) findViewById(R.id.cvEditName);
        cvCityArea = (CardView) findViewById(R.id.cvCityArea);
        cvName = (CardView) findViewById(R.id.cvName);
        cvDateTime = (CardView) findViewById(R.id.cvDateTime);
        cvAddress = (CardView) findViewById(R.id.cvAddress);
        cvOfferCode = (CardView) findViewById(R.id.cvOfferCode);
        cvPayment = (CardView) findViewById(R.id.cvPayment);

        tbTakeAway = (ToggleButton) findViewById(R.id.tbTakeAway);
        tbHomeDelivery = (ToggleButton) findViewById(R.id.tbHomeDelivery);

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPay = (TextView) findViewById(R.id.txtPay);
        txtBusinessAddress = (TextView) findViewById(R.id.txtBusinessAddress);

        textLayout = (LinearLayout) findViewById(R.id.textLayout);

        cbGetPromoCode = (CompoundButton) findViewById(R.id.cbGetPromoCode);

        etOfferCode = (EditText) findViewById(R.id.etOfferCode);
        etOrderDate = (EditText) findViewById(R.id.etOrderDate);
        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etBusinessName = (EditText) findViewById(R.id.etBusinessName);

        spOrderTime = (AppCompatSpinner) findViewById(R.id.spOrderTime);
        spOrderCity = (AppCompatSpinner) findViewById(R.id.spOrderCity);

        btnApply = (Button) findViewById(R.id.btnApply);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnViewOrder = (Button) findViewById(R.id.btnViewOrder);

        ibAdd = (ImageButton) findViewById(R.id.ibAdd);
        ibViewMore = (ImageButton) findViewById(R.id.ibViewMore);
        ibBusinessViewMore = (ImageButton) findViewById(R.id.ibBusinessViewMore);

        cbGetPromoCode.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        ibAdd.setOnClickListener(this);
        ibViewMore.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        btnViewOrder.setOnClickListener(this);
        ibBusinessViewMore.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("OrderMaster") != null) {
            objOrderMaster = intent.getParcelableExtra("OrderMaster");
            txtPay.setText(getResources().getString(R.string.cifRupee) + " " + Globals.dfWithPrecision.format(objOrderMaster.getNetAmount()));
        }
        if (intent.getParcelableArrayListExtra("TaxMaster") != null) {
            alTaxMaster = intent.getParcelableArrayListExtra("TaxMaster");
        }
        if (intent.getStringExtra("ParentActivity") != null) {
            activityName = intent.getStringExtra("ParentActivity");
        }

        //check data from sharePreference
        SaveCheckOutObjectInSharePreference();

        if (objCheckOut == null) {
            isDataFilter = false;
            SetCardVisibility(Globals.linktoOrderTypeMasterId, true);
        } else {
            if (objCheckOut.getOrderType() == Globals.OrderType.HomeDelivery.getValue()) {
                isDataFilter = false;
                SetCardVisibility(objCheckOut.getOrderType(), false);
            } else {
                //every time check linktobusinessGroupMasterId
                if (Service.CheckNet(this)) {
                    isGroup = true;
                    RequestBusinessMaster((short) 0, null);
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
        }

        etOrderDate.addTextChangedListener(new TextWatcher() {
            String strDate = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etOrderDate.getText().toString().equals("") && !(strDate.equals(etOrderDate.getText().toString()))) {
                    if (!isSelected) {
                        strDate = etOrderDate.getText().toString();
                        if (objCheckOut != null && !objCheckOut.getOrderDate().equals(etOrderDate.getText().toString())) {
                            etOfferCode.setText("");
                            cbGetPromoCode.setVisibility(View.VISIBLE);
                            etOfferCode.setVisibility(View.GONE);
                            btnApply.setVisibility(View.GONE);
                            isDateChange = true;
                            if (Service.CheckNet(CheckOutActivity.this)) {
                                isDateChange = true;
                                RequestTimeSlots();
                            } else {
                                Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), CheckOutActivity.this, 1000);
                            }
                        }

                    } else {
                        isSelected = false;
                    }
                }
            }
        });

        spOrderTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //((android.widget.TextView) view).setTextColor(ContextCompat.getColor(CheckOutActivity.this, R.color.white_blur));
                if (!isSelected) {
                    if (tbHomeDelivery.isChecked()) {
                        SaveCheckOutData(null, null, Globals.OrderType.HomeDelivery.getValue());
                    } else if (tbTakeAway.isChecked()) {
                        SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                    }
                } else {
                    isSelected = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spOrderCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSelected) {
                    if (Service.CheckNet(CheckOutActivity.this)) {
                        isCityFilter = true;
                        RequestBusinessMaster(alBusinessMaster.get(0).getLinktoBusinessGroupMasterId(), (String) parent.getAdapter().getItem(position));
                    } else {
                        Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), CheckOutActivity.this, 1000);
                    }
                } else {
                    if (objCheckOut == null) {
                        isSelected = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etOfferCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    if (etOfferCode.getText().toString().equals("")) {
                        btnApply.setText(getResources().getString(R.string.coaCancel));
                        OfferMaster objOfferMaster = new OfferMaster();
                        objOfferMaster.setOfferCode("Remove");
                        if (tbHomeDelivery.isChecked()) {
                            SaveCheckOutData(null, objOfferMaster, Globals.OrderType.HomeDelivery.getValue());
                        } else if (tbTakeAway.isChecked()) {
                            SaveCheckOutData(null, objOfferMaster, Globals.OrderType.TakeAway.getValue());
                        }

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
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    if (isChecked) {
                        if (tbTakeAway.isChecked()) {
                            isDataFilter = true;
                            SetCardVisibility(Globals.OrderType.HomeDelivery.getValue(), true);
                            tbTakeAway.setChecked(false);
                        }
                    } else {
                        if (tbTakeAway.isChecked()) {
                            buttonView.setChecked(false);
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
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    if (isChecked) {
                        if (tbHomeDelivery.isChecked()) {
                            isDataFilter = true;
                            SetCardVisibility(Globals.OrderType.TakeAway.getValue(), true);
                            tbHomeDelivery.setChecked(false);
                        }
                    } else {
                        if (tbHomeDelivery.isChecked()) {
                            buttonView.setChecked(false);
                        } else {
                            buttonView.setChecked(true);
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() != 1) {
                isBackPressed = true;
                if (tbTakeAway.isChecked()) {
                    SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                } else if (tbHomeDelivery.isChecked()) {
                    SaveCheckOutData(null, null, Globals.OrderType.HomeDelivery.getValue());
                }
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
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
                    if (Service.CheckNet(this)) {
                        RequestVerifyOfferCode();
                    } else {
                        Globals.ShowSnackBar(snackFocus, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                    }
                }
            } else if (v.getId() == R.id.ibViewMore) {
                if (Service.CheckNet(this)) {
                    isDataLoad = true;
                    RequestCustomerMaster();
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }

            } else if (v.getId() == R.id.btnViewOrder) {
                isBackPressed = true;
                if (tbTakeAway.isChecked()) {
                    SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                } else if (tbHomeDelivery.isChecked()) {
                    SaveCheckOutData(null, null, Globals.OrderType.HomeDelivery.getValue());
                }
                finish();
            } else if (v.getId() == R.id.btnPlaceOrder) {
                ConfirmDialog confirmDialog = new ConfirmDialog(objCheckOut, false, null);
                confirmDialog.show(getSupportFragmentManager(), "");
            } else if (v.getId() == R.id.ibAdd) {
                Globals.ReplaceFragment(new AddAddressFragment(CheckOutActivity.this, null), getSupportFragmentManager(), getResources().getString(R.string.title_add_address_fragment), R.id.checkOutMainLayout);
            } else if (v.getId() == R.id.ibBusinessViewMore) {
                if (spOrderCity != null && spOrderCity.getAdapter() != null) {
                    if (Service.CheckNet(this)) {
                        isDataLoad = true;
                        if (objCheckOut.getObjBusinessMaster() != null) {
                            RequestBusinessMaster(objCheckOut.getObjBusinessMaster().getLinktoBusinessGroupMasterId(), (String) spOrderCity.getAdapter().getItem(spOrderCity.getSelectedItemPosition()));
                        } else {
                            RequestBusinessMaster(objBusinessMaster.getLinktoBusinessGroupMasterId(), (String) spOrderCity.getAdapter().getItem(spOrderCity.getSelectedItemPosition()));
                        }
                    } else {
                        Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                    }
                }
            }
        }
    }

    public void OrderDateOnClick(View view) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Globals.ShowDatePickerDialog(etOrderDate, CheckOutActivity.this, true);
        }
    }


    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        this.alCustomerAddressTran = alCustomerAddressTran;
        if (isDataLoad) {
            isDataLoad = false;
            if (alCustomerAddressTran != null) {
                AddressSelectorBottomDialog addressSelectorBottomDialog = new AddressSelectorBottomDialog(alCustomerAddressTran, null);
                addressSelectorBottomDialog.show(getSupportFragmentManager(), "");
            }
        } else {
            if (Service.CheckNet(this)) {
                RequestTimeSlots();
            } else {
                Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
            }
        }

    }

    @Override
    public void AddressSelectorResponse(CustomerAddressTran objCustomerAddressTran, BusinessMaster objBusinessMaster) {
        if (objCustomerAddressTran != null) {
            SaveCheckOutData(objCustomerAddressTran, null, Globals.OrderType.HomeDelivery.getValue());
            SetPrimaryAddress();
        } else if (objBusinessMaster != null) {
            this.objBusinessMaster = objBusinessMaster;
            SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
            SetBusinessAddress();
        }
    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster) {
        progressDialog.dismiss();
        SetOffer(objOfferMaster);
        if (tbTakeAway.isChecked()) {
            SaveCheckOutData(null, objOfferMaster, Globals.OrderType.TakeAway.getValue());
        } else if (tbHomeDelivery.isChecked()) {
            SaveCheckOutData(null, objOfferMaster, Globals.OrderType.HomeDelivery.getValue());
        }
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
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getResources().getString(R.string.title_add_address_fragment))) {
                getSupportFragmentManager().popBackStack(getResources().getString(R.string.title_add_address_fragment), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            isBackPressed = true;
            if (tbTakeAway.isChecked()) {
                SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
            } else if (tbHomeDelivery.isChecked()) {
                SaveCheckOutData(null, null, Globals.OrderType.HomeDelivery.getValue());
            }
            finish();
        }
    }

    @Override
    public void AddNewAddress(CustomerAddressTran objCustomerAddressTran) {
        SaveCheckOutData(objCustomerAddressTran, null, Globals.OrderType.HomeDelivery.getValue());
        SetCheckOutData(null, Globals.OrderType.HomeDelivery.getValue());
    }

    @Override
    public void BookingResponse(String errorCode, ArrayList<BookingMaster> alBookingMaster) {

    }


    @Override
    public void BusinessResponse(String errorCode, BusinessMaster objBusinessMaster, ArrayList<BusinessMaster> alBusinessMaster) {
        progressDialog.dismiss();
        BusinessMasterResponse(objBusinessMaster, alBusinessMaster);
    }

    @Override
    public void TimeSlotsResponse(ArrayList<SpinnerItem> alTimeSlot) {
        progressDialog.dismiss();
        alOrderTime = alTimeSlot;
        TimeSlotResponse();
    }

    //region Private Methods
    private void RequestCustomerMaster() {
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        objCustomerAddressJSONParser.SelectAllCustomerAddressTran(CheckOutActivity.this, null, customerMasterId);
    }

    private void RequestTimeSlots() {
        progressDialog.show(getSupportFragmentManager(), "");

        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllTimeSlots(null, CheckOutActivity.this, String.valueOf(Globals.linktoBusinessMasterId), etOrderDate.getText().toString(), true);
    }

    private void FillOrderTime() {
        if (alOrderTime != null && alOrderTime.size() != 0) {
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(getResources().getString(R.string.coaTime));
            objSpinnerItem.setValue(0);

            alOrderTime.add(0, objSpinnerItem);

            SpinnerAdapter adapter = new SpinnerAdapter(CheckOutActivity.this, alOrderTime, true,true);
            spOrderTime.setAdapter(adapter);
            isSelected = true;
        }
    }

    private void SetPrimaryAddress() {
        if (objCheckOut == null) {
            for (CustomerAddressTran objCustomerAddressTran : alCustomerAddressTran) {
                if (objCustomerAddressTran.getIsPrimary()) {
                    SetCheckOutData(objCustomerAddressTran, Globals.OrderType.HomeDelivery.getValue());
                    SaveCheckOutData(objCustomerAddressTran, null, Globals.OrderType.HomeDelivery.getValue());
                    break;
                }
            }
        } else {
            SetCheckOutData(null, Globals.OrderType.HomeDelivery.getValue());
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

    private void SetCheckOutData(CustomerAddressTran objCustomerAddress, int orderType) {
        if (orderType == Globals.OrderType.HomeDelivery.getValue()) {
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
                tbHomeDelivery.setChecked(true);
                isSelected = true;
                spOrderTime.setSelection(objCheckOut.getOrderTimeIndex());
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
                } else {
                    txtArea.setText("");
                }
                if (objCustomerAddress.getAddress() != null) {
                    txtAddress.setText(objCustomerAddress.getAddress());
                }
                tbHomeDelivery.setChecked(true);
                cbGetPromoCode.setVisibility(View.VISIBLE);
                etOfferCode.setVisibility(View.GONE);
                btnApply.setVisibility(View.GONE);

            }
        } else if (orderType == Globals.OrderType.TakeAway.getValue()) {
            if (objCheckOut == null) {
                tbTakeAway.setChecked(true);
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", CheckOutActivity.this) != null) {
                    etName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", CheckOutActivity.this));
                }
                if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", CheckOutActivity.this) != null) {
                    etPhone.setText(objSharePreferenceManage.GetPreference("LoginPreference", "Phone", CheckOutActivity.this));
                }
                FillCity();
                SetBusinessAddress();
                cbGetPromoCode.setVisibility(View.VISIBLE);
                etOfferCode.setVisibility(View.GONE);
                btnApply.setVisibility(View.GONE);
            } else {
                tbTakeAway.setChecked(true);
                isSelected = true;
                spOrderTime.setSelection(objCheckOut.getOrderTimeIndex());
                FillCity();
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
                etName.setText(objCheckOut.getName());
                if (objCheckOut.getPhone() != null && !objCheckOut.getPhone().equals("")) {
                    etPhone.setText(objCheckOut.getPhone());
                }
                spOrderCity.setSelection(objCheckOut.getCityIndex());
                if (objCheckOut.getObjBusinessMaster() != null) {
                    etBusinessName.setText(objCheckOut.getObjBusinessMaster().getBusinessName());
                    etBusinessName.setEnabled(false);
                    txtBusinessAddress.setText(objCheckOut.getObjBusinessMaster().getAddress());
                }
            }
        }
    }

    private void FillCity() {
        if (alBusinessMaster == null && objBusinessMaster != null) {
            ibBusinessViewMore.setVisibility(View.INVISIBLE);
            SpinnerItem objSpinnerItem = new SpinnerItem();
            objSpinnerItem.setText(objBusinessMaster.getCity());
            objSpinnerItem.setValue(0);

            ArrayList<SpinnerItem> alCity = new ArrayList<>();
            alCity.add(objSpinnerItem);
            SpinnerAdapter cityAdapter = new SpinnerAdapter(CheckOutActivity.this, alCity, true,true);
            spOrderCity.setAdapter(cityAdapter);
            isSelected = true;
        } else {
            ibBusinessViewMore.setVisibility(View.VISIBLE);
            ArrayList<SpinnerItem> alCity = new ArrayList<>();
            short cnt = 0;
            boolean isDuplicate = false;
            for (BusinessMaster objBusiness : alBusinessMaster) {
                SpinnerItem objSpinnerItem = new SpinnerItem();
                if (alCity.size() == 0) {
                    objSpinnerItem.setText(objBusiness.getCity());
                    objSpinnerItem.setValue(cnt);
                    alCity.add(objSpinnerItem);
                } else {
                    for (SpinnerItem objSpinner : alCity) {
                        if (objSpinner.getText().equals(objBusiness.getCity())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        objSpinnerItem.setText(objBusiness.getCity());
                        objSpinnerItem.setValue(cnt);
                        alCity.add(objSpinnerItem);
                    }
                }
                cnt++;
            }
            SpinnerAdapter cityAdapter = new SpinnerAdapter(CheckOutActivity.this, alCity, true,true);
            spOrderCity.setAdapter(cityAdapter);
            isSelected = true;
        }
    }

    private void SetBusinessAddress() {
        if (objBusinessMaster != null) {
            etBusinessName.setText(objBusinessMaster.getBusinessName());
            etBusinessName.setEnabled(false);
            txtBusinessAddress.setText(objBusinessMaster.getAddress());
        } else if (alBusinessMaster != null) {
            etBusinessName.setText(alBusinessMaster.get(0).getBusinessName());
            etBusinessName.setEnabled(false);
            txtBusinessAddress.setText(alBusinessMaster.get(0).getAddress());
            objBusinessMaster = alBusinessMaster.get(0);
        }
    }

    private void SetCardVisibility(int orderType, boolean isDataLoad) {
        if (orderType == Globals.OrderType.TakeAway.getValue()) {
            cvEditName.setVisibility(View.VISIBLE);
            cvName.setVisibility(View.GONE);
            cvCityArea.setVisibility(View.VISIBLE);
            cvAddress.setVisibility(View.GONE);
            cvOfferCode.setVisibility(View.VISIBLE);
            cvDateTime.setVisibility(View.VISIBLE);
            cvPayment.setVisibility(View.VISIBLE);
            if (objCheckOut == null) {
                isSelected = true;
                etOrderDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(new Date()));
            } else {
                isSelected = true;
                etOrderDate.setText(objCheckOut.getOrderDate());
            }
            if (isDataLoad) {
                if (Service.CheckNet(this)) {
                    RequestBusinessMaster((short) 0, null);
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            } else {
                objBusinessMaster = objCheckOut.getObjBusinessMaster();
                if (Service.CheckNet(this)) {
                    if (objBusinessMaster != null && objBusinessMaster.getLinktoBusinessGroupMasterId() == 0) {
                        RequestTimeSlots();
                    } else if (objBusinessMaster != null) {
                        isCityLoad = true;
                        RequestBusinessMaster(objBusinessMaster.getLinktoBusinessGroupMasterId(), null);
                    }
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }

        } else if (orderType == Globals.OrderType.HomeDelivery.getValue()) {
            cvEditName.setVisibility(View.GONE);
            cvName.setVisibility(View.VISIBLE);
            cvCityArea.setVisibility(View.GONE);
            cvAddress.setVisibility(View.VISIBLE);
            cvOfferCode.setVisibility(View.VISIBLE);
            cvDateTime.setVisibility(View.VISIBLE);
            cvPayment.setVisibility(View.VISIBLE);
            if (objCheckOut == null) {
                isSelected = true;
                etOrderDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(new Date()));
            } else {
                isSelected = true;
                etOrderDate.setText(objCheckOut.getOrderDate());
            }
            if (isDataLoad) {
                if (Service.CheckNet(this)) {
                    RequestCustomerMaster();
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            } else {
                if (Service.CheckNet(this)) {
                    RequestTimeSlots();
                } else {
                    Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
        }
    }

    private void RequestBusinessMaster(short linktoBusinessGroupMasterId, String city) {
        progressDialog.show(getSupportFragmentManager(), "");
        BusinessJSONParser objBusinessJSONParser = new BusinessJSONParser();
        if (linktoBusinessGroupMasterId == 0) {
            objBusinessJSONParser.SelectBusinessMaster(CheckOutActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
        } else {
            objBusinessJSONParser.SelectAllBusinessMasterByBusinessGroup(CheckOutActivity.this, String.valueOf(linktoBusinessGroupMasterId), city);
        }

    }

    private void SaveCheckOutData(CustomerAddressTran objCustomerAddress, OfferMaster objOffer, int orderType) {
        if (objCheckOut == null) {
            objCheckOut = new CheckOut();
        }
        if (orderType == Globals.OrderType.HomeDelivery.getValue()) {
            objCheckOut.setOrderType(orderType);
            objCheckOut.setOrderDate(etOrderDate.getText().toString());
            objCheckOut.setOrderTimeIndex(spOrderTime.getSelectedItemPosition());
            objCheckOut.setOrderTime((String) spOrderTime.getAdapter().getItem(spOrderTime.getSelectedItemPosition()));
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
        } else {
            objCheckOut.setOrderType(orderType);
            objCheckOut.setOrderDate(etOrderDate.getText().toString());
            objCheckOut.setOrderTimeIndex(spOrderTime.getSelectedItemPosition());
            objCheckOut.setOrderTime((String) spOrderTime.getAdapter().getItem(spOrderTime.getSelectedItemPosition()));
            objCheckOut.setCityIndex(spOrderCity.getSelectedItemPosition());
            objCheckOut.setCity((String) spOrderCity.getAdapter().getItem(spOrderCity.getSelectedItemPosition()));
            objCheckOut.setBranch(etBusinessName.getText().toString());
            objCheckOut.setName(etName.getText().toString());
            objCheckOut.setPhone(etPhone.getText().toString());
            objCheckOut.setObjBusinessMaster(objBusinessMaster);
            if (objOffer != null) {
                if (objOffer.getOfferCode().equals("Remove")) {
                    objCheckOut.setObjOfferMaster(null);
                } else {
                    objCheckOut.setObjOfferMaster(objOffer);
                }
            }
        }
        SaveCheckOutObjectInSharePreference();
    }

    private void RequestOrderMaster() {
        progressDialog.show(getSupportFragmentManager(), "");
        Date orderDateTime = null;
        try {
            if (objCheckOut.getOrderTime().equals(getResources().getString(R.string.coaTime))) {
                Calendar calendar = Calendar.getInstance();
                orderDateTime = new SimpleDateFormat(Globals.DateFormat + " " + Globals.DisplayTimeFormat, Locale.US).parse(objCheckOut.getOrderDate() + " " + new SimpleDateFormat(Globals.DisplayTimeFormat, Locale.US).format(calendar.getTime()));
            } else {
                orderDateTime = new SimpleDateFormat(Globals.DateFormat + " " + Globals.DisplayTimeFormat, Locale.US).parse(objCheckOut.getOrderDate() + " " + objCheckOut.getOrderTime());

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        objOrderMaster.setOrderDateTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(orderDateTime));
        if (objCheckOut.getObjBusinessMaster() != null) {
            objOrderMaster.setLinktoBusinessMasterId(objCheckOut.getObjBusinessMaster().getBusinessMasterId());
        } else {
            objOrderMaster.setLinktoBusinessMasterId(Globals.linktoBusinessMasterId);
        }
        objOrderMaster.setlinktoOrderTypeMasterId((short) objCheckOut.getOrderType());
        if (objCheckOut.getObjCustomerAddressTran() != null) {
            objOrderMaster.setlinktoCustomerAddressTranId(objCheckOut.getObjCustomerAddressTran().getCustomerAddressTranId());
        }
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
                SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                objSharePreferenceManage.RemovePreference("CartItemListPreference", "CartItemList", CheckOutActivity.this);
                objSharePreferenceManage.ClearPreference("CartItemListPreference", CheckOutActivity.this);
                objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", CheckOutActivity.this);
                objSharePreferenceManage.ClearPreference("CheckOutDataPreference", CheckOutActivity.this);
                MenuActivity.i = 0;
                MenuActivity.isViewChange = false;
                ItemAdapter.alWishItemMaster = new ArrayList<>();
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

    private void SaveCheckOutObjectInSharePreference() {
        Gson gson = new Gson();
        SharePreferenceManage objSharePreferenceManage;
        try {
            if (objCheckOut == null) {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = objSharePreferenceManage.GetPreference("CheckOutDataPreference", "CheckOutData", CheckOutActivity.this);
                if (string != null) {
                    objCheckOut = gson.fromJson(string,
                            CheckOut.class);
                    if(activityName!=null && !activityName.equals(getResources().getString(R.string.title_home))){
                        if(activityName!=null && !activityName.equals(getResources().getString(R.string.title_activity_wish_list)))
                        {
                            if (objCheckOut.getOrderType() != Globals.linktoOrderTypeMasterId && !isBackPressed) {
                                objCheckOut = null;
                                objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", CheckOutActivity.this);
                                objSharePreferenceManage.ClearPreference("CheckOutDataPreference", CheckOutActivity.this);
                            }
                        }
                    }else if(activityName==null){
                        if (objCheckOut.getOrderType() != Globals.linktoOrderTypeMasterId && !isBackPressed) {
                            objCheckOut = null;
                            objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", CheckOutActivity.this);
                            objSharePreferenceManage.ClearPreference("CheckOutDataPreference", CheckOutActivity.this);
                        }
                    }
                }else {
                    //this case come when cart click from home page or wishlist
                    if((activityName!=null && activityName.equals(getResources().getString(R.string.title_home))) || (activityName!=null && activityName.equals(getResources().getString(R.string.title_activity_wish_list)))) {
                        if (Globals.linktoOrderTypeMasterId == 0) {
                            objCheckOut = null;
                            Globals.linktoOrderTypeMasterId = (short) Globals.OrderType.HomeDelivery.getValue();
                        }
                    }
                }

            } else {
                objSharePreferenceManage = new SharePreferenceManage();
                String string = gson.toJson(objCheckOut);
                objSharePreferenceManage.CreatePreference("CheckOutDataPreference", "CheckOutData", string, CheckOutActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BusinessMasterResponse(BusinessMaster objBusinessMaster,ArrayList<BusinessMaster> alBusinessMaster){
        if (isGroup) {
            //if linktoBusinessGroupMasterId save in preference and in database have null so clear preference
            isGroup = false;
            if (objBusinessMaster != null) {
                if (objBusinessMaster.getLinktoBusinessGroupMasterId() == 0 && objCheckOut.getObjBusinessMaster().getLinktoBusinessGroupMasterId() != 0) {
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.RemovePreference("CheckOutDataPreference", "CheckOutData", CheckOutActivity.this);
                    objSharePreferenceManage.ClearPreference("CheckOutDataPreference", CheckOutActivity.this);
                    objCheckOut = null;
                    isDataFilter = true;
                    SetCardVisibility(Globals.OrderType.TakeAway.getValue(), true);
                } else {
                    isDataFilter = false;
                    SetCardVisibility(objCheckOut.getOrderType(), false);
                }
            }
        } else {
            if (isDataLoad) {
                isDataLoad = false;
                if (alBusinessMaster != null) {
                    AddressSelectorBottomDialog addressSelectorBottomDialog = new AddressSelectorBottomDialog(null, alBusinessMaster);
                    addressSelectorBottomDialog.show(getSupportFragmentManager(), "");
                }
            } else {
                this.objBusinessMaster = objBusinessMaster;
                if (objBusinessMaster != null) {
                    if (objBusinessMaster.getLinktoBusinessGroupMasterId() == 0) {
                        if (Service.CheckNet(this)) {
                            RequestTimeSlots();
                        } else {
                            Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                        }
                    } else {
                        if (Service.CheckNet(this)) {
                            RequestBusinessMaster(objBusinessMaster.getLinktoBusinessGroupMasterId(), null);
                        } else {
                            Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                        }
                    }
                } else if (alBusinessMaster != null) {
                    this.alBusinessMaster = alBusinessMaster;
                    if (isCityLoad) {
                        isCityLoad = false;
                        if (objCheckOut != null && objCheckOut.getObjBusinessMaster() != null) {
                            this.objBusinessMaster = objCheckOut.getObjBusinessMaster();
                        } else {
                            this.objBusinessMaster = alBusinessMaster.get(0);
                        }
                        FillCity();
                        if (Service.CheckNet(this)) {
                            RequestTimeSlots();
                        } else {
                            Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                        }
                    } else {
                        if (isCityFilter) {
                            SetBusinessAddress();
                            SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                            isCityFilter = false;
                        } else {
                            if (Service.CheckNet(this)) {
                                RequestTimeSlots();
                            } else {
                                Globals.ShowSnackBar(checkOutMainLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                            }
                        }
                    }
                }
            }
        }
    }

    private void TimeSlotResponse(){
        if (isDateChange) {
            isDateChange = false;
            OfferMaster objOfferMaster = new OfferMaster();
            objOfferMaster.setOfferCode("Remove");
            FillOrderTime();
            if (tbHomeDelivery.isChecked()) {
                SaveCheckOutData(null, objOfferMaster, Globals.OrderType.HomeDelivery.getValue());
            } else if (tbTakeAway.isChecked()) {
                SaveCheckOutData(null, objOfferMaster, Globals.OrderType.TakeAway.getValue());
            }
        } else {
            if (objCheckOut == null) {
                if (Globals.linktoOrderTypeMasterId == Globals.OrderType.TakeAway.getValue()) {
                    if (objBusinessMaster != null) {
                        FillOrderTime();
                        SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                        SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                    }else if (alBusinessMaster != null) {
                        FillOrderTime();
                        SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                        SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                    }
                } else if (Globals.linktoOrderTypeMasterId == Globals.OrderType.HomeDelivery.getValue()) {
                    FillOrderTime();
                    SetPrimaryAddress();
                }
            } else {
                if (isDataFilter) {
                    objCheckOut = null;
                    if (tbTakeAway.isChecked()) {
                        if (objBusinessMaster != null) {
                            FillOrderTime();
                            SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                            SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                        } else if (alBusinessMaster != null) {
                            FillOrderTime();
                            SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                            SaveCheckOutData(null, null, Globals.OrderType.TakeAway.getValue());
                        }
                    } else if (tbHomeDelivery.isChecked()) {
                        FillOrderTime();
                        SetPrimaryAddress();
                    }
                } else {
                    if (objCheckOut.getOrderType() == Globals.OrderType.TakeAway.getValue()) {
                        if (objBusinessMaster != null) {
                            FillOrderTime();
                            SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                        } else if (alBusinessMaster != null) {
                            FillOrderTime();
                            SetCheckOutData(null, Globals.OrderType.TakeAway.getValue());
                        }
                    } else if (objCheckOut.getOrderType() == Globals.OrderType.HomeDelivery.getValue()) {
                        FillOrderTime();
                        SetPrimaryAddress();
                    }
                }
            }
        }
    }
    //endregion
}
