import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/*
    Debes incluir el paquete de Play Services en las dependencias de Gradle
*/

public class Push {
    private App app;

    private static OnPushNotificationCallback callback;

    private static final String PROPERTY_USER_ID = "domibi.utils.push.userId";
    private static final String PROPERTY_REGISTERED_APP_VERSION = "domibi.utils.push.appVersion";
    private static final String PROPERTY_REGISTRATION_ID = "domibi.utils.push.registrationId";

    private static final String SENDER_ID = "790656611550";

    private GoogleCloudMessaging gcm;

    public interface OnPushRegisterCallback {
        public void onRegister(String regId);
    }

    public interface OnPushNotificationCallback {
        public void onPush(Intent intent);
    }

    public Push(App app) {
        this.app = app;

        gcm = GoogleCloudMessaging.getInstance(this.app.getContext());
    }

    /**
     * Comprueba si las notificaciones PUSH están disponibles en el dispositivo
     *
     * @return true si están disponibles, false en caso contrario
     */
    public boolean isAvailable () {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.app.getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                this.app.toasts().showLong("Debes instalar Google Play Services para disfrutar de ciertas funciones en esta aplicación");
                /*GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();*/
            }
            return false;
        }
        return true;
    }

    /**
     * Registra el dispositivo en Google Cloud Messaging si es necesario
     *
     * @param callback Se llama con el identificador de registro
     */
    public void register (final OnPushRegisterCallback callback) {
        if ( ! isAvailable()) return;

        String regId = this.getRegistrationId();
        if (regId == null) {
            this.registerInBackground(new Callbacks.SimpleCallback() {
                @Override
                public void onCallback(Object data) {
                    callback.onRegister((String) data);
                }
            });
            Log.d("Push", "Registrando en Push...");
        } else {
            callback.onRegister(regId);
            Log.d("Push", "Ya se está registrado en Push con el ID: " + regId);
        }
    }

    private String getRegistrationId () {
        // Comprobamos si la aplicación se ha actualizado. Si es así ignoramos el identificador
        // guardado y volvemos a obtener uno nuevo.
        // No hay garantía de que el anterior siga funcionando.
        int registeredAppVersion = this.app.preferences().getInt(PROPERTY_REGISTERED_APP_VERSION, 0);
        if (registeredAppVersion < this.app.packageInfo().getVersionCode()) {
            return null;
        }

        String registrationId = this.app.preferences().getString(PROPERTY_REGISTRATION_ID, null);
        return registrationId;
    }

    private void registerInBackground (final Callbacks.SimpleCallback callback) {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                try {
                    String regId = gcm.register(SENDER_ID);

                    // Enviamos el identificador a nuestro backend
                    callback.onCallback(regId);

                    storeRegistrationId(regId);
                } catch (IOException ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }.execute();
    }

    private void storeRegistrationId (String regId) {
        this.app.preferences().setString(PROPERTY_REGISTRATION_ID, regId);
        this.app.preferences().setInt(PROPERTY_REGISTERED_APP_VERSION, this.app.packageInfo().getVersionCode());
    }

    public void registerCallback (OnPushNotificationCallback c) {
        callback = c;
    }

    public OnPushNotificationCallback getCallback () {
        return callback;
    }
}
