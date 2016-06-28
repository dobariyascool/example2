package com.arraybit.abposw;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CustomerMaster;
import com.arraybit.parser.CustomerJSONParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@SuppressWarnings({"ConstantConditions", "UnnecessaryReturnStatement"})
public class UserProfileFragment extends Fragment implements CustomerJSONParser.CustomerRequestListener, View.OnClickListener {

    CustomerMaster objCustomerMaster;
    EditText etFirstName, etMobile, etBirthDate;
    RadioButton rbMale, rbFemale;
    Button btnUpdate;
    TextView txtLoginChar, txtFullName, txtEmail;
    SharePreferenceManage objSharePreferenceManage;
    ProgressDialog progressDialog;
    int customerMasterId;
    Date birthDate;
    View view;
    UpdateResponseListener objUpdateResponseListener;
    ImageView ivProfile;
    String imagePhysicalNameBytes, imageName, strImageName;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS", Locale.US);


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getActivity().getResources().getString(R.string.title_fragment_your_profile));
        setHasOptionsMenu(true);


        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);


        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etBirthDate = (EditText) view.findViewById(R.id.etDateOfBirth);
        etBirthDate.setInputType(InputType.TYPE_NULL);


        txtLoginChar = (TextView) view.findViewById(R.id.txtLoginChar);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtFullName = (TextView) view.findViewById(R.id.txtFullName);


        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);


        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);


        setHasOptionsMenu(true);

        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            customerMasterId = Integer.valueOf(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
        }

        if (Service.CheckNet(getActivity())) {
            UserRequest();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }


        etMobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Globals.HideKeyBoard(getActivity(), v);
                }
                return false;
            }
        });

        etBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
                }
            }
        });

        ivProfile.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        txtLoginChar.setOnClickListener(this);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Globals.HideKeyBoard(getActivity(), getView());
                getActivity().getSupportFragmentManager().popBackStack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void CustomerResponse(String errorCode, CustomerMaster objCustomerMaster) {
        progressDialog.dismiss();
        if (errorCode == null || errorCode.equals("")) {
            this.objCustomerMaster = objCustomerMaster;
            SetUserName();
        } else {
            SetError(errorCode, objCustomerMaster);
        }

    }

    public void EditTextOnClick() {
        Globals.ShowDatePickerDialog(etBirthDate, getActivity(), false);
    }

    public void SelectImage(int requestCode, Intent data) {
        String picturePath = "";
        if (requestCode == 100) {
            strImageName = "CameraImage_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length()) + ".jpg";
            File file = new File(android.os.Environment.getExternalStorageDirectory(), strImageName);
            picturePath = file.getAbsolutePath();
            imageName = file.getName();
        } else if (requestCode == 101 && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            File file = new File(picturePath);
            imageName = file.getName();
            cursor.close();
        }
        if (!picturePath.equals("")) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bitmapOptions);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] byteData = bos.toByteArray();
            imagePhysicalNameBytes = Base64.encodeToString(byteData, Base64.DEFAULT);
            Glide.with(getActivity()).load(picturePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    if (txtLoginChar.getVisibility() == View.VISIBLE) {
                        txtLoginChar.setVisibility(View.GONE);
                        ivProfile.setVisibility(View.VISIBLE);
                    }
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
            return;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {
            Globals.HideKeyBoard(getActivity(), v);
            if (!ValidateControls()) {
                Globals.ShowSnackBar(v, getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                return;
            }
            if (Service.CheckNet(getActivity())) {
                UpdateUserProfileRequest();
            } else {
                Globals.ShowSnackBar(btnUpdate, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
            }
        } else if (v.getId() == R.id.ivProfile) {
            Globals.SelectImage(getActivity(), 100, 101);
        } else if (v.getId() == R.id.txtLoginChar) {
            Globals.SelectImage(getActivity(), 100, 101);
        }
    }


    // region Private Methods
    private void SetUserName() {
        if (objCustomerMaster != null) {
            txtEmail.setText(objCustomerMaster.getEmail1());
            txtLoginChar.setText(objCustomerMaster.getEmail1().substring(0, 1).toUpperCase());
            txtFullName.setText(objCustomerMaster.getCustomerName());
            etFirstName.setText(objCustomerMaster.getCustomerName());
            etMobile.setText(objCustomerMaster.getPhone1());
            if (objCustomerMaster.getGender().equals(rbFemale.getText().toString())) {
                rbFemale.setChecked(true);
            } else {
                rbMale.setChecked(true);
            }
            if (objCustomerMaster.getBirthDate() != null) {
                try {
                    birthDate = new SimpleDateFormat(Globals.DateFormat, Locale.US).parse(objCustomerMaster.getBirthDate());
                    etBirthDate.setText(new SimpleDateFormat(Globals.DateFormat, Locale.US).format(birthDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (objCustomerMaster.getXs_ImagePhysicalName() != null && !objCustomerMaster.getXs_ImagePhysicalName().equals("")) {
                ivProfile.setVisibility(View.VISIBLE);
                txtLoginChar.setVisibility(View.GONE);
                imageName = objCustomerMaster.getImageName();
                Glide.with(getActivity()).load(objCustomerMaster.getXs_ImagePhysicalName()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });
            } else {
                ivProfile.setVisibility(View.GONE);
                txtLoginChar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void UserRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getFragmentManager(), "ProgressDialog");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        objCustomerJSONParser.SelectCustomerMaster(getActivity(), null, null, String.valueOf(customerMasterId), this, String.valueOf(Globals.linktoBusinessMasterId));
    }


    private void UpdateUserProfileRequest() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");

        CustomerJSONParser objCustomerJSONParser = new CustomerJSONParser();
        CustomerMaster objCustomerMaster = new CustomerMaster();
        objSharePreferenceManage = new SharePreferenceManage();

        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            objCustomerMaster.setCustomerMasterId(Short.parseShort(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
        } else {
            objCustomerMaster.setCustomerMasterId(0);
        }
        objCustomerMaster.setCustomerName(etFirstName.getText().toString().trim());
        objCustomerMaster.setPhone1(etMobile.getText().toString().trim());
        if (rbMale.isChecked()) {
            objCustomerMaster.setGender(rbMale.getText().toString());
        }
        if (rbFemale.isChecked()) {
            objCustomerMaster.setGender(rbFemale.getText().toString());
        }
        if (!etBirthDate.getText().toString().isEmpty()) {
            try {
                birthDate = new SimpleDateFormat("d/M/yyyy", Locale.US).parse(etBirthDate.getText().toString());
                objCustomerMaster.setBirthDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(birthDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (imageName != null && !imageName.equals("")) {
            if (imagePhysicalNameBytes != null && !imagePhysicalNameBytes.equals("")) {
                strImageName = imageName.substring(0, imageName.lastIndexOf(".")) + "_" + simpleDateFormat.format(new Date()) + imageName.substring(imageName.lastIndexOf("."), imageName.length());
                objCustomerMaster.setImageNamePhysicalNameBytes(imagePhysicalNameBytes);
            }else{
                strImageName = imageName;
            }
            objCustomerMaster.setImageName(strImageName);
        }
        if(objSharePreferenceManage.GetPreference("LoginPreference","IntegrationId",getActivity())!=null){
            if(objSharePreferenceManage.GetPreference("LoginPreference", "isLoginWithFb",getActivity())!=null)
            {
                if(objSharePreferenceManage.GetPreference("LoginPreference", "isLoginWithFb",getActivity()).equals("true")){
                    objCustomerMaster.setFacebookUserId(objSharePreferenceManage.GetPreference("LoginPreference","IntegrationId",getActivity()));
                }else{
                    objCustomerMaster.setGooglePlusUserId(objSharePreferenceManage.GetPreference("LoginPreference","IntegrationId",getActivity()));
                }
            }
        }
        objCustomerJSONParser.UpdateCustomerMaster(objCustomerMaster, getActivity(), this);

    }

    private void SetError(String errorCode, CustomerMaster objCustomerMaster) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(btnUpdate, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            case "-2":
                Globals.ShowSnackBar(btnUpdate, getResources().getString(R.string.MsgUpdateprofileFail), getActivity(), 1000);
                break;
            default:
                Globals.ShowSnackBar(btnUpdate, getResources().getString(R.string.MsgUpdateProfile), getActivity(), 1000);
                ClearControls();
                if (objCustomerMaster != null) {
                    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
                    objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerName", objCustomerMaster.getCustomerName(), getActivity());
                    objSharePreferenceManage.CreatePreference("LoginPreference", "Phone", objCustomerMaster.getPhone1(), getActivity());
                    objSharePreferenceManage.CreatePreference("LoginPreference", "CustomerProfileUrl", objCustomerMaster.getXs_ImagePhysicalName(), getActivity());
                }
                getActivity().getSupportFragmentManager().popBackStack();
                objUpdateResponseListener = (UpdateResponseListener) getActivity();
                objUpdateResponseListener.UpdateResponse();
                break;
        }

    }

    private boolean ValidateControls() {
        boolean IsValid = true;
        if (!etMobile.getText().toString().equals("") && etMobile.getText().length() != 10) {
            etMobile.setError("Enter 10 digit " + getResources().getString(R.string.suPhone));
            IsValid = false;
        } else {
            etMobile.clearError();
        }
        return IsValid;
    }

    private void ClearControls() {
        etFirstName.setText("");
        etMobile.setText("");
        etBirthDate.setText("");
    }

    interface UpdateResponseListener {
        void UpdateResponse();
    }

    //endregion
}
