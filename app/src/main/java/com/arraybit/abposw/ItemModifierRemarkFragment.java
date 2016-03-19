package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.adapter.ItemOptionValueAdapter;
import com.arraybit.adapter.OfferAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OfferMaster;
import com.arraybit.modal.OptionMaster;
import com.arraybit.modal.OptionValueTran;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.OfferJSONParser;
import com.arraybit.parser.OptionValueJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ItemModifierRemarkFragment extends Fragment implements OptionValueJSONParser.OptionValueRequestListener,View.OnClickListener,ItemJSONParser.ItemMasterRequestListener,ModifierSelectionFragmentDialog.ModifierResponseListener{

    public static ArrayList<OptionMaster> alOptionValue;
    RecyclerView rvOptionValue;
    com.arraybit.abposw.ProgressDialog progressDialog = new com.arraybit.abposw.ProgressDialog();
    ItemMaster objItemMaster;
    ArrayList<OptionMaster> alOptionMaster;
    String strOptionName;
    ArrayList<OptionValueTran> lstOptionValueTran;
    Button btnAdd;
    StringBuilder sbOptionValue,sbModifierName;
    TextView txtItemDescription,txtItemRate,txtModifier;
    ImageView ivItem,ivModifier;
    ArrayList<ItemMaster> alItemMasterModifier;

    public ItemModifierRemarkFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_modifier_remark, container, false);

        ivItem = (ImageView)view.findViewById(R.id.ivItem);
        ivModifier = (ImageView)view.findViewById(R.id.ivModifier);

        txtItemDescription = (TextView)view.findViewById(R.id.txtItemDescription);
        txtItemRate = (TextView)view.findViewById(R.id.txtItemRate);
        txtModifier = (TextView)view.findViewById(R.id.txtModifier);

        rvOptionValue = (RecyclerView)view.findViewById(R.id.rvOptionValue);

        btnAdd = (Button)view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);
        ivModifier.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivModifier){
            ModifierSelectionFragmentDialog modifierSelectionFragmentDialog = new ModifierSelectionFragmentDialog(alItemMasterModifier);
            modifierSelectionFragmentDialog.setTargetFragment(this,0);
            modifierSelectionFragmentDialog.show(getActivity().getSupportFragmentManager(),"");
        }
    }

    @Override
    public void ModifierResponse(boolean isChange) {
         sbModifierName = new StringBuilder();
            if (ModifierSelectionFragmentDialog.alFinalCheckedModifier.size() > 0) {
                for (int i = 0; i < ModifierSelectionFragmentDialog.alFinalCheckedModifier.size(); i++) {
                    sbModifierName.append(ModifierSelectionFragmentDialog.alFinalCheckedModifier.get(i).getItemName()).append(", ");
                }
            }
        if (!sbModifierName.toString().equals("")) {
            txtModifier.setVisibility(View.VISIBLE);
            txtModifier.setText(sbModifierName.toString());
        } else {
            txtModifier.setVisibility(View.GONE);
            txtModifier.setText("");
        }
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
            }else{
                if(strOptionName.equals(lstOptionValue.get(i).getOptionName())){
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    lstOptionValueTran.add(lstOptionValue.get(i));
                    lstOptionValueTran.add(lstOptionValue.get(i));
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
}
