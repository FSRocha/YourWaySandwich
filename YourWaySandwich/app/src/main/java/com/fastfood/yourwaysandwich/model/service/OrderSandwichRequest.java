package com.fastfood.yourwaysandwich.model.service;

class OrderSandwichRequest extends Request {

    private int sandwichId;

    OrderSandwichRequest(int sandwichId) {
        super(RequestType.ORDER_SANDWICH);
        this.sandwichId = sandwichId;
    }

    public int getSandwichId() {
        return sandwichId;
    }
}
