package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("UnusedAssignment")
@SuppressLint("ValidFragment")
public class AddQtyRemarkDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageButton ibMinus, ibPlus;
    TextView txtItemName;
    Button btnOk;
    EditText etQuantity, etRemark;
    ItemMaster objItemMaster;
    double totalAmount, totalTax;
    boolean isDuplicate, isEdit = false;
    String strItemName;
    ArrayList<ItemMaster> alOrderItemModifierTran = new ArrayList<>();
    AddQtyRemarkDialogListener objAddQtyRemarkDialogListener;
    double totalModifierAmount;

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

        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_menu)) || getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_activity_wish_list)) || getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_cart_item_fragment))) {
            txtItemName.setText(objItemMaster.getItemName());
        } else {
            txtItemName.setVisibility(View.GONE);
        }
        ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);
        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        etQuantity.setSelectAllOnFocus(true);
        etRemark = (EditText) view.findViewById(R.id.etRemark);

        btnOk = (Button) view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        ibPlus.setOnClickListener(this);
        etQuantity.setOnClickListener(this);

        if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_cart_item_fragment))) {
            isEdit = true;
            etRemark.setText(objItemMaster.getItemRemark());
            etQuantity.setText(String.valueOf(objItemMaster.getQuantity()));
        }

        etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etQuantity.setSelectAllOnFocus(true);
                }
            }
        });

        if(isEdit) {
            etRemark.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            Globals.HideKeyBoard(getActivity(), v);
                        }
                    }
                    return false;
                }
            });
        }

        etQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        Globals.HideKeyBoard(getActivity(), v);
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Globals.HideKeyBoard(getActivity(), v);
        if (v.getId() == R.id.btnOk) {
            if (etQuantity.getText().toString().equals("0")) {
                etQuantity.setText("1");
            }
            if (getActivity().getTitle().equals(getActivity().getResources().getString(R.string.title_cart_item_fragment))) {
                UpdateOrderItem();
                objAddQtyRemarkDialogListener = (AddQtyRemarkDialogListener) getTargetFragment();
                objAddQtyRemarkDialogListener.AddQtyRemarkResponse(objItemMaster);
                dismiss();
            } else {
                dismiss();
                SetOrderItem();
                if(getArguments()!=null && getArguments().getBoolean("isDetailActivity")){
                    ItemMaster objItem = new ItemMaster();
                    objItem.setItemName(strItemName);
                    objAddQtyRemarkDialogListener = (AddQtyRemarkDialogListener) getActivity();
                    objAddQtyRemarkDialogListener.AddQtyRemarkResponse(objItem);
                }else {
                    if (getActivity().getTitle().toString().equals(getResources().getString(R.string.title_activity_menu))) {
                        MenuActivity menuActivity = (MenuActivity) getActivity();
                        menuActivity.SetCartItemResponse(strItemName);
                    } else if (getActivity().getTitle().toString().equals(getResources().getString(R.string.title_activity_wish_list))) {
                        WishListActivity wishListActivity = (WishListActivity) getActivity();
                        wishListActivity.SetCartItemResponse(strItemName);
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ShowMessage", true);
                        returnIntent.putExtra("ItemName", strItemName);
                        getActivity().setResult(Activity.RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                }
            }
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
            strItemName = objItemMaster.getItemName();
            objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
            objOrderItemTran.setItemName(objItemMaster.getItemName());
            objOrderItemTran.setRate(objItemMaster.getRate());
            objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
            objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
            objOrderItemTran.setTax(objItemMaster.getTax());
            objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
            objOrderItemTran.setTaxRate(objItemMaster.getTaxRate());
            CountTax(objOrderItemTran, isDuplicate);
            objOrderItemTran.setTotalTax(totalTax);
            if (!etRemark.getText().toString().isEmpty()) {
                objOrderItemTran.setRemark(etRemark.getText().toString().trim());
                objOrderItemTran.setItemRemark(etRemark.getText().toString().trim());
            }
            totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
            objOrderItemTran.setTotalAmount(totalAmount);
            Globals.counter = Globals.counter + 1;
            Globals.alOrderItemTran.add(objOrderItemTran);
        } else {
            for (ItemMaster objFilterOrderItemTran : Globals.alOrderItemTran) {
                if ((objFilterOrderItemTran.getItemMasterId() == objItemMaster.getItemMasterId()) &&
                        ((objFilterOrderItemTran.getRemark() != null && objFilterOrderItemTran.getRemark().equals(etRemark.getText().toString()))
                                || (objFilterOrderItemTran.getRemark() == null && etRemark.getText().toString().isEmpty()))) {
                    isDuplicate = true;
                    strItemName = objItemMaster.getItemName();
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
                strItemName = objItemMaster.getItemName();
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setRate(objItemMaster.getRate());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setTaxRate(objItemMaster.getTaxRate());
                CountTax(objOrderItemTran, isDuplicate);
                objOrderItemTran.setTax(objItemMaster.getTax());
                objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                objOrderItemTran.setTotalTax(totalTax);
                if (!etRemark.getText().toString().isEmpty()) {
                    objOrderItemTran.setRemark(etRemark.getText().toString().trim());
                    objOrderItemTran.setItemRemark(etRemark.getText().toString().trim());
                }
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate();
                objOrderItemTran.setTotalAmount(totalAmount);
                Globals.counter = Globals.counter + 1;
                Globals.alOrderItemTran.add(objOrderItemTran);
            }
        }
    }

    private void UpdateOrderItem() {
        objItemMaster.setItemRemark(etRemark.getText().toString().trim());
        objItemMaster.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
        CountUpdateTax();
        objItemMaster.setTotalTax(totalTax);
        UpdateOrderItemModifier();
        if (alOrderItemModifierTran.size() != 0) {
            objItemMaster.setAlOrderItemModifierTran(alOrderItemModifierTran);
        }
        objItemMaster.setSellPrice(objItemMaster.getRate() * Integer.parseInt(etQuantity.getText().toString()));
        if (alOrderItemModifierTran.size() != 0) {
            objItemMaster.setTotalAmount((objItemMaster.getRate() + totalModifierAmount) * Integer.parseInt(etQuantity.getText().toString()));
        } else {
            objItemMaster.setTotalAmount(objItemMaster.getRate() * Integer.parseInt(etQuantity.getText().toString()));
        }
    }

    private void UpdateOrderItemModifier() {
        alOrderItemModifierTran = new ArrayList<>();
        totalModifierAmount = 0;
        if (objItemMaster.getAlOrderItemModifierTran() != null && objItemMaster.getAlOrderItemModifierTran().size() != 0) {
            for (ItemMaster objItemModifier : objItemMaster.getAlOrderItemModifierTran()) {
                ItemMaster objModifier = new ItemMaster();
                objModifier = objItemModifier;
                objModifier.setRate(objItemModifier.getRate());
                objModifier.setSellPrice(objItemModifier.getRate() * Integer.parseInt(etQuantity.getText().toString()));
                totalModifierAmount = totalModifierAmount + objModifier.getRate();
                alOrderItemModifierTran.add(objModifier);
            }
        }
    }

    private void CountUpdateTax() {
        totalTax = 0;
        int cnt = 0;
        double rate;
        if (objItemMaster.getTax() != null && !objItemMaster.getTax().equals("")) {
            ArrayList<String> alTax = new ArrayList<>(Arrays.asList(objItemMaster.getTax().split(",")));
            for (String tax : alTax) {
                if (objItemMaster.getTaxRate() == 0) {
                    totalTax = totalTax + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100;
                    if (cnt == 0) {
                        objItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 1) {
                        objItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 2) {
                        objItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                    } else if (cnt == 3) {
                        objItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                    } else {
                        objItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getRate()) * Double.valueOf(tax) / 100);
                    }
                } else {
                    rate = objItemMaster.getRate() + objItemMaster.getTaxRate();
                    totalTax = totalTax + ((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    if (cnt == 0) {
                        objItemMaster.setTax1((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 1) {
                        objItemMaster.setTax2((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 2) {
                        objItemMaster.setTax3((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else if (cnt == 3) {
                        objItemMaster.setTax4((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    } else {
                        objItemMaster.setTax5((Integer.valueOf(etQuantity.getText().toString()) * rate) * Double.valueOf(tax) / 100);
                    }
                }

                cnt++;
            }
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

    public interface AddQtyRemarkDialogListener {
        void AddQtyRemarkResponse(ItemMaster objItemMaster);
    }

    //endregion
}
