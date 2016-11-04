package com.ibs.tecnicos.seyte.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Preferences {
    private App app;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences (App app) {
        this.app = app;

        preferences = PreferenceManager.getDefaultSharedPreferences(this.app.getContext());
        editor = preferences.edit();
    }

    /**
     * Obtiene una preferencia de tipo entero por su clave
     *
     * @param key Clave de la preferencia
     * @param defaultValue Valor por defecto de la preferencia
     *
     * @return Valor entero de la preferencia
     */
    public int getInt (String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    /**
     * Guarda una preferencia de tipo entero
     *
     * @param key Clave que identifica a la preferencia
     * @param value Valor entero
     */
    public void setInt (String key, int value) {
        editor.putInt(key, value);
        save();
    }

    /**
     * Devuelve una preferencia de tipo boleano por su clave
     *
     * @param key Clave de la preferencia
     * @param defaultValue Valor por defecto para la preferencia
     *
     * @return Valor boleano de la preferencia
     */
    public boolean getBoolean (String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * Guarda una preferencia de tipo boleano
     *
     * @param key Clave que identifica a la preferencia
     * @param value Valor boleano
     */
    public void setBoolean (String key, boolean value) {
        editor.putBoolean(key, value);
        save();
    }

    /**
     * Devuelve una preferencia de tipo flotante por su clave
     *
     * @param key Clave de la preferencia
     * @param defaultValue Valor por defecto para la preferencia
     *
     * @return Valor flotante de la preferencia
     */
    public float getFloat (String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    /**
     * Guarda una preferencia de tipo flotante
     *
     * @param key Clave que identifica a la preferencia
     * @param value Valor flotante
     */
    public void setFloat (String key, float value) {
        editor.putFloat(key, value);
        save();
    }

    /**
     * Devuelve una preferencia de tipo cadena por su clave
     *
     * @param key Clave de la preferencia
     * @param defaultValue Valor por defecto para la preferencia
     *
     * @return Valor cadena de la preferencia
     */
    public String getString (String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /**
     * Guarda una preferencia de tipo cadena
     *
     * @param key Clave que identifica a la preferencia
     * @param value Valor cadena
     */
    public void setString (String key, String value) {
        editor.putString(key, value);
        save();
    }
    
    public void setSerializable(String key, Object object) {

        if (object == null) {
            this.setString(key, null);
            return;
        }

        boolean success = writeObjectToFile(app.getContext(), object, key + ".dat");
        if (success) {
            this.setString(key, key + ".dat");
        }

    }
    
    public Object getSerializable(String key, Object defaultValue) {

        String filePath = this.getString(key, null);

        if (filePath == null) {
            return null;
        }

        Object object = readObjectFromFile(app.getContext(), filePath);
        if (object == null) {
            return defaultValue;
        }

        return object;
    }

    private void save () {
        editor.commit();
    }

    private static boolean writeObjectToFile(Context context, Object object, String filename) {

        ObjectOutputStream objectOut = null;
        try {

            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();

        } catch (IOException e) {
            Log.e(null, e.getMessage());
            return false;
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    Log.e(null, e.getMessage());
                    return false;
                }
            }
        }

        return true;
    }


    /**
     *
     * @param context
     * @param filename
     * @return
     */
    private static Object readObjectFromFile(Context context, String filename) {

        ObjectInputStream objectIn = null;
        Object object = null;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            Log.e(null, e.getMessage());// Do nothing
            return null;
        } catch (IOException e) {
            Log.e(null, e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(null, e.getMessage());
            return null;
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    Log.e(null, e.getMessage());
                    return null;
                }
            }
        }

        return object;
    }
}