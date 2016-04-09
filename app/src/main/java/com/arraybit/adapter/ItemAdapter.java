package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.arraybit.abposw.MenuActivity;
import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public static ArrayList<String> alString = new ArrayList<>();
    public static ArrayList<ItemMaster> alWishItemMaster = new ArrayList<>();
    public static boolean isRemove = false;
    public boolean isItemAnimate = false;
    boolean isTileGrid = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemClickListener objItemClickListener;
    int previousPosition;
    int width, height, cnt = 0;
    boolean isDuplicate = false;
    private LayoutInflater inflater;


    public ItemAdapter(Context context, ArrayList<ItemMaster> alItemMaster, ItemClickListener objItemClickListener) {
        this.context = context;
        this.alItemMaster = alItemMaster;
        inflater = LayoutInflater.from(context);
        this.objItemClickListener = objItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (MenuActivity.isViewChange) {
            if (MenuActivity.i == 1) {
                isTileGrid = false;
                view = inflater.inflate(R.layout.row_category_item_grid, parent, false);
            } else {
                isTileGrid = true;
                view = inflater.inflate(R.layout.row_category_item_tile, parent, false);
            }
        } else {
            isTileGrid = false;
            view = inflater.inflate(R.layout.row_category_item, parent, false);
        }

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);
        if (!isTileGrid) {
            if (objItemMaster.getXs_ImagePhysicalName().equals("null")) {
                Picasso.with(holder.ivItem.getContext()).load(R.drawable.default_image).into(holder.ivItem);
            } else {
                Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getXs_ImagePhysicalName()).into(holder.ivItem);
            }
        }
        holder.txtItemName.setText(objItemMaster.getItemName());
        if (objItemMaster.getShortDescription().equals("")) {
            holder.txtItemDescription.setVisibility(View.GONE);
        } else {
            holder.txtItemDescription.setVisibility(View.VISIBLE);
            holder.txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        holder.txtItemPrice.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getRate()));

        if (objItemMaster.getIsDineInOnly()) {
            holder.cvItem.setClickable(false);
            holder.txtItemDineOnly.setVisibility(View.VISIBLE);
            holder.ibLike.setVisibility(View.INVISIBLE);
            if (!isTileGrid) {
                holder.btnAdd.setVisibility(View.GONE);
                holder.btnAddDisable.setVisibility(View.VISIBLE);
            }

        } else {
            holder.cvItem.setClickable(true);
            holder.txtItemDineOnly.setVisibility(View.INVISIBLE);
            holder.ibLike.setVisibility(View.VISIBLE);
            if (!isTileGrid) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.btnAddDisable.setVisibility(View.GONE);
            }

        }

        //holder.ibLike.setId(position);
        //holder.ibLike.setTag(alItemMaster.get(position));

        CheckDuplicate(null, objItemMaster);

        if (objItemMaster.getIsChecked() == -1) {
            holder.ibLike.setChecked(false);
        } else {
            holder.ibLike.setChecked(true);
        }

        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    @Override
    public long getItemId(int position) {
        return alItemMaster.get(position).getItemMasterId();
    }

    public void ItemDataChanged(ArrayList<ItemMaster> result) {
        alItemMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    private void CheckDuplicate(String isChecked, ItemMaster objItemMaster) {
        cnt = 0;
        if (isChecked != null) {
            if (alWishItemMaster.size() == 0) {
                ItemMaster objWishItemMaster = new ItemMaster();
                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
                if (isChecked.equals("1")) {
                    objWishItemMaster.setIsChecked((short) 1);
                } else {
                    objWishItemMaster.setIsChecked((short) -1);
                }
                alWishItemMaster.add(objWishItemMaster);
            } else {
                isDuplicate = false;
                for (ItemMaster objItem : alWishItemMaster) {
                    if (objItem.getItemMasterId() == objItemMaster.getItemMasterId()) {
                        if (isChecked.equals("0")) {
                            if (objItemMaster.getIsChecked() == 1) {
                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
                                isDuplicate = true;
                                break;
                            }
                        } else if (isChecked.equals("1")) {
                            alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
                            alWishItemMaster.get(cnt).setIsChecked((short) 1);
                            isDuplicate = true;
                            break;
                        }
                    }
                    cnt++;
                }
                if(!isDuplicate) {
                    ItemMaster objWishItemMaster = new ItemMaster();
                    objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
                    objWishItemMaster.setIsChecked((short) 1);
                    alWishItemMaster.add(objWishItemMaster);
                }
            }
        } else {
            if (alWishItemMaster.size() > 0) {
                for (ItemMaster objItem : alWishItemMaster) {
                    if (String.valueOf(objItem.getItemMasterId()).equals(String.valueOf(objItemMaster.getItemMasterId()))) {
                        if (objItem.getIsChecked() == 1) {
                            objItemMaster.setIsChecked((short) 1);
                        } else if (objItem.getIsChecked() == -1) {
                            objItemMaster.setIsChecked((short) -1);
                        }
                    }
                }
            }
        }
    }


//    private void CheckDuplicate(String isChecked, ItemMaster objItemMaster) {
//        cnt = 0;
//        if (isChecked != null) {
//            if (alWishItemMaster.size() == 0) {
//                ItemMaster objWishItemMaster = new ItemMaster();
//                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                if (isChecked.equals("1")) {
//                    objWishItemMaster.setIsChecked((short) 1);
//                } else {
//                    objWishItemMaster.setIsChecked((short) -1);
//                }
//                alWishItemMaster.add(objWishItemMaster);
//            } else {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if(isChecked.equals("0")){
//                        if(objItem.getItemMasterId() == objItemMaster.getItemMasterId()){
//                            if (objItemMaster.getIsChecked() == 1) {
//                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
//                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
//                                alWishItemMaster.get(cnt).setIsDeleted(true);
//                                break;
//                            }
////                            else {
////                                ItemMaster objWishItemMaster = new ItemMaster();
////                                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
////                                objWishItemMaster.setIsChecked((short) 1);
////                                objWishItemMaster.setIsDeleted(false);
////                                alWishItemMaster.add(objWishItemMaster);
////                                break;
////                            }
//                        }
//                    }
//                    else if(isChecked.equals("1")) {
//                        if (objItem.getItemMasterId() == objItemMaster.getItemMasterId()) {
//                            alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
//                            alWishItemMaster.get(cnt).setIsChecked((short) 1);
//                            alWishItemMaster.get(cnt).setIsDeleted(false);
//                            break;
//                        }else{
//                            ItemMaster objWishItemMaster = new ItemMaster();
//                            objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                            objWishItemMaster.setIsChecked((short) 1);
//                            objWishItemMaster.setIsDeleted(false);
//                            alWishItemMaster.add(objWishItemMaster);
//                            break;
//                        }
//                    }
//                    cnt++;
//                }
//            }
//        } else {
//            if (alWishItemMaster.size() > 0) {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if (String.valueOf(objItem.getItemMasterId()).equals(String.valueOf(objItemMaster.getItemMasterId()))) {
//                        if (objItem.getIsChecked() == 1 && (!objItem.getIsDeleted())) {
//                            objItemMaster.setIsChecked((short) 1);
//                        } else if (objItem.getIsChecked() == -1 && objItem.getIsDeleted()) {
//                            objItemMaster.setIsChecked((short) -1);
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private void CheckDuplicate(String isChecked, ItemMaster objItemMaster) {
//        cnt = 0;
//        if (isChecked != null) {
//            if (alWishItemMaster.size() == 0) {
//                ItemMaster objWishItemMaster = new ItemMaster();
//                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                if (isChecked.equals("1")) {
//                    objWishItemMaster.setIsChecked((short) 1);
//                } else {
//                    objWishItemMaster.setIsChecked((short) -1);
//                }
//                alWishItemMaster.add(objWishItemMaster);
//            } else {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if(isChecked.equals("0")){
//                        if(objItem.getItemMasterId() == objItemMaster.getItemMasterId()){
//                            if (objItemMaster.getIsChecked() == 1) {
//                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
//                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
//                                alWishItemMaster.get(cnt).setIsDeleted(true);
//                            }
////                            else {
////                                ItemMaster objWishItemMaster = new ItemMaster();
////                                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
////                                objWishItemMaster.setIsChecked((short) 1);
////                                objWishItemMaster.setIsDeleted(false);
////                                alWishItemMaster.add(objWishItemMaster);
////                                break;
////                            }
//                        }
//                    }
//                    else if(isChecked.equals("1")) {
//                        if (objItem.getItemMasterId() == objItemMaster.getItemMasterId()) {
////                            if (objItemMaster.getIsChecked() == 1) {
////                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
////                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
////                                alWishItemMaster.get(cnt).setIsDeleted(true);
////                            }else {
//                                ItemMaster objWishItemMaster = new ItemMaster();
//                                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                                objWishItemMaster.setIsChecked((short) 1);
//                                objWishItemMaster.setIsDeleted(false);
//                                alWishItemMaster.add(objWishItemMaster);
//                                break;
////                            }
//                        }else{
//                            ItemMaster objWishItemMaster = new ItemMaster();
//                            objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                            objWishItemMaster.setIsChecked((short) 1);
//                            objWishItemMaster.setIsDeleted(false);
//                            alWishItemMaster.add(objWishItemMaster);
//                            break;
//                        }
//                    }
//                    cnt++;
//                }
//            }
//        } else {
//            if (alWishItemMaster.size() > 0) {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if (String.valueOf(objItem.getItemMasterId()).equals(String.valueOf(objItemMaster.getItemMasterId()))) {
//                        if (objItem.getIsChecked() == 1 && (!objItem.getIsDeleted())) {
//                            objItemMaster.setIsChecked((short) 1);
//                        } else if (objItem.getIsChecked() == -1 && objItem.getIsDeleted()) {
//                            objItemMaster.setIsChecked((short) -1);
//                        }
//                    }
//                }
//            }
//        }
//    }


//    private void CheckDuplicate(String isChecked, ItemMaster objItemMaster) {
//        cnt = 0;
//        if (isChecked != null) {
//            if (alWishItemMaster.size() == 0) {
//                ItemMaster objWishItemMaster = new ItemMaster();
//                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                if (isChecked.equals("1")) {
//                    objWishItemMaster.setIsChecked((short) 1);
//                } else {
//                    objWishItemMaster.setIsChecked((short) -1);
//                }
//                alWishItemMaster.add(objWishItemMaster);
//            } else {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if (objItem.getItemMasterId() == objItemMaster.getItemMasterId()) {
//                        if (isChecked.equals("0")) {
//                            if (objItemMaster.getIsChecked() == 1) {
//                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
//                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
//                                alWishItemMaster.get(cnt).setIsDeleted(true);
//                                break;
//                            } else {
//                                ItemMaster objWishItemMaster = new ItemMaster();
//                                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                                objWishItemMaster.setIsChecked((short) -1);
//                                objWishItemMaster.setIsDeleted(true);
//                                alWishItemMaster.add(objWishItemMaster);
//                                break;
//                            }
//                        }
//                    } else {
//                        if (isChecked.equals("1")) {
//                            ItemMaster objWishItemMaster = new ItemMaster();
//                            objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                            objWishItemMaster.setIsChecked((short) 1);
//                            alWishItemMaster.add(objWishItemMaster);
//                            break;
//                        } else {
//                            if (objItemMaster.getIsChecked() == 1) {
//                                alWishItemMaster.get(cnt).setItemMasterId(objItemMaster.getItemMasterId());
//                                alWishItemMaster.get(cnt).setIsChecked((short) -1);
//                                alWishItemMaster.get(cnt).setIsDeleted(true);
//                                break;
//                            } else {
//                                ItemMaster objWishItemMaster = new ItemMaster();
//                                objWishItemMaster.setItemMasterId(objItemMaster.getItemMasterId());
//                                objWishItemMaster.setIsChecked((short) -1);
//                                objWishItemMaster.setIsDeleted(true);
//                                alWishItemMaster.add(objWishItemMaster);
//                                break;
//                            }
//                        }
//                    }
//                    cnt++;
//                }
//            }
//        } else {
//            if (alWishItemMaster.size() > 0) {
//                for (ItemMaster objItem : alWishItemMaster) {
//                    if (String.valueOf(objItem.getItemMasterId()).equals(String.valueOf(objItemMaster.getItemMasterId()))) {
//                        if (objItem.getIsChecked() == 1 && (!objItem.getIsDeleted())) {
//                            objItemMaster.setIsChecked((short) 1);
//                        } else if (objItem.getIsChecked() == -1 && objItem.getIsDeleted()) {
//                            objItemMaster.setIsChecked((short) -1);
//                        }
//                    }
//                }
//            }
//        }
//    }

    public interface ItemClickListener {
        void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName);

        void AddItemOnClick(ItemMaster objItemMaster);
    }

//    private void CheckDuplicate(String itemId, boolean isChecked,ItemMaster objItemMaster) {
//        if(objItemMaster==null) {
//            if (alString.size() == 0) {
//                alString.add(itemId);
//            }else{
//                for (String itemMasterId : alString) {
//                    if (itemMasterId.equals(itemId)) {
//                        isDuplicate = true;
//                    }
//                }
//                if (isChecked) {
//                    if (!isDuplicate) {
//                        alString.add(itemId);
//                    }
//                } else {
//                    if (isDuplicate) {
//                        alString.remove(itemId);
//                        isDuplicate = false;
//                        isRemove = true;
//                    }
//                }
//            }
//        }else{
//            if(alString.size() > 0) {
//                for (String itemMasterId : alString) {
//                    if (itemMasterId.equals(String.valueOf(objItemMaster.getItemMasterId()))) {
//                        isDuplicate = true;
//                    }
//                }
//                if (isDuplicate) {
//                    objItemMaster.setIsChecked((short) 1);
//                    isDuplicate = false;
//                }else{
//                    if(isRemove){
//                        objItemMaster.setIsChecked((short) -1);
//                        isRemove = false;
//                    }
//                }
//            }
//        }
//    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice, txtItemDineOnly;
        ImageView ivItem;
        CardView cvItem;
        Button btnAdd, btnAddDisable;
        ToggleButton ibLike;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            ibLike = (ToggleButton) itemView.findViewById(R.id.ibLike);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemDescription = (TextView) itemView.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
            txtItemDineOnly = (TextView) itemView.findViewById(R.id.txtItemDineOnly);

            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnAddDisable = (Button) itemView.findViewById(R.id.btnAddDisable);

            if (!isTileGrid && MenuActivity.isViewChange) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                width = displayMetrics.widthPixels / 2 - 24;
                height = displayMetrics.widthPixels / 2 - 24;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                ivItem.setLayoutParams(layoutParams);
            }

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objItemClickListener.AddItemOnClick(alItemMaster.get(getAdapterPosition()));
                }
            });

            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), v, v.getTransitionName());
                    } else {
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), null, null);
                    }
                }
            });

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ibLike.isChecked()) {
                        alItemMaster.get(getAdapterPosition()).setIsChecked((short) 1);
                        CheckDuplicate("1", alItemMaster.get(getAdapterPosition()));
                    } else {
                        CheckDuplicate("0", alItemMaster.get(getAdapterPosition()));
                        alItemMaster.get(getAdapterPosition()).setIsChecked((short) -1);
                    }
                }
            });

//            ibLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    //buttonView.setChecked(true);
//                    //int position = getAdapterPosition();
////                    if (alItemMaster.get(buttonView.getId()).getIsChecked() == -1) {
////                        alItemMaster.get(buttonView.getId()).setIsChecked((short) 1);
////                    }else{
////                        alItemMaster.get(buttonView.getId()).setIsChecked((short) -1);
////                    }
//                }
//            });
//              ibLike.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      if(ibLike.isChecked()){
//                          alItemMaster.get(getAdapterPosition()).setIsChecked((short) 1);
//                      }else{
//                          alItemMaster.get(getAdapterPosition()).setIsChecked((short) -1);
//                      }
//                  }
//              });
        }
    }
}
