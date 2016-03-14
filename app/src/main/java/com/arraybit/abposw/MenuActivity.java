package com.arraybit.abposw;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements CategoryJSONParser.CategoryRequestListener {

    FrameLayout menuActivity;
    TabLayout tabLayout;
    ViewPager viewPager;
    ProgressDialog progressDialog;
    PageAdapter pageAdapter;
    ArrayList<CategoryMaster> alCategoryMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuActivity = (FrameLayout) findViewById(R.id.menuActivity);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (Service.CheckNet(this)) {
            RequestItemMaster();
        } else {
            Globals.ShowSnackBar(menuActivity, getResources().getString(R.string.MsgCheckConnection), this, 1000);
        }
    }

    @Override
    public void CategoryResponse(ArrayList<CategoryMaster> alCategoryMaster) {
        progressDialog.dismiss();
        this.alCategoryMaster = alCategoryMaster;
        SetTabLayout();
    }

    //region Private Methods
    private void RequestItemMaster() {
        progressDialog = new ProgressDialog();
        progressDialog.show(MenuActivity.this.getSupportFragmentManager(), "");
        CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();
        objCategoryJSONParser.SelectAllCategoryMaster(MenuActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetTabLayout() {
        if (alCategoryMaster == null) {
            Globals.ShowSnackBar(menuActivity, getResources().getString(R.string.MsgSelectFail), MenuActivity.this, 1000);
        } else {
            pageAdapter.addFragment(new ItemListFragment(alCategoryMaster), "Information");

            viewPager.setAdapter(pageAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
    //endregion

    //region Page Adapter
    static class PageAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
    //endregion
}
