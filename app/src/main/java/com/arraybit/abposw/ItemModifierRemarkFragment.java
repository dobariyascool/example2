package com.arraybit.abposw;


import android.annotation.SuppressLint;
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
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
@SuppressLint("ValidFragment")
public class ItemModifierRemarkFragment extends Fragment implements OptionValueJSONParser.OptionValueRequestListener,View.OnClickListener,ItemJSONParser.ItemMasterRequestListener,ModifierAdapter.ModifierCheckedChangeListener {

    public static ArrayList<OptionMaster> alOptionValue;
    RecyclerView rvOptionValue,rvModifier;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    ItemMaster objItemMaster;
    ArrayList<OptionMaster> alOptionMaster;
    String strOptionName;
    ArrayList<OptionValueTran> lstOptionValueTran;
    Button btnAdd;
    StringBuilder sbOptionValue,sbModifierName;
    TextView txtItemDescription,txtItemRate;
    ImageView ivItem;
    ArrayList<ItemMaster> alItemMasterModifier;
    ArrayList<ItemMaster> alCheckedModifier = new ArrayList<>();
    boolean isDuplicate = false;

    public ItemModifierRemarkFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_modifier_remark, container, false);

        ivItem = (ImageView)view.findViewById(R.id.ivItem);

        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(objItemMaster.getItemName());

        setHasOptionsMenu(true);

        txtItemDescription = (TextView)view.findViewById(R.id.txtItemDescription);
        txtItemRate = (TextView)view.findViewById(R.id.txtItemRate);

        rvModifier = (RecyclerView)view.findViewById(R.id.rvModifier);
        rvOptionValue = (RecyclerView)view.findViewById(R.id.rvOptionValue);

        btnAdd = (Button)view.findViewById(R.id.btnAdd);

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
        if(alItemMaster==null){
            rvModifier.setVisibility(View.GONE);
        }else if(alItemMaster.size()==0){
            rvModifier.setVisibility(View.GONE);
        }else{
            rvModifier.setVisibility(View.VISIBLE);
            rvModifier.setAdapter(new ModifierAdapter(getActivity(), alItemMaster, this));
            rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }

//    @Override
//    public void ModifierResponse(boolean isChange) {
//         sbModifierName = new StringBuilder();
//            if (ModifierSelectionFragmentDialog.alFinalCheckedModifier.size() > 0) {
//                for (int i = 0; i < ModifierSelectionFragmentDialog.alFinalCheckedModifier.size(); i++) {
//                    sbModifierName.append(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getItemName()).append(", ");
//                }
//            }
//        if (!sbModifierName.toString().equals("")) {
//            txtModifier.setVisibility(View.VISIBLE);
//            txtModifier.setText(sbModifierName.toString());
//        } else {
//            txtModifier.setVisibility(View.GONE);
//            txtModifier.setText("");
//        }
//    }

    private void RequestOptionValue() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        OptionValueJSONParser objOptionValueJSONParser = new OptionValueJSONParser();
        objOptionValueJSONParser.SelectAllItemOptionValue(String.valueOf(objItemMaster.getItemMasterId()), getActivity(), this);
    }

    private void RequestItemModifier() {
        progressDialog.dismiss();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemModifier(this,getActivity(), String.valueOf(objItemMaster.getItemMasterId()));
    }

    private void SetRecyclerView(ArrayList<OptionValueTran> lstOptionValue) {
        if (lstOptionValue == null) {
        } else if (lstOptionValue.size() == 0) {

        } else {
            SetOptionMasterList(lstOptionValue);
            rvOptionValue.setAdapter(new ItemOptionValueAdapter(getActivity(),alOptionMaster));
            rvOptionValue.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void SetOptionMasterList(ArrayList<OptionValueTran> lstOptionValue){
        alOptionMaster = new ArrayList<>();
        lstOptionValueTran = new ArrayList<>();
        OptionMaster objOptionMaster = new OptionMaster();
        for(int i=0;i<lstOptionValue.size();i++){
            if(strOptionName==null){
                strOptionName = lstOptionValue.get(i).getOptionName();
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(lstOptionValue.get(i).getOptionName());
                objOptionMaster.setOptionMasterId(lstOptionValue.get(i).getlinktoOptionMasterId());
                lstOptionValueTran.add(lstOptionValue.get(i));
            }else{
                if(strOptionName.equals(lstOptionValue.get(i).getOptionName())){
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    if(i==lstOptionValue.size()-1){
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                }else{
                    objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                    alOptionMaster.add(objOptionMaster);
                    strOptionName = lstOptionValue.get(i).getOptionName();
                    objOptionMaster = new OptionMaster();
                    lstOptionValueTran = new ArrayList<>();
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    objOptionMaster.setOptionRowId(-1);
                    objOptionMaster.setOptionName(lstOptionValue.get(i).getOptionName());
                    objOptionMaster.setOptionMasterId(lstOptionValue.get(i).getlinktoOptionMasterId());
                    if(i==lstOptionValue.size()-1){
                        objOptionMaster.setAlOptionValueTran(lstOptionValueTran);
                        alOptionMaster.add(objOptionMaster);
                    }
                }
            }
        }

        alOptionValue =  new ArrayList<>();
        if(alOptionMaster.size()>0) {
            for (int j = 0; j < alOptionMaster.size(); j++) {
                objOptionMaster = new OptionMaster();
                objOptionMaster.setOptionRowId(-1);
                objOptionMaster.setOptionName(null);
                objOptionMaster.setOptionMasterId(alOptionMaster.get(j).getOptionMasterId());
                alOptionValue.add(objOptionMaster);
            }
        }
    }

    private void SetDetail(){
        if (objItemMaster.getSm_ImagePhysicalName().equals("null")) {
            Picasso.with(ivItem.getContext()).load(R.drawable.default_image).into(ivItem);
        } else {
            Picasso.with(ivItem.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(ivItem);
        }
        if(objItemMaster.getShortDescription().equals("")) {
            txtItemDescription.setText(objItemMaster.getItemName());
        }else{
            txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        txtItemRate.setText("Rs "+objItemMaster.getRate());
    }

    private void SetItemRemark(){
        sbOptionValue = new StringBuilder();
        for(int i=0;i<alOptionValue.size();i++){
            if(alOptionValue.get(i).getOptionName()!=null){
                sbOptionValue.append(alOptionValue.get(i).getOptionName()).append(",");
            }
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
}
