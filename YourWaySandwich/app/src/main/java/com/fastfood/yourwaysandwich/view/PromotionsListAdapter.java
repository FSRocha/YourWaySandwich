package com.fastfood.yourwaysandwich.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Promotion;
import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

class PromotionsListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Promotion> mPromotionsList;

    PromotionsListAdapter(Context context, ArrayList<Promotion> list) {
        mPromotionsList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    void updateList(List<Promotion> newList) {
        mPromotionsList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPromotionsList.size();
    }

    @Override
    public Promotion getItem(int position) {
        return mPromotionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.promotion_item, null);
            viewHolder = new ListViewHolder();
            viewHolder.mPromotionName = (TextView) convertView.findViewById(R.id.promotion_name);
            viewHolder.mPromotionDescription = (TextView) convertView.findViewById(R.id.promotion_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        Promotion item = mPromotionsList.get(position);
        viewHolder.mPromotionName.setText(item.getName());
        viewHolder.mPromotionDescription.setText(item.getDescription());
        return convertView;
    }

    private static class ListViewHolder {
        TextView mPromotionName;
        TextView mPromotionDescription;
    }
}
