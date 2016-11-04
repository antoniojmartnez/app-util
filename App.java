package com.ibs.tecnicos.seyte.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.ibs.tecnicos.seyte.api.ApiClient;
import com.ibs.tecnicos.seyte.util.EventBus;

public abstract class App extends Application {
    public static App instance;

    private EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        //activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        this.eventBus = new EventBus();

        this.init();
    }

    public abstract void init ();

    /**
     * Devuelve el contexto de la aplicación
     *
     * @return Contexto de la aplicación
     */
    public Context getContext(){
        return instance.getApplicationContext();
    }

    private Activity currentActivity = null;

    public Activity getCurrentActivity () {
        return this.currentActivity;
    }

    public void setCurrentActivity (Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /* -------------------------------------------------------- */

    public ApiClient apiClient () {
        return new ApiClient(this);
    }


    public Animations animations () {
        return new Animations(this);
    }

    public Cache cache () {
        return new Cache(this);
    }

    public Clipboard clipboard () {
        return new Clipboard(this);
    }

    public Connectivity connectivity () {
        return new Connectivity(this);
    }

    public Camera camera () {
        return new Camera(this);
    }

    public Dates dates () {
        return new Dates(this);
    }

    public Dialogs dialogs () { return new Dialogs(this); }

    public Display display () {
        return new Display(this);
    }

    public EventBus events() {
        return this.eventBus;
    }

    public Files files () {
        return new Files(this);
    }

    public Intents intents(Context context) { return new Intents(context); }

    public Keyboard keyboard () {
        return new Keyboard(this);
    }

    public Language language(Context baseContext) { return new Language(this, baseContext); }

    public Location location () {
        return new Location(this);
    }

    public Network network () {
        return new Network(this);
    }

    public Notifications notifications (int largeIconResourceId, int smallIconResourceId) {
        return new Notifications(largeIconResourceId, smallIconResourceId, this);
    }

    public PackageInfo packageInfo () {
        return new PackageInfo(this);
    }

    public Preferences preferences () {
        return new Preferences(this);
    }

    public Push push () {
        return new Push(this);
    }

    public Resource resource (Context baseContext) {
        return new Resource(this, baseContext);
    }

    public Security security () { return new Security(this); }

    public Toasts toasts () {
        return new Toasts(this);
    }

    public void runOnMain (Runnable r) {
        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(r);
    }

    public Class<?> getMainClass() {
        String packageName = this.getPackageName();
        Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
        String className =  launchIntent.getComponent().getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e("ClassName", e.getMessage());
        }
        return null;
    }

    /*public static void init () {
        //ApiClient.init();

        int appVersionCode = utils.getAppVersionCode();

        if (preferences.getInt("appVersionCode", 0) < appVersionCode) {
            preferences.setInt("appVersionCode", appVersionCode);
            App.cache.clean();
        }
    }

    public static void login(String login, String password, final ApiCallback callback) {
        ApiClient.login(login, password, new ApiCallback() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
                callback.onAll(data, null);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
                callback.onAll(null, error);
            }

            @Override
            public void onAll(Object data, String error) {

            }
        });
    }

    public static void logout () {
        ApiClient.logout();
        App.cache.clean();
    }

    public static boolean isLoggedIn () {
        return ApiClient.isLoggedIn();
    }*/

    /*public static void setEmptyView (AbsListView list, String emptyMessage) {
        LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View emptyView = inflater.inflate(R.layout.empty_list, null);

        TextView messageView = (TextView) emptyView.findViewById(R.id.emptyListMessageTextView);
        ImageView iconView = (ImageView) emptyView.findViewById(R.id.emptyListIconView);
        if (App.connectivity.isConnected()) {
            messageView.setText(emptyMessage);
            iconView.setImageResource(R.drawable.empty);
        } else {
            messageView.setText("No hay conexión a Internet");
            iconView.setImageResource(R.drawable.no_internet);
        }


        ((ViewGroup) list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);
    }*/
}
