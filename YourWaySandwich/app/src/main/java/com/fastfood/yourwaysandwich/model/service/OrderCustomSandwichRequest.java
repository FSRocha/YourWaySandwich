package com.fastfood.yourwaysandwich.model.service;

import java.util.List;

class OrderCustomSandwichRequest extends Request {

    private int sandwichId;

    private List<Integer> extras;

    OrderCustomSandwichRequest(int sandwichId, List<Integer> extras) {
        super(RequestType.ORDER_CUSTOM_SANDWICH);
        this.sandwichId = sandwichId;
        this.extras = extras;
    }

    public int getSandwichId() {
        return sandwichId;
    }

    public List<Integer> getExtras() {
        return extras;
    }
}
