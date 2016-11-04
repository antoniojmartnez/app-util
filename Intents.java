package com.ibs.tecnicos.seyte.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Seyte on 31/10/2014.
 */
public class Intents {

    private Context context;

    public class ExtendedIntent extends Intent {

        private Context context;
        private Class<?> cls;

        public ExtendedIntent(Context context, Class<?> cls) {
            super(context, cls);
            this.context = context;
            this.cls = cls;
        }

        public ExtendedIntent putExtra(String name, Serializable value) {
            super.putExtra(name, value);
            return this;
        }

        public ExtendedIntent addFlags(int flags) {
            super.addFlags(flags);
            return this;
        }

        public void start() {

            if (Activity.class.isAssignableFrom(this.cls)) {
                context.startActivity(this);
            } else if (Service.class.isAssignableFrom(this.cls)) {
                context.startService(this);
            }

        }

        public void startForResult(int requestedCode) {
            ((Activity) context).startActivityForResult(this, requestedCode);
        }

        public void stop() {

            if (Service.class.isAssignableFrom(this.cls)) {
                context.stopService(this);
            }

        }
    }

    public Intents(Context context) {
        this.context = context;
    }

    public  ExtendedIntent with(Class<?> cls) {
        return new ExtendedIntent(context, cls);
    }

    public void goWithGMaps(LatLng latLng, String locationName) {
        String geoUri = "http://maps.google.com/maps?q=loc:"
                + latLng.latitude + "," + latLng.longitude + " (" + locationName + ")";
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri)));
    }

//    public void goWithGMaps(double lat, double log) {
//
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?daddr="
//                        + String.valueOf(lat) + "," + String.valueOf(log)));
//        context.startActivity(intent);
//    }

    public void goToDial(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public void goToMail(String to) {
//        /* Create the Intent */
//        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//        /* Fill it with Data */
//        emailIntent.setType("plain/text");
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ to });
////        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
////        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
//        /* Send it off to the Activity-Chooser */
////        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//        context.startActivity(emailIntent);
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=&body=&to=" + to);
        testIntent.setData(data);
        context.startActivity(testIntent);

    }

    public void goToURL(String url) {

        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

    }

    public void open(File file) {
        String path = file.getAbsolutePath();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        context.startActivity(intent);
    }
}
