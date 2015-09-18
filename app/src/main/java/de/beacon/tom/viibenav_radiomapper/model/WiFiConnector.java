package de.beacon.tom.viibenav_radiomapper.model;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by TomTheBomb on 01.08.2015.
 */
public class WiFiConnector {

    public static final String TAG = "WiFiConnector";
    private WifiManager wifi;

    private static WiFiConnector singleton;

    private WiFiConnector(WifiManager wifi) {
        this.wifi = wifi;
    }

    public static WiFiConnector getConnector(Activity a){
        // Avoid possible errors with multiple threads accessing this method -> synchronized
        synchronized(WiFiConnector.class) {
            if (singleton == null) {
                WifiManager wifi = (WifiManager) a.getSystemService(a.WIFI_SERVICE);
                singleton = new WiFiConnector(wifi);
            }
        }
        return singleton;
    }

    public void enableWiFi(){
        Log.d(TAG, "Enable WiFi");
        wifi.setWifiEnabled(true);
    }

    public void disableWiFi(){
        wifi.setWifiEnabled(false);
    }

    public boolean WiFiEnabled(){
        return wifi.isWifiEnabled();
    }
}
