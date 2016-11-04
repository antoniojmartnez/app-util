package com.ibs.tecnicos.seyte.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera {
    private App app;

    private enum MediaType { PHOTO, VIDEO }

    public enum MediaQuality {
        LOW(0),
        HIGH(1);

        private final int value;

        private MediaQuality(final int value) {
            this.value = value;
        }

        public int getValue() { return this.value; }
    }

    public interface OnMediaCallback {
        public void onMedia(File file);
        public void onCancel();
    }

    public Camera(App app) {
        this.app = app;
    }

    private void captureMedia (final MediaType mediaType, final File file, MediaQuality quality, Integer maxDuration, final OnMediaCallback callback) {
        Intent captureIntent;
        if (mediaType == MediaType.PHOTO) {
            captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        } else {
            captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }

        if (captureIntent.resolveActivity(this.app.getPackageManager()) != null) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            if (mediaType == MediaType.VIDEO) {
                if (quality == null) quality = MediaQuality.HIGH;
                captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality.getValue());
                if (maxDuration != null) {
                    captureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxDuration);
                }
            }

            ProxyActivity.launchActivityForResult(app, captureIntent, new ProxyActivity.OnActivityResultCallback() {
                @Override
                public void onResult(int requestCode, int resultCode, Intent data) {
                    if (resultCode == Activity.RESULT_OK) {
                        callback.onMedia(file);
                    } else {
                        file.delete();
                        callback.onCancel();
                    }
                }
            });
        }
    }

    private void captureMedia (MediaType mediaType, String fileName, MediaQuality quality, Integer maxDuration, OnMediaCallback callback) {
        File storageDir;
        String dir;

        if (mediaType == MediaType.PHOTO) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } else {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        }
        dir += "/" + this.app.packageInfo().getPackageName();

        storageDir = new File(dir);
        storageDir.mkdirs();

        File mediaFile = null;
        try {
            mediaFile = new File(storageDir.getAbsolutePath() + "/" + fileName);

            if (mediaFile.createNewFile()) {
                this.captureMedia(mediaType, mediaFile, quality, maxDuration, callback);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void captureMedia (MediaType mediaType, MediaQuality quality, Integer maxDuration, OnMediaCallback callback) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String extension = (mediaType == MediaType.PHOTO) ? ".jpg" : ".mp4";
        String imageFileName = timeStamp + extension;

        this.captureMedia(mediaType, imageFileName, quality, maxDuration, callback);
    }

    public void capturePhoto (File file, OnMediaCallback callback) {
       this.captureMedia(MediaType.PHOTO, file, null, null, callback);
    }

    private void capturePhoto (String filePath, OnMediaCallback callback) {
        this.captureMedia(MediaType.PHOTO, filePath, null, null, callback);
    }

    public void capturePhoto (OnMediaCallback callback) {
        this.captureMedia(MediaType.PHOTO, null, null, callback);
    }

    public void captureVideo (File file, MediaQuality quality, Integer maxDuration, OnMediaCallback callback) {
        this.captureMedia(MediaType.VIDEO, file, quality, maxDuration, callback);
    }

    private void captureVideo (String filePath, MediaQuality quality, Integer maxDuration, OnMediaCallback callback) {
        this.captureMedia(MediaType.VIDEO, filePath, quality, maxDuration, callback);
    }

    public void captureVideo (MediaQuality quality, Integer maxDuration, OnMediaCallback callback) {
        this.captureMedia(MediaType.VIDEO, quality, maxDuration, callback);
    }
}
