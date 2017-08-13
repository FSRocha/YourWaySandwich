package com.fastfood.yourwaysandwich.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

class SandwichListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Sandwich> mSandwichList;

    SandwichListAdapter(Context context, ArrayList<Sandwich> list) {
        mSandwichList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    void updateList(List<Sandwich> newList) {
        mSandwichList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSandwichList.size();
    }

    @Override
    public Sandwich getItem(int position) {
        return mSandwichList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.sandwich_list_item, null);
            viewHolder = new ListViewHolder();
            viewHolder.mSandwichName = (TextView) convertView.findViewById(R.id.sandwich_name);
            viewHolder.mSandwichPrice = (TextView) convertView.findViewById(R.id.sandwich_price);
            viewHolder.mSandwichIngredients = (TextView) convertView.findViewById(R.id.sandwich_ingredients);
            viewHolder.mExtraBatch = convertView.findViewById(R.id.extra_batch_img);
            viewHolder.mSandwichPicture = (ImageView) convertView.findViewById(R.id.sandwich_picture);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        Sandwich item = mSandwichList.get(position);
        viewHolder.mSandwichName.setText(item.getName());
        viewHolder.mSandwichPrice.setText(String.valueOf(item.getPrice()));
        viewHolder.mSandwichIngredients.setText(item.getIngredientsText());
        viewHolder.mExtraBatch.setVisibility(item.hasExtras()?View.VISIBLE:View.GONE);

        // TODO Implement imageDownloaderTask
        // new ImageDownloaderTask(viewHolder.mSandwichPicture).execute(item.getImageUrl());

        return convertView;
    }

    private static class ListViewHolder {
        TextView mSandwichName;
        TextView mSandwichPrice;
        TextView mSandwichIngredients;
        View mExtraBatch;
        ImageView mSandwichPicture;
    }
}
