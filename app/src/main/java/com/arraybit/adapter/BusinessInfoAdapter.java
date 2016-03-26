package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.modal.BusinessInfoAnswerMaster;
import com.arraybit.modal.BusinessInfoQuestionMaster;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class BusinessInfoAdapter extends RecyclerView.Adapter<BusinessInfoAdapter.BusinessInfoViewHolder> {

    View view;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster;

    public BusinessInfoAdapter(Context context, ArrayList<BusinessInfoQuestionMaster> alBusinessInfoQuestionMaster) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.alBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster;
    }

    @Override
    public BusinessInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_business_info, parent, false);
        return new BusinessInfoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BusinessInfoViewHolder holder, int position) {
        BusinessInfoQuestionMaster objBusinessInfoQuestionMaster = alBusinessInfoQuestionMaster.get(position);

        holder.txtQuestion.setText(objBusinessInfoQuestionMaster.getQuestion());
        holder.txtNumber.setText(position + 1 + ". ");

        SetLayout(holder, objBusinessInfoQuestionMaster.getAlBusinessInfoAnswerMaster(), objBusinessInfoQuestionMaster.getQuestionType());
    }

    @Override
    public int getItemCount() {
        return alBusinessInfoQuestionMaster.size();
    }

    private void SetLayout(BusinessInfoViewHolder holder, ArrayList<BusinessInfoAnswerMaster> alBusinessInfoAnswerMaster, short QuestionType) {
        if (QuestionType == 1) {
            TextView txtAnswer = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(68, 0, 68, 0);
            txtAnswer.setLayoutParams(layoutParams);
            txtAnswer.setText(alBusinessInfoAnswerMaster.get(0).getAnswer());
            holder.businessInfoLayout.addView(txtAnswer);
        } else if (QuestionType == 2) {
            GridLayout gridLayout = new GridLayout(context);
            LinearLayout.LayoutParams gridLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            gridLayoutParams.setMargins(68, 0, 68, 0);
            gridLayoutParams.gravity = Gravity.CENTER;
            gridLayout.setColumnCount(3);
            gridLayout.setLayoutParams(gridLayoutParams);
            RadioButton[] radioButton = new RadioButton[alBusinessInfoAnswerMaster.size()];
            for (int i = 0; i < alBusinessInfoAnswerMaster.size(); i++) {
                radioButton[i] = new RadioButton(context);
                GridLayout.LayoutParams radioButtonParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(GridLayout.LayoutParams.WRAP_CONTENT));
                radioButtonParams.setGravity(Gravity.FILL);
                radioButton[i].applyStyle(R.style.RadioButton);
                radioButton[i].setLayoutParams(radioButtonParams);
                radioButton[i].setId(i);
                radioButton[i].setWidth(0);
                radioButton[i].setEnabled(false);
                radioButton[i].setGravity(Gravity.CENTER_VERTICAL);
                radioButton[i].setText(alBusinessInfoAnswerMaster.get(i).getAnswer());
                ((GridLayout.LayoutParams) radioButton[i].getLayoutParams()).columnSpec =
                        GridLayout.spec(GridLayout.UNDEFINED, 1f);
                if (alBusinessInfoAnswerMaster.get(i).getIsAnswer()) {
                    radioButton[i].setChecked(true);
                }
                gridLayout.addView(radioButton[i]);
            }
            holder.businessInfoLayout.addView(gridLayout);
        } else if (QuestionType == 3) {
            GridLayout gridLayout = new GridLayout(context);
            LinearLayout.LayoutParams gridLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            gridLayoutParams.setMargins(68, 0, 68, 0);
            gridLayoutParams.gravity = Gravity.CENTER;
            gridLayout.setLayoutParams(gridLayoutParams);
            gridLayout.setColumnCount(3);
            CheckBox[] checkBox = new CheckBox[alBusinessInfoAnswerMaster.size()];
            for (int i = 0; i < alBusinessInfoAnswerMaster.size(); i++) {
                checkBox[i] = new CheckBox(context);
                GridLayout.LayoutParams checkBoxParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(GridLayout.LayoutParams.WRAP_CONTENT));
                checkBoxParams.setGravity(Gravity.FILL);
                checkBox[i].applyStyle(R.style.CheckBox);
                checkBox[i].setLayoutParams(checkBoxParams);
                checkBox[i].setId(i);
                checkBox[i].setWidth(0);
                checkBox[i].setEnabled(false);
                checkBox[i].setGravity(Gravity.CENTER_VERTICAL);
                checkBox[i].setText(alBusinessInfoAnswerMaster.get(i).getAnswer());
                ((GridLayout.LayoutParams) checkBox[i].getLayoutParams()).columnSpec =
                        GridLayout.spec(GridLayout.UNDEFINED, 1f);
                if (alBusinessInfoAnswerMaster.get(i).getIsAnswer()) {
                    checkBox[i].setChecked(true);
                }
                gridLayout.addView(checkBox[i]);
            }
            holder.businessInfoLayout.addView(gridLayout);
        }
    }

    class BusinessInfoViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion, txtNumber;
        LinearLayout businessInfoLayout;

        public BusinessInfoViewHolder(View itemView) {
            super(itemView);

            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            businessInfoLayout = (LinearLayout) itemView.findViewById(R.id.businessInfoLayout);
        }
    }
}


