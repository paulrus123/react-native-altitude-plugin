import React, { useEffect, useState } from 'react';
import { StyleSheet, View, Text, Button, Platform } from 'react-native';
import AltitudeService from 'react-native-altitude-plugin';
import * as Location from 'expo-location';

export default function App() {
  const [altitude, setAltitude] = useState(null);
  const [isTracking, setIsTracking] = useState(false);
  const [hasPermission, setHasPermission] = useState(false);
  const [unsubscribe, setUnsubscribe] = useState(null);

  useEffect(() => {
    checkPermissions();
  }, []);

  const checkPermissions = async () => {
    if (Platform.OS === 'android') {
      const { status: locationStatus } = 
        await Location.requestForegroundPermissionsAsync();
      
      setHasPermission(locationStatus === 'granted');
      
      if (locationStatus !== 'granted') {
        alert('Permission to access location was denied');
      }
    }
  };

  const startTracking = () => {
    if (!hasPermission) {
      alert('Location permission is required');
      return;
    }

    // Start the altitude service
    console.log('Starting altitude service');
    AltitudeService.startService();
    setIsTracking(true);

    // Add listener for altitude updates
    const unsubscribeFunction = AltitudeService.addAltitudeListener((newAltitude) => {
      setAltitude(newAltitude);
    });

    // Store unsubscribe function for cleanup
    setUnsubscribe(() => unsubscribeFunction);
  };

  const stopTracking = () => {
    console.log('Stopping altitude service');
    if (unsubscribe) {
      unsubscribe();
    }
    AltitudeService.stopService();
    setIsTracking(false);
    setAltitude(null);
    setUnsubscribe(null);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Altitude Tracker</Text>
      
      <View style={styles.altitudeContainer}>
        <Text style={styles.altitudeText}>
          {altitude !== null 
            ? `Current Altitude: ${altitude.toFixed(2)}m` 
            : 'Not tracking'}
        </Text>
      </View>

      <View style={styles.buttonContainer}>
        {!isTracking ? (
          <Button 
            title="Start Tracking" 
            onPress={startTracking}
            disabled={!hasPermission}
          />
        ) : (
          <Button 
            title="Stop Tracking" 
            onPress={stopTracking}
            color="#FF6B6B"
          />
        )}
      </View>

      {!hasPermission && (
        <Text style={styles.warning}>
          Location permission is required for altitude tracking
        </Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  altitudeContainer: {
    backgroundColor: '#f5f5f5',
    padding: 20,
    borderRadius: 10,
    marginVertical: 20,
    minWidth: 250,
    alignItems: 'center',
  },
  altitudeText: {
    fontSize: 18,
  },
  buttonContainer: {
    marginTop: 20,
  },
  warning: {
    color: '#FF6B6B',
    marginTop: 20,
    textAlign: 'center',
  },
});
