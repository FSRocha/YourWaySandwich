package com.fastfood.yourwaysandwich.model.service;

class Request {

    RequestType type;

    Request(RequestType type){
        this.type = type;
    }

    RequestType getType(){
        return this.type;
    }
}
