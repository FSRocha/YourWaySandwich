package com.fastfood.yourwaysandwich.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Implement retrofit interface with YourWay mobile API
 */
public interface YourWayMobileApi {

    String ENDPOINT = "http://10.0.2.2:8080";

    @GET("/api/lanche")
    Call<List<Sandwich>> getMenu();

    @GET("/api/ingrediente/de/{id_lanche}")
    Call<List<Ingredient>> getSandwichIngredients(@Path("id_lanche") int sandwichId);

    @GET("/api/lanche")
    Call<Sandwich> getSandwichDetails(@Query("id_lanche") int sandwichId);

    @GET("/api/ingrediente")
    Call<List<Ingredient>> getAvailableIngredients();

    @PUT("/api/pedido/{id_lanche}")
    Call<OrderedItem> orderSandwich(@Path("id_lanche") int sandwichId);

    @PUT("/api/pedido/{id_lanche}")
    Call<OrderedItem> orderCustomSandwich(@Path("id_lanche") int sandwichId, @Body Extras extras);

    @GET("/api/promocao")
    Call<List<Promotion>> getPromotions();

    @GET("/api/pedido")
    Call<List<OrderedItem>> getCart();

}
