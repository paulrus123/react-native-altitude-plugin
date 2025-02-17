/**
 * This Metro configuration is needed when using a local package via relative path in package.json.
 * Without this, Metro won't be able to find and bundle the react-native-altitude-plugin package
 * that's referenced in our dependencies as "react-native-altitude-plugin": "..".
 * 
 * This tells Metro:
 * 1. To look for files in the parent directory (workspaceRoot) where our plugin lives
 * 2. To resolve node_modules from both the example app and the plugin directory
 */

const { getDefaultConfig } = require('expo/metro-config');
const path = require('path');

const projectRoot = __dirname;
const workspaceRoot = path.resolve(projectRoot, '..');

const config = getDefaultConfig(projectRoot);

// 1. Watch all files within the monorepo
config.watchFolders = [workspaceRoot];
// 2. Let Metro know where to resolve packages
config.resolver.nodeModulesPaths = [
  path.resolve(projectRoot, 'node_modules'),
  path.resolve(workspaceRoot, 'node_modules'),
];

module.exports = config;