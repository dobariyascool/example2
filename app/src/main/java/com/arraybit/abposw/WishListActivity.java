package com.arraybit.abposw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;

import java.util.ArrayList;

public class WishListActivity extends AppCompatActivity implements ItemJSONParser.ItemMasterRequestListener, ItemAdapter.ItemClickListener {

    LinearLayout errorLayout;
    RecyclerView rvWishItemMaster;
    ArrayList<String> alString;
    ArrayList<ItemMaster> alItemMaster;
    boolean isShowMsg = true;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        if (getSupportActionBar() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        rvWishItemMaster = (RecyclerView) findViewById(R.id.rvWishItemMaster);
        rvWishItemMaster.setVisibility(View.GONE);

        if (Service.CheckNet(this)) {
            RequestItemMaster();
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgCheckConnection), rvWishItemMaster);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            SaveWishListInSharePreference(true);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SaveWishListInSharePreference(true);
        super.onBackPressed();
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        this.alItemMaster = alItemMaster;
        SetRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    isShowMsg = data.getBooleanExtra("ShowMessage", false);
                }
                //SetCartNumber();
                isShowMsg = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        Intent i = new Intent(WishListActivity.this, DetailActivity.class);
        i.putExtra("ItemMaster", objItemMaster);
        startActivityForResult(i, 0);
    }

    @Override
    public void AddItemOnClick(ItemMaster objItemMaster) {
        if (objItemMaster.getLinktoItemMasterIdModifiers().equals("") && objItemMaster.getLinktoOptionMasterIds().equals("")) {
            AddQtyRemarkDialogFragment objAddQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(objItemMaster);
            objAddQtyRemarkDialogFragment.show(getSupportFragmentManager(), "");
        } else {
            Intent i = new Intent(WishListActivity.this, ItemModifierRemarkActivity.class);
            i.putExtra("ItemMaster", objItemMaster);
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void LikeOnClick(int position) {
        itemAdapter.RemoveData(position);
        if (alItemMaster.size() == 0) {
            SetRecyclerView();
        }
    }

    //region Private Methods
    private void RequestItemMaster() {
        ItemJSONParser objItemJSONParser = new ItemJSONParser();

        SaveWishListInSharePreference(false);
        StringBuilder sbItemMasterIds = new StringBuilder();
        if (alString != null && alString.size() != 0) {
            for (String str : alString) {
                sbItemMasterIds.append(str);
                sbItemMasterIds.append(",");
            }
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster);
        }
        if (!sbItemMasterIds.toString().equals("")) {
            objItemJSONParser.SelectAllItemMaster(null, this, String.valueOf(1), null, null, String.valueOf(Globals.linktoBusinessMasterId), sbItemMasterIds.toString());
        } else {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster);
        }
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        if (isBackPressed) {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this);
                if (alString.size() > 0) {
                    if (ItemAdapter.alWishItemMaster.size() > 0) {
                        for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                if (!CheckDuplicateId(alString, String.valueOf(objWishItemMaster.getItemMasterId()), (short) 1)) {
                                    alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                                }
                            } else {
                                CheckDuplicateId(alString, String.valueOf(objWishItemMaster.getItemMasterId()), (short) -1);
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
                    }
                } else {
                    if (ItemAdapter.alWishItemMaster.size() > 0) {
                        alString = new ArrayList<>();
                        for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
                    }
                }
            } else {
                if (ItemAdapter.alWishItemMaster.size() > 0) {
                    alString = new ArrayList<>();
                    for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
                        if (objWishItemMaster.getIsChecked() != -1) {
                            alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                        }
                    }
                    objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, WishListActivity.this);
                }
            }
        } else {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this) != null) {
                ItemAdapter.alWishItemMaster = new ArrayList<>();
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", WishListActivity.this);
                if (alString.size() > 0) {
                    for (String itemMasterId : alString) {
                        ItemMaster objItemMaster = new ItemMaster();
                        objItemMaster.setItemMasterId(Integer.parseInt(itemMasterId));
                        objItemMaster.setIsChecked((short) 1);
                        ItemAdapter.alWishItemMaster.add(objItemMaster);
                    }
                }
            } else {
                ItemAdapter.alWishItemMaster = new ArrayList<>();
            }
        }
    }

    private boolean CheckDuplicateId(ArrayList<String> arrayList, String id, short isCheck) {
        boolean isDuplicate = false;
        int cnt = 0;
        for (String strId : arrayList) {
            if (strId.equals(id)) {
                isDuplicate = true;
                if (isCheck == -1) {
                    arrayList.remove(cnt);
                    break;
                }
            }
            cnt++;
        }
        return isDuplicate;
    }

    public void SetRecyclerView() {
        if (alItemMaster == null) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvWishItemMaster);
        } else if (alItemMaster.size() == 0) {
            Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvWishItemMaster);
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvWishItemMaster);
            itemAdapter = new ItemAdapter(WishListActivity.this, alItemMaster, this, true);
            rvWishItemMaster.setAdapter(itemAdapter);
            rvWishItemMaster.setLayoutManager(new LinearLayoutManager(this));
        }
    }
    //endregion
}
