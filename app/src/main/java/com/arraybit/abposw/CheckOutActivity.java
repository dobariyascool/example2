package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CheckOut;
import com.arraybit.modal.CustomerAddressTran;
import com.arraybit.modal.OfferMaster;
import com.arraybit.modal.OrderMaster;
import com.arraybit.modal.TaxMaster;
import com.arraybit.parser.CustomerAddressJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, CustomerAddressJSONParser.CustomerAddressRequestListener, AddressSelectorBottomDialog.AddressSelectorResponseListener, OfferJSONParser.OfferRequestListener {

    public static OfferMaster objOffer;
    public static CustomerAddressTran objCustomerAddressTran;
    public static CheckOut objCheckOut;
    LinearLayout textLayout;
    TextView txtCity, txtArea, txtAddress, txtPhone, txtName, txtPay;
    CompoundButton cbGetPromoCode;
    ToggleButton tbHomeDelivery, tbTakeAway;
    EditText etOfferCode, etOrderDate, etOrderTime;
    Button btnApply, btnViewMore, btnAdd, btnViewOrder, btnPlaceOrder;
    ProgressDialog progressDialog = new ProgressDialog();
    String customerMasterId;
    ArrayList<CustomerAddressTran> alCustomerAddressTran;
    ArrayList<TaxMaster> alTaxMaster;
    OrderMaster objOrderMaster;
    View snackFocus;


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

        etOfferCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etOfferCode.getText().toString().equals("")) {
                    btnApply.setText(getResources().getString(R.string.coaCancel));
                } else {
                    btnApply.setText(getResources().getString(R.string.coaApply));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tbHomeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tbHomeDelivery.isChecked()) {
                    if (tbTakeAway.isChecked()) {
                        tbTakeAway.setChecked(false);
                    } else {
                        tbHomeDelivery.setChecked(true);
                    }
                }
            }
        });
        tbHomeDelivery.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (tbTakeAway.isChecked()) {
                        tbTakeAway.setChecked(false);
                    } else {
                        buttonView.setChecked(true);
                    }
                }
            }
        });
        tbTakeAway.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (tbHomeDelivery.isChecked()) {
                        tbHomeDelivery.setChecked(false);
                    } else {
                        buttonView.setChecked(true);
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cbGetPromoCode) {
            cbGetPromoCode.setVisibility(View.GONE);
            etOfferCode.setVisibility(View.VISIBLE);
            btnApply.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btnApply) {
            snackFocus = v;
            if (btnApply.getText().equals(getResources().getString(R.string.coaCancel))) {
                objOffer = null;
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
            finish();
        }
    }

    public void OrderDateOnClick(View view) {
        Globals.ShowDatePickerDialog(etOrderDate, CheckOutActivity.this, true);
    }

    public void OrderTimeOnClick(View view) {
        Globals.ShowTimePickerDialog(etOrderTime, CheckOutActivity.this);
    }

    @Override
    public void CustomerAddressResponse(String errorCode, ArrayList<CustomerAddressTran> alCustomerAddressTran, CustomerAddressTran objCustomerAddressTran) {
        progressDialog.dismiss();
        this.alCustomerAddressTran = alCustomerAddressTran;
        SetPrimaryAddress(null);
    }

    @Override
    public void AddressSelectorResponse(CustomerAddressTran objCustomerAddressTran) {
        this.objCustomerAddressTran = objCustomerAddressTran;
        SetPrimaryAddress(objCustomerAddressTran);
    }

    @Override
    public void OfferResponse(ArrayList<OfferMaster> alOfferMaster, OfferMaster objOfferMaster) {
        progressDialog.dismiss();
        SetOffer(objOfferMaster);
    }

    private void RequestCustomerMaster() {
        progressDialog.show(getSupportFragmentManager(), "");

        CustomerAddressJSONParser objCustomerAddressJSONParser = new CustomerAddressJSONParser();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this) != null) {
            customerMasterId = objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", CheckOutActivity.this);
        }
        objCustomerAddressJSONParser.SelectAllCustomerAddressTran(CheckOutActivity.this, null, customerMasterId);
    }

    private void SetPrimaryAddress(CustomerAddressTran objCustomerAddress) {
        if (objCustomerAddress == null) {
            for (CustomerAddressTran objCustomerAddressTran : alCustomerAddressTran) {
                if (objCustomerAddressTran.getIsPrimary()) {
                    txtName.setText(objCustomerAddressTran.getCustomerName());
                    if (objCustomerAddressTran.getMobileNum() != null) {
                        txtPhone.setText(objCustomerAddressTran.getMobileNum());
                    }
                    if (objCustomerAddressTran.getCity() != null) {
                        txtCity.setText(objCustomerAddressTran.getCity());
                    }
                    if (objCustomerAddressTran.getArea() != null) {
                        txtArea.setText(objCustomerAddressTran.getArea());
                    }
                    if (objCustomerAddressTran.getAddress() != null) {
                        txtAddress.setText(objCustomerAddressTran.getAddress());
                    }
                    etOrderDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(new Date()));
                    etOrderTime.setText(new SimpleDateFormat(Globals.TimeFormat, Locale.US).format(new Date()));
                    if (objOffer != null) {
                        cbGetPromoCode.setVisibility(View.GONE);
                        etOfferCode.setVisibility(View.VISIBLE);
                        btnApply.setVisibility(View.VISIBLE);
                        etOfferCode.setText(objOffer.getOfferCode());
                    } else {
                        cbGetPromoCode.setVisibility(View.VISIBLE);
                        etOfferCode.setVisibility(View.GONE);
                        btnApply.setVisibility(View.GONE);
                    }
                    if (Globals.linktoOrderTypeMasterId == Globals.OrderType.HomeDelivery.getValue()) {
                        tbHomeDelivery.setChecked(true);
                    } else if (Globals.linktoOrderTypeMasterId == Globals.OrderType.TakeAway.getValue()) {
                        tbTakeAway.setChecked(true);
                    }
                    break;
                }
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
                objOffer = objOfferMaster;
            }
        }
    }

    private void SetCheckOutData(CustomerAddressTran objCustomerAddress,OfferMaster objOffer){
        objCheckOut = new CheckOut();
        if(tbHomeDelivery.isChecked()) {
            objCheckOut.setOrderType(Globals.OrderType.HomeDelivery.getValue());
        }else if(tbTakeAway.isChecked()){
            objCheckOut.setOrderType(Globals.OrderType.TakeAway.getValue());
        }
        objCheckOut.setOrderDate(etOrderDate.getText().toString());
        objCheckOut.setOrderTime(etOrderTime.getText().toString());
        objCheckOut.setObjCustomerAddressTran(objCustomerAddress);
        objCheckOut.setObjOfferMaster(objOffer);
    }
}
