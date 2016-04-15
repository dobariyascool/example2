package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.arraybit.adapter.ModifierAdapter;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class ModifierSelectionFragmentDialog extends DialogFragment implements View.OnClickListener, ModifierAdapter.ModifierCheckedChangeListener {

    public static ArrayList<ItemMaster> alFinalCheckedModifier = new ArrayList<>();
    RecyclerView rvModifier;
    Button btnCancel, btnDone;
    ArrayList<ItemMaster> alItemModifier;
    ModifierResponseListener objModifierResponseListener;
    ArrayList<ItemMaster> alCheckedModifier;
    boolean isDuplicate = false;


    public ModifierSelectionFragmentDialog(ArrayList<ItemMaster> alItemModifier) {
        this.alItemModifier = alItemModifier;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modifier_selection_fragment_dialog, container, false);
        getDialog().getWindow().requestFeature(STYLE_NO_TITLE);

        alCheckedModifier = new ArrayList<>();

        rvModifier = (RecyclerView) view.findViewById(R.id.rvModifier);
        rvModifier.setAdapter(new ModifierAdapter(getActivity(), alItemModifier, this));
        rvModifier.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        SetRecyclerViewElevation();
        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        objModifierResponseListener = (ModifierResponseListener) getTargetFragment();
        objModifierResponseListener.ModifierResponse(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel) {
            dismiss();
            objModifierResponseListener = (ModifierResponseListener) getTargetFragment();
            objModifierResponseListener.ModifierResponse(false);

        } else if (v.getId() == R.id.btnDone) {
            dismiss();
            SetModifier();
            objModifierResponseListener = (ModifierResponseListener) getTargetFragment();
            objModifierResponseListener.ModifierResponse(true);
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

    //region Private Methods and Interface
    private void SetModifier() {
        alFinalCheckedModifier = new ArrayList<>();
        for (int i = 0; i < alCheckedModifier.size(); i++) {
            alFinalCheckedModifier.add(alCheckedModifier.get(i));
        }
    }

    private void SetRecyclerViewElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            rvModifier.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offwhite));
            rvModifier.setElevation(8f);
            rvModifier.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else {
            rvModifier.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bottom_border));
        }
    }

    interface ModifierResponseListener {
        void ModifierResponse(boolean isChange);
    }
    //endregion
}
