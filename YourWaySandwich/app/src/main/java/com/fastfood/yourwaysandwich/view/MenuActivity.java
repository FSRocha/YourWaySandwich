package com.fastfood.yourwaysandwich.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.MenuCallbacks;
import com.fastfood.yourwaysandwich.presenter.MenuOperations;
import com.fastfood.yourwaysandwich.presenter.MenuPresenter;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuCallbacks,
        AdapterView.OnItemClickListener {

    private SandwichListAdapter mListAdapter;
    private MenuOperations mMenuPresenter;
    private List<Sandwich> mSandwichList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ListView mListView = (ListView) findViewById(R.id.sandwich_list);
        mListAdapter = new SandwichListAdapter(this, new ArrayList<Sandwich>());
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(this);
        mMenuPresenter = new MenuPresenter();
        mMenuPresenter.createMenu(ApplicationGlobal.getInstance(), this, this);
        Button promotionsButton = (Button) findViewById(R.id.promotions_button);
        promotionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuPresenter != null) {
                    mMenuPresenter.selectPromotions();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMenuPresenter != null) {
            mMenuPresenter.destroyMenu();
        }
    }

    @Override
    public void onShowSandwichList(List<Sandwich> sandwichList) {
        mSandwichList = sandwichList;
        mListAdapter.updateList(mSandwichList);
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSandwichSelected() {
        Intent detailsActivity = new Intent(this, DetailsActivity.class);
        startActivity(detailsActivity);
    }

    @Override
    public void onPromotionsSelected() {
        Intent promotionsActivity = new Intent(MenuActivity.this, PromotionsActivity.class);
        startActivity(promotionsActivity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMenuPresenter != null) {
            mMenuPresenter.selectSandwich(mSandwichList.get(position));
        }
    }
}