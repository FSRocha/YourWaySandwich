package com.fastfood.yourwaysandwich.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.MenuCallbacks;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuCallbacks {

    private ListView mListView;
    private SandwichListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mListView = (ListView) findViewById(R.id.sandwich_list);
        mListAdapter = new SandwichListAdapter(this, new ArrayList<Sandwich>());
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onShowSandwichList(List<Sandwich> sandwichList) {

    }

    @Override
    public void onError(String errorTitle, String errorMsg) {

    }
}
