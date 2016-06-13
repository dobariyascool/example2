package com.arraybit.abposw;


import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackMaster;
import com.arraybit.modal.FeedbackQuestionMaster;
import com.arraybit.parser.FeedbackQuestionJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.CompoundButton;
import com.rey.material.widget.EditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class FeedbackViewFragment extends Fragment implements FeedbackQuestionJSONParser.FeedbackSubmitRequestListener {

    public final static String ITEMS_COUNT_KEY = "TableTabFragment$ItemsCount";
    LinearLayout feedbackViewFragment;
    ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster, alFeedbackAnswer;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster, lstAnswerMaster;
    int rowPosition = -1, currentView, rowNumber = -1, feedbackType;
    FeedbackMaster objFeedbackMaster;
    FeedbackAnswerMaster objFeedbackAnswerMaster;
    SharePreferenceManage objSharePreferenceManage;
    View focusView;
    ProgressDialog progressDialog = new ProgressDialog();

    public FeedbackViewFragment() {
        // Required empty public constructor
    }

    public static FeedbackViewFragment createInstance(ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster, int position) {
        FeedbackViewFragment feedbackViewFragment = new FeedbackViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ITEMS_COUNT_KEY, alFeedbackQuestionMaster);
        bundle.putInt("Position", position);
        feedbackViewFragment.setArguments(bundle);
        return feedbackViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_view, container, false);

        feedbackViewFragment = (LinearLayout) view.findViewById(R.id.feedbackViewFragment);

        Bundle bundle = getArguments();
        alFeedbackQuestionMaster = bundle.getParcelableArrayList(ITEMS_COUNT_KEY);
        currentView = bundle.getInt("Position");

        SetLayout();

        if (currentView != FeedbackActivity.alFinalFeedbackAnswer.size()) {
            FeedbackActivity.alFinalFeedbackAnswer.get(currentView).addAll(alFeedbackAnswer);
        }

        return view;
    }


    @Override
    public void FeedbackSubmitResponse(String errorCode, FeedbackMaster objCustomerMaster) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    //region Private Method

    private void SetLayout() {
        CreateAnswerList();
        for (int i = 0; i < alFeedbackQuestionMaster.size(); i++) {
            if (Globals.QuestionType.Input.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetInputLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.QuestionType.SingleSelect.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetSingleChoiceLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.QuestionType.MultiSelect.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetMultiChoiceLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.QuestionType.Rating.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetRatingLayout(alFeedbackQuestionMaster.get(i), i);
            } else if (Globals.QuestionType.Simple_Feedback.getValue() == alFeedbackQuestionMaster.get(i).getQuestionType()) {
                SetSimpleFeedbackLayout();
            }
        }
    }

    private void CreateAnswerList() {
        alFeedbackAnswer = new ArrayList<>();
        ArrayList<FeedbackAnswerMaster> lstFeedbackAnswerMaster;
        for (FeedbackQuestionMaster objFeedbackAnswerMaster : alFeedbackQuestionMaster) {
            FeedbackQuestionMaster objFeedbackQuestion = new FeedbackQuestionMaster();
            objFeedbackQuestion.setFeedbackRowPosition(-1);
            objFeedbackQuestion.setFeedbackQuestionMasterId(objFeedbackAnswerMaster.getFeedbackQuestionMasterId());
            objFeedbackQuestion.setQuestionType(objFeedbackAnswerMaster.getQuestionType());
            if (objFeedbackAnswerMaster.getQuestionType() != Globals.QuestionType.Simple_Feedback.getValue()) {
                lstFeedbackAnswerMaster = new ArrayList<>();
                if (objFeedbackAnswerMaster.getAlFeedbackAnswerMaster() != null && objFeedbackAnswerMaster.getAlFeedbackAnswerMaster().size() != 0) {
                    for (FeedbackAnswerMaster objAnswerMaster : objFeedbackAnswerMaster.getAlFeedbackAnswerMaster()) {
                        FeedbackAnswerMaster objFeedbackAnswer = new FeedbackAnswerMaster();
                        lstFeedbackAnswerMaster.add(objFeedbackAnswer);
                    }
                    objFeedbackQuestion.setAlFeedbackAnswerMaster(lstFeedbackAnswerMaster);
                    alFeedbackAnswer.add(objFeedbackQuestion);
                } else {
                    alFeedbackAnswer.add(objFeedbackQuestion);
                }
            }
        }
    }

    private void SetSingleChoiceLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        CardView cardView = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.cardview_layout, feedbackViewFragment, false);
        final LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setPadding(16, 4, 16, 4);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setId(position);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumberLayoutParams.gravity = Gravity.TOP;
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
        txtQuestionLayoutParams.gravity = Gravity.TOP;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtQuestion.setTextSize(18f);

        LinearLayout childLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(childLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txt = new TextView(getActivity());
        LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(txtLayoutParams);
        txt.setVisibility(View.INVISIBLE);

        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroupLayoutParams.weight = 1f;
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        if (Build.VERSION.SDK_INT >= 21) {
            radioGroup.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
        }

        if (objFeedbackQuestionMaster.getAlFeedbackAnswerMaster() != null) {
            alFeedbackAnswerMaster = objFeedbackQuestionMaster.getAlFeedbackAnswerMaster();
        }

        final RadioButton[] rbAnswer = new RadioButton[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerMaster.size(); j++) {
            rbAnswer[j] = new RadioButton(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rbAnswer[j].setId(j);
            rbAnswer[j].setTag(alFeedbackAnswerMaster.get(j).getFeedbackAnswerMasterId());
            rbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            rbAnswer[j].setText(alFeedbackAnswerMaster.get(j).getAnswer());
            rbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            rbAnswer[j].setGravity(Gravity.START | Gravity.CENTER);
            rbAnswer[j].setTextSize(14f);
            rbAnswer[j].applyStyle(R.style.RadioButton);

            rbAnswer[j].setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                    Globals.HideKeyBoard(getActivity(), buttonView);
                    if (rowNumber == -1) {
                        rowNumber = linearLayout.getId();
                    }
                    if (linearLayout.getId() == rowNumber) {
                        if (rowPosition == -1) {
                            rowPosition = buttonView.getId();
                            alFeedbackAnswer.get(rowNumber).setFeedbackRowPosition(rowPosition);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setAnswer(buttonView.getText().toString());
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setFeedbackAnswerMasterId((Integer) buttonView.getTag());
                        } else {
                            rbAnswer[alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()].setChecked(false);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()).setAnswer(null);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()).setFeedbackAnswerMasterId(0);
                            rowPosition = buttonView.getId();
                            alFeedbackAnswer.get(rowNumber).setFeedbackRowPosition(rowPosition);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setAnswer(buttonView.getText().toString());
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setFeedbackAnswerMasterId((Integer) buttonView.getTag());
                        }
                    } else {
                        rowPosition = buttonView.getId();
                        rowNumber = linearLayout.getId();
                        if (alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition() != -1) {
                            rbAnswer[alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()].setChecked(false);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()).setAnswer(null);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(alFeedbackAnswer.get(rowNumber).getFeedbackRowPosition()).setFeedbackAnswerMasterId(0);
                            rowPosition = buttonView.getId();
                            alFeedbackAnswer.get(rowNumber).setFeedbackRowPosition(rowPosition);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setAnswer(buttonView.getText().toString());
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setFeedbackAnswerMasterId((Integer) buttonView.getTag());
                        } else {
                            alFeedbackAnswer.get(rowNumber).setFeedbackRowPosition(rowPosition);
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setAnswer(buttonView.getText().toString());
                            alFeedbackAnswer.get(rowNumber).getAlFeedbackAnswerMaster().get(rowPosition).setFeedbackAnswerMasterId((Integer) buttonView.getTag());
                        }
                    }
                }
            });
            radioGroup.addView(rbAnswer[j]);
        }

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        childLayout.addView(txt);
        childLayout.addView(radioGroup);
        linearLayout.addView(headerLayout);
        linearLayout.addView(childLayout);
        cardView.addView(linearLayout);
        feedbackViewFragment.addView(cardView);
    }

    private void SetMultiChoiceLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        CardView cardView = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.cardview_layout, feedbackViewFragment, false);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setPadding(16, 4, 16, 4);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumberLayoutParams.gravity = Gravity.TOP;
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.gravity = Gravity.TOP;
        txtQuestionLayoutParams.weight = 1f;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtQuestion.setTextSize(18f);

        LinearLayout childLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(childLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txt = new TextView(getActivity());
        LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(txtLayoutParams);
        txt.setVisibility(View.INVISIBLE);

        LinearLayout answerLinearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams answerLinearLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        answerLinearLayoutParams.weight = 1f;
        answerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        answerLinearLayout.setGravity(Gravity.START | Gravity.CENTER);
        answerLinearLayout.setLayoutParams(answerLinearLayoutParams);

        if (objFeedbackQuestionMaster.getAlFeedbackAnswerMaster() != null) {
            alFeedbackAnswerMaster = objFeedbackQuestionMaster.getAlFeedbackAnswerMaster();
        }
        final CheckBox[] cbAnswer = new CheckBox[alFeedbackAnswerMaster.size()];

        for (int j = 0; j < alFeedbackAnswerMaster.size(); j++) {
            cbAnswer[j] = new CheckBox(getActivity());
            LinearLayout.LayoutParams rbAnswerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cbAnswer[j].setId(j);
            cbAnswer[j].setTag(alFeedbackAnswerMaster.get(j).getFeedbackAnswerMasterId());
            cbAnswer[j].setLayoutParams(rbAnswerLayoutParams);
            cbAnswer[j].setText(alFeedbackAnswerMaster.get(j).getAnswer());
            cbAnswer[j].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            cbAnswer[j].setGravity(Gravity.START | Gravity.CENTER);
            cbAnswer[j].setTextSize(14f);
            cbAnswer[j].applyStyle(R.style.CheckBox);
            cbAnswer[j].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                    Globals.HideKeyBoard(getActivity(), buttonView);
                    if (isChecked) {
                        alFeedbackAnswer.get(position).getAlFeedbackAnswerMaster().get(buttonView.getId()).setAnswer(buttonView.getText().toString());
                        alFeedbackAnswer.get(position).getAlFeedbackAnswerMaster().get(buttonView.getId()).setFeedbackAnswerMasterId((Integer) buttonView.getTag());
                    } else {
                        alFeedbackAnswer.get(position).getAlFeedbackAnswerMaster().get(buttonView.getId()).setAnswer(null);
                        alFeedbackAnswer.get(position).getAlFeedbackAnswerMaster().get(buttonView.getId()).setFeedbackAnswerMasterId(0);
                    }
                }
            });

            answerLinearLayout.addView(cbAnswer[j]);
        }

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        childLayout.addView(txt);
        childLayout.addView(answerLinearLayout);
        linearLayout.addView(headerLayout);
        linearLayout.addView(childLayout);
        cardView.addView(linearLayout);
        feedbackViewFragment.addView(cardView);
    }


    private void SetInputLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        CardView cardView = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.cardview_layout, feedbackViewFragment, false);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(4, 4, 4, 4);

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtQuestion.setTextSize(18f);

        final EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextLayoutParams);
        editText.setPadding(30, 0, 0, 0);
        editText.applyStyle(R.style.EditText);
        editText.setSingleLine();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alFeedbackAnswer.get(position).setFeedbackAnswer(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        linearLayout.addView(headerLayout);
        linearLayout.addView(editText);
        cardView.addView(linearLayout);
        feedbackViewFragment.addView(cardView);
    }


    private void SetRatingLayout(final FeedbackQuestionMaster objFeedbackQuestionMaster, final int position) {
        CardView cardView = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.cardview_layout, feedbackViewFragment, false);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 8, 16, 8);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(4, 4, 4, 4);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout headerLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams headerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerLayoutParams.setMargins(0, 0, 0, 0);
        headerLayout.setLayoutParams(linearLayoutParams);
        headerLayout.setGravity(Gravity.CENTER);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView txtNumber = new TextView(getActivity());
        LinearLayout.LayoutParams txtNumberLayoutParams = new LinearLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtNumber.setLayoutParams(txtNumberLayoutParams);
        txtNumber.setText(position + 1 + ".");
        txtNumber.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtNumber.setTextSize(18f);

        TextView txtQuestion = new TextView(getActivity());
        LinearLayout.LayoutParams txtQuestionLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtQuestionLayoutParams.weight = 1f;
        txtQuestion.setLayoutParams(txtQuestionLayoutParams);
        txtQuestion.setText(objFeedbackQuestionMaster.getFeedbackQuestion());
        txtQuestion.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        txtQuestion.setTextSize(18f);

        RatingBar ratingBar = new RatingBar(getActivity());
        LinearLayout.LayoutParams ratingBarLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingBar.setLayoutParams(ratingBarLayoutParams);
        ratingBar.setNumStars(5);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Globals.HideKeyBoard(getActivity(), ratingBar);
                alFeedbackAnswer.get(position).setFeedbackAnswer(String.valueOf(ratingBar.getRating()));
            }
        });

        headerLayout.addView(txtNumber);
        headerLayout.addView(txtQuestion);
        linearLayout.addView(headerLayout);
        linearLayout.addView(ratingBar);
        cardView.addView(linearLayout);
        feedbackViewFragment.addView(cardView);
    }

    private void SetSimpleFeedbackLayout() {
        feedbackViewFragment.removeAllViews();
        ScrollView scrollview = new ScrollView(getActivity());
        LinearLayout.LayoutParams scLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scLayoutParams.setMargins(16, 64, 16, 64);
        scrollview.setLayoutParams(scLayoutParams);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        final RadioButton[] radioButton = new RadioButton[3];

        for (int i = 0; i < 3; i++) {
            radioButton[i] = new RadioButton(getActivity());
            LinearLayout.LayoutParams rbLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton[i].setLayoutParams(rbLayoutParams);
            radioButton[i].setId(i);
            radioButton[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
            radioButton[i].applyStyle(R.style.RadioButton);

            if (i == 0) {
                radioButton[i].setText(Globals.FeedbackType.Suggestion.toString());
                radioButton[i].setTag(Globals.FeedbackType.Suggestion.getValue());
                rowPosition = i;
                feedbackType = Globals.FeedbackType.Suggestion.getValue();
                radioButton[i].setChecked(true);
            } else if (i == 1) {
                radioButton[i].setText(Globals.FeedbackType.BugReport.toString());
                radioButton[i].setTag(Globals.FeedbackType.BugReport.getValue());
            } else {
                radioButton[i].setText(Globals.FeedbackType.OtherQuery.toString());
                radioButton[i].setTag(Globals.FeedbackType.OtherQuery.getValue());
            }

            radioButton[i].setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                    Globals.HideKeyBoard(getActivity(), buttonView);
                    radioButton[rowPosition].setChecked(false);
                    rowPosition = buttonView.getId();
                    feedbackType = (int) buttonView.getTag();
                }
            });

            radioGroup.addView(radioButton[i]);
        }

        final EditText etUserName = new EditText(getActivity());
        LinearLayout.LayoutParams etUserNameLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etUserName.setLayoutParams(etUserNameLayoutParams);
        etUserName.applyStyle(R.style.EditText);
        etUserName.setHint(getActivity().getResources().getString(R.string.fbName));
        etUserName.setSingleLine();
        etUserName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final EditText etEmail = new EditText(getActivity());
        LinearLayout.LayoutParams etEmailLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etEmail.setLayoutParams(etEmailLayoutParams);
        etEmail.applyStyle(R.style.EditText);
        etEmail.setHint(getActivity().getResources().getString(R.string.fbEmail));
        etEmail.setSingleLine();
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        final EditText etMobileNo = new EditText(getActivity());
        LinearLayout.LayoutParams etMobileNoLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        etMobileNo.setLayoutParams(etMobileNoLayoutParams);
        etMobileNo.applyStyle(R.style.EditText);
        etMobileNo.setHint(getActivity().getResources().getString(R.string.fbMobileNo));
        etMobileNo.setSingleLine();
        etMobileNo.setInputType(InputType.TYPE_CLASS_NUMBER);

        final EditText etFeedback = new EditText(getActivity());
        LinearLayout.LayoutParams etFeedbackLayoutParams = new LinearLayout.LayoutParams(600, 200);
        etFeedback.setLayoutParams(etFeedbackLayoutParams);
        etFeedback.applyStyle(R.style.EditText);
        etFeedback.setHint(getActivity().getResources().getString(R.string.fbFeedback));
        etFeedback.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etFeedback.setMaxLines(10);
        etFeedback.setVerticalScrollBarEnabled(true);

        SetUser(etEmail, etUserName);
        Button btnSubmit = new Button(getActivity());
        LinearLayout.LayoutParams btnSubmitLayoutParams = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnSubmit.setLayoutParams(btnSubmitLayoutParams);
        btnSubmit.applyStyle(R.style.Button);
        btnSubmit.setText(getActivity().getResources().getString(R.string.fbSubmit));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(getActivity(), v);
                progressDialog.show(getActivity().getSupportFragmentManager(), "");
                focusView = v;
                if (!ValidateControls(etEmail, etFeedback, etMobileNo)) {
                    progressDialog.dismiss();
                    Globals.ShowSnackBar(focusView, getActivity().getResources().getString(R.string.MsgValidation), getActivity(), 1000);
                } else {
                    if (Service.CheckNet(getActivity())) {
                        objFeedbackMaster = new FeedbackMaster();
                        objFeedbackMaster.setName(etUserName.getText().toString());
                        objFeedbackMaster.setEmail(etEmail.getText().toString());
                        objFeedbackMaster.setPhone(etMobileNo.getText().toString());
                        objFeedbackMaster.setFeedback(etFeedback.getText().toString());
                        objFeedbackMaster.setFeedbackType((short) feedbackType);
                        objFeedbackMaster.setlinktoBusinessMasterId(Globals.linktoBusinessMasterId);
                        objSharePreferenceManage = new SharePreferenceManage();
                        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
                            objFeedbackMaster.setlinktoCustomerMasterId(Integer.parseInt(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity())));
                        }
                        lstAnswerMaster = new ArrayList<>();
                        for (ArrayList<FeedbackQuestionMaster> lstFeedbackQuestionMaster : FeedbackActivity.alFinalFeedbackAnswer) {
                            if (lstFeedbackQuestionMaster.size() > 0) {
                                int current = 0;
                                for (FeedbackQuestionMaster objFeedbackQuestionMaster : lstFeedbackQuestionMaster) {
                                    if (objFeedbackQuestionMaster.getFeedbackAnswer() != null) {
                                        objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                                        objFeedbackAnswerMaster.setFeedbackAnswerMasterId(objFeedbackQuestionMaster.getlinktoFeedbackAnswerMasterId());
                                        objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                                        objFeedbackAnswerMaster.setAnswer(objFeedbackQuestionMaster.getFeedbackAnswer());
                                        lstAnswerMaster.add(objFeedbackAnswerMaster);
                                    } else {
                                        if (lstFeedbackQuestionMaster.get(current).getAlFeedbackAnswerMaster() != null) {
                                            for (FeedbackAnswerMaster objFilterAnswerMaster : objFeedbackQuestionMaster.getAlFeedbackAnswerMaster()) {
                                                if (objFilterAnswerMaster.getAnswer() != null) {
                                                    objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                                                    objFeedbackAnswerMaster.setFeedbackAnswerMasterId(objFilterAnswerMaster.getFeedbackAnswerMasterId());
                                                    objFeedbackAnswerMaster.setlinktoFeedbackQuestionMasterId(objFeedbackQuestionMaster.getFeedbackQuestionMasterId());
                                                    lstAnswerMaster.add(objFeedbackAnswerMaster);
                                                }
                                            }
                                        }
                                    }
                                    current++;
                                }
                            }
                        }
                        FeedbackQuestionJSONParser objFeedbackQuestionJSONParser = new FeedbackQuestionJSONParser();
                        objFeedbackQuestionJSONParser.InsertFeedbackMaster(FeedbackViewFragment.this, getActivity(), objFeedbackMaster, lstAnswerMaster);
                    } else {
                        Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });

        linearLayout.addView(radioGroup);
        linearLayout.addView(etUserName);
        linearLayout.addView(etEmail);
        linearLayout.addView(etMobileNo);
        linearLayout.addView(etFeedback);
        linearLayout.addView(btnSubmit);
        scrollview.addView(linearLayout);
        feedbackViewFragment.addView(scrollview);
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                Globals.ShowSnackBar(focusView, getResources().getString(R.string.MsgFeedbackSubmit), getActivity(), 1000);
                getActivity().finish();
                break;
        }
    }

    private void SetUser(EditText etEmail, EditText etName) {
        objSharePreferenceManage = new SharePreferenceManage();
        if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()) != null) {
            etEmail.setText(objSharePreferenceManage.GetPreference("LoginPreference", "UserName", getActivity()));
            etEmail.setEnabled(false);
            if (objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()) != null) {
                etName.setText(objSharePreferenceManage.GetPreference("LoginPreference", "CustomerName", getActivity()));
                etName.setEnabled(false);
            } else {
                etName.setEnabled(true);
            }
        } else {
            etEmail.setText("");
            etName.setText("");
            etName.setEnabled(true);
            etEmail.setEnabled(true);
        }
    }

    private boolean ValidateControls(EditText etEmail, EditText etFeedback, EditText etMobileNo) {
        boolean IsValid = true;

        if (etEmail.getText().toString().equals("") && !etFeedback.getText().toString().equals("")) {
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            etFeedback.clearError();
            IsValid = false;
        } else if (!etEmail.getText().toString().equals("") && etFeedback.getText().toString().equals("")) {

            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
            } else {
                etEmail.clearError();
            }
            etFeedback.setError("Enter " + getResources().getString(R.string.fbFeedback));
            IsValid = false;
        } else if (etEmail.getText().toString().equals("") && etFeedback.getText().toString().equals("")) {
            etFeedback.setError("Enter " + getResources().getString(R.string.fbFeedback));
            etEmail.setError("Enter " + getResources().getString(R.string.fbEmail));
            IsValid = false;
        }else if (!etEmail.getText().toString().equals("") && !etFeedback.getText().toString().equals("")) {
            if (!Globals.IsValidEmail(etEmail.getText().toString())) {
                etEmail.setError("Enter Valid " + getResources().getString(R.string.fbEmail));
                IsValid = false;
            } else {
                etEmail.clearError();
                etFeedback.clearError();
            }
        }
        if (!etMobileNo.getText().toString().equals("") && etMobileNo.getText().length() != 10) {
            etMobileNo.setError("Enter 10 digit " + getResources().getString(R.string.fbMobileNo));
            IsValid = false;
        } else {
            etMobileNo.clearError();
        }
        return IsValid;
    }
    //endregion
}
