package com.arraybit.abposw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
public class ItemListFragment extends Fragment implements ItemJSONParser.ItemMasterRequestListener, ItemAdapter.ItemClickListener {

    public final static String ITEMS_COUNT_KEY = "ItemTabFragment";
    static int cnt = 0;
    ProgressDialog progressDialog = new ProgressDialog();
    LinearLayout errorLayout;
    CategoryMaster objCategoryMaster;
    RecyclerView rvItemMaster;
    ItemAdapter itemAdapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
    String OptionIds;
    int currentPage = 1;
    boolean isLayoutChange;
    Context context;

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

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        rvItemMaster = (RecyclerView) view.findViewById(R.id.rvItemMaster);
        rvItemMaster.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        Bundle bundle = getArguments();
        objCategoryMaster = bundle.getParcelable(ITEMS_COUNT_KEY);

        if (currentPage >= 1 && linearLayoutManager.canScrollVertically()) {
            currentPage = 1;
            RequestItemMaster();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
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
                        cnt = 0;
                        RequestItemMaster();
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });

        rvItemMaster.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!itemAdapter.isItemAnimate) {
                    itemAdapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        cnt = 0;
                        RequestItemMaster();
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster) {
        if (progressDialog != null && progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        this.alItemMaster = alItemMaster;
        SetRecyclerView(false);
    }

    @Override
    public void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("ItemMaster", objItemMaster);
        getActivity().startActivityForResult(i, 0);
    }

    @Override
    public void AddItemOnClick(ItemMaster objItemMaster) {
        if (objItemMaster.getLinktoItemMasterIdModifiers().equals("") && objItemMaster.getLinktoOptionMasterIds().equals("")) {
            AddQtyRemarkDialogFragment objAddQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(objItemMaster);
            objAddQtyRemarkDialogFragment.show(getActivity().getSupportFragmentManager(), "");
        } else {
            Intent i = new Intent(getActivity(), ItemModifierRemarkActivity.class);
            i.putExtra("ItemMaster", objItemMaster);
            getActivity().startActivityForResult(i, 0);
        }
    }

    public void SetRecyclerView(boolean isCurrentPageChange) {
//        try {
            if (isCurrentPageChange) {
                itemAdapter.isItemAnimate = false;
                isLayoutChange = true;
                rvItemMaster.setAdapter(rvItemMaster.getAdapter());
                if (MenuActivity.isViewChange) {
                    rvItemMaster.setLayoutManager(gridLayoutManager);
                } else {
                    rvItemMaster.setLayoutManager(linearLayoutManager);
                }
            } else {
                if (alItemMaster == null) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgSelectFail), rvItemMaster);

                    }
                } else if (alItemMaster.size() == 0) {
                    if (currentPage == 1) {
                        Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgNoRecord), rvItemMaster);

                    }
                } else {
                    Globals.SetErrorLayout(errorLayout, false, null, rvItemMaster);
                    if (currentPage > 1) {
                        itemAdapter.ItemDataChanged(alItemMaster);
                        return;
                    } else if (alItemMaster.size() < 10) {
                        currentPage += 1;
                    }
                    itemAdapter = new ItemAdapter(context, alItemMaster, this);
                    rvItemMaster.setAdapter(itemAdapter);
                    if (MenuActivity.isViewChange) {
                        rvItemMaster.setLayoutManager(gridLayoutManager);
                    } else {
                        rvItemMaster.setLayoutManager(linearLayoutManager);
                    }
                }
            }
//        }
//        catch (Exception e) {
//            System.out.println("Exception"+e.toString());
//            throw e;
//        }
    }

    public void ItemByOptionName(String OptionIds) {
        this.OptionIds = OptionIds;
        currentPage = 1;

        RequestItemMaster();

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
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });

        rvItemMaster.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
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
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    //region Private Methods
    private void RequestItemMaster() {
        if (cnt == 0) {
            if (progressDialog.getDialog() != null && progressDialog.getDialog().isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");
            cnt = 1;
        }
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
        if (objCategoryMaster.getCategoryMasterId() == 0) {
            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), null, OptionIds, String.valueOf(Globals.linktoBusinessMasterId));
        } else {
            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), String.valueOf(objCategoryMaster.getCategoryMasterId()), OptionIds, String.valueOf(Globals.linktoBusinessMasterId));
        }
    }
    //endregion
}
