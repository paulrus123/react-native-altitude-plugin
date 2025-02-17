# Altitude Plugin Example

This is a sample implementation of the react-native-altitude-plugin using Expo.

## Getting Started

1. Clone the repository
2. Install dependencies:

```bash
cd example
npm install
```

3. Build and run on Android:

```bash
eas build --profile development --platform android
eas build:run --platform android
```

## Features Demonstrated

- Permission handling
- Starting/stopping the altitude service
- Real-time altitude updates
- Basic UI implementation
- Error handling

## Implementation Notes

- The example uses `expo-location` for permission handling
- Altitude updates are displayed in real-time
- The UI provides feedback about permission status
- Includes proper cleanup when stopping tracking

## Troubleshooting

If you encounter any issues:

1. Ensure all permissions are granted
2. Check that the device has a barometer sensor
3. Verify that location services are enabled
4. Make sure the app is running in the foreground

For more detailed implementation guidance, see the main package README.

