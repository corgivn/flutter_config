## iOS Advanced Setup

The plugin requires iOS 15.0 or later. Make sure `IPHONEOS_DEPLOYMENT_TARGET` of your `Runner` target
(and `platform :ios` in your `Podfile`, if you use CocoaPods) is at least `15.0`.

### Swift Package Manager

Flutter builds plugins with Swift Package Manager by default. Swift packages cannot run build scripts,
so when your app uses Swift Package Manager you need to add one Run Script build phase that writes the
env values into your app bundle. With CocoaPods this step is optional, the plugin generates the values itself.

In Xcode, select the `Runner` target > Build Phases > `+` > New Run Script Phase, uncheck
"Based on dependency analysis" and paste:

```sh
FLUTTER_CONFIG_DIR=$(ruby -rjson -e 'puts JSON.parse(File.read(ARGV[0]))["plugins"]["ios"].find { |p| p["name"] == "flutter_config" }["path"]' "${SRCROOT}/../.flutter-plugins-dependencies")
"${FLUTTER_CONFIG_DIR%/}/ios/Classes/BuildDotenvPlist.rb" "${SRCROOT}/" "${TARGET_BUILD_DIR}/${UNLOCALIZED_RESOURCES_FOLDER_PATH}/flutter_config.plist"
```

The script reads the same env file as the rest of this guide (`.envfile`, `ENVFILE` or `.env`)
and writes `flutter_config.plist` into the app bundle. The plugin reads it at runtime.

### Reading variables natively

No additional setup is required is you are only reading env varibles from Obj-C/Swift code.

Read variables declared in `.env` from your Obj-C classes like:

```objective-c
// import the module (works with both Swift Package Manager and CocoaPods)
@import flutter_config;

// then read individual keys like:
NSString *apiUrl = [FlutterConfigPlugin envFor:@"API_URL"];

// or just fetch the whole config
NSDictionary *config = [FlutterConfigPlugin env];
```

### Availability in Build settings and Info.plist

Extra steps are required if you are reading env varibles from your `info.plist` file

1. Under `Runner/Flutter`:
   ![img](./pic1.png)
   You need to add the following code to both `Debug.xcconfig` and `Release.xcconfig`

   ```objective-c
   #include? "tmp.xcconfig"
   ```

   It is also recommended to add this file to gitignore
   ```
   **/ios/Flutter/tmp.xcconfig
   ```


2. In the Xcode menu, go to Product > Scheme > Edit Scheme
   ![img](./pic2.png)

3. under Build > Pre-actions you need to add 2 new run script actions with the following code:
   ![img](./pic3.png)

   ```
   echo ".env" > ${SRCROOT}/.envfile
   ```

   ```
   FLUTTER_CONFIG_DIR=$(ruby -rjson -e 'puts JSON.parse(File.read(ARGV[0]))["plugins"]["ios"].find { |p| p["name"] == "flutter_config" }["path"]' "${SRCROOT}/../.flutter-plugins-dependencies")
   "${FLUTTER_CONFIG_DIR%/}/ios/Classes/BuildXCConfig.rb" ${SRCROOT}/ ${SRCROOT}/Flutter/tmp.xcconfig
   ```

   The old `${SRCROOT}/.symlinks/plugins/flutter_config/...` path only exists when the app uses CocoaPods.

4. Make sure you select `Runner` from the `Provide build settings from` dropdown

   Your finished scripts should l ook like this:
   ![img](./pic5.png)

5. By default, you should only need to do this for the runner scheme if you only have one environment.

This should now create a `tmp.xcconfig` file which can be accessed by `info.plist`

### Different environments

The basic idea in iOS is to have one scheme per environment file, so you can easily alternate between them.

Start by creating a new scheme:

- In the Xcode menu, go to Product > Scheme > Edit Scheme
- Click Duplicate Scheme on the bottom
- Give it a proper name on the top left. For instance: "Myapp (staging)"

#### NOTE
You need to make sure that your scheme name matches the `flavor` name which you defined on the flutter side of things
eg. `flutter run ios --flavor develop`

Then edit the newly created scheme to make it use a different env file. From the same "manage scheme" window:

In the Xcode menu, go to Product > Scheme > Manage Schemes > select your scheme > Click edit

- Follow steps 3 and 4 again for each scheme and replace you env files

```
echo ".env.staging" > ${SRCROOT}/.envfile   # replace .env.staging for your file
```

This is still a bit experimental and dirty – let us know if you have a better idea on how to make iOS use different configurations opening a pull request or issue!
