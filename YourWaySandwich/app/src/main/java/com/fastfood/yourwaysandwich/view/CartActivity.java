package com.fastfood.yourwaysandwich.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.CartCallbacks;
import com.fastfood.yourwaysandwich.presenter.CartOperations;
import com.fastfood.yourwaysandwich.presenter.CartPresenter;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartCallbacks {

    private SandwichListAdapter mListAdapter;
    private CartOperations mCartPresenter;
    private TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ListView mListView = (ListView) findViewById(R.id.sandwich_list);
        mListAdapter = new SandwichListAdapter(this, new ArrayList<Sandwich>());
        mListView.setAdapter(mListAdapter);
        mTotalPrice = (TextView) findViewById(R.id.total_price_txt);
        mCartPresenter = new CartPresenter();
        mCartPresenter.createCart(ApplicationGlobal.getInstance(), this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCartPresenter != null) {
            mCartPresenter.destroyCart();
        }
    }

    @Override
    public void onShowSandwichList(List<Sandwich> sandwichList, float totalPrice) {
        mListAdapter.updateList(sandwichList);
        mTotalPrice.setText(formatPrice(totalPrice));
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    private String formatPrice(float price) {
        return String.format("R$ %.2f", price);
    }
}