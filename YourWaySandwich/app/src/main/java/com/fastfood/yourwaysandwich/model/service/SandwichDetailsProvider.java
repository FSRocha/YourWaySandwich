package com.fastfood.yourwaysandwich.model.service;

import com.fastfood.yourwaysandwich.model.Sandwich;

import java.util.List;

public interface SandwichDetailsProvider {

    Sandwich getSandwichDetails(int sandwichId);

    void addSandwichDetails(List<Sandwich> sandwichList);
}
