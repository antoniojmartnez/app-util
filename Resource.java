package com.ibs.tecnicos.seyte.app;

import android.content.Context;

/**
 * Created by ajmartinez on 14/09/2015.
 */
public class Resource {

    private App app;
    private Context context;

    public Resource(App app, Context context) {
        this.app = app;
        this.context = context;
    }

    private int get(String name, String type) {

        return context.getResources().getIdentifier(name, type, context.getPackageName());

    }

    public int getString(String name) {

        return this.get(name, "string");

    }

}
