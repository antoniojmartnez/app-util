package com.ibs.tecnicos.seyte.app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Files {
    private App app;

    public static enum StorageType {Internal, External};
    private static final int EXTERNAL_STORAGE = 1;

    public interface FileDownloadCallback {
        public void onSuccess (File file);
        public void onError (String error);
    }

    public Files (App app) {
        this.app = app;
    }

    /**
     * Devuelve la ruta absoluta de un archivo dada una ruta relativa y el tipo de almacenamiento
     *
     * @param filePath Ruta relativa del archivo
     * @param storageType Tipo de almacenamiento
     *
     * @return Ruta absoluta del archivo
     */
    public String getAbsolutePath (String filePath, StorageType storageType) {
        if (storageType == StorageType.Internal) {
            filePath = this.app.getContext().getFilesDir().getAbsolutePath() + "/" + filePath;
        } else {
            filePath = this.app.getContext().getExternalFilesDir(null).getAbsolutePath() + "/" + filePath;
        }

        return filePath;
    }

    /**
     * Descarga un archivo a la memoria interna del dispositivo
     *
     * @param url URL del archivo a descargar
     * @param fileName Nombre del archivo destino
     * @param storageType Tipo de almacenamiento
     * @param callback Callback. Se llama a execute
     */
    public void download (final String url, final String fileName, final StorageType storageType, final FileDownloadCallback callback) {
        AsyncTask downloadTask = new AsyncTask() {
            boolean ok = true;
            String error;
            File theFile;

            @Override
            protected Object doInBackground(Object[] params) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url2 = new URL(url);
                    connection = (HttpURLConnection) url2.openConnection();
                    connection.connect();

                    // Expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        this.ok = false;
                        this.error = "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                    }

                    // This will be useful to display download percentage
                    // might be -1: server did not report the length
                    int fileLength = connection.getContentLength();

                    // Download the file
                    input = connection.getInputStream();
                    String filePath = getAbsolutePath(fileName, storageType);
                    output = new FileOutputStream(filePath);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // Allow canceling with back button
                        /*if (isCancelled()) {
                            input.close();
                            return null;
                        }*/
                        total += count;

                        // Publishing the progress....
                        if (fileLength > 0) // Only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }

                    this.theFile = new File(filePath);
                } catch (Exception e) {
                    this.ok = false;
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }

                return null;
                /*URL website = null;
                try {
                    website = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                ReadableByteChannel rbc = null;
                try {
                    rbc = Channels.newChannel(website.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String filePath = getAbsolutePath(fileName, storageType);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    callback.onCallback(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;*/
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (this.ok) {
                    callback.onSuccess(theFile);
                } else {
                    callback.onError(this.error);
                }
            }
        };

        downloadTask.execute();
    }

    /**
     * Devuelve el contenido de un archivo
     *
     * @param fileName Nombre del archivo
     * @param storageType Tipo de almacenamiento
     * @param encoding Codificación
     *
     * @return Contenido del archivo
     */
    public String getContents (String fileName, StorageType storageType, String encoding) {
        String filePath = this.getAbsolutePath(fileName, storageType);

        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        try {
            BufferedReader br = new BufferedReader( new InputStreamReader(fis, encoding) );
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getFile (String fileName, StorageType storageType) {
        File file = new File(this.getAbsolutePath(fileName, storageType));
        return file;
    }
}
