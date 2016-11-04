package com.ibs.tecnicos.seyte.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Keyboard {
    private App app;

    public Keyboard (App app) {
        this.app = app;
    }


    /**
     * Oculta el teclado virtual
     *
     * @param a Activity actual
     */
    public void hide (Activity a) {
        InputMethodManager inputManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = a.getCurrentFocus();
        if (v == null) return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Muestra el teclado virtual
     *
     *  @param e EditText vinculada
     */
    public void show (EditText e) {
        InputMethodManager inputManager = (InputMethodManager) this.app.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        e.setFocusable(true);
        e.setFocusableInTouchMode(true);
        e.requestFocus();
        inputManager.showSoftInput(e, InputMethodManager.SHOW_IMPLICIT);
    }
}
