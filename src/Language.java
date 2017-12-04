import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by seyte on 07/05/2014.
 */
public class Language {
    private App app;
    private Context baseContext;

    public Language(App app, Context baseContext) {

        this.app = app;
        this.baseContext = baseContext;
    }


    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase("")) {
            return;
        }
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        //Esto no funciona, EXPLOTA!! No le gusta el getBaseContext de domibi
        Resources resources = this.baseContext.getResources();
        ///////////////////////////////////////////////////////////////////
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, displayMetrics);
    }
}
