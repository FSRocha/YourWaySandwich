package com.fastfood.yourwaysandwich.presenter;

import android.content.Context;

public interface MenuOperations {

    void createMenu(MenuCallbacks callbacks, Context context) throws NullPointerException;

    void destroyMenu();
}
