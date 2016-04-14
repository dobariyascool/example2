package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("ValidFragment")
public class AddQtyRemarkDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageButton ibMinus, ibPlus;
    Button btnCancel, btnOk;
    EditText etQuantity, etRemark;
    ItemMaster objItemMaster;
    double totalAmount, totalTax;
    boolean isDuplicate;

    public AddQtyRemarkDialogFragment(ItemMaster objItemMaster) {
        // Required empty public constructor
        this.objItemMaster = objItemMaster;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_qty_remark_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etRemark = (EditText) view.findViewById(R.id.etRemark);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {
            dismiss();
            SetOrderItem();
            if (getActivity().getTitle().toString().equals(getResources().getString(R.string.title_activity_menu))) {
                MenuActivity menuActivity = (MenuActivity) getActivity();
                menuActivity.SetCartItemResponse();
            } else if(getActivity().getTitle().toString().equals(getResources().getString(R.string.title_activity_wish_list))){
                WishListActivity wishListActivity = (WishListActivity) getActivity();
                wishListActivity.SetCartItemResponse();
            }else{
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
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
        }
    }

    //region Private methods
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
            if (!etRemark.getText().toString().isEmpty()) {
                objOrderItemTran.setRemark(etRemark.getText().toString());
            }
            totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
            objOrderItemTran.setTotalAmount(totalAmount);
            Globals.counter = Globals.counter + 1;
            Globals.alOrderItemTran.add(objOrderItemTran);
        } else {
            for(ItemMaster objFilterOrderItemTran : Globals.alOrderItemTran){
                if ((objFilterOrderItemTran.getItemMasterId() == objItemMaster.getItemMasterId()) &&
                        ((objFilterOrderItemTran.getRemark() != null && objFilterOrderItemTran.getRemark().equals(etRemark.getText().toString()))
                        || (objFilterOrderItemTran.getRemark() == null && etRemark.getText().toString().isEmpty()))) {
                    isDuplicate = true;
                    objFilterOrderItemTran.setSellPrice(objFilterOrderItemTran.getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                    objFilterOrderItemTran.setTotalAmount((objFilterOrderItemTran.getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()));
                    objFilterOrderItemTran.setQuantity(objFilterOrderItemTran.getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                    CountTax(objFilterOrderItemTran, isDuplicate);
                    objFilterOrderItemTran.setTotalTax(objFilterOrderItemTran.getTotalTax() + totalTax);
                    break;
                }
            }
            if (!isDuplicate) {
                ItemMaster objOrderItemTran = new ItemMaster();
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setRate(objItemMaster.getRate());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                CountTax(objOrderItemTran, isDuplicate);
                objOrderItemTran.setTax(objItemMaster.getTax());
                objOrderItemTran.setTotalTax(totalTax);
                if (!etRemark.getText().toString().isEmpty()) {
                    objOrderItemTran.setRemark(etRemark.getText().toString());
                }
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
                objOrderItemTran.setTotalAmount(totalAmount);
                Globals.counter = Globals.counter + 1;
                Globals.alOrderItemTran.add(objOrderItemTran);
            }
        }
    }

    private void CountTax(ItemMaster objOrderItemMaster, boolean isDuplicate) {
        totalTax = 0;
        int cnt = 0;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            ArrayList<String> alTax = new ArrayList<>(Arrays.asList(objItemMaster.getTax().split(",")));
            for(String tax : alTax) {
                if (isDuplicate) {
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
                }
                cnt++;
            }
        }
    }
    //endregion
}
