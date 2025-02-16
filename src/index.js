// src/index.js
import { NativeModules, NativeEventEmitter } from 'react-native';

const { AltitudeService } = NativeModules;

const altitudeServiceEmitter = new NativeEventEmitter(AltitudeService);

const AltitudeServiceModule = {
  startService: () => {
    AltitudeService.startService();
  },
  stopService: () => {
    AltitudeService.stopService();
  },
  addAltitudeListener: (callback) => {
    const subscription = altitudeServiceEmitter.addListener('AltitudeUpdated', callback);
    return () => subscription.remove();
  },
};

export default AltitudeServiceModule;