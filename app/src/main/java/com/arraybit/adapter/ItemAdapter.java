package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arraybit.abposw.MenuActivity;
import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public static ArrayList<ItemMaster> alWishItemMaster = new ArrayList<>();
    public boolean isItemAnimate = false;
    boolean isTileGrid = false, isWishListChange;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemClickListener objItemClickListener;
    int previousPosition;
    int width, height, cnt = 0;
    boolean isDuplicate = false, isLikeClick;
    boolean isVeg, isNonVeg, isJain;
    private LayoutInflater inflater;


    public ItemAdapter(Context context, ArrayList<ItemMaster> alItemMaster, ItemClickListener objItemClickListener, boolean isLikeClick) {
        this.context = context;
        this.alItemMaster = alItemMaster;
        inflater = LayoutInflater.from(context);
        this.objItemClickListener = objItemClickListener;
        this.isLikeClick = isLikeClick;
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
            if (objItemMaster.getXs_ImagePhysicalName() == null || objItemMaster.getXs_ImagePhysicalName().equals("")) {
                Picasso.with(holder.ivItem.getContext()).load(R.drawable.default_image).into(holder.ivItem);
            } else {
                Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getXs_ImagePhysicalName()).into(holder.ivItem);
            }
            if (objItemMaster.getShortDescription().equals("")) {
                holder.txtItemDescription.setVisibility(View.INVISIBLE);
            } else {
                holder.txtItemDescription.setVisibility(View.VISIBLE);
                holder.txtItemDescription.setText(objItemMaster.getShortDescription());
            }
        }

        holder.txtItemPrice.setText(view.getResources().getString(R.string.cifRupee) + " " + Globals.dfWithPrecision.format(objItemMaster.getRate()));

        if (objItemMaster.getIsDineInOnly()) {
            holder.txtItemDineOnly.setVisibility(View.VISIBLE);
            if (MenuActivity.isViewChange) {
                holder.ibLike.setVisibility(View.GONE);
            } else {
                holder.ibLike.setVisibility(View.INVISIBLE);
            }
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

        if (!objItemMaster.getLinktoOptionMasterIds().equals("")) {
            if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.DoubleSpicy.getValue()))) {
                holder.ivExtraSpicy.setVisibility(View.VISIBLE);
                holder.ivSpicy.setVisibility(View.GONE);
                holder.ivSweet.setVisibility(View.GONE);
            } else if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Spicy.getValue()))) {
                holder.ivSpicy.setVisibility(View.VISIBLE);
                holder.ivExtraSpicy.setVisibility(View.GONE);
                holder.ivSweet.setVisibility(View.GONE);
            } else if (CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Sweet.getValue()))) {
                holder.ivSweet.setVisibility(View.VISIBLE);
                holder.ivExtraSpicy.setVisibility(View.GONE);
                holder.ivSpicy.setVisibility(View.GONE);
            } else {
                holder.ivSweet.setVisibility(View.GONE);
                holder.ivExtraSpicy.setVisibility(View.GONE);
                holder.ivSpicy.setVisibility(View.GONE);
            }

            isVeg = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Veg.getValue()));
            isNonVeg = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.NonVeg.getValue()));
            isJain = CheckOptionValue(objItemMaster.getLinktoOptionMasterIds(), String.valueOf(Globals.OptionValue.Jain.getValue()));
            if (isNonVeg && !isVeg) {
                holder.ivNonVeg.setVisibility(View.VISIBLE);
                holder.ivJain.setVisibility(View.GONE);
            } else if (isJain && !isNonVeg) {
                holder.ivJain.setVisibility(View.VISIBLE);
                holder.ivNonVeg.setVisibility(View.GONE);
            } else {
                holder.ivJain.setVisibility(View.GONE);
                holder.ivNonVeg.setVisibility(View.GONE);
            }
        } else {
            holder.ivJain.setVisibility(View.GONE);
            holder.ivNonVeg.setVisibility(View.GONE);
            holder.ivSweet.setVisibility(View.GONE);
            holder.ivExtraSpicy.setVisibility(View.GONE);
            holder.ivSpicy.setVisibility(View.GONE);
        }

        if (!isTileGrid && !MenuActivity.isViewChange) {
            if ((holder.ivJain.getVisibility() == View.VISIBLE) || (holder.ivSpicy.getVisibility() == View.VISIBLE) || (holder.ivSweet.getVisibility() == View.VISIBLE) || (holder.ivExtraSpicy.getVisibility() == View.VISIBLE)) {
                holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
                if (objItemMaster.getItemName().length() > 15) {
                    holder.txtItemName.setText(objItemMaster.getItemName().substring(0, 15) + "...");
                } else {
                    holder.txtItemName.setText(objItemMaster.getItemName());
                }

            } else {
                holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(objItemMaster.getItemName().length())});
                holder.txtItemName.setText(objItemMaster.getItemName());
            }
        } else if (MenuActivity.isViewChange) {
            if(!isTileGrid) {
                if ((holder.ivJain.getVisibility() == View.VISIBLE) || (holder.ivSpicy.getVisibility() == View.VISIBLE) || (holder.ivSweet.getVisibility() == View.VISIBLE) || (holder.ivExtraSpicy.getVisibility() == View.VISIBLE)) {
                    holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    if (objItemMaster.getItemName().length() > 9) {
                        holder.txtItemName.setText(objItemMaster.getItemName().substring(0, 9) + "...");
                    } else {
                        holder.txtItemName.setText(objItemMaster.getItemName());
                    }
                } else {
                    holder.txtItemName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(objItemMaster.getItemName().length())});
                    holder.txtItemName.setText(objItemMaster.getItemName());
                }
            }else{
                holder.txtItemName.setText(objItemMaster.getItemName());
            }
        }

        CheckDuplicate(null, objItemMaster);

        if (objItemMaster.getIsChecked() == -1 || objItemMaster.getIsChecked() == 0) {
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

    public void RemoveData(int position, boolean isRemoveFromList) {
        if (isRemoveFromList) {
            CheckDuplicate(String.valueOf(0), alItemMaster.get(position));
        }
        alItemMaster.remove(position);
        notifyItemRemoved(position);
    }

    public void UpdateWishList(int position, short isCheck) {
        isItemAnimate = false;
        isWishListChange = true;
        CheckDuplicate(String.valueOf(isCheck), alItemMaster.get(position));
        alItemMaster.get(position).setIsChecked(isCheck);
        notifyItemChanged(position);
    }

    public void CheckIdInCurrentListAndUpdate(short isChecked,int itemMasterId,short oldCheckValue){
        int count=0;
        boolean isDuplicate = false;
        for(ItemMaster objItemMaster : alItemMaster) {
            if (objItemMaster.getItemMasterId() == itemMasterId) {
                isItemAnimate = false;
                isWishListChange = true;
                objItemMaster.setIsChecked(oldCheckValue);
                CheckDuplicate(String.valueOf(isChecked), objItemMaster);
                objItemMaster.setIsChecked(isChecked);
                notifyItemChanged(count);
                isDuplicate = true;
                break;
            }
            count++;
        }
        if(!isDuplicate){
            ItemMaster objItem = new ItemMaster();
            objItem.setItemMasterId(itemMasterId);
            objItem.setIsChecked(oldCheckValue);
            CheckDuplicate(String.valueOf(isChecked),objItem);
        }
    }

    public void CheckIdInCurrentList(short isChecked,int itemMasterId,short oldCheckValue,ItemMaster objWishList){
        int count=0;
        boolean isDuplicate = false;
        for(ItemMaster objItemMaster : alItemMaster) {
            if (objItemMaster.getItemMasterId() == itemMasterId) {
                isDuplicate = true;
                CheckDuplicate(String.valueOf(0), objItemMaster);
                alItemMaster.remove(count);
                notifyItemRemoved(count);
                break;
            }
            count++;
        }
        if(!isDuplicate){
            if(objWishList!=null) {
                CheckDuplicate(String.valueOf(isChecked), objWishList);
                alItemMaster.add(alItemMaster.size(), objWishList);
                notifyItemInserted(alItemMaster.size());
            }
        }
    }

    private boolean CheckOptionValue(String optionValueIds, String optionValue) {
        List<String> items = Arrays.asList(optionValueIds.split(","));
        boolean isMatch = false;
        for (String str : items) {
            if (str.equals(optionValue)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
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
                if (!isDuplicate) {
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

    public interface ItemClickListener {
        void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName, int position);

        void AddItemOnClick(ItemMaster objItemMaster);

        void LikeOnClick(int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice, txtItemDineOnly;
        ImageView ivItem, ivJain, ivSpicy, ivExtraSpicy, ivSweet, ivNonVeg;
        CardView cvItem;
        Button btnAdd, btnAddDisable;
        ToggleButton ibLike;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            ivJain = (ImageView) itemView.findViewById(R.id.ivJain);
            ivSpicy = (ImageView) itemView.findViewById(R.id.ivSpicy);
            ivExtraSpicy = (ImageView) itemView.findViewById(R.id.ivDoubleSpicy);
            ivSweet = (ImageView) itemView.findViewById(R.id.ivSweet);
            ivNonVeg = (ImageView) itemView.findViewById(R.id.ivNonVeg);

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
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), v, v.getTransitionName(), getAdapterPosition());
                    } else {
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), null, null, getAdapterPosition());
                    }
                }
            });

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ibLike.isChecked()) {
                        alItemMaster.get(getAdapterPosition()).setIsChecked((short) 1);
                        CheckDuplicate("1", alItemMaster.get(getAdapterPosition()));
//                        Toast.makeText(context, alItemMaster.get(getAdapterPosition()).getItemName()+" is added to Wishlist", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,String.format(context.getResources().getString(R.string.MsgWishListItem),  alItemMaster.get(getAdapterPosition()).getItemName()), Toast.LENGTH_SHORT).show();
                    } else {
                        if (isLikeClick) {
                            CheckDuplicate("0", alItemMaster.get(getAdapterPosition()));
                            alItemMaster.get(getAdapterPosition()).setIsChecked((short) -1);
                            objItemClickListener.LikeOnClick(getAdapterPosition());
                        } else {
                            CheckDuplicate("0", alItemMaster.get(getAdapterPosition()));
                            alItemMaster.get(getAdapterPosition()).setIsChecked((short) -1);
                        }
                    }
                }
            });
        }
    }
}
