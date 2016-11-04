package com.ibs.tecnicos.seyte.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        App.instance.push().getCallback().onPush(intent);
    }
}