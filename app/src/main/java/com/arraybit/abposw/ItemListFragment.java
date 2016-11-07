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
import com.arraybit.global.SharePreferenceManage;
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
    LinearLayoutManager linearLayoutManager, filterLinearLayoutManager;
    GridLayoutManager gridLayoutManager, filterGridLayoutManager;
    ArrayList<ItemMaster> alItemMaster;
    String OptionIds;
    int currentPage = 1, currentTab;
    boolean isDuplicate = false;
    Context context;

    public static ItemListFragment createInstance(CategoryMaster objCategoryMaster, int currentTab) {
        ItemListFragment itemTabFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEMS_COUNT_KEY, objCategoryMaster);
        bundle.putInt("TabPosition", currentTab);
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
        currentTab = bundle.getInt("TabPosition", -1);


        if (currentPage >= 1 && linearLayoutManager.canScrollVertically()) {
            currentPage = 1;
            RequestItemMaster(false);
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
                        RequestItemMaster(false);
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
                        RequestItemMaster(false);
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    @Override
    public void ItemMasterResponse(ArrayList<ItemMaster> alItemMaster, boolean isFilter) {
        if (progressDialog != null && progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        this.alItemMaster = alItemMaster;
        if (isFilter) {
            currentPage = 1;
        }
        SetRecyclerView(false, false, isFilter);
    }

    @Override
    public void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName,int position) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("Position",position);
        i.putExtra("ItemMaster", objItemMaster);
        getActivity().startActivityForResult(i, 0);
    }

    @Override
    public void AddItemOnClick(ItemMaster objItemMaster) {
        if (objItemMaster.getLinktoItemMasterIdModifiers().equals("") && objItemMaster.getLinktoOptionMasterIds().equals("")) {
            AddQtyRemarkDialogFragment objAddQtyRemarkDialogFragment = new AddQtyRemarkDialogFragment(objItemMaster);
            objAddQtyRemarkDialogFragment.show(getActivity().getSupportFragmentManager(),"Dialog");
        } else {
            Intent i = new Intent(getActivity(), ItemModifierRemarkActivity.class);
            i.putExtra("ItemMaster", objItemMaster);
            getActivity().startActivityForResult(i, 0);
        }
    }

    @Override
    public void LikeOnClick(int position) {
//        SaveWishListInSharePreference(true);
    }

    public void SetRecyclerView(boolean isCurrentPageChange, boolean isWishListCheck, boolean isFilterLayoutManager) {
        if (isCurrentPageChange) {
            //check duplicate id in wishList
            if (rvItemMaster.getAdapter() != null) {
                itemAdapter.isItemAnimate = false;
                if (isWishListCheck) {
                    if (ItemAdapter.alWishItemMaster.size() > 0) {
                        for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
                            if (rvItemMaster.getAdapter().getItemCount() > 0) {
                                for (int i = 0; i < rvItemMaster.getAdapter().getItemCount(); i++) {
                                    if (String.valueOf(objWishItemMaster.getItemMasterId()).equals(String.valueOf(rvItemMaster.getAdapter().getItemId(i)))) {
                                        isDuplicate = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (isDuplicate) {
                            rvItemMaster.getAdapter().notifyDataSetChanged();
                        } else {
                            rvItemMaster.setAdapter(rvItemMaster.getAdapter());
                            if (MenuActivity.isViewChange) {
                                rvItemMaster.setLayoutManager(gridLayoutManager);
                            } else {
                                rvItemMaster.setLayoutManager(linearLayoutManager);
                            }
                        }
                    } else {
                        rvItemMaster.setAdapter(rvItemMaster.getAdapter());
                        if (MenuActivity.isViewChange) {
                            rvItemMaster.setLayoutManager(gridLayoutManager);
                        } else {
                            rvItemMaster.setLayoutManager(linearLayoutManager);
                        }
                    }
                } else {
                    rvItemMaster.setAdapter(rvItemMaster.getAdapter());
                    if (MenuActivity.isViewChange) {
                        rvItemMaster.setLayoutManager(gridLayoutManager);
                    } else {
                        rvItemMaster.setLayoutManager(linearLayoutManager);
                    }
                }
            }
        } else {
            if (alItemMaster == null) {
                if (currentPage == 1) {
                    Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgSelectFail), rvItemMaster, 0);

                }
            } else if (alItemMaster.size() == 0) {
                if (currentPage == 1) {
                    Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgItem), rvItemMaster, 0);
                }
            } else {
                Globals.SetErrorLayout(errorLayout, false, null, rvItemMaster, 0);
                if (currentPage > 1) {
                    if (rvItemMaster.getAdapter() != null && rvItemMaster.getAdapter().getItemCount() > 9) {
                        itemAdapter.ItemDataChanged(alItemMaster);
                    }
                    return;
                } else if (alItemMaster.size() < 10) {
                    currentPage += 1;
                }
                itemAdapter = new ItemAdapter(getActivity(), alItemMaster, this, false);
                rvItemMaster.setAdapter(itemAdapter);
                if (MenuActivity.isViewChange) {
                    if (isFilterLayoutManager) {
                        rvItemMaster.setLayoutManager(filterGridLayoutManager);
                    } else {
                        rvItemMaster.setLayoutManager(gridLayoutManager);
                    }
                } else {
                    if (isFilterLayoutManager) {
                        rvItemMaster.setLayoutManager(filterLinearLayoutManager);
                    } else {
                        rvItemMaster.setLayoutManager(linearLayoutManager);
                    }

                }
            }
        }
    }

    public void UpdateWishList(int position,short isCheck){
        if(position!=-1) {
            itemAdapter.UpdateWishList(position,isCheck);
        }
    }

    public void ItemByOptionName(String OptionIds) {
        this.OptionIds = OptionIds;
        currentPage = 1;
        cnt = 0;

        filterLinearLayoutManager = new LinearLayoutManager(getActivity());
        filterLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        filterGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        filterGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        RequestItemMaster(true);

        rvItemMaster.addOnScrollListener(new EndlessRecyclerOnScrollListener(filterLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!itemAdapter.isItemAnimate) {
                    itemAdapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestItemMaster(false);
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });

        rvItemMaster.addOnScrollListener(new EndlessRecyclerOnScrollListener(filterGridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!itemAdapter.isItemAnimate) {
                    itemAdapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestItemMaster(false);
                    } else {
                        Globals.ShowSnackBar(rvItemMaster, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
    }

    public void CheckItemMasterIdInCurrentList(short isChecked,int itemMasterId,short oldCheckValue){
        itemAdapter.CheckIdInCurrentListAndUpdate(isChecked,itemMasterId,oldCheckValue);
    }

    //region Private Methods
    private void RequestItemMaster(boolean isOptionFilter) {
        if (cnt == 0) {
            if (progressDialog.getDialog() != null && progressDialog.getDialog().isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.show(getActivity().getSupportFragmentManager(), "ProgressDialog");
            cnt = 1;
        }
        ItemJSONParser objItemJSONParser = new ItemJSONParser();
//        if (objCategoryMaster.getCategoryMasterId() == 0) {
//            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), null, OptionIds, String.valueOf(Globals.linktoBusinessMasterId), null, isOptionFilter);
//        } else {
            objItemJSONParser.SelectAllItemMaster(this, getActivity(), String.valueOf(currentPage), String.valueOf(objCategoryMaster.getCategoryMasterId()), OptionIds, String.valueOf(Globals.linktoBusinessMasterId), null, isOptionFilter);
//        }
    }

    private void SaveWishListInSharePreference(boolean isBackPressed) {
        SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
        ArrayList<String> alString;
        if (isBackPressed) {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity()) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity());
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
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
                    }
                } else {
                    if (ItemAdapter.alWishItemMaster.size() > 0) {
                        alString = new ArrayList<>();
                        for (ItemMaster objWishItemMaster : ItemAdapter.alWishItemMaster) {
                            if (objWishItemMaster.getIsChecked() != -1) {
                                alString.add(String.valueOf(objWishItemMaster.getItemMasterId()));
                            }
                        }
                        objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
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
                    objSharePreferenceManage.CreateStringListPreference("WishListPreference", "WishList", alString, getActivity());
                }
            }
        } else {
            if (objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity()) != null) {
                alString = objSharePreferenceManage.GetStringListPreference("WishListPreference", "WishList", getActivity());
                ItemAdapter.alWishItemMaster = new ArrayList<>();
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

    //endregion
}
