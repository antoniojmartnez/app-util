package com.ibs.tecnicos.seyte.app;

import android.app.*;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Location {
    private App app;

    private LocationManager locationManager;

    public Location (App app) {
        this.app = app;
        locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
    }

    public interface OnLocationCallback {
        public void onLocation (LatLng latlng);
        public void onError (String error);
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
     * Calcula la distancia en metros entre 2 posiciones geográficas (latitudes y longitudes)
     *
     * @param fromLat Latitud posición origen
     * @param fromLon Longitud posición origen
     * @param toLat Latitud posición destino
     * @param toLon Longitud posición destino
     *
     * @return Distancia en metros
     */
    public static double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }

    public LatLng getLocationByAddress(String street, String city) {

        Geocoder geocoder = new Geocoder(app);
        List<Address> address = null;

        try {
            address = geocoder.getFromLocationName(street + " " + city, 1);
        } catch (IOException e) {
            return null;
        }

        if (address.size() == 0) {
            return null;
        }

        return new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());

    }

    public boolean isLocationAvailable() {

        int availableProviders = locationManager.getProviders(true).size();

        return availableProviders == 0;
    }

    public android.location.Location getLastKnownLocation() {

        android.location.Location location = null;

        String[] providers = {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER};

        for(String provider : providers) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) { return location; }
        }

        return location;
    }

}
