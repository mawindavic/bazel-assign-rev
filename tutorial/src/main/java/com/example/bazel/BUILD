load("@rules_jvm_external//:defs.bzl", "artifact")
load("@build_bazel_rules_android//android:rules.bzl","android_binary", "android_library")

load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//src:__subpackages__"],
)

PACKAGE = "com.example.bazel"

android_library(
    name = "bazel_res",
    custom_package = PACKAGE,
    manifest = "AndroidManifest.xml",
    exports = [
        artifact("androidx.appcompat:appcompat"),
        artifact("com.google.android.material:material"),
        artifact("androidx.constraintlayout:constraintlayout"),
    ] 
    
)

kt_jvm_library(
    name = "bazel_kt",
    srcs = glob(["*.kt","*.java"]),
    exports = [
        ":bazel_res",
        artifact("junit:junit"),
        artifact("androidx.test.espresso:espresso-core"),
        artifact("androidx.test.ext:junit"),
    ],
)


android_library(
    name = "greeter_activity",
    srcs = [
        "Greeter.java",
        "MainActivity.java",
    ],
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]),
    deps = [
        ":bazel_res",
        ":bazel_kt",
        ],
)
