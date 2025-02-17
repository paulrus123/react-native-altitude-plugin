const { withAndroidManifest, AndroidConfig } = require('@expo/config-plugins');
const { addWarning } = require('@expo/config-plugins/build/utils/warnings');

const { getMainApplicationOrThrow } = AndroidConfig.Manifest;

async function addAltitudeService(androidManifest) {
  const mainApplication = getMainApplicationOrThrow(androidManifest);

  if (!mainApplication.service) {
    mainApplication.service = [];
  }

  mainApplication.service.push({
    $: {
      "android:name": ".AltitudeProviderForegroundService",
      "android:enabled": "true",
      "android:exported": "false",
      "android:foregroundServiceType": "location"
    }
  });

  return androidManifest;
}

async function addPermissions(androidManifest) {
  const permissions = [
    'android.permission.FOREGROUND_SERVICE',
    'android.permission.ACCESS_FINE_LOCATION',
    'android.permission.ACCESS_COARSE_LOCATION',
  ];

  permissions.forEach(permission => {
    AndroidConfig.Permissions.addPermission(androidManifest, permission);
  });

  return androidManifest;
}

const withAltitude = (config) => {
  return withAndroidManifest(config, async (config) => {
    const androidManifest = config.modResults;

    try {
      await addAltitudeService(androidManifest);
      await addPermissions(androidManifest);
    } catch (e) {
      addWarning(
        'react-native-altitude-plugin',
        'Could not configure altitude plugin manifest entries.'
      );
    }

    return config;
  });
};

module.exports = withAltitude;