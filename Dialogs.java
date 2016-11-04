package com.ibs.tecnicos.seyte.app;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class Dialogs {
    private App app;

    private static final int DEFAULT_THEME = AlertDialog.THEME_HOLO_LIGHT;

    public Dialogs (App app) {
        this.app = app;
    }

    public interface InfoDialogCallback {
        public void onOk ();
    }

    public interface ConfirmDialogCallback {
        public void onResult (boolean result);
    }

    public interface SpinnerDialogCallback {
        public void onResult (String option);
    }

    public interface InputDialogCallback {
        public void onResult (String text);
        public void onCancel ();
    }

    public interface NumberPickerDialogCallback {
        public void onResult (int value);
        public void onCancel ();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void info (Context context, String text, final InfoDialogCallback callback) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onOk();
            }
        };

        AlertDialog.Builder builder;
        try {
            builder =  new AlertDialog.Builder(context, DEFAULT_THEME);
        } catch (NoSuchMethodError e) {
            Log.e(null, "Older SDK, using old Builder");
            builder =  new AlertDialog.Builder(context);
        }
        builder.setMessage(text)
                .setNeutralButton("Ok", dialogClickListener)
                .show();
    }

    public void info (Context context, String text) {
        this.info(context, text, new InfoDialogCallback() {
            @Override
            public void onOk() {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void confirm (Context context, String text, String positiveString, String negativeString, final ConfirmDialogCallback callback) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onResult(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        callback.onResult(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context, DEFAULT_THEME);
        builder.setCancelable(false);
        builder.setMessage(text)
                .setPositiveButton(positiveString, dialogClickListener)
                .setNegativeButton(negativeString, dialogClickListener)
                .show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void confirm (Context context, String title, String text, String positiveString, String negativeString, boolean cancelable, final ConfirmDialogCallback callback) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        callback.onResult(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        callback.onResult(false);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context, DEFAULT_THEME);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.setPositiveButton(positiveString, dialogClickListener);
        builder.setNegativeButton(negativeString, dialogClickListener);

        builder.show();

    }

    public void yesNo (Context context, String text, ConfirmDialogCallback callback) {
        confirm(context, text, "Si", "No", callback);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void spinner (Context context, String title, final String[] options, final SpinnerDialogCallback callback) {
        AlertDialog.Builder builder;
        try {
            builder =  new AlertDialog.Builder(context, DEFAULT_THEME);
        } catch (NoSuchMethodError e) {
            Log.e(null, "Older SDK, using old Builder");
            builder =  new AlertDialog.Builder(context);
        }
        //AlertDialog.Builder builder = new AlertDialog.Builder(context, DEFAULT_THEME);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callback.onResult(options[which]);
            }
        });

        builder.show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void input (Context context, String title, String hint, int inputType, String buttonText, final InputDialogCallback callback) {
        AlertDialog.Builder builder;
        try {
            builder =  new AlertDialog.Builder(context, DEFAULT_THEME);
        } catch (NoSuchMethodError e) {
            Log.e(null, "Older SDK, using old Builder");
            builder =  new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setCancelable(true);

        final EditText input = new EditText(context);
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        input.setInputType(inputType);
        input.setHint(hint);
        builder.setView(input);

        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResult(input.getText().toString());
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callback.onCancel();
            }
        });

        builder.show();

        input.requestFocus();
        app.keyboard().show(input);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void input (Context context, String title, String hint, String buttonText, final InputDialogCallback callback) {
        AlertDialog.Builder builder;
        try {
            builder =  new AlertDialog.Builder(context, DEFAULT_THEME);
        } catch (NoSuchMethodError e) {
            Log.e(null, "Older SDK, using old Builder");
            builder =  new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setCancelable(true);

        final EditText input = new EditText(context);
        input.setHint(hint);
        builder.setView(input);

        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResult(input.getText().toString());
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callback.onCancel();
            }
        });

        builder.show();

        app.keyboard().show(input);
    }

    public void numberPicker (Context context, String title, String prompt, int min, int max, int step, String buttonText, final NumberPickerDialogCallback callback) {

        this.numberPicker(context, title, prompt, min, max, step, min, buttonText, callback);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void numberPicker (Context context, String title, String prompt, int min, int max, int step, int init, String buttonText, final NumberPickerDialogCallback callback) {
        AlertDialog.Builder builder;
        try {
            builder =  new AlertDialog.Builder(context, DEFAULT_THEME);
        } catch (NoSuchMethodError e) {
            Log.e(null, "Older SDK, using old Builder");
            builder =  new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setMessage(prompt);
        builder.setCancelable(true);

        ArrayList<String> valuesList = new ArrayList<String>();
        int initIndex = 0;
        int i = 0;
        for (int v = min; v <= max; v += step) {
            valuesList.add(v + "");
            if (v == init) {
                initIndex = i;
            }
            i++;
        }

        final NumberPicker numberPicker = new NumberPicker(context);
        final String[] values = new String[valuesList.size()];
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(valuesList.toArray(values));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(valuesList.size() - 1);
        numberPicker.setValue(initIndex);

        builder.setView(numberPicker);

        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onResult(Integer.parseInt(values[numberPicker.getValue()]));
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                callback.onCancel();
            }
        });

        builder.show();
    }
}
