package com.ibs.tecnicos.seyte.app;

import android.annotation.SuppressLint;
import android.content.Context;

public class Clipboard {
    private App app;

    public Clipboard (App app) {
        this.app = app;
    }

    /**
     * Copia una cadena de texto al portapapeles
     *
     * @param text Texto
     */
    @SuppressLint("NewApi")
    public void setText (String text) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.app.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.app.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("",text);
            clipboard.setPrimaryClip(clip);
        }
    }
}