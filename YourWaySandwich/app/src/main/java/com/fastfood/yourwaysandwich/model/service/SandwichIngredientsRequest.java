package com.fastfood.yourwaysandwich.model.service;

class SandwichIngredientsRequest extends Request {

    private int sandwichId;

    SandwichIngredientsRequest(int sandwichId) {
        super(RequestType.GET_SANDWICH_INGREDIENTS);
        this.sandwichId = sandwichId;
    }

    public int getSandwichId() {
        return sandwichId;
    }
}
