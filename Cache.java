package com.ibs.tecnicos.seyte.app;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Cache {
    private App app;
    private File cacheDir;

    public Cache (App app) {
        this.app = app;

        this.cacheDir = this.app.getContext().getCacheDir();
    }

    private String makeSlug (String input) {
        Pattern NONLATIN = Pattern.compile("[^\\w-]");
        Pattern WHITESPACE = Pattern.compile("[\\s]");

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Añade un objeto a la caché. El objeto debe implementar la interfaz Serializable de Java
     *
     * @param key Clave para identificar al objeto
     * @param data El objeto que se desea almacenar
     */
    public void put (String key, Serializable data) {
        if (data == null) return;

        key = this.makeSlug(key);

        try {
            File file = new File(this.cacheDir, key + ".dat");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e(null, "PutCache: FileNotFoundException");
        } catch (IOException e) {
            Log.e(null, "PutCache: IOException");
        }
    }

    /**
     * Devuelve un objeto de la caché si existe
     *
     * @param key
     *
     * @return El objeto si existe o null en caso contrario
     */
    public Object get (String key) {
        key = this.makeSlug(key);

        try {
            Object o;

            File file = new File(this.cacheDir, key + ".dat");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            o = ois.readObject();
            ois.close();
            fis.close();

            return o;
        } catch (FileNotFoundException e) {
            //Log.e(null, "GetCache: FileNotFoundException");
            return null;
        } catch (ClassNotFoundException e) {
            //Log.e(null, "GetCache: ClassNotFoundException");
            return null;
        } catch (OptionalDataException e) {
            //Log.e(null, "GetCache: OptionalDataException");
            return null;
        } catch (IOException e) {
            //Log.e(null, "GetCache: IOException");
            return null;
        }
    }

    /**
     * Comprueba si existe una objeto en la cache para una clave dada
     *
     * @param key Clave del objeto
     *
     * @return Duración en segundos del objeto, o null si no existe
     */
    public Integer exists (String key) {
        key = this.makeSlug(key);

        File file = new File(this.cacheDir, key + ".dat");
        if (file.exists()) {
            long timeFile = file.lastModified();
            long timeNow = new Date().getTime();

            int res = (int) (timeNow - timeFile);
            if (res >= 1000) {
                res = res / 1000;
            } else {
                res = 0;
            }
            return res;
        } else {
            return null;
        }
    }

    /**
     * Vacía completamente la caché
     */
    public void clean () {
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
