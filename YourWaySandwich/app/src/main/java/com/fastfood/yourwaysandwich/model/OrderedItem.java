package com.fastfood.yourwaysandwich.model;

import java.util.List;

public class OrderedItem {

    public int id;
    public int id_sandwich;
    public List<Integer> extras;
    public long date;

    public int getId_sandwich() {
        return id_sandwich;
    }

    public List<Integer> getExtras() {
        return extras;
    }
}
