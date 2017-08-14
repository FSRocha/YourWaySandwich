package com.fastfood.yourwaysandwich.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

class IngredientListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Ingredient> mIngredientsList;
    private Context mContext;

    IngredientListAdapter(Context context, ArrayList<Ingredient> list) {
        mIngredientsList = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    void updateList(List<Ingredient> newList) {
        mIngredientsList = new ArrayList<>(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mIngredientsList.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return mIngredientsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.ingredient_item, null);
            viewHolder = new ListViewHolder();
            viewHolder.mIngredientName = (TextView) convertView.findViewById(R.id.ingredient_name);
            viewHolder.mIngredientPrice = (TextView) convertView.findViewById(R.id.ingredient_price);
            viewHolder.mIngredientAmount = (TextView) convertView.findViewById(R.id.ingredient_amount);
            viewHolder.mIngredientPicture = (ImageView) convertView.findViewById(R.id.ingredient_picture);
            viewHolder.mAddButton = (Button) convertView.findViewById(R.id.add_button);
            viewHolder.mRemoveButton = (Button) convertView.findViewById(R.id.remove_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }

        Ingredient item = mIngredientsList.get(position);
        viewHolder.mIngredientName.setText(item.getName());
        viewHolder.mIngredientPrice.setText(formatPrice(item.getPrice()));
//        viewHolder.mIngredientAmount.setText();


        // TODO Implement imageDownloaderTask
        // new ImageDownloaderTask(viewHolder.mIngredientPicture).execute(item.getImageUrl());

        return convertView;
    }

    private static class ListViewHolder {
        TextView mIngredientName;
        TextView mIngredientPrice;
        TextView mIngredientAmount;
        Button mAddButton;
        Button mRemoveButton;
        ImageView mIngredientPicture;
    }

    private String formatPrice(float price) {
        return String.format("R$ %.2f", price);
    }
}
