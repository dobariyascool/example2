package com.arraybit.abposw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.parser.CategoryJSONParser;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MenuActivity extends AppCompatActivity implements CategoryJSONParser.CategoryRequestListener, View.OnClickListener{

    public static short i = 0;
    public static boolean isViewChange = false;
    ProgressDialog progressDialog = new ProgressDialog();
    boolean isForceToChange = false;
    CoordinatorLayout menuActivity;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter itemPagerAdapter;
    ArrayList<CategoryMaster> alCategoryMaster;
    LinearLayout errorLayout;
    FloatingActionMenu famRoot;
    FloatingActionButton fabVeg, fabNonVeg, fabJain;
    short isVegCheck = 0, isNonVegCheck = 0, isJainCheck = 0;
    StringBuilder sbItemTypeMasterId;
    RelativeLayout relativeLayout;
    com.rey.material.widget.TextView txtCartNumber;
    Toolbar app_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (app_bar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        menuActivity = (CoordinatorLayout) findViewById(R.id.menuActivity);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        famRoot = (FloatingActionMenu) findViewById(R.id.famRoot);
        famRoot.setClosedOnTouchOutside(true);
        fabVeg = (FloatingActionButton) findViewById(R.id.fabVeg);
        fabNonVeg = (FloatingActionButton) findViewById(R.id.fabNonVeg);
        fabJain = (FloatingActionButton) findViewById(R.id.fabJain);

        fabVeg.setOnClickListener(this);
        fabNonVeg.setOnClickListener(this);
        fabJain.setOnClickListener(this);

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
        if (id == R.id.viewChange) {
            if (!errorLayout.isShown()) {
                ItemListFragment itemListFragment = (ItemListFragment) itemPagerAdapter.GetCurrentFragment(tabLayout.getSelectedTabPosition());
                i = (short) (i + 1);
                if (i == 1) {
                    item.setIcon(R.drawable.view_grid);
                    isViewChange = true;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView(true);
                } else if (i == 2) {
                    item.setIcon(R.drawable.view_grid_two);
                    isViewChange = true;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView(true);
                } else {
                    i = 0;
                    item.setIcon(R.drawable.view_list);
                    isViewChange = false;
                    isForceToChange = true;
                    itemListFragment.SetRecyclerView(true);
                }
            }
        } else if (id == R.id.logout) {
            SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
            objSharePreferenceManage.RemovePreference("LoginPreference", "RegisteredUserMasterId", this);
            objSharePreferenceManage.RemovePreference("LoginPreference", "UserName", this);
            objSharePreferenceManage.RemovePreference("LoginPreference", "UserPassword", this);
            objSharePreferenceManage.ClearPreference("LoginPreference", this);
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else if(id==android.R.id.home){
            finish();
            Globals.alOrderItemTran = new ArrayList<>();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        MenuItem menuItem = menu.findItem(R.id.cart_layout);

        relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        final ImageView ivCart = (ImageView) relativeLayout.findViewById(R.id.ivCart);
        txtCartNumber = (com.rey.material.widget.TextView) relativeLayout.findViewById(R.id.txtCartNumber);

        SetCartNumber();

        ivCart.setOnClickListener(this);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.viewChange).setVisible(true);
        menu.findItem(R.id.cart_layout).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        ItemListFragment itemListFragment = (ItemListFragment) itemPagerAdapter.GetCurrentFragment(tabLayout.getSelectedTabPosition());
        if (v.getId() == R.id.fabVeg) {
            if (isVegCheck == 0) {
                fabVeg.setSelected(true);
                fabVeg.setColorNormal(ContextCompat.getColor(this, R.color.accent_secondary));
                isVegCheck += 1;
            } else {
                fabVeg.setSelected(false);
                fabVeg.setColorNormal(ContextCompat.getColor(this, android.R.color.white));
                isVegCheck = 0;
            }
            CheckSelected();

            if (sbItemTypeMasterId.toString().equals("")) {
                itemListFragment.ItemByOptionName(null);
                famRoot.close(true);
            } else {
                itemListFragment.ItemByOptionName(sbItemTypeMasterId.toString());
                famRoot.close(true);
            }
        } else if (v.getId() == R.id.fabNonVeg) {
            if (isNonVegCheck == 0) {
                fabNonVeg.setSelected(true);
                fabNonVeg.setColorNormal(ContextCompat.getColor(this, R.color.accent_secondary));
                isNonVegCheck += 1;
            } else {
                fabNonVeg.setSelected(false);
                fabNonVeg.setColorNormal(ContextCompat.getColor(this, android.R.color.white));
                isNonVegCheck = 0;
            }
            CheckSelected();

            if (sbItemTypeMasterId.toString().equals("")) {
                itemListFragment.ItemByOptionName(null);
                famRoot.close(true);
            } else {
                itemListFragment.ItemByOptionName(sbItemTypeMasterId.toString());
                famRoot.close(true);
            }
        } else if (v.getId() == R.id.fabJain) {
            if (isJainCheck == 0) {
                fabJain.setSelected(true);
                fabJain.setColorNormal(ContextCompat.getColor(this, R.color.accent_secondary));
                isJainCheck += 1;
            } else {
                fabJain.setSelected(false);
                fabJain.setColorNormal(ContextCompat.getColor(this, android.R.color.white));
                isJainCheck = 0;
            }
            CheckSelected();

            if (sbItemTypeMasterId.toString().equals("")) {
                itemListFragment.ItemByOptionName(null);
                famRoot.close(true);
            } else {
                itemListFragment.ItemByOptionName(sbItemTypeMasterId.toString());
                famRoot.close(true);
            }
        } else if (v.getId() == R.id.ivCart) {
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(android.R.id.content, new CartItemFragment());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
            Intent intent = new Intent(this, CartItemActivity.class);
            this.startActivityForResult(intent, 0);
        }
    }

    public void SetCartItemResponse(){
        SetCartNumber();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                SetCartNumber();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //region Private Methods
    private void RequestCategoryMaster() {
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
                    viewPager.setCurrentItem(tab.getPosition());
                    ItemListFragment itemListFragment = (ItemListFragment) itemPagerAdapter.GetCurrentFragment(tabLayout.getSelectedTabPosition());

                    if (famRoot.isMenuButtonHidden()) {
                        famRoot.showMenuButton(true);
                    }

                    if (isForceToChange) {
                        if (fabVeg.isSelected() || fabNonVeg.isSelected() || fabJain.isSelected()) {
                            itemListFragment.ItemByOptionName(sbItemTypeMasterId.toString());
                            isForceToChange = false;
                        } else {
                            if (sbItemTypeMasterId == null) {
                                itemListFragment.SetRecyclerView(true);
                                isForceToChange = false;
                            } else {
                                itemListFragment.ItemByOptionName(null);
                            }
                        }
                    } else {
                        if (fabVeg.isSelected() || fabNonVeg.isSelected() || fabJain.isSelected()) {
                            itemListFragment.ItemByOptionName(sbItemTypeMasterId.toString());
                        } else {
                            if (sbItemTypeMasterId != null) {
                                itemListFragment.ItemByOptionName(null);
                            }
                        }
                    }
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

    private void CheckSelected() {
        sbItemTypeMasterId = new StringBuilder();
        if (fabVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.Veg.getValue()).append(",");
        }
        if (fabNonVeg.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.NonVeg.getValue()).append(",");
        }
        if (fabJain.isSelected()) {
            sbItemTypeMasterId.append(Globals.OptionValue.Jain.getValue()).append(",");
        }
    }

    private void SetCartNumber() {
        if (Globals.counter > 0) {
            txtCartNumber.setText(String.valueOf(Globals.counter));
            txtCartNumber.setSoundEffectsEnabled(true);
            txtCartNumber.setBackground(ContextCompat.getDrawable(MenuActivity.this, R.drawable.cart_number));
//            txtCartNumber.setAnimation(AnimationUtils.loadAnimation(MenuActivity.this, R.anim.fab_scale_up));
        } else {
            txtCartNumber.setBackgroundColor(ContextCompat.getColor(MenuActivity.this, android.R.color.transparent));
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
