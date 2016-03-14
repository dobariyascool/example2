package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.modal.CategoryMaster;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ItemListFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    ArrayList<CategoryMaster> alCategoryMaster;
    RecyclerView rvItemMaster;
    ProgressDialog progressDialog;
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

        rvItemMaster = (RecyclerView) view.findViewById(R.id.rvItemMaster);
        rvItemMaster.setNestedScrollingEnabled(false);
        rvItemMaster.setVisibility(View.GONE);

        RequestItemMaster();
        return view;
    }

    //region Private Methods
    private void RequestItemMaster() {
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        //ItemJSONParser objItemJSONParser = new ItemJSONParser();
        //objItemJSONParser.SelectAllItemMasterPageWise(this, getActivity(), String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId));
    }
    //endregion
}
