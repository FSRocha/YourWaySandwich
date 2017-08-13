package com.fastfood.yourwaysandwich.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.MenuCallbacks;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public void onShowSandwichList(List<Sandwich> sandwichList) {

    }

    @Override
    public void onError(String errorTitle, String errorMsg) {

    }
}
