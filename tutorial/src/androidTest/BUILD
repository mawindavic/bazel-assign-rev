PACKAGE = "com.example.bazel"


android_local_test(
    name = "test_app",
    srcs = [
    "java/com/example/bazel/EspressoTestsMatchers.java",
    "java/com/example/bazel/DrawableMatcher.java",
    "java/com/example/bazel/MainActivityTest.java"],
    custom_package = PACKAGE,
    manifest = "AndroidManifest.xml",
    test_class = "com.example.bazel.MainActivityTest",
    deps = [
        "//src/main/java/com/example/bazel:greeter_activity",
        "@maven//:androidx_test_ext_junit",
        "@maven//:androidx_test_espresso_espresso_core",
        "@maven//:org_robolectric_robolectric",
        "@maven//:org_robolectric_shadows_framework",
        "@robolectric//bazel:android-all",
    ],
)
