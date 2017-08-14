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

import java.util.ArrayList;
import java.util.Map;

class IngredientListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Ingredient> mIngredientsList;
    private Map<Ingredient, Integer> mMapList;
    private ExtraIngredientListener mExtraIngredientListener;

    IngredientListAdapter(Context context, Map<Ingredient, Integer> mapList,
                          ExtraIngredientListener listener) {
        mMapList = mapList;
        mIngredientsList = new ArrayList<>(mapList.keySet());
        mLayoutInflater = LayoutInflater.from(context);
        mExtraIngredientListener = listener;
    }

    void updateList(Map<Ingredient, Integer> newList) {
        mMapList = newList;
        mIngredientsList = new ArrayList<>(newList.keySet());
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ListViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.ingredient_item, null);
            viewHolder = new ListViewHolder();
            viewHolder.mIngredientName =
                    (TextView) convertView.findViewById(R.id.ingredient_name);
            viewHolder.mIngredientPrice =
                    (TextView) convertView.findViewById(R.id.ingredient_price);
            viewHolder.mIngredientAmount =
                    (TextView) convertView.findViewById(R.id.ingredient_amount);
            viewHolder.mIngredientPicture =
                    (ImageView) convertView.findViewById(R.id.ingredient_picture);
            viewHolder.mAddButton = (Button) convertView.findViewById(R.id.add_button);
            viewHolder.mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExtraIngredientListener.addExtra(getItem(position));
                }
            });
            viewHolder.mRemoveButton = (Button) convertView.findViewById(R.id.remove_button);
            viewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExtraIngredientListener.removeExtra(getItem(position));
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }
        Ingredient item = getItem(position);
        int amount = mMapList.get(item);
        viewHolder.mIngredientName.setText(item.getName());
        viewHolder.mIngredientPrice.setText(formatPrice(item.getPrice()));
        viewHolder.mIngredientAmount.setText(String.valueOf(amount));

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
