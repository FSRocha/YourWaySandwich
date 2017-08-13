package com.fastfood.yourwaysandwich.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.MenuCallbacks;
import com.fastfood.yourwaysandwich.presenter.MenuOperations;
import com.fastfood.yourwaysandwich.presenter.MenuPresenter;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuCallbacks {

    private SandwichListAdapter mListAdapter;
    private MenuOperations mMenuPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ListView mListView = (ListView) findViewById(R.id.sandwich_list);
        mListAdapter = new SandwichListAdapter(this, new ArrayList<Sandwich>());
        mListView.setAdapter(mListAdapter);
        mMenuPresenter = new MenuPresenter();
        mMenuPresenter.createMenu(this, this);
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
        mListAdapter.updateList(sandwichList);
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}