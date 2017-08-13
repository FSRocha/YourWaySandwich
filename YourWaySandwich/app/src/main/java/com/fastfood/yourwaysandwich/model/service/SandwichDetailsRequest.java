package com.fastfood.yourwaysandwich.model.service;

class SandwichDetailsRequest extends Request {

    private int sandwichId;

    SandwichDetailsRequest(int sandwichId) {
        super(RequestType.GET_SANDWICH_DETAILS);
        this.sandwichId = sandwichId;
    }

    public int getSandwichId() {
        return sandwichId;
    }
}
