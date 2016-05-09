package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.arraybit.modal.OrderMaster;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderMasterViewHolder> {
    public boolean isItemAnimate = false;
    Context context;
    LayoutInflater layoutInflater;
    View view;
    int previousPosition;
    ArrayList<OrderMaster> alOrderMaster;
    SimpleDateFormat sdfDate = new SimpleDateFormat(Globals.DateFormat, Locale.US);
    String today;
    Date toDate, currentDate;
    OrderOnClickListener objOrderOnClickListener;

    public OrderAdapter(Context context, ArrayList<OrderMaster> alOrderMaster, OrderOnClickListener objOrderOnClickListener) {
        this.context = context;
        this.alOrderMaster = alOrderMaster;
        layoutInflater = LayoutInflater.from(context);
        this.objOrderOnClickListener = objOrderOnClickListener;
    }

    @Override
    public OrderMasterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_order, parent, false);
        return new OrderMasterViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OrderMasterViewHolder holder, int position) {
        OrderMaster objOrderMaster = alOrderMaster.get(position);
        holder.txtOrderNumber.setText(objOrderMaster.getOrderNumber());
        holder.txtTotalAmount.setText(Globals.dfWithPrecision.format(objOrderMaster.getTotalAmount()));
        String[] strDateTime = objOrderMaster.getOrderDateTime().split("T");
        holder.txtOrderDate.setText(strDateTime[0] + "\n" + strDateTime[1]);
        try {
            today = sdfDate.format(new Date());
            currentDate = sdfDate.parse(today);
            toDate = sdfDate.parse(strDateTime[0]);
            if (toDate.compareTo(currentDate) < 0) {
                if (objOrderMaster.getlinktoOrderStatusMasterId() > 0) {
                    holder.ibCancelOrder.setVisibility(View.GONE);
                    holder.txtStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.ibCancelOrder.setVisibility(View.GONE);
                    holder.txtStatus.setVisibility(View.VISIBLE);
                }
            } else {
                if (objOrderMaster.getlinktoOrderStatusMasterId() > 0 || objOrderMaster.getIsPreOrder()) {
                    holder.ibCancelOrder.setVisibility(View.GONE);
                    holder.txtStatus.setVisibility(View.VISIBLE);
                    if (Globals.OrderStatus.Cancelled.getValue() == objOrderMaster.getlinktoOrderStatusMasterId()) {
                        holder.txtStatus.setText(Globals.OrderStatus.Cancelled.toString());
                    } else if (Globals.OrderStatus.Delivered.getValue() == objOrderMaster.getlinktoOrderStatusMasterId()) {
                        holder.txtStatus.setText(Globals.OrderStatus.Delivered.toString());
                    }// else {
//                        holder.txtStatus.setVisibility(View.INVISIBLE);
//                    }

                } else {
                    holder.txtStatus.setVisibility(View.GONE);
                    holder.ibCancelOrder.setVisibility(View.VISIBLE);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(holder.txtStatus.getVisibility()==View.VISIBLE) {
            if (Globals.OrderStatus.Cancelled.getValue() == objOrderMaster.getlinktoOrderStatusMasterId()) {
                holder.txtStatus.setText(Globals.OrderStatus.Cancelled.toString());
            } else if (Globals.OrderStatus.Delivered.getValue() == objOrderMaster.getlinktoOrderStatusMasterId()) {
                holder.txtStatus.setText(Globals.OrderStatus.Delivered.toString());
            } else {
                holder.txtStatus.setVisibility(View.INVISIBLE);
            }
        }

        if (objOrderMaster.getType() == 0) {
            holder.childDetailLayout.removeAllViewsInLayout();
            holder.childDetailLayout.setVisibility(View.GONE);
            holder.headerLayout.setVisibility(View.GONE);
            holder.ibVisible.setImageResource(R.drawable.expand_drawable);
        } else {
            holder.childDetailLayout.removeAllViewsInLayout();
            holder.childDetailLayout.setVisibility(View.VISIBLE);
            holder.headerLayout.setVisibility(View.VISIBLE);
            holder.ibVisible.setImageResource(R.drawable.collapse_drawable);
            SetDetailLayout(objOrderMaster.getAlOrderItemTran(), holder);
        }

        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    public void OrderDataChanged(ArrayList<OrderMaster> result) {
        alOrderMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public void UpdateOrderData(int position) {
        alOrderMaster.get(position).setlinktoOrderStatusMasterId((short) Globals.OrderStatus.Cancelled.getValue());
        isItemAnimate = false;
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return alOrderMaster.size();
    }

    private void SetDetailLayout(ArrayList<ItemMaster> alOrderItemModifierTran, OrderMasterViewHolder holder) {
        LinearLayout[] mainLayout = new LinearLayout[alOrderItemModifierTran.size()];
        TextView[] txtItemName = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtQty = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtRate = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtAmount = new TextView[alOrderItemModifierTran.size()];
        TextView[] txtRemark = new TextView[alOrderItemModifierTran.size()];
        String strRemark = null;

        for (int i = 0; i < alOrderItemModifierTran.size(); i++) {

            mainLayout[i] = new LinearLayout(context);
            LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mainLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout[i].setPadding(8, 0, 8, 0);
            mainLayout[i].setLayoutParams(mainLayoutParams);

            txtItemName[i] = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 0.5f;
            txtItemName[i].setLayoutParams(layoutParams);
            txtItemName[i].setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            txtItemName[i].setMaxLines(1);
            txtItemName[i].setText(alOrderItemModifierTran.get(i).getItemName());

            txtQty[i] = new TextView(context);
            LinearLayout.LayoutParams txtQtyLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtQtyLayoutParams.weight = 0.15f;
            txtQty[i].setLayoutParams(txtQtyLayoutParams);
            txtQty[i].setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            txtQty[i].setMaxLines(1);
            txtQty[i].setText(String.valueOf(alOrderItemModifierTran.get(i).getQuantity()));

            txtRate[i] = new TextView(context);
            LinearLayout.LayoutParams txtRateParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtRateParams.weight = 0.22f;
            txtRate[i].setLayoutParams(txtRateParams);
            txtRate[i].setGravity(Gravity.END);
            txtRate[i].setMaxLines(1);
            txtRate[i].setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            txtRate[i].setText(Globals.dfWithPrecision.format(alOrderItemModifierTran.get(i).getRate()));

            txtAmount[i] = new TextView(context);
            LinearLayout.LayoutParams txtAmountParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtAmountParams.weight = 0.3f;
            txtAmount[i].setLayoutParams(txtAmountParams);
            txtAmount[i].setGravity(Gravity.END);
            txtAmount[i].setMaxLines(1);
            txtAmount[i].setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
            txtAmount[i].setText(Globals.dfWithPrecision.format(alOrderItemModifierTran.get(i).getSellPrice()));

            txtRemark[i] = new TextView(context);
            LinearLayout.LayoutParams txtRemarkParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            txtRemark[i].setLayoutParams(txtRemarkParams);
            txtRemark[i].setTextColor(ContextCompat.getColor(context, R.color.error_text));
            txtRemark[i].setMaxLines(1);
            txtRemark[i].setPadding(8, 0, 8, 0);
            txtRemark[i].setTextSize(10f);

            if (alOrderItemModifierTran.get(i).getLinktoItemMasterIdModifiers().equals("0")) {
                txtItemName[i].setTextSize(14f);
                txtQty[i].setTextSize(14f);
                txtQty[i].setVisibility(View.VISIBLE);
                txtRate[i].setTextSize(14f);
                txtAmount[i].setTextSize(14f);
            } else {
                txtItemName[i].setTextSize(10f);
                txtQty[i].setTextSize(10f);
                txtQty[i].setVisibility(View.INVISIBLE);
                txtRate[i].setTextSize(10f);
                txtAmount[i].setTextSize(10f);
            }

            if (alOrderItemModifierTran.get(i).getRemark() != null && (!alOrderItemModifierTran.get(i).getRemark().equals(""))) {
                if (alOrderItemModifierTran.get(i).getRemark().substring(alOrderItemModifierTran.get(i).getRemark().length() - 1, alOrderItemModifierTran.get(i).getRemark().length()).equals(",")) {
                    strRemark = alOrderItemModifierTran.get(i).getRemark().substring(0, alOrderItemModifierTran.get(i).getRemark().length() - 1);
                } else if (alOrderItemModifierTran.get(i).getRemark().substring(alOrderItemModifierTran.get(i).getRemark().length() - 1, alOrderItemModifierTran.get(i).getRemark().length()).equals(" ")) {
                    strRemark = alOrderItemModifierTran.get(i).getRemark().substring(0, alOrderItemModifierTran.get(i).getRemark().length() - 2);
                } else {
                    strRemark = alOrderItemModifierTran.get(i).getRemark();
                }
            }

            mainLayout[i].addView(txtItemName[i]);
            mainLayout[i].addView(txtQty[i]);
            mainLayout[i].addView(txtRate[i]);
            mainLayout[i].addView(txtAmount[i]);
            holder.childDetailLayout.addView(mainLayout[i]);
            if (strRemark != null && (!strRemark.equals(""))) {
                if (i != alOrderItemModifierTran.size() - 1 && alOrderItemModifierTran.get(i + 1).getLinktoItemMasterIdModifiers().equals("0")) {
                    txtRemark[i].setText(strRemark);
                    holder.childDetailLayout.addView(txtRemark[i]);
                    strRemark = "";
                } else if (i == alOrderItemModifierTran.size() - 1) {
                    txtRemark[i].setText(strRemark);
                    holder.childDetailLayout.addView(txtRemark[i]);
                    strRemark = "";
                }
            }
        }
    }

    public interface OrderOnClickListener {
        void CancelOnClick(OrderMaster objOrderMaster, int position);
    }

    class OrderMasterViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNumber, txtOrderDate, txtTotalAmount, txtStatus;
        LinearLayout childDetailLayout, headerLayout;
        ImageButton ibCancelOrder, ibVisible;

        public OrderMasterViewHolder(final View itemView) {
            super(itemView);

            txtOrderNumber = (TextView) itemView.findViewById(R.id.txtOrderNumber);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);

            ibCancelOrder = (ImageButton) itemView.findViewById(R.id.ibCancelOrder);
            ibVisible = (ImageButton) itemView.findViewById(R.id.ibVisible);

            childDetailLayout = (LinearLayout) itemView.findViewById(R.id.childDetailLayout);
            headerLayout = (LinearLayout) itemView.findViewById(R.id.headerLayout);

            ibVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alOrderMaster.get(getAdapterPosition()).getType() == 0) {
                        alOrderMaster.get(getAdapterPosition()).setType((short) 1);
                        isItemAnimate = false;
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        alOrderMaster.get(getAdapterPosition()).setType((short) 0);
                        isItemAnimate = false;
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });


            ibCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objOrderOnClickListener.CancelOnClick(alOrderMaster.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
