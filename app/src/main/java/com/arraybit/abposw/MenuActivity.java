package com.arraybit.abposw;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MenuActivity extends AppCompatActivity implements CategoryJSONParser.CategoryRequestListener {

    public static short i = 0;
    public static boolean isViewChange = false;
    boolean isForceToChange = false;
    CoordinatorLayout menuActivity;
    TabLayout tabLayout;
    ViewPager viewPager;
    ProgressDialog progressDialog;
    PageAdapter itemPagerAdapter;
    ArrayList<CategoryMaster> alCategoryMaster;
    LinearLayout errorLayout;
    FloatingActionMenu famRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        menuActivity = (CoordinatorLayout) findViewById(R.id.menuActivity);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        famRoot = (FloatingActionMenu) findViewById(R.id.famRoot);

        if (Service.CheckNet(this)) {
            RequestCategoryMaster();
        } else {
            SetErrorLayout(true, getResources().getString(R.string.MsgCheckConnection));
        }
    }

    @Override
    public void CategoryResponse(ArrayList<CategoryMaster> alCategoryMaster) {
        progressDialog.dismiss();
        this.alCategoryMaster = alCategoryMaster;
        SetTabLayout();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.viewChange){
            if(!errorLayout.isShown()){
                ItemListFragment itemListFragment = (ItemListFragment) itemPagerAdapter.GetCurrentFragment(tabLayout.getSelectedTabPosition());
                i = (short) (i + 1);
                if(i == 1) {
                    item.setIcon(R.drawable.view_grid);
                    isViewChange = true;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView();
                }
                else if (i == 2){
                    item.setIcon(R.drawable.view_grid_two);
                    isViewChange = true;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView();
                }
                else {
                    i = 0;
                    item.setIcon(R.drawable.view_list);
                    isViewChange = false;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.viewChange).setVisible(true);
        menu.findItem(R.id.cart_layout).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    //region Private Methods
    private void RequestCategoryMaster() {
        progressDialog = new ProgressDialog();
        progressDialog.show(MenuActivity.this.getSupportFragmentManager(), "");
        CategoryJSONParser objCategoryJSONParser = new CategoryJSONParser();

        objCategoryJSONParser.SelectAllCategoryMaster(MenuActivity.this, String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetErrorLayout(boolean isShow, String errorMsg) {
        TextView txtMsg = (TextView) errorLayout.findViewById(R.id.txtMsg);
        if (isShow) {
            errorLayout.setVisibility(View.VISIBLE);
            txtMsg.setText(errorMsg);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            famRoot.setVisibility(View.GONE);

        } else {
            errorLayout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            famRoot.setVisibility(View.VISIBLE);
        }
    }


    private void SetTabLayout() {
        if (alCategoryMaster == null) {
            SetErrorLayout(true, getResources().getString(R.string.MsgSelectFail));
        } else {

            SetErrorLayout(false, null);

            CategoryMaster objCategoryMaster = new CategoryMaster();
            objCategoryMaster.setCategoryMasterId((short) 0);
            objCategoryMaster.setCategoryName("All");
            ArrayList<CategoryMaster> alCategory = new ArrayList<>();
            alCategory.add(objCategoryMaster);
            alCategoryMaster.addAll(0, alCategory);

            itemPagerAdapter = new PageAdapter(getSupportFragmentManager());

            for (int i = 0; i < alCategoryMaster.size(); i++) {
                itemPagerAdapter.AddFragment(ItemListFragment.createInstance(alCategoryMaster.get(i)), alCategoryMaster.get(i));
            }

            viewPager.setAdapter(itemPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    //set the current view on tab click
                    ItemListFragment itemListFragment = (ItemListFragment) itemPagerAdapter.GetCurrentFragment(tabLayout.getSelectedTabPosition());

                    if (famRoot.isMenuButtonHidden()) {
                        famRoot.showMenuButton(true);
                    }

                    if(isForceToChange) {
                        itemListFragment.SetRecyclerView();
                        isForceToChange = false;
                    }
                    else {
                        isForceToChange = false;
                    }
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }
    //endregion

    //region Page Adapter
    static class PageAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<CategoryMaster> fragmentTitleList = new ArrayList<>();

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void AddFragment(Fragment fragment, CategoryMaster title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public Fragment GetCurrentFragment(int position) {
            return fragmentList.get(position);
        }

        public CategoryMaster GetCategoryMaster(int position) {
            return fragmentTitleList.get(position);
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
            return fragmentTitleList.get(position).getCategoryName();
        }
    }
    //endregion
}
