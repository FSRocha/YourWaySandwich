package com.fastfood.yourwaysandwich.model.service;

class OrderCustomSandwichRequest extends Request {

    private int sandwichId;

    private int[] extras;

    OrderCustomSandwichRequest(int sandwichId, int[] extras) {
        super(RequestType.ORDER_CUSTOM_SANDWICH);
        this.sandwichId = sandwichId;
        this.extras = extras;
    }

    public int getSandwichId() {
        return sandwichId;
    }

    public int[] getExtras() {
        return extras;
    }
}
