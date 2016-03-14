package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.global.Globals;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ItemListFragment extends Fragment implements ItemJSONParser.ItemMasterRequestListener{

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    LinearLayout errorLayout;
    CategoryMaster objCategoryMaster;
    RecyclerView rvItemMaster;
    ProgressDialog progressDialog;
    ItemAdapter itemAdapter;
    LinearLayoutManager linearLayoutManager;
    int currentPage = 1;

    public static ItemListFragment createInstance(CategoryMaster objCategoryMaster) {
        ItemListFragment itemTabFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objCategoryMaster);
        itemTabFragment.setArguments(bundle);
        return itemTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_itemlist, container, false);

        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        rvItemMaster = (RecyclerView) view.findViewById(R.id.rvItemMaster);
        rvItemMaster.setNestedScrollingEnabled(false);
        rvItemMaster.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());

        Bundle bundle = getArguments();
        objCategoryMaster = bundle.getParcelable(ITEMS_COUNT_KEY);

        if(linearLayoutManager.canScrollVertically()){
            RequestItemMaster();
        }
        return view;
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alItemMaster);
    }

    //region Private Methods
    private void RequestItemMaster() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), String.valueOf(objCategoryMaster.getCategoryMasterId()));
    }

    private void SetRecyclerView(ArrayList<ItemMaster> lstItemMaster){
        if (lstItemMaster == null) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgSelectFail), rvItemMaster);
            }
        } else if (lstItemMaster.size() == 0) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, getResources().getString(R.string.MsgNoRecord), rvItemMaster);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvItemMaster);
            if (currentPage > 1) {
                //itemAdapter.OffersDataChanged(lstItemMaster);
                return;
            } else if (lstItemMaster.size() < 10) {
                currentPage += 1;
            }
            itemAdapter = new ItemAdapter(ItemListFragment.this.getActivity(), lstItemMaster);
            rvItemMaster.setAdapter(itemAdapter);
            rvItemMaster.setLayoutManager(linearLayoutManager);
        }
    }
    //endregion
}
