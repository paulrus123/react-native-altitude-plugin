// android/app/src/main/java/com/hoot/cycle/AltitudeServicePackage.java
package com.hoot.altitudeservice;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.hoot.altitudeservice.AltitudeServiceModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.util.Log;


public class AltitudeServicePackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        Log.i("AltitudeServicePackage", "createNativeModules");
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AltitudeServiceModule(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        Log.i("AltitudeServicePackage", "createViewManagers");
        return Collections.emptyList();
    }
}