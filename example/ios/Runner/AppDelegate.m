#include "AppDelegate.h"
#include "GeneratedPluginRegistrant.h"

// import the plugin
// #import "FlutterConfigPlugin.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  // then read individual keys like:
  // NSString *apiUrl = [FlutterConfigPlugin envFor:@"API_URL"];
  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

- (void)didInitializeImplicitFlutterEngine:(NSObject<FlutterImplicitEngineBridge>*)engineBridge {
  [GeneratedPluginRegistrant registerWithRegistry:engineBridge.pluginRegistry];
}

@end
