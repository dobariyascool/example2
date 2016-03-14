package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.ItemAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.CategoryMaster;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ItemListFragment extends Fragment implements ItemJSONParser.ItemMasterRequestListener{

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    static boolean isFilter = false;
    LinearLayout errorLayout;
    CategoryMaster objCategoryMaster;
    RecyclerView rvItemMaster;
    ProgressDialog progressDialog;
    ItemAdapter itemAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
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
        rvItemMaster.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        Bundle bundle = getArguments();
        objCategoryMaster = bundle.getParcelable(ITEMS_COUNT_KEY);

        if(currentPage >= 1 && linearLayoutManager.canScrollVertically()){
            currentPage = 1;
            RequestItemMaster();
       }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvItemMaster.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!itemAdapter.isItemAnimate) {
                    itemAdapter.isItemAnimate = true;
                }
            }
        });

        rvItemMaster.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!itemAdapter.isItemAnimate) {
                    itemAdapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestItemMaster();
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
            progressDialog.dismiss();
            this.alItemMaster = alItemMaster;
            SetRecyclerView(alItemMaster);
            isFilter = false;
    }

    //region Private Methods
    private void RequestItemMaster() {
        isFilter = true;
        progressDialog = new ProgressDialog();
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        if(objCategoryMaster.getCategoryMasterId()==0){
            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage),null,null);
        }else{
            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), String.valueOf(objCategoryMaster.getCategoryMasterId()),null);
        }
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
                itemAdapter.ItemDataChanged(lstItemMaster);
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
