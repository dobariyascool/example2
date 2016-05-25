package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.adapter.ItemOptionValueAdapter;
import com.arraybit.adapter.ItemSuggestedAdapter;
import com.arraybit.adapter.ModifierAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OptionMaster;
import com.arraybit.modal.OptionValueTran;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OptionValueJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DetailActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener, ItemSuggestedAdapter.ImageViewClickListener, View.OnClickListener, OptionValueJSONParser.OptionValueRequestListener, RemarkDialogFragment.RemarkResponseListener, ModifierAdapter.ModifierCheckedChangeListener {

    public static ArrayList<OptionMaster> alOptionValue;
    ImageView ivItemImage, ivTest, ivJain;
    TextView txtItemRate, txtShortDescription, txtHeader, txtDineIn, txtRemark, txtItemName;
    RecyclerView rvSuggestedItem, rvModifier, rvOptionValue;
    Toolbar app_bar;
    Button btnAdd, btnDisable, btnRemark;
    ItemMaster objItemMaster;
    ArrayList<ItemMaster> alItemMaster;
    ItemSuggestedAdapter itemSuggestedAdapter;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayout itemSuggestionLayout, dividerLayout;
    FrameLayout detailLayout;
    EditText etQuantity;
    ArrayList<OptionMaster> alOptionMaster;
    String strOptionName, strItemName;
    ArrayList<ItemMaster> alItemMasterModifier;
    ArrayList<OptionValueTran> lstOptionValueTran;
    boolean isRequestForModifier = false;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();
    boolean isDuplicate = false, isKeyClick = false, isItemSuggestedClick, isBannerClick, isItemMasterRequest;
    double totalAmount, totalModifierAmount, totalTax;
    StringBuilder sbOptionValue;
    ToggleButton tbLike;
    ImageButton ibMinus, ibPlus;
    int position;
    boolean isVeg, isNonVeg, isJain;
    LinearLayout wishListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        if (app_bar != null) {
            setSupportActionBar(app_bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }

        objItemMaster = getIntent().getParcelableExtra("ItemMaster");
        position = getIntent().getIntExtra("Position", -1);
        isBannerClick = getIntent().getBooleanExtra("isBannerClick", false);

        detailLayout = (FrameLayout) findViewById(R.id.detailLayout);
        itemSuggestionLayout = (LinearLayout) findViewById(R.id.itemSuggestionLayout);
        dividerLayout = (LinearLayout) findViewById(R.id.dividerLayout);
        wishListLayout = (LinearLayout) findViewById(R.id.wishListLayout);

        rvSuggestedItem = (RecyclerView) findViewById(R.id.rvSuggestedItem);
        rvModifier = (RecyclerView) findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView) findViewById(R.id.rvOptionValue);

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        ivTest = (ImageView) findViewById(R.id.ivTest);
        ivJain = (ImageView) findViewById(R.id.ivJain);

        txtItemRate = (TextView) findViewById(R.id.txtItemRate);
        txtItemName = (TextView) findViewById(R.id.txtItemName);
        txtShortDescription = (TextView) findViewById(R.id.txtShortDescription);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtDineIn = (TextView) findViewById(R.id.txtDineIn);
        txtRemark = (com.rey.material.widget.TextView) findViewById(R.id.txtRemark);

        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etQuantity.setSelectAllOnFocus(true);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDisable = (Button) findViewById(R.id.btnDisable);
        btnRemark = (Button) findViewById(R.id.btnRemark);

        ibMinus = (ImageButton) findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) findViewById(R.id.ibPlus);

        tbLike = (ToggleButton) findViewById(R.id.tbLike);

        btnAdd.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);
        tbLike.setOnClickListener(this);
        btnRemark.setOnClickListener(this);

        SetVisibility(false);

        if (isBannerClick) {
            if (Service.CheckNet(this)) {
                isItemMasterRequest = true;
                RequestItemMaster(getIntent().getIntExtra("ItemMasterId", 0));
            } else {
                Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
            }
        } else {
            if (objItemMaster != null) {
                GetItemDetail(objItemMaster);
                if (Service.CheckNet(this)) {
                    RequestItemSuggest();
                } else {
                    Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                }
            }
        }

        etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etQuantity.setSelectAllOnFocus(true);
                }
            }
        });

        etQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        isKeyClick = false;
                        Globals.HideKeyBoard(DetailActivity.this, v);
                    } else {
                        isKeyClick = true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isItemSuggestedClick) {
                    if (isBannerClick) {
                        SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                        finish();
                    } else {
                        if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                            finish();
                        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                            finish();
                        } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IsItemSuggestionClick", true);
                            returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                            objItemMaster.setIsChecked((short) 1);
                            returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                            returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                            returnIntent.putExtra("ItemMaster", objItemMaster);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("IsItemSuggestionClick", true);
                            returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                            objItemMaster.setIsChecked((short) 0);
                            returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                            returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                            returnIntent.putExtra("ItemMaster", objItemMaster);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                } else {
                    if (isBannerClick) {
                        SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                        finish();
                    } else {
                        Intent returnIntent = new Intent();
                        if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                            objItemMaster.setIsChecked((short) 1);
                            returnIntent.putExtra("IsWishListChange", true);
                            returnIntent.putExtra("Position", position);
                            returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                            objItemMaster.setIsChecked((short) 0);
                            returnIntent.putExtra("IsWishListChange", true);
                            returnIntent.putExtra("Position", position);
                            returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            finish();
                        }
                    }
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isItemSuggestedClick) {
            if (isBannerClick) {
                SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                finish();
            } else {
                if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                    finish();
                } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                    finish();
                } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsItemSuggestionClick", true);
                    returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                    objItemMaster.setIsChecked((short) 1);
                    returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                    returnIntent.putExtra("ItemMaster", objItemMaster);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("IsItemSuggestionClick", true);
                    returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                    objItemMaster.setIsChecked((short) 0);
                    returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                    returnIntent.putExtra("ItemMaster", objItemMaster);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        } else {
            if (isBannerClick) {
                SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                finish();
            } else {
                Intent returnIntent = new Intent();
                if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                    objItemMaster.setIsChecked((short) 1);
                    returnIntent.putExtra("IsWishListChange", true);
                    returnIntent.putExtra("Position", position);
                    returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                    objItemMaster.setIsChecked((short) 0);
                    returnIntent.putExtra("IsWishListChange", true);
                    returnIntent.putExtra("Position", position);
                    returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    finish();
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(DetailActivity.this, v);
        if (v.getId() == R.id.btnAdd) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            }
            SetOrderItemModifierTran();
            SetOrderItem();
            Intent returnIntent = new Intent();
            if (isItemSuggestedClick) {
                if (isBannerClick) {
                    SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                    returnIntent.putExtra("ShowMessage", true);
                    returnIntent.putExtra("ItemName", strItemName);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                        returnIntent.putExtra("ShowMessage", true);
                        returnIntent.putExtra("ItemName", strItemName);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                        returnIntent.putExtra("ShowMessage", true);
                        returnIntent.putExtra("ItemName", strItemName);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                        returnIntent.putExtra("IsItemSuggestionClick", true);
                        returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                        objItemMaster.setIsChecked((short) 1);
                        returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                        returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                        returnIntent.putExtra("ItemMaster", objItemMaster);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                        returnIntent.putExtra("IsItemSuggestionClick", true);
                        returnIntent.putExtra("ItemMasterOldChecked", objItemMaster.getIsChecked());
                        objItemMaster.setIsChecked((short) 0);
                        returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                        returnIntent.putExtra("ItemMasterId", objItemMaster.getItemMasterId());
                        returnIntent.putExtra("ItemMaster", objItemMaster);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            } else {
                if (isBannerClick) {
                    SaveWishListFromBannerClick(objItemMaster.getItemMasterId());
                    returnIntent.putExtra("ShowMessage", true);
                    returnIntent.putExtra("ItemName", strItemName);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    if (tbLike.isChecked() && objItemMaster.getIsChecked() == 1) {
                        returnIntent.putExtra("ShowMessage", true);
                        returnIntent.putExtra("ItemName", strItemName);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() == 0 || objItemMaster.getIsChecked() == -1)) {
                        returnIntent.putExtra("ShowMessage", true);
                        returnIntent.putExtra("ItemName", strItemName);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else if (tbLike.isChecked() && (objItemMaster.getIsChecked() != 1)) {
                        objItemMaster.setIsChecked((short) 1);
                        returnIntent.putExtra("IsWishListChange", true);
                        returnIntent.putExtra("Position", position);
                        returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    } else if (!tbLike.isChecked() && (objItemMaster.getIsChecked() != 0)) {
                        objItemMaster.setIsChecked((short) 0);
                        returnIntent.putExtra("IsWishListChange", true);
                        returnIntent.putExtra("Position", position);
                        returnIntent.putExtra("IsChecked", objItemMaster.getIsChecked());
                    }
                }
            }
        } else if (v.getId() == R.id.btnRemark) {
            RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment(txtRemark.getText().toString());
            remarkDialogFragment.show(getSupportFragmentManager(), "");
        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            etQuantity.requestFocus();
        } else if (v.getId() == R.id.ibPlus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            etQuantity.requestFocus();
        } else if (v.getId() == R.id.tbLike) {
            if (tbLike.isChecked()) {
                tbLike.setChecked(true);
            } else {
                tbLike.setChecked(false);
            }
        }
    }

    public void EditTextOnClick(View view) {
        if (!isKeyClick) {
            etQuantity.clearFocus();
            etQuantity.requestFocus();
        } else {
            isKeyClick = false;
        }
    }


    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster, boolean isFilter) {
        if (isBannerClick) {
            if (isItemMasterRequest) {
                progressDialog.dismiss();
                isItemMasterRequest = false;
                if (alItemMaster != null && alItemMaster.size() > 0) {
                    this.objItemMaster = alItemMaster.get(0);
                    objItemMaster.setIsChecked(CheckSuggestedItemInWishList(objItemMaster.getItemMasterId()));
                    GetItemDetail(objItemMaster);
                    if (Service.CheckNet(this)) {
                        RequestItemSuggest();
                    } else {
                        Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), this, 1000);
                    }
                }
            } else {
                if (isRequestForModifier) {
                    alItemMasterModifier = alItemMaster;
                    SetModifierRecyclerView();
                    isRequestForModifier = false;
                    if (objItemMaster.getLinktoOptionMasterIds().equals("")) {
                        rvOptionValue.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    } else {
                        if (Service.CheckNet(DetailActivity.this)) {
                            RequestOptionValue();
                        } else {
                            Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    this.alItemMaster = alItemMaster;
                    SetRecyclerView();
                    if (objItemMaster.getLinktoItemMasterIdModifiers().equals("")) {
                        rvModifier.setVisibility(View.GONE);
                        if (objItemMaster.getLinktoOptionMasterIds().equals("")) {
                            rvOptionValue.setVisibility(View.GONE);
                        } else {
                            if (Service.CheckNet(DetailActivity.this)) {
                                progressDialog.show(getSupportFragmentManager(), "");
                                RequestOptionValue();
                            } else {
                                Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                            }
                        }
                    } else if (!objItemMaster.getLinktoItemMasterIdModifiers().equals("")) {
                        if (Service.CheckNet(DetailActivity.this)) {
                            isRequestForModifier = true;
                            RequestItemModifier();
                        } else {
                            Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                        }
                    }
                }
            }
        } else {
            if (isRequestForModifier) {
                alItemMasterModifier = alItemMaster;
                SetModifierRecyclerView();
                isRequestForModifier = false;
                if (objItemMaster.getLinktoOptionMasterIds().equals("")) {
                    rvOptionValue.setVisibility(View.GONE);
                    progressDialog.dismiss();
                } else {
                    if (Service.CheckNet(DetailActivity.this)) {
                        RequestOptionValue();
                    } else {
                        Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                    }
                }
            } else {
                progressDialog.dismiss();
                this.alItemMaster = alItemMaster;
                SetRecyclerView();
                if (objItemMaster.getLinktoItemMasterIdModifiers().equals("")) {
                    rvModifier.setVisibility(View.GONE);
                    if (objItemMaster.getLinktoOptionMasterIds().equals("")) {
                        rvOptionValue.setVisibility(View.GONE);
                    } else {
                        if (Service.CheckNet(DetailActivity.this)) {
                            progressDialog.show(getSupportFragmentManager(), "");
                            RequestOptionValue();
                        } else {
                            Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                        }
                    }
                } else if (!objItemMaster.getLinktoItemMasterIdModifiers().equals("")) {
                    if (Service.CheckNet(DetailActivity.this)) {
                        isRequestForModifier = true;
                        RequestItemModifier();
                    } else {
                        Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
                    }
                }
            }
        }
    }

    @Override
    public void OptionValueResponse(ArrayList<OptionValueTran> alOptionValueTran) {
        SetOptionValueRecyclerView(alOptionValueTran);
    }

    @Override
    public void ImageOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        ClearData();
        this.objItemMaster = objItemMaster;
        isRequestForModifier = false;
        isItemSuggestedClick = true;
        objItemMaster.setIsChecked(CheckSuggestedItemInWishList(objItemMaster.getItemMasterId()));
        GetItemDetail(this.objItemMaster);
        if (Service.CheckNet(DetailActivity.this)) {
            RequestItemSuggest();
        } else {
            Globals.ShowSnackBar(detailLayout, getResources().getString(R.string.MsgCheckConnection), DetailActivity.this, 1000);
        }
    }

    @Override
    public void RemarkResponse(String strRemark) {
        if (strRemark != null && !strRemark.equals("")) {
            txtRemark.setVisibility(View.VISIBLE);
            txtRemark.setText(strRemark);
        } else {
            txtRemark.setVisibility(View.GONE);
            txtRemark.setText("");
        }
    }

    @Override
    public void ModifierCheckedChange(boolean isChecked, ItemMaster objItemModifier, boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
        if (isChecked) {
            if (alCheckedModifier.size() > 0) {
                for (ItemMaster objCheckedItemModifier : alCheckedModifier) {
                    if (objItemModifier.getItemMasterId() == objCheckedItemModifier.getItemMasterId()) {
                        this.isDuplicate = true;
                        break;
                    }
                }
                if (!this.isDuplicate) {
                    alCheckedModifier.add(objItemModifier);
                }
                this.isDuplicate = false;
            } else {
                alCheckedModifier.add(objItemModifier);
            }
        } else {
            for (ItemMaster objCheckedItemModifier : alCheckedModifier) {
                if (objItemModifier.getItemMasterId() == objCheckedItemModifier.getItemMasterId()) {
                    alCheckedModifier.remove(alCheckedModifier.indexOf(objCheckedItemModifier));
                    break;
                }
            }
        }
    }


    //region Private Method
    private void RequestItemSuggest() {
        progressDialog.show(getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemSuggested(this, String.valueOf(objItemMaster.getItemMasterId()), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void RequestItemMaster(int itemMasterId) {
        progressDialog.show(getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemMaster(null, this, String.valueOf(1), null, null, String.valueOf(Globals.linktoBusinessMasterId), String.valueOf(itemMasterId), false);
    }

    @SuppressLint("SetTextI18n")
    private void GetItemDetail(ItemMaster objItemMaster) {
        if (app_bar != null) {
            if (objItemMaster.getItemName() != null) {
                getSupportActionBar().setTitle(objItemMaster.getCategory());
            } else {
                getSupportActionBar().setTitle(this.getResources().getString(R.string.title_detail));
            }
        }

        if (objItemMaster.getIsDineInOnly()) {
            txtDineIn.setVisibility(View.VISIBLE);
            btnDisable.setVisibility(View.VISIBLE);
            wishListLayout.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
        } else {
            txtDineIn.setVisibility(View.GONE);
            wishListLayout.setVisibility(View.VISIBLE);
            btnDisable.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }

        if (objItemMaster.getMd_ImagePhysicalName() == null || objItemMaster.getMd_ImagePhysicalName().equals("")) {
            Picasso.with(ivItemImage.getContext()).load(R.drawable.default_image).into(ivItemImage);
        } else {
            Picasso.with(ivItemImage.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItemImage);
        }
        if (objItemMaster.getShortDescription().equals("")) {
            txtShortDescription.setVisibility(View.GONE);
        } else {
            txtShortDescription.setVisibility(View.VISIBLE);
            txtShortDescription.setText(objItemMaster.getShortDescription());
        }
        txtShortDescription.setText(objItemMaster.getShortDescription());
        txtItemName.setText(objItemMaster.getItemName());
        txtItemRate.setText(getResources().getString(R.string.cifRupee) + " " + objItemMaster.getRate());
        if (objItemMaster.getIsChecked() == 1) {
            tbLike.setChecked(true);
        } else {
            tbLike.setChecked(false);
        }

        if (!objItemMaster.getLinktoOptionMasterIds().equals("")) {
            if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.DoubleSpicy.getValue()))) {
                ivTest.setVisibility(View.VISIBLE);
                ivTest.setImageResource(R.mipmap.extra_spicy);
            } else if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Spicy.getValue()))) {
                ivTest.setVisibility(View.VISIBLE);
                ivTest.setImageResource(R.mipmap.spicy);
            } else if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Sweet.getValue()))) {
                ivTest.setVisibility(View.VISIBLE);
                ivTest.setImageResource(R.mipmap.sweet);
            } else {
                ivTest.setVisibility(View.GONE);
            }

            isVeg = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Veg.getValue()));
            isNonVeg = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.NonVeg.getValue()));
            isJain = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Jain.getValue()));
            if (isNonVeg && !isVeg) {
                ivJain.setVisibility(View.VISIBLE);
                ivJain.setImageResource(R.mipmap.nonvegicon);
            } else if (isJain && !isNonVeg) {
                ivJain.setVisibility(View.VISIBLE);
                ivJain.setImageResource(R.mipmap.jain_icon);
            } else {
                ivJain.setVisibility(View.GONE);
            }
        } else {
            ivTest.setVisibility(View.GONE);
            ivJain.setVisibility(View.GONE);
        }
    }

    private void SetRecyclerView() {
        if (alItemMaster == null || alItemMaster.size() == 0) {
            SetVisibility(false);
        } else {
            SetVisibility(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setAutoMeasureEnabled(true);
            rvSuggestedItem.setLayoutManager(layoutManager);
            itemSuggestedAdapter = new ItemSuggestedAdapter(this, alItemMaster, this);
            rvSuggestedItem.setAdapter(itemSuggestedAdapter);
        }
    }

    private void SetVisibility(boolean isShow) {
        if (isShow) {
            txtHeader.setVisibility(View.VISIBLE);
            dividerLayout.setVisibility(View.VISIBLE);
            itemSuggestionLayout.setVisibility(View.VISIBLE);
            rvSuggestedItem.setVisibility(View.VISIBLE);
        } else {
            txtHeader.setVisibility(View.GONE);
            dividerLayout.setVisibility(View.GONE);
            itemSuggestionLayout.setVisibility(View.GONE);
            rvSuggestedItem.setVisibility(View.GONE);
            rvModifier.setVisibility(View.GONE);
            rvOptionValue.setVisibility(View.GONE);
        }
    }

    private int IncrementDecrementValue(int id, int value) {
        if (id == R.id.ibPlus) {
            value++;
            etQuantity.setText(String.valueOf(value));
        } else {
            if (value > 1) {
                value--;
            }
            etQuantity.setText(String.valueOf(value));
        }
        return value;
    }

    private void RequestOptionValue() {
        //progressDialog.show(getSupportFragmentManager(), "");

        OptionValueJSONParser objOptionValueJSONParser = new OptionValueJSONParser();
        objOptionValueJSONParser.SelectAllItemOptionValue(String.valueOf(objItemMaster.getItemMasterId()), DetailActivity.this, null);
    }

    private void RequestItemModifier() {
        progressDialog.show(getSupportFragmentManager(), "");

        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemModifier(null, DetailActivity.this, String.valueOf(objItemMaster.getItemMasterId()), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetOptionValueRecyclerView(ArrayList<OptionValueTran> lstOptionValue) {
        if (lstOptionValue == null || lstOptionValue.size() == 0) {
            progressDialog.dismiss();
            rvOptionValue.setVisibility(View.GONE);
        } else {
            progressDialog.dismiss();
            rvOptionValue.setVisibility(View.VISIBLE);
            SetOptionMasterList(lstOptionValue);
            rvOptionValue.setAdapter(new ItemOptionValueAdapter(DetailActivity.this, alOptionMaster, true));
            rvOptionValue.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        }
    }

    private void SetModifierRecyclerView() {
        if (alItemMasterModifier == null || alItemMasterModifier.size() == 0) {
            rvModifier.setVisibility(View.GONE);
        } else {
            rvModifier.setVisibility(View.VISIBLE);
            rvModifier.setAdapter(new ModifierAdapter(DetailActivity.this, alItemMasterModifier, this));
            rvModifier.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        }
    }

    private void SetOptionMasterList(ArrayList<OptionValueTran> lstOptionValue) {
        alOptionMaster = new ArrayList<>();
        lstOptionValueTran = new ArrayList<>();
        strOptionName = null;
        OptionMaster objOptionMaster = new OptionMaster();
        for (OptionValueTran objOptionValueTran : lstOptionValue) {
            if (strOptionName == null) {
                strOptionName = objOptionValueTran.getOptionName();
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(objOptionValueTran.getOptionName());
                objOptionMaster.setOptionMasterId(objOptionValueTran.getlinktoOptionMasterId());
                lstOptionValueTran.add(objOptionValueTran);
                if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                    objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                    alOptionMaster.add(objOptionMaster);
                }
            } else {
                if (strOptionName.equals(objOptionValueTran.getOptionName())) {
                    lstOptionValueTran.add(objOptionValueTran);
                    if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                } else {
                    objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                    alOptionMaster.add(objOptionMaster);
                    strOptionName = objOptionValueTran.getOptionName();
                    objOptionMaster = new OptionMaster();
                    lstOptionValueTran = new ArrayList<>();
                    lstOptionValueTran.add(objOptionValueTran);
                    objOptionMaster.setOptionRowId(-1);
                    objOptionMaster.setOptionName(objOptionValueTran.getOptionName());
                    objOptionMaster.setOptionMasterId(objOptionValueTran.getlinktoOptionMasterId());
                    if (lstOptionValue.indexOf(objOptionValueTran) == lstOptionValue.size() - 1) {
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                }
            }
        }

        alOptionValue = new ArrayList<>();
        if (alOptionMaster.size() > 0) {
            for (OptionMaster objFilterOptionMaster : alOptionMaster) {
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(null);
                objOptionMaster.setOptionMasterId(objFilterOptionMaster.getOptionMasterId());
                alOptionValue.add(objOptionMaster);
            }
        }
    }

    private void SetOrderItemModifierTran() {
        try {
            for (ItemMaster objCheckedModifier : alCheckedModifier) {
                objCheckedModifier.setRate(objCheckedModifier.getMRP());
                objCheckedModifier.setSellPrice(objCheckedModifier.getMRP());
                totalModifierAmount = totalModifierAmount + objCheckedModifier.getMRP();
                objCheckedModifier.setTotalAmount(totalModifierAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItem() {
        if (Globals.alOrderItemTran.size() == 0) {
            ItemMaster objOrderItemTran = new ItemMaster();
            strItemName = objItemMaster.getItemName();
            objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
            objOrderItemTran.setItemName(objItemMaster.getItemName());
            objOrderItemTran.setRate(objItemMaster.getRate());
            objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
            objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
            objOrderItemTran.setTax(objItemMaster.getTax());
            CountTax(objOrderItemTran, isDuplicate);
            objOrderItemTran.setTotalTax(totalTax);
            SetItemRemark();
            if (txtRemark.getText().toString().isEmpty()) {
                if (!sbOptionValue.toString().equals("")) {
                    objOrderItemTran.setRemark(sbOptionValue.toString());
                    objOrderItemTran.setOptionValue(sbOptionValue.toString());
                }
            } else {
                objOrderItemTran.setItemRemark(txtRemark.getText().toString());
                if (!sbOptionValue.toString().equals("")) {
                    objOrderItemTran.setRemark(txtRemark.getText().toString() + ", " + sbOptionValue.toString());
                    objOrderItemTran.setOptionValue(sbOptionValue.toString());
                } else {
                    objOrderItemTran.setRemark(txtRemark.getText().toString());
                }
            }
            if (alCheckedModifier.size() != 0) {
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate() + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
            } else {
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
            }
            objOrderItemTran.setTotalAmount(totalAmount);
            if (Integer.valueOf(etQuantity.getText().toString()) > 1) {
                SetOrderItemModifierQty(alCheckedModifier, Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setAlOrderItemModifierTran(alCheckedModifier);
            } else {
                objOrderItemTran.setAlOrderItemModifierTran(alCheckedModifier);
            }
            Globals.counter = Globals.counter + 1;
            Globals.alOrderItemTran.add(objOrderItemTran);
        } else {
            SetItemRemark();
            CheckDuplicateRemarkModifier();
            if (!isDuplicate) {
                ItemMaster objOrderItemTran = new ItemMaster();
                strItemName = objItemMaster.getItemName();
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setRate(objItemMaster.getRate());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setTax(objItemMaster.getTax());
                CountTax(objOrderItemTran, isDuplicate);
                objOrderItemTran.setTotalTax(totalTax);
                if (txtRemark.getText().toString().isEmpty()) {
                    if (!sbOptionValue.toString().equals("")) {
                        objOrderItemTran.setRemark(sbOptionValue.toString());
                        objOrderItemTran.setOptionValue(sbOptionValue.toString());
                    }
                } else {
                    objOrderItemTran.setItemRemark(txtRemark.getText().toString());
                    if (!sbOptionValue.toString().equals("")) {
                        objOrderItemTran.setRemark(txtRemark.getText().toString() + ", " + sbOptionValue.toString());
                        objOrderItemTran.setOptionValue(sbOptionValue.toString());
                    } else {
                        objOrderItemTran.setRemark(txtRemark.getText().toString());
                    }
                }
                if (alCheckedModifier.size() != 0) {
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate() + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
                } else {
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
                }
                objOrderItemTran.setTotalAmount(totalAmount);
                if (Integer.valueOf(etQuantity.getText().toString()) > 1) {
                    SetOrderItemModifierQty(alCheckedModifier, Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setAlOrderItemModifierTran(alCheckedModifier);
                } else {
                    objOrderItemTran.setAlOrderItemModifierTran(alCheckedModifier);
                }
                Globals.counter = Globals.counter + 1;
                Globals.alOrderItemTran.add(objOrderItemTran);
            }
        }
    }

    private void CheckDuplicateRemarkModifier() {
        String[] strNewRemark = new String[0], strOldRemark;
        String strOptionValue;
        int cnt, cntModifier;
        for (ItemMaster objFilterOrderItemTran : Globals.alOrderItemTran) {
            cnt = 0;
            cntModifier = 0;
            if (txtRemark.getText().toString().isEmpty()) {
                strOptionValue = sbOptionValue.toString();
            } else {
                if (sbOptionValue.toString() != null && !sbOptionValue.toString().equals("")) {
                    strOptionValue = sbOptionValue.toString() + txtRemark.getText().toString();
                } else {
                    strOptionValue = txtRemark.getText().toString();
                }
            }

            if (!strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() != null && !objFilterOrderItemTran.getRemark().equals(""))) {
                if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(",")) {
                    strNewRemark = String.valueOf(strOptionValue.subSequence(0, strOptionValue.length()) + " ").split(", ");
                } else if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(" ")) {
                    strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                } else {
                    strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                }

                String listRemark = objFilterOrderItemTran.getRemark();
                if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(",")) {
                    strOldRemark = String.valueOf(listRemark.subSequence(0, listRemark.length()) + " ").split(", ");
                } else if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(" ")) {
                    strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                } else {
                    strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                }

                if (strNewRemark.length != 0) {
                    for (String newRemark : strNewRemark) {
                        for (String oldRemark : strOldRemark) {
                            if (newRemark.equals(oldRemark)) {
                                cnt = cnt + 1;
                            }
                        }
                    }
                }
            }
            if (objFilterOrderItemTran.getAlOrderItemModifierTran() != null && objFilterOrderItemTran.getAlOrderItemModifierTran().size() != 0) {
                if (objItemMaster.getItemMasterId() == objFilterOrderItemTran.getItemMasterId() && alCheckedModifier.size() != 0) {
                    ArrayList<ItemMaster> alOldOrderItemTran = objFilterOrderItemTran.getAlOrderItemModifierTran();
                    if (alCheckedModifier.size() != 0) {
                        if (alCheckedModifier.size() == alOldOrderItemTran.size()) {
                            for (ItemMaster objCheckedModifier : alCheckedModifier) {
                                for (ItemMaster objOldOrderItemTran : alOldOrderItemTran) {
                                    if (objCheckedModifier.getItemMasterId() == objOldOrderItemTran.getItemMasterId()) {
                                        cntModifier = cntModifier + 1;
                                    }
                                }
                            }
                        }
                    }
                    if (cntModifier == alCheckedModifier.size() && ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt) || (strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals(""))))) {
                        isDuplicate = true;
                        strItemName = objItemMaster.getItemName();
                        objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        if (alCheckedModifier.size() > 0) {
                            objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        } else {
                            objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        }
                        objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                        }
                        CountTax(objFilterOrderItemTran, isDuplicate);
                        objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                        break;
                    } else if (strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals("")) && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0) {
                        isDuplicate = true;
                        strItemName = objItemMaster.getItemName();
                        objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        if (alCheckedModifier.size() > 0) {
                            objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        } else {
                            objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        }
                        objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                        }
                        CountTax(objFilterOrderItemTran, isDuplicate);
                        objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                        break;
                    }
                }
            } else {
                if ((objItemMaster.getItemMasterId() == objFilterOrderItemTran.getItemMasterId())
                        && ((strOptionValue.equals("") && (objFilterOrderItemTran.getRemark() == null || objFilterOrderItemTran.getRemark().equals("")) && alCheckedModifier.size() == 0 && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0)
                        || (strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt && alCheckedModifier.size() == 0 && objFilterOrderItemTran.getAlOrderItemModifierTran().size() == 0))) {
                    isDuplicate = true;
                    strItemName = objItemMaster.getItemName();
                    objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()));
                    if (alCheckedModifier.size() > 0) {
                        objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                    } else {
                        objFilterOrderItemTran.setTotalAmount(objFilterOrderItemTran.getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                    }
                    objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                    if (objFilterOrderItemTran.getAlOrderItemModifierTran().size() > 0) {
                        SetOrderItemModifierQty(objFilterOrderItemTran.getAlOrderItemModifierTran(), objFilterOrderItemTran.getQuantity());
                    }
                    CountTax(objFilterOrderItemTran, isDuplicate);
                    objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                    break;
                }
            }
        }

    }

    private void SetItemRemark() {
        sbOptionValue = new StringBuilder();
        if (alOptionValue != null && alOptionValue.size() > 0) {
            for (OptionMaster objOptionMaster : alOptionValue) {
                if (objOptionMaster.getOptionName() != null) {
                    sbOptionValue.append(objOptionMaster.getOptionName()).append(", ");
                }
            }
        }
    }

    private void SetOrderItemModifierQty(ArrayList<ItemMaster> alItemMasterModifier, int Quantity) {
        for (ItemMaster objItemMasterModifier : alItemMasterModifier) {
            objItemMasterModifier.setSellPrice(objItemMasterModifier.getRate() * Quantity);
        }
    }

    private void CountTax(ItemMaster objOrderItemMaster, boolean isDuplicate) {
        totalTax = 0;
        int cnt = 0;
        double rate;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            ArrayList<String> alTax = new ArrayList<>(Arrays.asList(objItemMaster.getTax().split(",")));
            for (String tax : alTax) {
                if (isDuplicate) {
                    if (objItemMaster.getTaxRate() == 0) {
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1(objOrderItemMaster.getTax1() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2(objOrderItemMaster.getTax2() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3(objOrderItemMaster.getTax3() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4(objOrderItemMaster.getTax4() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5(objOrderItemMaster.getTax5() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        }
                    } else {
                        rate = objItemMaster.getRate() + objItemMaster.getTaxRate();
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1(objOrderItemMaster.getTax1() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2(objOrderItemMaster.getTax2() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3(objOrderItemMaster.getTax3() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4(objOrderItemMaster.getTax4() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5(objOrderItemMaster.getTax5() + (Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        }
                    }
                } else {
                    if (objItemMaster.getTaxRate() == 0) {
                        totalTax = totalTax + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100;
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                        }
                    } else {
                        rate = objItemMaster.getRate() + objItemMaster.getTaxRate();
                        totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        if (cnt == 0) {
                            objOrderItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 1) {
                            objOrderItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 2) {
                            objOrderItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else if (cnt == 3) {
                            objOrderItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        } else {
                            objOrderItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                        }
                    }
                }
                cnt++;
            }
        }
    }

    private void ClearData() {
        alOptionValue = new ArrayList<>();
        alCheckedModifier = new ArrayList<>();
        alItemMasterModifier = new ArrayList<>();
        alItemMaster = new ArrayList<>();
    }


    private boolean CheckOptionValue(String optionValueIds, String optionValue) {
        List<String> items = Arrays.asList(optionValueIds.split(","));
        boolean isMatch = false;
        for (String str : items) {
            if (str.equals(optionValue)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    private short CheckSuggestedItemInWishList(int itemMasterId) {
        if (ItemAdapter.alWishItemMaster.size() > 0) {
            for (ItemMaster objMaster : ItemAdapter.alWishItemMaster) {
                if (objMaster.getItemMasterId() == itemMasterId) {
                    if (objMaster.getIsChecked() == 1) {
                        return 1;
                    }
                }
            }

        } else {
            return 0;
        }
        return 0;
    }

    private void SaveWishListFromBannerClick(int itemMasterId) {
        boolean isDuplicate = false;
        if (ItemAdapter.alWishItemMaster.size() > 0){
            for (ItemMaster objMaster : ItemAdapter.alWishItemMaster) {
                if (objMaster.getItemMasterId() == itemMasterId) {
                    if (objMaster.getIsChecked() == 1 && tbLike.isChecked()) {
                        isDuplicate = true;
                    } else if (objMaster.getIsChecked() == 0 && !tbLike.isChecked()) {
                        isDuplicate = true;
                    } else {
                        isDuplicate = true;
                        objMaster.setIsChecked((short) (tbLike.isChecked() ? 1 : 0));
                    }
                }
            }
            if (!isDuplicate) {
                ItemMaster objItemMaster = new ItemMaster();
                objItemMaster.setItemMasterId(itemMasterId);
                objItemMaster.setIsChecked((short) (tbLike.isChecked() ? 1 : 0));
                ItemAdapter.alWishItemMaster.add(objItemMaster);
            }
        }else{
            ItemMaster objItemMaster = new ItemMaster();
            objItemMaster.setItemMasterId(itemMasterId);
            objItemMaster.setIsChecked((short) (tbLike.isChecked() ? 1 : 0));
            ItemAdapter.alWishItemMaster.add(objItemMaster);
        }
        SaveWishListInSharePreference();
    }

    private void SaveWishListInSharePreference() {
        ArrayList<String> alString = new ArrayList<>();
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
            if (objWishItemMaster.getIsChecked() != -1 && objWishItemMaster.getIsChecked()!=0) {
                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
            }
        }
        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, DetailActivity.this);
    }
    //endregion
}