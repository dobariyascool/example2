package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.FeedbackAnswerMaster;
import com.arraybit.modal.FeedbackQuestionMaster;
import com.arraybit.parser.FeedbackQuestionJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class FeedbackActivity extends AppCompatActivity implements FeedbackQuestionJSONParser.FeedbackQuestionRequestListener, View.OnClickListener {

    public static ArrayList<ArrayList<FeedbackQuestionMaster>> alFinalFeedbackAnswer = new ArrayList<>();
    Toolbar app_bar;
    ProgressDialog progressDialog = new ProgressDialog();
    ViewPager viewPager;
    FrameLayout feedbackLayout;
    LinearLayout errorLayout;
    TextView txtFeedbackGroup, txtPrevious, txtNext;
    ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster, alFilterFeedbackQuestionMaster;
    ArrayList<String> alQuestionGroup;
    String strGroupName, strQuestionName;
    int cnt;
    FeedbackPagerAdapter feedbackPagerAdapter;
    ArrayList<FeedbackAnswerMaster> alFeedbackAnswerMaster;
    ImageView ivNext, ivPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        feedbackLayout = (FrameLayout) findViewById(R.id.feedbackLayout);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        txtFeedbackGroup = (TextView) findViewById(R.id.txtFeedbackGroup);
        txtPrevious = (TextView) findViewById(R.id.txtPrevious);
        txtNext = (TextView) findViewById(R.id.txtNext);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SetViewPagerElevation();

        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);

        txtPrevious.setOnClickListener(this);
        txtNext.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);

        if (Service.CheckNet(this)) {
            RequestFeedbackQuestion();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                alFinalFeedbackAnswer = new ArrayList<>();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void FeedbackQuestionResponse(ArrayList<FeedbackQuestionMaster> alFeedbackQuestionMaster) {
        progressDialog.dismiss();
        this.alFeedbackQuestionMaster = alFeedbackQuestionMaster;
        SetViewPage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alFinalFeedbackAnswer = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivNext) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else if (v.getId() == R.id.ivPrevious) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else if (v.getId() == R.id.txtNext) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else if (v.getId() == R.id.txtPrevious) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    //region Private Method
    private void SetVisibility(int position) {
        if (position == 0) {
            txtNext.setEnabled(true);
            ivNext.setEnabled(true);
            txtPrevious.setEnabled(false);
            ivPrevious.setEnabled(false);
        } else if (position == viewPager.getOffscreenPageLimit() - 1) {
            txtNext.setEnabled(false);
            ivNext.setEnabled(false);
            txtPrevious.setEnabled(true);
            ivPrevious.setEnabled(true);
        } else if (position == -1) {
            txtNext.setEnabled(false);
            ivNext.setEnabled(false);
            txtPrevious.setEnabled(false);
            ivPrevious.setEnabled(false);
        } else {
            txtNext.setEnabled(true);
            ivNext.setEnabled(true);
            txtPrevious.setEnabled(true);
            ivPrevious.setEnabled(true);
        }
    }

    private void SetViewPagerElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            viewPager.setPadding(0, 0, 0, 8);
            viewPager.setBackgroundColor(ContextCompat.getColor(FeedbackActivity.this, R.color.offwhite));
            viewPager.setElevation(8f);
            viewPager.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        } else {
            viewPager.setBackground(ContextCompat.getDrawable(FeedbackActivity.this, R.drawable.bottom_border));
            viewPager.setPadding(0, 0, 0, 16);
        }
    }

    private void SetErrorLayout(boolean isShow, String errorMsg) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            txtFeedbackGroup.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            txtFeedbackGroup.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    private void RequestFeedbackQuestion() {
        progressDialog.show(FeedbackActivity.this.getSupportFragmentManager(), "");
        FeedbackQuestionJSONParser objFeedbackQuestionJSONParser = new FeedbackQuestionJSONParser();

        objFeedbackQuestionJSONParser.SelectAllFeedbackQuestionAnswer(FeedbackActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetFilterQuestionAnswerByGroup(String groupName) {
        alFilterFeedbackQuestionMaster = new ArrayList<>();
        FeedbackAnswerMaster objFeedbackAnswerMaster;
        FeedbackQuestionMaster objQuestionMaster = null;
        alFeedbackAnswerMaster = new ArrayList<>();
        int current = 0;
        for (FeedbackQuestionMaster objFeedbackQuestionMaster : alFeedbackQuestionMaster) {
            if (objFeedbackQuestionMaster.getFeedbackQuestionGroup().equals(groupName)) {
                if (objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue() || objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Rating.getValue()) {
                    alFilterFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
                } else {
                    if (strQuestionName == null) {
                        if (objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue() || objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Rating.getValue()) {
                            alFilterFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
                        } else {
                            strQuestionName = objFeedbackQuestionMaster.getFeedbackQuestion();
                            objQuestionMaster = objFeedbackQuestionMaster;
                            objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                            objFeedbackAnswerMaster.setFeedbackAnswerMasterId(objFeedbackQuestionMaster.getlinktoFeedbackAnswerMasterId());
                            objFeedbackAnswerMaster.setAnswer(objFeedbackQuestionMaster.getFeedbackAnswer());
                            alFeedbackAnswerMaster.add(objFeedbackAnswerMaster);
                        }
                    } else {
                        if (strQuestionName.equals(objFeedbackQuestionMaster.getFeedbackQuestion())) {
                            objQuestionMaster = objFeedbackQuestionMaster;
                            objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                            objFeedbackAnswerMaster.setFeedbackAnswerMasterId(objFeedbackQuestionMaster.getlinktoFeedbackAnswerMasterId());
                            objFeedbackAnswerMaster.setAnswer(objFeedbackQuestionMaster.getFeedbackAnswer());
                            alFeedbackAnswerMaster.add(objFeedbackAnswerMaster);
                            if (current == alFeedbackQuestionMaster.size() - 1) {
                                objQuestionMaster.setAlFeedbackAnswerMaster(alFeedbackAnswerMaster);
                                alFilterFeedbackQuestionMaster.add(objQuestionMaster);
                            }
                        } else {
                            objQuestionMaster.setAlFeedbackAnswerMaster(alFeedbackAnswerMaster);
                            alFilterFeedbackQuestionMaster.add(objQuestionMaster);
                            objQuestionMaster = objFeedbackQuestionMaster;
                            alFeedbackAnswerMaster = new ArrayList<>();
                            strQuestionName = objFeedbackQuestionMaster.getFeedbackQuestion();
                            if (objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Input.getValue() || objFeedbackQuestionMaster.getQuestionType() == Globals.QuestionType.Rating.getValue()) {
                                alFilterFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
                            } else {
                                objQuestionMaster = objFeedbackQuestionMaster;
                                objFeedbackAnswerMaster = new FeedbackAnswerMaster();
                                objFeedbackAnswerMaster.setFeedbackAnswerMasterId(objFeedbackQuestionMaster.getlinktoFeedbackAnswerMasterId());
                                objFeedbackAnswerMaster.setAnswer(objFeedbackQuestionMaster.getFeedbackAnswer());
                                alFeedbackAnswerMaster.add(objFeedbackAnswerMaster);
                                if (current == alFeedbackQuestionMaster.size() - 1) {
                                    objQuestionMaster.setAlFeedbackAnswerMaster(alFeedbackAnswerMaster);
                                    alFilterFeedbackQuestionMaster.add(objQuestionMaster);
                                }

                            }

                        }
                    }
                }
            }
            current++;
        }
    }

    private void SetViewPage() {
        feedbackPagerAdapter = new FeedbackPagerAdapter(getSupportFragmentManager());
        ArrayList<FeedbackQuestionMaster> arrayList = new ArrayList<>();
        if (alFeedbackQuestionMaster == null || alFeedbackQuestionMaster.size() == 0) {
            FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
            objFeedbackQuestionMaster.setQuestionType((short) Globals.QuestionType.Simple_Feedback.getValue());
            alFilterFeedbackQuestionMaster = new ArrayList<>();
            alFilterFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
            alFinalFeedbackAnswer.add(arrayList);
            feedbackPagerAdapter.AddFragment(FeedbackViewFragment.createInstance(alFilterFeedbackQuestionMaster, cnt), getResources().getString(R.string.fbFeedbackGroup));
            txtFeedbackGroup.setText(getResources().getString(R.string.fbFeedbackGroup));
            viewPager.setAdapter(feedbackPagerAdapter);
            viewPager.setOffscreenPageLimit(0);
            SetVisibility(-1);
        } else {
            alQuestionGroup = new ArrayList<>();
            cnt = 0;
            for (FeedbackQuestionMaster objFeedbackQuestionMaster : alFeedbackQuestionMaster) {
                if (strGroupName == null) {
                    strGroupName = objFeedbackQuestionMaster.getFeedbackQuestionGroup();
                } else {
                    if (strGroupName.equals(objFeedbackQuestionMaster.getFeedbackQuestionGroup())) {
                        strGroupName = objFeedbackQuestionMaster.getFeedbackQuestionGroup();
                        if (cnt == alFeedbackQuestionMaster.size() - 1) {
                            alQuestionGroup.add(strGroupName);
                        }
                    } else {
                        alQuestionGroup.add(strGroupName);
                        strGroupName = objFeedbackQuestionMaster.getFeedbackQuestionGroup();
                        if (cnt == alFeedbackQuestionMaster.size() - 1) {
                            alQuestionGroup.add(strGroupName);
                        }
                    }
                }
                cnt++;
            }
            alQuestionGroup.add(Globals.QuestionType.Simple_Feedback.toString());
            cnt = 0;
            for (String strGroup : alQuestionGroup) {
                if (!strGroup.equals(Globals.QuestionType.Simple_Feedback.toString())) {
                    SetFilterQuestionAnswerByGroup(strGroup);
                } else {
                    FeedbackQuestionMaster objFeedbackQuestionMaster = new FeedbackQuestionMaster();
                    objFeedbackQuestionMaster.setQuestionType((short) Globals.QuestionType.Simple_Feedback.getValue());
                    alFilterFeedbackQuestionMaster = new ArrayList<>();
                    alFilterFeedbackQuestionMaster.add(objFeedbackQuestionMaster);
                }
                alFinalFeedbackAnswer.add(arrayList);
                feedbackPagerAdapter.AddFragment(FeedbackViewFragment.createInstance(alFilterFeedbackQuestionMaster, cnt), strGroup);

                cnt++;
            }

            viewPager.setAdapter(feedbackPagerAdapter);
            viewPager.setOffscreenPageLimit(alQuestionGroup.size());
            txtFeedbackGroup.setText(feedbackPagerAdapter.GetFeedbackQuestionGroup(0));
            SetVisibility(0);
        }

        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.HideKeyBoard(FeedbackActivity.this, v);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Globals.HideKeyBoard(FeedbackActivity.this,viewPager);
                if (feedbackPagerAdapter.GetFeedbackQuestionGroup(position).equals(Globals.QuestionType.Simple_Feedback.toString())) {
                    txtFeedbackGroup.setText(getResources().getString(R.string.fbFeedbackGroup));
                } else {
                    txtFeedbackGroup.setText(feedbackPagerAdapter.GetFeedbackQuestionGroup(position));
                }
                SetVisibility(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //endregion

    //region PagerAdapter
    static class FeedbackPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> feedbackFragmentList = new ArrayList<>();
        private final List<String> feedbackFragmentTitle = new ArrayList<>();

        public FeedbackPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, String strGroup) {
            feedbackFragmentList.add(fragment);
            feedbackFragmentTitle.add(strGroup);
        }

        public Fragment GetCurrentFragment(int position) {
            return feedbackFragmentList.get(position);
        }


        public String GetFeedbackQuestionGroup(int position) {
            return feedbackFragmentTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return feedbackFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return feedbackFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return feedbackFragmentTitle.get(position);
        }

    }
    //endregion
}
