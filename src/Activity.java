import android.os.Bundle;

public class Activity extends android.app.Activity {
    protected App app;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = (App) this.getApplicationContext();
    }

    protected void onResume() {
        super.onResume();
        this.app.setCurrentActivity(this);
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = this.app.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            this.app.setCurrentActivity(null);
    }
}
