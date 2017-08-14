package com.fastfood.yourwaysandwich;

import com.fastfood.yourwaysandwich.model.AvailableIngredientsProvider;
import com.fastfood.yourwaysandwich.model.Ingredient;
import com.fastfood.yourwaysandwich.model.PromotionsCalculator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PromotionsCalculatorTest {

    Ingredient mLettuce;
    Ingredient mBacon;
    Ingredient mBeef;
    Ingredient mEgg;
    Ingredient mCheese;
    Ingredient mBread;

    AvailableIngredientsProvider mIngredientsProviderMock = new IngredientsProviderMock();

    @Before
    public void setup() {
        mLettuce = new Ingredient();
        mLettuce.id = 1;
        mLettuce.name = "Alface";
        mLettuce.price = 0.4f;
        mLettuce.image = "https://goo.gl/9DhCgk";

        mBacon = new Ingredient();
        mBacon.id = 2;
        mBacon.name = "Bacon";
        mBacon.price = 2f;
        mBacon.image = "https://goo.gl/8qkVH0";

        mBeef = new Ingredient();
        mBeef.id = 3;
        mBeef.name = "Hamburguer de Carne";
        mBeef.price = 3f;
        mBeef.image = "https://goo.gl/U01SnT";

        mEgg = new Ingredient();
        mEgg.id = 4;
        mEgg.name = "Ovo";
        mEgg.price = 0.8f;
        mEgg.image = "https://goo.gl/weL1Rj";

        mCheese = new Ingredient();
        mCheese.id = 5;
        mCheese.name = "Queijo";
        mCheese.price = 1.5f;
        mCheese.image = "https://goo.gl/D69Ow2";

        mBread = new Ingredient();
        mBread.id = 6;
        mBread.name = "Pão com gergelim";
        mBread.price = 1f;
        mBread.image = "https://goo.gl/evgjyj";

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(mBread);
        ingredients.add(mBacon);
        ingredients.add(mBeef);
        ingredients.add(mCheese);
        mIngredientsProviderMock.setAvailableIngredients(ingredients);
    }

    @Test
    public void calculatePrice_x_bacon_no_promotion() throws Exception {
        PromotionsCalculator calculator = new PromotionsCalculator(mIngredientsProviderMock);
        List<Integer> ingredients = new ArrayList<>();
        ingredients.add(6);
        ingredients.add(2);
        ingredients.add(3);
        ingredients.add(5);

        List<Integer> extras = new ArrayList<>();

        assertEquals(7.5f, calculator.calculatePrice(ingredients, extras), 0);
    }

    @Test
    public void calculatePrice_no_promotion() throws Exception {
        PromotionsCalculator calculator = new PromotionsCalculator(mIngredientsProviderMock);
        List<Integer> ingredients = new ArrayList<>();
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        List<Integer> extras = new ArrayList<>();

        assertEquals(5f, calculator.calculatePrice(ingredients, extras), 0);
    }

    @Test
    public void calculatePrice_light_promotion() throws Exception {
        PromotionsCalculator calculator = new PromotionsCalculator(mIngredientsProviderMock);
        List<Integer> ingredients = new ArrayList<>();
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(1);

        List<Integer> extras = new ArrayList<>();

        assertEquals(4f, calculator.calculatePrice(ingredients, extras), 0);
    }

    @Test
    public void calculatePrice_light_promotion_with_extras() throws Exception {
        PromotionsCalculator calculator = new PromotionsCalculator(mIngredientsProviderMock);
        List<Integer> ingredients = new ArrayList<>();
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(6);
        ingredients.add(1);

        List<Integer> extras = new ArrayList<>();
        extras.add(4);

        assertEquals(4.68f, calculator.calculatePrice(ingredients, extras), 0);
    }


    class IngredientsProviderMock implements AvailableIngredientsProvider {

        List<Ingredient> mIngredientsList;

        @Override
        public List<Ingredient> getAvailableIngredients() {
            return mIngredientsList;
        }

        @Override
        public void setAvailableIngredients(List<Ingredient> ingredients) {
            mIngredientsList = ingredients;
        }
    }
}