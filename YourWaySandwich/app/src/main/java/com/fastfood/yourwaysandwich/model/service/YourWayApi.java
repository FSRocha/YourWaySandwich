package com.fastfood.yourwaysandwich.model.service;


/**
 * Interface to wrap the access to the Your Way API services into asynchronous calls
 **/
interface YourWayApi {

    /**
     * Request all available sandwiches
     */
    void getMenu();

    /**
     * Request the ingredients list of the specified sandwich
     *
     * @param sandwichId The sandwich id to request its ingredients
     */
    void getSandwichIngredients(int sandwichId);

    /**
     * Request the details of the specified sandwich
     *
     * @param sandwichId The sandwich id to request its details
     */
    void getSandwichDetails(int sandwichId);

    /**
     * Request all available ingredients
     */
    void getAvailableIngredients();

    /**
     * Add the specified sandwich to the user cart
     *
     * @param sandwichId The sandwich id to be included in the cart
     */
    void orderSandwich(int sandwichId);

    /**
     * Add the specified sandwich which was customized to the user cart
     *
     * @param sandwichId The sandwich id which was customized to be
     * @param extras The list of extra ingredients to be added
     */
    void orderCustomSandwich(int sandwichId, int[] extras);

    /**
     * Request the promotion descriptions currently available
     */
    void getPromotions();

    /**
     * Request the current user cart
     */
    void getCart();

}
