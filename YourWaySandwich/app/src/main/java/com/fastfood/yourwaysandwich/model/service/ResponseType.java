package com.fastfood.yourwaysandwich.model.service;

/**
 * Types of responses expected when calling Your Way API services
 */
enum ResponseType {

    MENU,
    SANDWICH_INGREDIENTS,
    SANDWICH_DETAILS,
    AVAILABLE_INGREDIENTS,
    ORDERED_SANDWICH,
    ORDERED_CUSTOM_SANDWICH,
    PROMOTIONS,
    CART,
    ERROR
}