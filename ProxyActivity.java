package com.ibs.tecnicos.seyte.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class ProxyActivity extends android.app.Activity {
    private static OnActivityResultCallback activityResultCallback;

    private interface OnActivityResult {
        public void onResult(int requestCode, int resultCode, Intent data);
    }

    public static abstract class OnActivityResultCallback implements Serializable, OnActivityResult {}

    public ProxyActivity() {}

    public static void launchActivityForResult (App app, Intent launchIntent, OnActivityResultCallback callback) {
        activityResultCallback = callback;

        Intent i = new Intent(app.getContext(), ProxyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("intent", launchIntent);
        app.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(null, getIntent().getExtras().size() + "");

        Intent launchIntent = (Intent) getIntent().getParcelableExtra("intent");
        startActivityForResult(launchIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        activityResultCallback.onResult(requestCode, resultCode, data);
        finish();
    }
}
