package com.ibs.tecnicos.seyte.app;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Display {
    private App app;

    public Display (App app) {
        this.app = app;
    }

    /**
     * Convierte de DP a PX
     *
     * @param dp Valor en DP
     *
     * @return Valor en PX
     */
    public float dpToPixels (float dp) {
        Resources resources = this.app.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);

        return px;
    }

    /**
     * Convierte de PX a DP
     *
     * @param px Valor en PX
     *
     * @return Valor en DP
     */
    public float pixelsToDp (float px) {
        Resources resources = this.app.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);

        return dp;
    }
}