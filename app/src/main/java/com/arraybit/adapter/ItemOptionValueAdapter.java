package com.arraybit.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.arraybit.abposw.DetailActivity;
import com.arraybit.abposw.ItemModifierRemarkFragment;
import com.arraybit.abposw.R;
import com.arraybit.modal.OptionMaster;
import com.arraybit.modal.OptionValueTran;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class ItemOptionValueAdapter extends RecyclerView.Adapter<ItemOptionValueAdapter.ItemOptionValueViewHolder> {

    int position = -1, rowNumber = -1;
    View view;
    Context context;
    ArrayList<OptionMaster> alOptionMaster;
    boolean isActivity;

    public ItemOptionValueAdapter(Context context, ArrayList<OptionMaster> result, boolean isActivity) {
        this.context = context;
        alOptionMaster = result;
        this.isActivity = isActivity;
    }

    @Override
    public ItemOptionValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_option_name, parent, false);
        return new ItemOptionValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemOptionValueViewHolder holder, int position) {
        OptionMaster objOptionMaster = alOptionMaster.get(position);
        holder.txtOptionName.setText(objOptionMaster.getOptionName());
        if (objOptionMaster.getAlOptionValueTran() != null) {
            SetRadioButton(objOptionMaster.getAlOptionValueTran(), holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return alOptionMaster.size();
    }

    private void SetRadioButton(final ArrayList<OptionValueTran> alOptionValueTran, final ItemOptionValueViewHolder holder, final int rowPosition) {
        final RadioButton[] radioButton = new RadioButton[alOptionValueTran.size()];

        final LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams radioGroupParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(radioGroupParams);
        linearLayout.setId(rowPosition);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < alOptionValueTran.size(); i++) {
            radioButton[i] = new RadioButton(context);
            LinearLayout.LayoutParams radioButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton[i].setPadding(16, 0, 16, 0);
            radioButton[i].setLayoutParams(radioButtonParams);
            radioButton[i].setId(i);
            radioButton[i].setText(alOptionValueTran.get(i).getOptionValue());
            radioButton[i].setTextSize(14f);
            radioButton[i].setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            radioButton[i].applyStyle(R.style.RadioButton);
            radioButton[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isActivity) {
                        if (rowNumber == -1) {
                            rowNumber = linearLayout.getId();
                        }
                        if (linearLayout.getId() == rowNumber) {
                            if (position == -1) {
                                position = buttonView.getId();
                                DetailActivity.alOptionValue.get(rowNumber).setOptionRowId(position);
                                DetailActivity.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());

                            } else {
                                radioButton[DetailActivity.alOptionValue.get(rowNumber).getOptionRowId()].setChecked(false);
                                position = buttonView.getId();
                                DetailActivity.alOptionValue.get(rowNumber).setOptionRowId(position);
                                DetailActivity.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            }
                        } else {
                            position = buttonView.getId();
                            rowNumber = linearLayout.getId();
                            if (DetailActivity.alOptionValue.get(rowNumber).getOptionRowId() != -1) {
                                radioButton[DetailActivity.alOptionValue.get(rowNumber).getOptionRowId()].setChecked(false);
                                position = buttonView.getId();
                                DetailActivity.alOptionValue.get(rowNumber).setOptionRowId(position);
                                DetailActivity.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            } else {
                                DetailActivity.alOptionValue.get(rowNumber).setOptionRowId(position);
                                DetailActivity.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            }
                        }
                    } else {
                        if (rowNumber == -1) {
                            rowNumber = linearLayout.getId();
                        }
                        if (linearLayout.getId() == rowNumber) {
                            if (position == -1) {
                                position = buttonView.getId();
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionRowId(position);
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());

                            } else {
                                radioButton[ItemModifierRemarkFragment.alOptionValue.get(rowNumber).getOptionRowId()].setChecked(false);
                                position = buttonView.getId();
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionRowId(position);
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            }
                        } else {
                            position = buttonView.getId();
                            rowNumber = linearLayout.getId();
                            if (ItemModifierRemarkFragment.alOptionValue.get(rowNumber).getOptionRowId() != -1) {
                                radioButton[ItemModifierRemarkFragment.alOptionValue.get(rowNumber).getOptionRowId()].setChecked(false);
                                position = buttonView.getId();
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionRowId(position);
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            } else {
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionRowId(position);
                                ItemModifierRemarkFragment.alOptionValue.get(rowNumber).setOptionName(buttonView.getText().toString());
                            }
                        }
                    }
                }
            });

            linearLayout.addView(radioButton[i]);
        }

        holder.layoutChild.addView(linearLayout);
    }

    class ItemOptionValueViewHolder extends RecyclerView.ViewHolder {

        TextView txtOptionName;
        LinearLayout layoutChild;

        public ItemOptionValueViewHolder(View itemView) {
            super(itemView);

            txtOptionName = (TextView) itemView.findViewById(R.id.txtOptionName);
            layoutChild = (LinearLayout) itemView.findViewById(R.id.layoutChild);

        }
    }
}
