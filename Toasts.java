package com.ibs.tecnicos.seyte.app;

import android.view.Gravity;

public class Toasts {
    private static android.widget.Toast theToast;
    private App app;

    public Toasts (App app) {
        this.app = app;

        if (this.theToast == null) {
            this.theToast = android.widget.Toast.makeText(this.app, "", android.widget.Toast.LENGTH_SHORT);
        }
    }

    private void show (String text, int duration) {
        this.theToast.setText(text);
        this.theToast.setDuration(duration);
        this.theToast.show();
    }

    private void show (String text, int duration, int gravity) {

        this.theToast.setGravity(gravity, 0, 0);
        this.show(text, duration);

    }

    public void showShort (String text) {
        this.show(text, android.widget.Toast.LENGTH_SHORT);
    }

    public void showLong (String text) {
        this.show(text, android.widget.Toast.LENGTH_LONG);
    }

    public void showLongCentered (String text) { this.show(text, android.widget.Toast.LENGTH_LONG, Gravity.CENTER);}
}
