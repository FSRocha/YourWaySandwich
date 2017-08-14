package com.fastfood.yourwaysandwich.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Promotion;
import com.fastfood.yourwaysandwich.presenter.PromotionsCallbacks;
import com.fastfood.yourwaysandwich.presenter.PromotionsOperations;
import com.fastfood.yourwaysandwich.presenter.PromotionsPresenter;

import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity implements PromotionsCallbacks {

    private PromotionsListAdapter mListAdapter;
    private PromotionsOperations mPromotionsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        ListView mListView = (ListView) findViewById(R.id.promotions_list);
        mListAdapter = new PromotionsListAdapter(this, new ArrayList<Promotion>());
        mListView.setAdapter(mListAdapter);
        mPromotionsPresenter = new PromotionsPresenter();
        mPromotionsPresenter.createPromotions(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPromotionsPresenter != null) {
            mPromotionsPresenter.destroyPromotions();
        }
    }

    @Override
    public void onShowPromotionsList(List<Promotion> promotionsList) {
        mListAdapter.updateList(promotionsList);
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}