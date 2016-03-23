package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class ItemModifierRemarkFragment extends Fragment implements OptionValueJSONParser.OptionValueRequestListener, View.OnClickListener, ItemJSONParser.ItemMasterRequestListener, ModifierAdapter.ModifierCheckedChangeListener {

    public static ArrayList<OptionMaster> alOptionValue;
    RecyclerView rvOptionValue, rvModifier;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    ItemMaster objItemMaster;
    ArrayList<OptionMaster> alOptionMaster;
    String strOptionName;
    ArrayList<OptionValueTran> lstOptionValueTran;
    Button btnAdd;
    StringBuilder sbOptionValue, sbModifierName;
    TextView txtItemDescription, txtItemRate;
    ImageView ivItem;
    ImageButton ibMinus, ibPlus;
    EditText etQuantity, etRemark;
    ArrayList<ItemMaster> alItemMasterModifier;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();
    boolean isDuplicate = false;
    double totalAmount, totalModifierAmount, totalTax;

    public ItemModifierRemarkFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
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
        app_bar.setTitle(getResources().getString(R.string.title_item_modifier_remark));

        setHasOptionsMenu(true);

        ivItem = (ImageView) view.findViewById(R.id.ivItem);

        txtItemDescription = (TextView) view.findViewById(R.id.txtItemDescription);
        txtItemRate = (TextView) view.findViewById(R.id.txtItemRate);

        rvModifier = (RecyclerView) view.findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView) view.findViewById(R.id.rvOptionValue);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etRemark = (EditText) view.findViewById(R.id.etRemark);

        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        SetDetail();

        if (Service.CheckNet(getActivity())) {
            RequestOptionValue();
            RequestItemModifier();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        return view;
    }

    @Override
    public void OptionValueResponse(ArrayList<OptionValueTran> alOptionValueTran) {
        progressDialog.dismiss();
        SetRecyclerView(alOptionValueTran);
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        progressDialog.dismiss();
        alItemMasterModifier = alItemMaster;
        if (alItemMaster == null) {
            rvModifier.setVisibility(View.GONE);
        } else if (alItemMaster.size() == 0) {
            rvModifier.setVisibility(View.GONE);
        } else {
            rvModifier.setVisibility(View.VISIBLE);
            rvModifier.setAdapter(new ModifierAdapter(getActivity(), alItemMaster, this));
            rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
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
            SetOrderItemModifierTran();
            SetOrderItem();
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void ModifierCheckedChange(boolean isChecked, ItemMaster objItemModifier, boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
        if (isChecked) {
            if (alCheckedModifier.size() > 0) {
                for (int i = 0; i < alCheckedModifier.size(); i++) {
                    if (objItemModifier.getItemMasterId() == alCheckedModifier.get(i).getItemMasterId()) {
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
            for (int i = 0; i < alCheckedModifier.size(); i++) {
                if (objItemModifier.getItemMasterId() == alCheckedModifier.get(i).getItemMasterId()) {
                    alCheckedModifier.remove(i);
                    break;
                }
            }
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
        progressDialog.dismiss();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemModifier(this, getActivity(), String.valueOf(objItemMaster.getItemMasterId()));
    }

    private void SetRecyclerView(ArrayList<OptionValueTran> lstOptionValue) {
        if (lstOptionValue == null || lstOptionValue.size() == 0) {
            rvOptionValue.setVisibility(View.GONE);
        } else {
            SetOptionMasterList(lstOptionValue);
            rvOptionValue.setAdapter(new ItemOptionValueAdapter(getActivity(), alOptionMaster));
            rvOptionValue.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetOptionMasterList(ArrayList<OptionValueTran> lstOptionValue) {
        alOptionMaster = new ArrayList<>();
        lstOptionValueTran = new ArrayList<>();
        OptionMaster objOptionMaster = new OptionMaster();
        for (int i = 0; i < lstOptionValue.size(); i++) {
            if (strOptionName == null) {
                strOptionName = lstOptionValue.get(i).getOptionName();
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(lstOptionValue.get(i).getOptionName());
                objOptionMaster.setOptionMasterId(lstOptionValue.get(i).getlinktoOptionMasterId());
                lstOptionValueTran.add(lstOptionValue.get(i));
            } else {
                if (strOptionName.equals(lstOptionValue.get(i).getOptionName())) {
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    if (i == lstOptionValue.size() - 1) {
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                } else {
                    objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                    alOptionMaster.add(objOptionMaster);
                    strOptionName = lstOptionValue.get(i).getOptionName();
                    objOptionMaster = new OptionMaster();
                    lstOptionValueTran = new ArrayList<>();
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    objOptionMaster.setOptionRowId(-1);
                    objOptionMaster.setOptionName(lstOptionValue.get(i).getOptionName());
                    objOptionMaster.setOptionMasterId(lstOptionValue.get(i).getlinktoOptionMasterId());
                    if (i == lstOptionValue.size() - 1) {
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                }
            }
        }

        alOptionValue = new ArrayList<>();
        if (alOptionMaster.size() > 0) {
            for (int j = 0; j < alOptionMaster.size(); j++) {
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(null);
                objOptionMaster.setOptionMasterId(alOptionMaster.get(j).getOptionMasterId());
                alOptionValue.add(objOptionMaster);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void SetDetail() {
        if (objItemMaster.getSm_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItem.getContext()).load(R.drawable.default_image).into(ivItem);
        } else {
            Picasso.with(ivItem.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItem);
        }
        if (objItemMaster.getShortDescription().equals("")) {
            txtItemDescription.setText(objItemMaster.getItemName());
        } else {
            txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        txtItemRate.setText("Rs " + objItemMaster.getRate());
    }

    private void SetItemRemark() {
        sbOptionValue = new StringBuilder();
        for (int i = 0; i < alOptionValue.size(); i++) {
            if (alOptionValue.get(i).getOptionName() != null) {
                sbOptionValue.append(alOptionValue.get(i).getOptionName()).append(",");
            }
        }
    }

    private void SetOrderItem() {
        if (Globals.alOrderItemTran.size() == 0) {
            ItemMaster objOrderItemTran = new ItemMaster();
            objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
            objOrderItemTran.setItemName(objItemMaster.getItemName());
            objOrderItemTran.setRate(objItemMaster.getRate());
            objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
            objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
            objOrderItemTran.setTax(objItemMaster.getTax());
            CountTax(objOrderItemTran, isDuplicate);
            objOrderItemTran.setTotalTax(totalTax);
            SetItemRemark();
            if (etRemark.getText().toString().isEmpty()) {
                if (!sbOptionValue.toString().equals("")) {
                    objOrderItemTran.setRemark(sbOptionValue.toString());
                }
            } else {
                if (!sbOptionValue.toString().equals("")) {
                    objOrderItemTran.setRemark(etRemark.getText().toString() + "," + sbOptionValue.toString());
                }
            }
            if (alCheckedModifier.size() != 0) {
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate() + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
            }else{
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
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setRate(objItemMaster.getRate());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setTax(objItemMaster.getTax());
                CountTax(objOrderItemTran, isDuplicate);
                objOrderItemTran.setTotalTax(totalTax);
                if (etRemark.getText().toString().isEmpty()) {
                    if (!sbOptionValue.toString().equals("")) {
                        objOrderItemTran.setRemark(sbOptionValue.toString());
                    }
                } else {
                    if (!sbOptionValue.toString().equals("")) {
                        objOrderItemTran.setRemark(etRemark.getText().toString() + "," + sbOptionValue.toString());
                    }
                }
                if (alCheckedModifier.size() != 0) {
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate() + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString()));
                }else{
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
            for (int i = 0; i < alCheckedModifier.size(); i++) {
                alCheckedModifier.get(i).setRate(alCheckedModifier.get(i).getMRP());
                alCheckedModifier.get(i).setSellPrice(alCheckedModifier.get(i).getMRP());
                totalModifierAmount = totalModifierAmount + alCheckedModifier.get(i).getMRP();
                alCheckedModifier.get(i).setTotalAmount(totalModifierAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckDuplicateRemarkModifier() {
        String[] strNewRemark = new String[0], strOldRemark;
        String strOptionValue;
        int cnt, cntModifier;
        for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
            cnt = 0;
            cntModifier = 0;
            if (etRemark.getText().toString().isEmpty()) {
                strOptionValue = sbOptionValue.toString();
            } else {
                strOptionValue = sbOptionValue.toString() + etRemark.getText().toString();
            }

            if (!strOptionValue.equals("") && Globals.alOrderItemTran.get(i).getRemark() != null) {
                if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(",")) {
                    strNewRemark = String.valueOf(strOptionValue.subSequence(0, strOptionValue.length()) + " ").split(", ");
                } else if (strOptionValue.subSequence(strOptionValue.length() - 1, strOptionValue.length()).toString().equals(" ")) {
                    strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                } else {
                    strNewRemark = strOptionValue.subSequence(0, strOptionValue.length()).toString().split(", ");
                }

                String listRemark = Globals.alOrderItemTran.get(i).getRemark();
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
            if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran() != null && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() != 0) {
                if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId() && alCheckedModifier.size() != 0) {
                    ArrayList<ItemMaster> alOldOrderItemTran = Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran();
                    if (alCheckedModifier.size() != 0) {
                        if (alCheckedModifier.size() == alOldOrderItemTran.size()) {
                            for (int j = 0; j < alCheckedModifier.size(); j++) {
                                for (int k = 0; k < alOldOrderItemTran.size(); k++) {
                                    if (alCheckedModifier.get(j).getItemMasterId() == alOldOrderItemTran.get(k).getItemMasterId()) {
                                        cntModifier = cntModifier + 1;
                                    }
                                }
                            }
                        }
                    }
                    if (cntModifier == alCheckedModifier.size() && ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt) || (sbOptionValue.toString().equals("") && (Globals.alOrderItemTran.get(i).getRemark()==null || Globals.alOrderItemTran.get(i).getRemark().equals(""))))) {
                        isDuplicate = true;
                        Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        if (alCheckedModifier.size() > 0) {
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        }else{
                            Globals.alOrderItemTran.get(i).setTotalAmount(Globals.alOrderItemTran.get(i).getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        }
                        Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                        }
                        CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                        Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                        break;
                    } else if (sbOptionValue.toString().equals("") && (Globals.alOrderItemTran.get(i).getRemark()==null || Globals.alOrderItemTran.get(i).getRemark().equals("")) && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0) {
                        isDuplicate = true;
                        Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        if (alCheckedModifier.size() > 0) {
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                        }else{
                            Globals.alOrderItemTran.get(i).setTotalAmount(Globals.alOrderItemTran.get(i).getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                        }
                        Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                            SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                        }
                        CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                        Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                        break;
                    }
                }
            } else {
                if ((objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId())
                        && ((sbOptionValue.toString().equals("") && (Globals.alOrderItemTran.get(i).getRemark()==null || Globals.alOrderItemTran.get(i).getRemark().equals("")) && alCheckedModifier.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0)
                        || (strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt && alCheckedModifier.size() == 0 && Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() == 0))) {
                    isDuplicate = true;
                    Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()));
                    if (alCheckedModifier.size() > 0) {
                        Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) + (alCheckedModifier.get(alCheckedModifier.size() - 1).getTotalAmount() * Integer.valueOf(etQuantity.getText().toString())));
                    }else{
                        Globals.alOrderItemTran.get(i).setTotalAmount(Globals.alOrderItemTran.get(i).getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                    }
                    Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                    if (Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran().size() > 0) {
                        SetOrderItemModifierQty(Globals.alOrderItemTran.get(i).getAlOrderItemModifierTran(), Globals.alOrderItemTran.get(i).getQuantity());
                    }
                    CountTax(Globals.alOrderItemTran.get(i), isDuplicate);
                    Globals.alOrderItemTran.get(i).setTotalTax(Globals.alOrderItemTran.get(i).getTotalTax() + totalTax);
                    break;
                }
            }
        }

    }

    private void SetOrderItemModifierQty(ArrayList<ItemMaster> alItemMasterModifier, int Quantity) {
        for (int j = 0; j < alItemMasterModifier.size(); j++) {
            alItemMasterModifier.get(j).setSellPrice(alItemMasterModifier.get(j).getSellPrice() * Quantity);
        }
    }

    private void CountTax(ItemMaster objOrderItemMaster, boolean isDuplicate) {
        totalTax = 0;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            String[] strTax = objItemMaster.getTax().split(",");
            for (int i = 0; i < strTax.length; i++) {
                if (isDuplicate) {
                    totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    if (i == 0) {
                        objOrderItemMaster.setTax1(objOrderItemMaster.getTax1() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 1) {
                        objOrderItemMaster.setTax2(objOrderItemMaster.getTax2() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 2) {
                        objOrderItemMaster.setTax3(objOrderItemMaster.getTax3() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 3) {
                        objOrderItemMaster.setTax4(objOrderItemMaster.getTax4() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else {
                        objOrderItemMaster.setTax5(objOrderItemMaster.getTax5() + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    }
                } else {
                    totalTax = totalTax + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100;
                    if (i == 0) {
                        objOrderItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 1) {
                        objOrderItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 2) {
                        objOrderItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else if (i == 3) {
                        objOrderItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    } else {
                        objOrderItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(strTax[i]) / 100);
                    }
                }
            }
        }
    }
    //endregion
}
