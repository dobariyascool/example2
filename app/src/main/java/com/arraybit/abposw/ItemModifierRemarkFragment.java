package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.ItemOptionValueAdapter;
import com.arraybit.adapter.ModifierAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OptionMaster;
import com.arraybit.modal.OptionValueTran;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OptionValueJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class ItemModifierRemarkFragment extends Fragment implements OptionValueJSONParser.OptionValueRequestListener, View.OnClickListener, ItemJSONParser.ItemMasterRequestListener, ModifierAdapter.ModifierCheckedChangeListener,RemarkDialogFragment.RemarkResponseListener {

    public static ArrayList<OptionMaster> alOptionValue;
    RecyclerView rvOptionValue, rvModifier;
    ProgressDialog progressDialog = new ProgressDialog();
    ItemMaster objItemMaster;
    ArrayList<OptionMaster> alOptionMaster;
    String strOptionName, strItemName;
    ArrayList<OptionValueTran> lstOptionValueTran;
    Button btnAdd,btnRemark;
    StringBuilder sbOptionValue;
    TextView txtItemDescription,txtItemName, txtItemRate,txtRemark;
    ImageButton ibMinus, ibPlus;
    EditText etQuantity;
    ArrayList<ItemMaster> alItemMasterModifier;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();
    boolean isDuplicate = false,isKeyClick = false;
    double totalAmount, totalModifierAmount, totalTax;
    String activityName;
    AddQtyRemarkDialogFragment.AddQtyRemarkDialogListener objAddQtyRemarkDialogListener;

    public ItemModifierRemarkFragment(ItemMaster objItemMaster,String activityName) {
        this.objItemMaster = objItemMaster;
        this.activityName = activityName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_modifier_remark, container, false);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        if(objItemMaster!=null && objItemMaster.getCategory()!=null) {
            ((AppCompatActivity) getActivity()).setTitle(objItemMaster.getCategory());
        }else{
            app_bar.setTitle(getResources().getString(R.string.title_item_modifier_remark));
        }

        setHasOptionsMenu(true);


        txtItemDescription = (TextView) view.findViewById(R.id.txtItemDescription);
        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtItemRate = (TextView) view.findViewById(R.id.txtItemRate);
        txtRemark = (TextView) view.findViewById(R.id.txtRemark);

        rvModifier = (RecyclerView) view.findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView) view.findViewById(R.id.rvOptionValue);

        rvModifier.setVisibility(View.GONE);
        rvOptionValue.setVisibility(View.GONE);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etQuantity.setSelectAllOnFocus(true);


        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnRemark = (Button) view.findViewById(R.id.btnRemark);

        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnRemark.setOnClickListener(this);

        SetDetail();

        if (Service.CheckNet(getActivity())) {
            RequestOptionValue();
            RequestItemModifier();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
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
                        Globals.HideKeyBoard(getActivity(), v);
                    } else {
                        isKeyClick = true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void OptionValueResponse(ArrayList<OptionValueTran> alOptionValueTran) {
        SetOptionValueRecyclerView(alOptionValueTran);
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster, boolean isFilter) {
        alItemMasterModifier = alItemMaster;
        SetModifierRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(activityName.equals(getActivity().getResources().getString(R.string.title_activity_menu))){
                getActivity().finish();
            }else if(activityName.equals(getActivity().getResources().getString(R.string.title_detail))){
                getActivity().getSupportFragmentManager().popBackStack();
            }
//            if(objItemMaster.getCategory()!=null && objItemMaster.getCategory().equals(getActivity().getTitle())) {
//                getActivity().getSupportFragmentManager().popBackStack();
//            }else{
//                getActivity().finish();
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.ibMinus) {
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
        } else if (v.getId() == R.id.btnAdd) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            }
            SetOrderItemModifierTran();
            SetOrderItem();
            if(getArguments()!=null && getArguments().getBoolean("isDetailActivity")){
                objAddQtyRemarkDialogListener = (AddQtyRemarkDialogFragment.AddQtyRemarkDialogListener)getActivity();
                ItemMaster objItem = new ItemMaster();
                objItem.setItemName(strItemName);
                objAddQtyRemarkDialogListener.AddQtyRemarkResponse(objItem);
                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ShowMessage", true);
                returnIntent.putExtra("ItemName", strItemName);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }

        }else if (v.getId() == R.id.btnRemark) {
            RemarkDialogFragment remarkDialogFragment = new RemarkDialogFragment(txtRemark.getText().toString());
            remarkDialogFragment.setTargetFragment(this,0);
            remarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
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

    public void SetEditClickEvent(){
        if (!isKeyClick) {
            etQuantity.clearFocus();
            etQuantity.requestFocus();
        } else {
            isKeyClick = false;
        }
    }

    //region Private Methods
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
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        OptionValueJSONParser objOptionValueJSONParser = new OptionValueJSONParser();
        objOptionValueJSONParser.SelectAllItemOptionValue(String.valueOf(objItemMaster.getItemMasterId()), getActivity(), this);
    }

    private void RequestItemModifier() {
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemModifier(this, getActivity(), String.valueOf(objItemMaster.getItemMasterId()), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetOptionValueRecyclerView(ArrayList<OptionValueTran> lstOptionValue) {
        if (lstOptionValue == null || lstOptionValue.size() == 0) {
            rvOptionValue.setVisibility(View.GONE);
        } else {
            rvOptionValue.setVisibility(View.VISIBLE);
            SetOptionMasterList(lstOptionValue);
            rvOptionValue.setAdapter(new ItemOptionValueAdapter(getActivity(), alOptionMaster,false));
            rvOptionValue.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetModifierRecyclerView() {
        if (alItemMasterModifier == null || alItemMasterModifier.size() == 0) {
            progressDialog.dismiss();
            rvModifier.setVisibility(View.GONE);
        } else {
            progressDialog.dismiss();
            rvModifier.setVisibility(View.VISIBLE);
            rvModifier.setAdapter(new ModifierAdapter(getActivity(), alItemMasterModifier, this));
            rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetOptionMasterList(ArrayList<OptionValueTran> lstOptionValue) {
        alOptionMaster = new ArrayList<>();
        lstOptionValueTran = new ArrayList<>();
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

    @SuppressLint("SetTextI18n")
    private void SetDetail() {
        if(objItemMaster!=null) {
            txtItemName.setText(objItemMaster.getItemName());
            if (objItemMaster.getShortDescription().equals("")) {
                txtItemDescription.setVisibility(View.GONE);
            } else {
                txtItemDescription.setVisibility(View.VISIBLE);
                txtItemDescription.setText(objItemMaster.getShortDescription());
            }
            txtItemRate.setText(getResources().getString(R.string.cifRupee) + " " + Globals.dfWithPrecision.format(objItemMaster.getRate()));
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
                }else{
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
                    }else{
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
                if(sbOptionValue.toString() !=null && !sbOptionValue.toString().equals("")) {
                    strOptionValue = sbOptionValue.toString() + txtRemark.getText().toString();
                }else{
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
    //endregion
}
