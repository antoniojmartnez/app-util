package com.ibs.tecnicos.seyte.app;

public class Callbacks {
    public interface SimpleCallback {
        public void onCallback(Object data);
    }

    public interface ConfirmCallback {
        public void onSuccess(Object data);
        public void onError(Object error);
    }
}