//    void SetRadioButton(ArrayList<OptionValueTran> alOptionValueTran, final ItemOptionValueViewHolder holder, final int rowPosition) {
//        final RadioButton[] radioButton = new RadioButton[alOptionValueTran.size()];
//        final GridLayout gridLayout = new GridLayout(context);
//        LinearLayout.LayoutParams gridLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        gridLayoutParams.gravity = Gravity.CENTER;
//        gridLayout.setId(rowPosition);
//        gridLayout.setLayoutParams(gridLayoutParams);
//        gridLayout.setColumnCount(3);
////        gridLayout.setRowOrderPreserved(true);
////        gridLayout.setColumnOrderPreserved(false);
////        gridLayout.setAlignmentMode(GridLayout.VERTICAL);
//        //linearLayout.setOrientation(GridLayout);
//        for (int i = 0; i < alOptionValueTran.size(); i++) {
//            radioButton[i] = new RadioButton(context);
//            GridLayout.LayoutParams radioButtonParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(GridLayout.LayoutParams.WRAP_CONTENT));
//            radioButtonParams.setGravity(Gravity.FILL);
//            radioButton[i].setLayoutParams(radioButtonParams);
//            radioButton[i].setId(i);
//            radioButton[i].setWidth(0);
//            radioButton[i].setText(alOptionValueTran.get(i).getOptionValue());
//            ((GridLayout.LayoutParams) radioButton[i].getLayoutParams()).columnSpec =
//                    GridLayout.spec(GridLayout.UNDEFINED,1f);
////            radioButton[i].setGravity(Gravity.CENTER_VERTICAL);
////            radioButton[i].setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    if (position == -1) {
////                        position = v.getId();
////                    } else {
////                        radioButton[position].setChecked(false);
////                        position = v.getId();
////                    }
////                }
////            });
//
//            radioButton[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(rowNumber==-1){
//                        rowNumber = gridLayout.getId();
//                    }
//                    if(gridLayout.getId()==rowNumber) {
//                        if (position == -1) {
//                            position = buttonView.getId();
//                            alAnswerMaster.get(rowNumber).setOptionRowId(position);
//                        } else {
//                            radioButton[alAnswerMaster.get(rowNumber).getOptionRowId()].setChecked(false);
//                            position = buttonView.getId();
//                            alAnswerMaster.get(rowNumber).setOptionRowId(position);
//                        }
//                    }else{
//                        position = buttonView.getId();
//                        rowNumber = gridLayout.getId();
//                        if(alAnswerMaster.get(rowNumber).getOptionRowId()!=-1){
//                            radioButton[alAnswerMaster.get(rowNumber).getOptionRowId()].setChecked(false);
//                            position = buttonView.getId();
//                            alAnswerMaster.get(rowNumber).setOptionRowId(position);
//                        }else{
//                            alAnswerMaster.get(rowNumber).setOptionRowId(position);
//                        }
//                    }
//                }
//            });
//
//            gridLayout.addView(radioButton[i]);
//        }
//
//        //linearLayout.addView(radioGroup);
//        holder.layoutChild.addView(gridLayout);
//    }

