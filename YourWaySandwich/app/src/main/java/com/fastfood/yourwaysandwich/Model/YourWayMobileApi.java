package com.fastfood.yourwaysandwich.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Implement retrofit interface with YourWay mobile API
 */
public interface YourWayMobileApi {

    String ENDPOINT = "http://localhost:8080/api";

    @GET("/lanche")
    Call<List<Sandwich>> getMenu();

    @GET("/ingrediente/de")
    Call<List<Ingredient>> getSandwichIngredients(@Query("id_lanche") int sandwichId);

    @GET("/lanche")
    Call<Sandwich> getSandwichDetails(@Query("id_lanche") int sandwichId);

    @GET("/ingrediente")
    Call<List<Ingredient>> getAvailableIngredients();

    @PUT("/pedido")
    Call<OrderedItem> orderSandwich(@Query("id_lanche") int sandwichId);

    @PUT("/pedido")
    Call<Boolean> orderCustSandwich(@Query("id_lanche") int sandwichId, @Body int[] extras);

    @GET("/promocao")
    Call<List<String>> getPromotions();

    @GET("/pedido")
    Call<List<Sandwich>> getCart();

}
