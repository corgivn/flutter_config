// swift-tools-version: 5.9

import PackageDescription

let package = Package(
    name: "flutter_config",
    platforms: [
        .iOS("15.0")
    ],
    products: [
        .library(name: "flutter-config", targets: ["flutter_config"])
    ],
    dependencies: [
        .package(name: "FlutterFramework", path: "../FlutterFramework")
    ],
    targets: [
        .target(
            name: "flutter_config",
            dependencies: [
                .product(name: "FlutterFramework", package: "FlutterFramework")
            ],
            resources: [
                .process("PrivacyInfo.xcprivacy")
            ],
            cSettings: [
                .headerSearchPath("include/flutter_config")
            ]
        )
    ]
)
