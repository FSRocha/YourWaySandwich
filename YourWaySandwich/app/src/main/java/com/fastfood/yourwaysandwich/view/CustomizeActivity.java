package com.fastfood.yourwaysandwich.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.presenter.CustomizeCallbacks;
import com.fastfood.yourwaysandwich.presenter.CustomizeOperations;
import com.fastfood.yourwaysandwich.presenter.CustomizePresenter;

import java.util.HashMap;
import java.util.Map;

public class CustomizeActivity extends AppCompatActivity implements CustomizeCallbacks,
        ExtraIngredientListener {

    private IngredientListAdapter mListAdapter;
    private CustomizeOperations mCustomizePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        ListView mListView = (ListView) findViewById(R.id.ingredients_list);
        mListAdapter = new IngredientListAdapter(this, new HashMap<Ingredient, Integer>(), this);
        mListView.setAdapter(mListAdapter);
        mCustomizePresenter = new CustomizePresenter();
        mCustomizePresenter.createCustomize(ApplicationGlobal.getInstance(),
                ApplicationGlobal.getInstance(), this);
        Button addExtrasButton = (Button) findViewById(R.id.add_extras_button);
        addExtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomizePresenter != null) {
                    mCustomizePresenter.addExtras();
                }
            }
        });
    }

    @Override
    public void onShowIngredientsList(Map<Ingredient, Integer> ingredientList) {
        mListAdapter.updateList(ingredientList);
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onExtrasAdded() {
        Toast.makeText(this, R.string.extras_added_success_msg, Toast.LENGTH_LONG).show();
        Intent detailsActivity = new Intent(this, DetailsActivity.class);
        detailsActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(detailsActivity);
    }

    @Override
    public void addExtra(Ingredient ingredient) {
        if (mCustomizePresenter != null) {
            mCustomizePresenter.addExtraIngredient(ingredient);
        }
    }

    @Override
    public void removeExtra(Ingredient ingredient) {
        if (mCustomizePresenter != null) {
            mCustomizePresenter.removeExtraIngredient(ingredient);
        }
    }
}