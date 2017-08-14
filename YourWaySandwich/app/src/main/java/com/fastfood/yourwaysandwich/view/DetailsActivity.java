package com.fastfood.yourwaysandwich.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fastfood.yourwaysandwich.ApplicationGlobal;
import com.fastfood.yourwaysandwich.R;
import com.fastfood.yourwaysandwich.model.Sandwich;
import com.fastfood.yourwaysandwich.presenter.DetailsCallbacks;
import com.fastfood.yourwaysandwich.presenter.DetailsOperations;
import com.fastfood.yourwaysandwich.presenter.DetailsPresenter;

public class DetailsActivity extends AppCompatActivity implements DetailsCallbacks {

    private DetailsOperations mDetailsPresenter;
    private ImageView mSandwichPicture;
    private TextView mSandwichName;
    private TextView mSandwichPrice;
    private TextView mSandwichIngredients;
    private ImageView mExtraBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mSandwichPicture = (ImageView) findViewById(R.id.sandwich_picture);
        mSandwichName = (TextView) findViewById(R.id.sandwich_name);
        mSandwichPrice = (TextView) findViewById(R.id.sandwich_price);
        mSandwichIngredients = (TextView) findViewById(R.id.sandwich_ingredients);
        mExtraBatch = (ImageView) findViewById(R.id.extra_batch_img);
        mDetailsPresenter = new DetailsPresenter();
        mDetailsPresenter.createDetails(ApplicationGlobal.getInstance(), this, this);

        Button orderButton = (Button) findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPresenter != null) {
                    mDetailsPresenter.orderSandwich();
                }
            }
        });

        Button customizeButton = (Button) findViewById(R.id.customize_button);
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPresenter != null) {
                    mDetailsPresenter.customizeSandwich();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDetailsPresenter != null) {
            mDetailsPresenter.destroyDetails();
        }
    }

    @Override
    public void onSandwichUpdated(Sandwich sandwich) {
        mSandwichName.setText(sandwich.getName());
        mSandwichPrice.setText(formatPrice(sandwich.getPrice()));
        mSandwichIngredients.setText(sandwich.getIngredientsText());
        mExtraBatch.setVisibility(sandwich.hasExtras() ? View.VISIBLE : View.GONE);

        // TODO Implement imageDownloaderTask
        // new ImageDownloaderTask(mSandwichPicture).execute(sandwich.getImageUrl());
    }

    @Override
    public void onSandwichOrdered() {
        Toast.makeText(this, R.string.order_success_msg, Toast.LENGTH_LONG).show();
        Intent menuActivity = new Intent(this, MenuActivity.class);
        menuActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(menuActivity);
    }

    @Override
    public void onCustomizeSelected() {
        Intent customizeActivity = new Intent(this, CustomizeActivity.class);
        startActivity(customizeActivity);
    }

    @Override
    public void onError(String errorTitle, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    private String formatPrice(float price) {
        return String.format("R$ %.2f", price);
    }
}