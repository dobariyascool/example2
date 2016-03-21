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

@SuppressLint("ValidFragment")
public class AddQtyRemarkDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageButton ibMinus, ibPlus;
    Button btnCancel, btnOk;
    EditText etQuantity, etRemark;
    ItemMaster objItemMaster;
    double totalAmount;
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
            if(getActivity().getTitle().toString().equals(getResources().getString(R.string.title_activity_menu))){
                MenuActivity menuActivity = (MenuActivity) getActivity();
                menuActivity.CartItemChangeResponse();
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
            if (!etRemark.getText().toString().isEmpty()) {
                objOrderItemTran.setRemark(etRemark.getText().toString());
            }
            totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
            objOrderItemTran.setTotalAmount(totalAmount);
            Globals.counter = Globals.counter + 1;
            Globals.alOrderItemTran.add(objOrderItemTran);
        } else {
            for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                if (Globals.alOrderItemTran.get(i).getItemMasterId() == objItemMaster.getItemMasterId() &&
                        (Globals.alOrderItemTran.get(i).getRemark() != null && Globals.alOrderItemTran.get(i).getRemark().equals(etRemark.getText().toString()))
                        || (Globals.alOrderItemTran.get(i).getRemark() == null && etRemark.getText().toString().isEmpty())) {
                    isDuplicate = true;
                    Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                    Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()));
                    Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
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
    //endregion
}
