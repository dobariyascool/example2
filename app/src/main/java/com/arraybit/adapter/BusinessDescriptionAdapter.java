package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arraybit.abposw.R;
import com.arraybit.modal.BusinessDescription;

import java.util.ArrayList;

/// <summary>
/// Adapter for BusinessDescription
/// </summary>
public class BusinessDescriptionAdapter extends RecyclerView.Adapter<BusinessDescriptionAdapter.BusinessDescriptionViewHolder> {

	View view;
	Context context;
	LayoutInflater layoutInflater;
	ArrayList<BusinessDescription> alBusinessDescription;

	public BusinessDescriptionAdapter(Context context, ArrayList<BusinessDescription> result) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.alBusinessDescription = result;
	}

	@Override
	public BusinessDescriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		view=layoutInflater.inflate(R.layout.row_business_description,parent,false);
		return new BusinessDescriptionViewHolder(view);
	}

	@Override
	public void onBindViewHolder(BusinessDescriptionViewHolder holder, int position) {
		BusinessDescription objBusinessDescription = alBusinessDescription.get(position);
		holder.txtKeyword.setText(objBusinessDescription.getKeyword());

	}

	@Override
	public int getItemCount() {
		return this.alBusinessDescription.size();
	}


	class BusinessDescriptionViewHolder extends RecyclerView.ViewHolder {
		TextView txtKeyword;

		public BusinessDescriptionViewHolder(View view) {
			super(view);

			txtKeyword = (TextView)view.findViewById(R.id.txtKeyword);
		}
	}
}
