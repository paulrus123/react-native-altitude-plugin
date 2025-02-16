package com.hoot.altitudeservice;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hoot.altitudeservice.AltitudeProviderForegroundService;
import android.util.Log;

public class AltitudeServiceModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AltitudeServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        Log.i("AltitudeService", "Initializing service");
    }

    @Override
    public String getName() {
        return "AltitudeService";
    }

    @ReactMethod
    public void startService() {
        Log.i("AltitudeService", "Starting service");
        Intent serviceIntent = new Intent(reactContext, AltitudeProviderForegroundService.class);
        reactContext.startForegroundService(serviceIntent);
    }

    @ReactMethod
    public void stopService() {
        Log.i("AltitudeService", "Stopping service");
        Intent serviceIntent = new Intent(reactContext, AltitudeProviderForegroundService.class);
        reactContext.stopService(serviceIntent);
    }

        // Add this method to support event emission
    @ReactMethod
    public void addListener(String eventName) {
        // Keep: Required for RN built in Event Emitter Calls.        
        Log.i("AltitudeService", "Added listener for " + eventName);

    }

    // Add this method to support event emission
    @ReactMethod
    public void removeListeners(Integer count) {
        // Keep: Required for RN built in Event Emitter Calls.
        Log.i("AltitudeService", "Removed " + count + " listener(s)");

    }
}