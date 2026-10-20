#import "./include/flutter_config/FlutterConfigPlugin.h"

#if !SWIFT_PACKAGE
// Written during CocoaPods builds by ios/Classes/BuildDotenvConfig.rb.
#import "../../../Classes/GeneratedDotEnv.m"
#endif

// Written into the app bundle by ios/Classes/BuildDotenvPlist.rb (see doc/IOS.md).
static NSString *const kFlutterConfigPlistName = @"flutter_config";

@implementation FlutterConfigPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_config"
            binaryMessenger:[registrar messenger]];
  FlutterConfigPlugin* instance = [[FlutterConfigPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

+ (NSDictionary *)env {
  static NSDictionary *env;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    NSString *path = [[NSBundle mainBundle] pathForResource:kFlutterConfigPlistName ofType:@"plist"];
    NSDictionary *bundled = path ? [NSDictionary dictionaryWithContentsOfFile:path] : nil;
    if (bundled != nil) {
      env = bundled;
      return;
    }
#if SWIFT_PACKAGE
    NSLog(@"[flutter_config] %@.plist was not found in the app bundle. "
          @"Add the flutter_config build phase described in doc/IOS.md.", kFlutterConfigPlistName);
    env = @{};
#else
    env = (NSDictionary *)DOT_ENV;
#endif
  });
  return env;
}

+ (NSString *)envFor: (NSString *)key {
  NSString *value = (NSString *)[self.env objectForKey:key];
  return value;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"loadEnvVariables" isEqualToString:call.method]) {
    result([FlutterConfigPlugin env]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
