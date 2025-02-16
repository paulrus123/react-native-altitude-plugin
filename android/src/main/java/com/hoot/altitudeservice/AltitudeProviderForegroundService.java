package com.hoot.altitudeservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AltitudeProviderForegroundService extends Service implements SensorEventListener {
    private static final String CHANNEL_ID = "AltitudeServiceChannel";
    private Handler handler;
    private Runnable runnable;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private float currentPressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

    @Override
    public void onCreate() {
        Log.i("AltitudeProviderForegroundService", "onCreate");
        super.onCreate();
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Altitude Service")
                .setContentText("Monitoring altitude in background")
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Using a system icon instead
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("AltitudeProviderForegroundService", "run");
                float altitude = calculateAltitude();

                ReactContext reactContext = ((MainApplication) getApplication()).getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
                if (reactContext != null) {
                    reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("AltitudeUpdated", altitude);
                }

                handler.postDelayed(this, 1000); // Adjust the interval as needed
            }
        };
        handler.post(runnable);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        
        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (BuildConfig.DEBUG) {
            Log.d("AltitudeService", "Detailed debug information");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("AltitudeProviderForegroundService", "onSensorChanged");
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            currentPressure = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
    }

    private float calculateAltitude() {
        // Using the barometric formula: h = (1 - (P/P0)^0.190284) * 145366.45
        // where P is the current pressure and P0 is the reference pressure at sea level
        return SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, currentPressure);
    }

    private void createNotificationChannel() {
        Log.i("AltitudeProviderForegroundService", "createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Altitude Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Service will continue running until explicitly stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}