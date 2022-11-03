# Bazel Log

1. Error on first building of the app
    - **command** -> `bazel build //src/main:app`  
    - **error** -> 
        ``` 
        ERROR: /Users/mawinda/Desktop/bazel-android-project/examples/android/tutorial/src/main/BUILD:1:15: While resolving toolchains for target //src/main:app: no matching toolchains found for types @bazel_tools//tools/android:sdk_toolchain_type
        ERROR: Analysis of target '//src/main:app' failed; build aborted: 
        INFO: Elapsed time: 1.910s
        INFO: 0 processes.
        FAILED: Build did NOT complete successfully (34 packages loaded, 168 targets configured)
        ```
    - **explanation** -> 
        - Bazel requires android sdk build tools to build the app, but since my sdk was not in path, it could not find the tools.

            I decided to add the sdk to path and reload bashrc file for the changes to take effect

    - **solution** -> 
        - Added android sdk to path
        ```
        echo "export ANDROID_HOME=$HOME/Library/Android/Sdk/" >> ~/.bashrc
        ```
        then reloaded bashrc
            ```
            source ~/.bashrc
            ``` 

2. Missing Android Sdk level 25
    
    I had set api level to 25 which was not available on the android sdk
    - **command** -> `bazel build //src/main:app` 
    - **error** -> 
        ``` 
        ERROR: /Users/mawinda/Desktop/bazel-android-project/examples/android/tutorial/WORKSPACE:1:23: fetching android_sdk_repository rule //external:androidsdk: Android SDK api level 25 was requested but it is not installed in the Android SDK at /Users/mawinda/Library/Android/sdk. The api levels found were [33, 32, 31, 30, 29, 28]. Please choose an available api level or install api level 25 from the Android SDK Manager.
        ERROR: Analysis of target '//src/main:app' failed; build aborted: Android SDK api level 25 was requested but it is not installed in the Android SDK at /Users/mawinda/Library/Android/sdk. The api levels found were [33, 32, 31, 30, 29, 28]. Please choose an available api level or install api level 25 from the Android SDK Manager.
        INFO: Elapsed time: 0.152s
        INFO: 0 processes.
        FAILED: Build did NOT complete successfully (6 packages loaded, 8 targets configured)
        Fetching @local_jdk; fetching
        Fetching @remotejdk17_macos_aarch64_toolchain_config_repo; fetching
        Fetching @remotejdk17_macos_toolchain_config_repo; fetching
        ```
     - **explanation** -> 
        I had set api level to 25  as i was following the bazel setup tutorial and the android sdks available were 33,32,31,30,29,28. 
        
        I had an option to download api level 25, use the existings ones or just remove the api level for it to use max available sdk, but decided to use the existing sdks. I changed api level to 30 in the project because I was not sure of what to expect by removing the api level completely


        Later after testing i removed api_level declaration from android_sdk_repository, for bazel to use the max available sdk


     - **solution** -> 

        ```
        android_sdk_repository(name = "androidsdk",api_level = 30,build_tools_version = "30.0.3")
        ```
            
        **updated solution**
         ```
          android_sdk_repository(name = "androidsdk")
         ```
    - **results** -> 

        Build was successful

        ```
        INFO: Analyzed target //src/main:app (55 packages loaded, 1498 targets configured).
        INFO: Found 1 target...
        Target //src/main:app up-to-date:
        bazel-bin/src/main/app_deploy.jar
        bazel-bin/src/main/app_unsigned.apk
        bazel-bin/src/main/app.apk
        INFO: Elapsed time: 560.257s, Critical Path: 8.67s
        INFO: 68 processes: 31 internal, 25 darwin-sandbox, 12 worker.
        INFO: Build completed successfully, 68 total actions

        ```

3. Manifest merger Issue when adding external dependencies
    - **command** ->  ` bazel build //src/main:app --verbose_failures --sandbox_debug`
    - **error** -> 
        ```
        INFO: Analyzed target //src/main:app (0 packages loaded, 0 targets configured).
            INFO: Found 1 target...
            ERROR: /Users/mawinda/Desktop/bazel-android-project/examples/android/tutorial/src/main/java/com/example/bazel/BUILD:5:16: Merging manifest for //src/main/java/com/example/bazel:greeter_activity failed: (Exit 1): sandbox-exec failed: error executing command 
            (cd /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/sandbox/darwin-sandbox/2239/execroot/__main__ && \
            exec env - \
                PATH=/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin \
                TMPDIR=/var/folders/9l/9ldh5lv1541grf6dvsn4ndh40000gn/T/ \
            /usr/bin/sandbox-exec -f /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/sandbox/darwin-sandbox/2239/sandbox.sb /var/tmp/_bazel_mawinda/install/0a609247a9eb0c65a5f1ae48ca487674/process-wrapper '--timeout=0' '--kill_delay=15' bazel-out/darwin_arm64-opt-exec-2B5CBBC6/bin/external/bazel_tools/src/tools/android/java/com/google/devtools/build/android/ResourceProcessorBusyBox --tool MERGE_MANIFEST -- --manifest src/main/java/com/example/bazel/AndroidManifest.xml --mergeType LIBRARY --customPackage com.example.bazel --manifestOutput bazel-out/android-armeabi-v7a-fastbuild/bin/src/main/java/com/example/bazel/_renamed/greeter_activity/AndroidManifest.xml)
            Error: /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/sandbox/darwin-sandbox/2239/execroot/__main__/src/main/java/com/example/bazel/AndroidManifest.xml:11:3-24:17 Error:
                tools:replace specified at line:11 for attribute tools:androidx.emoji2.androidx-startup, but no new value specified
            Error: /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/sandbox/darwin-sandbox/2239/execroot/__main__/src/main/java/com/example/bazel/AndroidManifest.xml Error:
                Validation failed, exiting
            Oct 25, 2022 8:10:33 AM com.google.devtools.build.android.ManifestMergerAction main
            SEVERE: Error during merging manifests
            com.google.devtools.build.android.AndroidManifestProcessor$ManifestProcessingException: Manifest merger failed with multiple errors, see logs
                at com.google.devtools.build.android.AndroidManifestProcessor.mergeManifest(AndroidManifestProcessor.java:186)
                at com.google.devtools.build.android.ManifestMergerAction.main(ManifestMergerAction.java:217)
                at com.google.devtools.build.android.ResourceProcessorBusyBox$Tool$5.call(ResourceProcessorBusyBox.java:93)
                at com.google.devtools.build.android.ResourceProcessorBusyBox.processRequest(ResourceProcessorBusyBox.java:234)
                at com.google.devtools.build.android.ResourceProcessorBusyBox.main(ResourceProcessorBusyBox.java:177)

            Warning: 
            See http://g.co/androidstudio/manifest-merger for more information about the manifest merger.
            Target //src/main:app failed to build
            INFO: Elapsed time: 0.408s, Critical Path: 0.29s
            INFO: 3 processes: 3 internal.
            FAILED: Build did NOT complete successfully
        ```
    - **explanation** -> 

        I was using the most current dependencies and faced manifest merger issue, below
         
         ```
            Error: /private/var/tmp/bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/sandbox/darwin-sandbox/2213/execroot/main_/src/main/AndroidManifest.xml:20:19-74 Error:
                Attribute provider#androidx.startup.InitializationProvider@authorities value=(androidx.emoji2.androidx-startup) from [@maven//:androidx_emoji2_emoji2] AndroidManifest.xml:20:19-74
                is also present at [@maven//:androidx_lifecycle_lifecycle_process] AndroidManifest.xml:20:19-74 value=(androidx.lifecycle.process.androidx-startup).
                Suggestion: add 'tools:replace="android:authorities"' to <provider> element at AndroidManifest.xml:20:9-22:20 to override.
            Oct 25, 2022 12:29:40 AM com.google.devtools.build.android.ManifestMergerAction main
            SEVERE: Error during merging manifests
         ```

         I tried adding 'tools:replace="android:authorities"' in manifest but got a new error :->  but no new value specified, Since the original project was not in androidx, decided to experiment with the earlier versions of androidx dependencies until i settle for the one in solution section 

         **still researching on this 

    - **solution** -> 
      
      Downgraded dependency to 
       
       ```
        maven_install(
        artifacts = [
            "androidx.appcompat:appcompat:1.3.1",
            "com.google.android.material:material:1.4.0",
            "androidx.constraintlayout:constraintlayout:2.1.3",
 
        ],
        repositories = [
            "https://maven.google.com",
            "https://jcenter.bintray.com",
            "https://repo1.maven.org/maven2",
        ],
        )

       ```
    - **results** -> 

        Build was successful 

        ```
        INFO: Analyzed target //src/main:app (0 packages loaded, 0 targets configured).
        INFO: Found 1 target...
        Target //src/main:app up-to-date:
        bazel-bin/src/main/app_deploy.jar
        bazel-bin/src/main/app_unsigned.apk
        bazel-bin/src/main/app.apk
        INFO: Elapsed time: 0.467s, Critical Path: 0.31s
        INFO: 3 processes: 1 internal, 1 darwin-sandbox, 1 worker.
        INFO: Build completed successfully, 3 total actions
        ```

 
 3. Error getting packages while upgrading app to androidx
    - **command** -> `bazel build //src/main:app --verbose_failures --sandbox_debug`
    - **error** -> 
        ```
           INFO: Analyzed target //src/main:app (0 packages loaded, 0 targets configured).
            INFO: Found 1 target...
            ERROR: /Users/mawinda/Desktop/bazel-android-project/examples/android/tutorial/src/main/java/com/example/bazel/BUILD:5:16: Building src/main/java/com/example/bazel/libgreeter_activity.jar (2 source files) failed: (Exit 1): java failed: error executing command 
            (cd /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__ && \
            exec env - \
                LC_CTYPE=en_US.UTF-8 \
                PATH=/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin \
            external/remotejdk11_macos_aarch64/bin/java -XX:-CompactStrings '--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.resources=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED' '--add-opens=java.base/java.nio=ALL-UNNAMED' '--add-opens=java.base/java.lang=ALL-UNNAMED' '--patch-module=java.compiler=external/remote_java_tools/java_tools/java_compiler.jar' '--patch-module=jdk.compiler=external/remote_java_tools/java_tools/jdk_compiler.jar' -jar external/remote_java_tools/java_tools/JavaBuilder_deploy.jar @bazel-out/android-armeabi-v7a-fastbuild/bin/src/main/java/com/example/bazel/libgreeter_activity.jar-0.params @bazel-out/android-armeabi-v7a-fastbuild/bin/src/main/java/com/example/bazel/libgreeter_activity.jar-1.params)
            # Configuration: 9faf1cc262e7a34a2d000fb1b58cf7c6fdb812f57de21c3f8a4dc10e05ca8f13
            # Execution platform: @local_config_platform//:host
            src/main/java/com/example/bazel/MainActivity.java:8: error: package androidx.appcompat.app does not exist
            import androidx.appcompat.app.AppCompatActivity;
                                        ^
            src/main/java/com/example/bazel/MainActivity.java:13: error: cannot find symbol
            public class MainActivity extends AppCompatActivity {
                                            ^
            symbol: class AppCompatActivity
            src/main/java/com/example/bazel/MainActivity.java:14: error: method does not override or implement a method from a supertype
            @Override
            ^
            src/main/java/com/example/bazel/MainActivity.java:16: error: cannot find symbol
                super.onCreate(savedInstanceState);
                ^
            symbol:   variable super
            location: class MainActivity
            src/main/java/com/example/bazel/MainActivity.java:19: error: cannot find symbol
                setContentView(R.layout.activity_main);
                ^
            symbol:   method setContentView(int)
            location: class MainActivity
            src/main/java/com/example/bazel/MainActivity.java:21: error: cannot find symbol
                Button clickMeButton = findViewById(R.id.clickMeButton);
                                    ^
            symbol:   method findViewById(int)
            location: class MainActivity
            src/main/java/com/example/bazel/MainActivity.java:22: error: cannot find symbol
                TextView helloBazelTextView = findViewById(R.id.helloBazelTextView);
                                            ^
            symbol:   method findViewById(int)
            location: class MainActivity
            Target //src/main:app failed to build
            INFO: Elapsed time: 0.281s, Critical Path: 0.11s
            INFO: 3 processes: 3 internal.
            FAILED: Build did NOT complete successfully 
        ```
    - **explanation** -> 
      
        All my dependecies were under android_binary instead of android_library. Went back to bazel documentation to check what i was doing wrong and realised i had missed a point. android_binary rule build android application package, while android_library contains contain attributes need by bazel to build library module. I moved my dependencies to android_library


    - **solution** -> 
    
       Moved dependencies to android_libray 

       src/main/java/com/example/bazel/BUILD android_libray
       ```
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
            srcs = glob(["*.kt"]),
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

       ```

       /src/main/java/com/example/bazel/BUILD android_binary

       ```
        android_binary(
            name = "app",
            manifest = "AndroidManifest.xml",
            visibility = ["//visibility:public"],
            deps = [
                "//src/main/java/com/example/bazel:greeter_activity"],
        )
       ```

    - **result** -> 
        Build was successful

        ```
        INFO: Analyzed target //src/main:app (0 packages loaded, 0 targets configured).
        INFO: Found 1 target...
        Target //src/main:app up-to-date:
        bazel-bin/src/main/app_deploy.jar
        bazel-bin/src/main/app_unsigned.apk
        bazel-bin/src/main/app.apk
        INFO: Elapsed time: 0.467s, Critical Path: 0.31s
        INFO: 3 processes: 1 internal, 1 darwin-sandbox, 1 worker.
        INFO: Build completed successfully, 3 total actions
        ```

4. Strict Indirect dependency issue
    After adding livedata, project failed to build 
    - **command** -> `bazel build //src/main:app --verbose_failures --sandbox_debug`
    - **error** -> 
        ```
          INFO: Analyzed target //src/main:app (1 packages loaded, 154 targets configured).
        INFO: Found 1 target...
        ERROR: /Users/mawinda/Desktop/bazel-android-project/examples/android/tutorial/src/main/java/com/example/bazel/BUILD:36:16: Building src/main/java/com/example/bazel/libgreeter_activity.jar (2 source files) failed: (Exit 1): java failed: error executing command 
        (cd /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__ && \
        exec env - \
            LC_CTYPE=en_US.UTF-8 \
            PATH=/Users/mawinda/Library/Caches/bazelisk/downloads/bazelbuild/bazel-5.3.2-darwin-arm64/bin:/opt/homebrew/bin:/opt/homebrew/sbin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin \
        external/remotejdk11_macos_aarch64/bin/java -XX:-CompactStrings '--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.resources=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED' '--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED' '--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED' '--add-opens=java.base/java.nio=ALL-UNNAMED' '--add-opens=java.base/java.lang=ALL-UNNAMED' '--patch-module=java.compiler=external/remote_java_tools/java_tools/java_compiler.jar' '--patch-module=jdk.compiler=external/remote_java_tools/java_tools/jdk_compiler.jar' -jar external/remote_java_tools/java_tools/JavaBuilder_deploy.jar @bazel-out/android-armeabi-v7a-fastbuild/bin/src/main/java/com/example/bazel/libgreeter_activity.jar-0.params @bazel-out/android-armeabi-v7a-fastbuild/bin/src/main/java/com/example/bazel/libgreeter_activity.jar-1.params)
        # Configuration: 9faf1cc262e7a34a2d000fb1b58cf7c6fdb812f57de21c3f8a4dc10e05ca8f13
        # Execution platform: @local_config_platform//:host
        src/main/java/com/example/bazel/MainActivity.java:12: error: [strict] Using type androidx.lifecycle.MutableLiveData from an indirect dependency (TOOL_INFO: "bazel-out/android-armeabi-v7a-fastbuild/bin/external/maven/_aar/androidx_lifecycle_lifecycle_livedata_core/classes_and_libs_merged.jar").
        private MutableLiveData<Integer> image = new MutableLiveData<Integer>();
                ^
        src/main/java/com/example/bazel/MainActivity.java:20: error: [strict] Using type androidx.core.content.ContextCompat from an indirect dependency (TOOL_INFO: "bazel-out/android-armeabi-v7a-fastbuild/bin/external/maven/_aar/androidx_core_core/classes_and_libs_merged.jar").
                if(mImage != null) imageView.setImageDrawable(ContextCompat.getDrawable(this, mImage));
                                                                ^
        Target //src/main:app failed to build
        INFO: Elapsed time: 24.588s, Critical Path: 10.64s
        INFO: 360 processes: 11 internal, 266 darwin-sandbox, 83 worker.
        FAILED: Build did NOT complete successfully  
        ```
    - **explanation** ->
        The issue was as a result of using indirect dependency for MutableLiveData and ContextCompact. 
        Even after adding specific dependencies that have them i.e 
        androidx.lifecycle:lifecycle-livedata for MutabaleLiveData and androidx.core:core for ContextCompact, the issues persisted.
        Decided to switch off experimental_strict_java_deps which ensures Java target explicitly declares all directly used targets as dependencies as I do more research on it

    - **solution** -> 
        turned off strict java deps by adding this flag to command `--experimental_strict_java_deps=off`

        new command: `bazel mobile-install //src/main:app --start_app --verbose_failures ----experimental_strict_java_deps=off`
    - **results** -> 
      app build successfully
        ```
            INFO: Build option --experimental_strict_java_deps has changed, discarding analysis cache.
            INFO: Analyzed target //src/main:app (0 packages loaded, 1796 targets configured).
            INFO: Found 1 target...
            Target //src/main:app up-to-date:
            bazel-bin/src/main/app_deploy.jar
            bazel-bin/src/main/app_unsigned.apk
            bazel-bin/src/main/app.apk
            INFO: Elapsed time: 7.968s, Critical Path: 7.55s
            INFO: 28 processes: 1 internal, 13 darwin-sandbox, 14 worker.
            INFO: Build completed successfully, 28 total actions
        ```

5. Roboelectric Test failed due to JUnit4 Test Runner Security Manager is deprecate
    - **command** -> `bazel test  //src/androidTest:test_app --experimental_strict_java_deps=off  --verbose_failures`
    - **error** -> 
        ```
        INFO: Analyzed target //src/androidTest:test_app (1 packages loaded, 5 targets configured).
        INFO: Found 1 test target...
        FAIL: //src/androidTest:test_app (see /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__/bazel-out/darwin_arm64-fastbuild/testlogs/src/androidTest/test_app/test.log)
        Target //src/androidTest:test_app up-to-date:
        bazel-bin/src/androidTest/test_app.jar
        bazel-bin/src/androidTest/test_app
        INFO: Elapsed time: 3.340s, Critical Path: 3.21s
        INFO: 9 processes: 3 internal, 5 darwin-sandbox, 1 worker.
        INFO: Build completed, 1 test FAILED, 9 total actions
        //src/androidTest:test_app                                               FAILED in 0.9s
        /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__/bazel-out/darwin_arm64-fastbuild/testlogs/src/androidTest/test_app/test.log

        INFO: Build completed, 1 test FAILED, 9 total actions
        ```
        /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__/bazel-out/darwin_arm64-fastbuild/testlogs/src/androidTest/test_app/test.log

        ```
            exec ${PAGER:-/usr/bin/less} "$0" || exit 1
            Executing tests from //src/androidTest:test_app
            -----------------------------------------------------------------------------
            JUnit4 Test Runner
            java.lang.UnsupportedOperationException: The Security Manager is deprecated and will be removed in a future release
                at java.base/java.lang.System.setSecurityManager(System.java:416)
                at com.google.testing.junit.runner.junit4.JUnit4Runner.installSecurityManager(JUnit4Runner.java:256)
                at com.google.testing.junit.runner.junit4.JUnit4Runner.run(JUnit4Runner.java:113)
                at com.google.testing.junit.runner.BazelTestRunner.runTestsInSuite(BazelTestRunner.java:148)
                at com.google.testing.junit.runner.BazelTestRunner.main(BazelTestRunner.java:75)

            BazelTestRunner exiting with a return value of 1
            JVM shutdown hooks (if any) will run now.
            The JVM will exit once they complete.
        ```
    - **explanation** -> 
        The error above is due to Java Security Manager being deprecated. I searched for a replacement and how to change it. My finding is that it does not have a replacement and decided to  enable Security Manager by add --jvmopt=-Djava.security.manager=allow to the command
        
    - **results** -> 
       Adds --jvmopt to command to remove the error
       new command: `bazel test  //src/androidTest:test_app --jvmopt=-Djava.security.manager=allow --experimental_strict_java_deps=off  --verbose_failures --test_output=errors`

6. Roboelectric failed due to IllegalAccessException
    - **command** -> `bazel test  //src/androidTest:test_app --jvmopt=-Djava.security.manager=allow --experimental_strict_java_deps=off  --verbose_failures --test_output=errors`
    - **error** -> 
        ```
        INFO: Analyzed target //src/androidTest:test_app (0 packages loaded, 0 targets configured).
        INFO: Found 1 test target...
        FAIL: //src/androidTest:test_app (see /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__/bazel-out/darwin_arm64-fastbuild/testlogs/src/androidTest/test_app/test.log)
        INFO: From Testing //src/androidTest:test_app:
        ==================== Test output for //src/androidTest:test_app:
        JUnit4 Test Runner
        .[WARN] Unknown chunk type '200'.
        System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        E.System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        System.logW: No Compatibility callbacks set! Querying change 210923482
        E
        Time: 2.696
        There were 2 failures:
        1) checkImageIsBazelImage(com.example.bazel.MainActivityTest)
        java.lang.RuntimeException: java.lang.IllegalAccessException: class androidx.test.espresso.base.ThreadPoolExecutorExtractor$2 cannot access a member of class androidx.loader.content.ModernAsyncTask with modifiers "public static final"
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.getCompatAsyncTaskThreadPool(ThreadPoolExecutorExtractor.java:4)
            at androidx.test.espresso.base.BaseLayerModule.provideCompatAsyncTaskMonitor(BaseLayerModule.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.provideCompatAsyncTaskMonitor(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.get(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.get(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.base.UiControllerImpl_Factory.get(UiControllerImpl_Factory.java:1)
            at androidx.test.espresso.base.UiControllerImpl_Factory.get(UiControllerImpl_Factory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.base.UiControllerModule_ProvideUiControllerFactory.get(UiControllerModule_ProvideUiControllerFactory.java:1)
            at androidx.test.espresso.base.UiControllerModule_ProvideUiControllerFactory.get(UiControllerModule_ProvideUiControllerFactory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.DaggerBaseLayerComponent$ViewInteractionComponentImpl.viewInteraction(DaggerBaseLayerComponent.java:1)
            at androidx.test.espresso.Espresso.onView(Espresso.java:1)
            at com.example.bazel.MainActivityTest.checkImageIsBazelImage(MainActivityTest.java:33)
            at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
            at java.base/java.lang.reflect.Method.invoke(Method.java:577)
            at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
            at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
            at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
            at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
            at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
            at org.junit.rules.ExternalResource$1.evaluate(ExternalResource.java:48)
            at org.junit.rules.RunRules.evaluate(RunRules.java:20)
            at org.robolectric.RobolectricTestRunner$HelperTestRunner$1.evaluate(RobolectricTestRunner.java:580)
            at org.robolectric.internal.SandboxTestRunner$2.lambda$evaluate$2(SandboxTestRunner.java:287)
            at org.robolectric.internal.bytecode.Sandbox.lambda$runOnMainThread$0(Sandbox.java:99)
            at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
            at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
            at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
            at java.base/java.lang.Thread.run(Thread.java:833)
        Caused by: java.lang.IllegalAccessException: class androidx.test.espresso.base.ThreadPoolExecutorExtractor$2 cannot access a member of class androidx.loader.content.ModernAsyncTask with modifiers "public static final"
            at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:394)
            at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:674)
            at java.base/java.lang.reflect.Field.checkAccess(Field.java:1140)
            at java.base/java.lang.reflect.Field.get(Field.java:425)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor$2.call(ThreadPoolExecutorExtractor.java:3)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor$2.call(ThreadPoolExecutorExtractor.java:6)
            at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.runOnMainThread(ThreadPoolExecutorExtractor.java:9)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.getCompatAsyncTaskThreadPool(ThreadPoolExecutorExtractor.java:1)
            ... 30 more
        2) checkTextIsBazel(com.example.bazel.MainActivityTest)
        java.lang.RuntimeException: java.lang.IllegalAccessException: class androidx.test.espresso.base.ThreadPoolExecutorExtractor$2 cannot access a member of class androidx.loader.content.ModernAsyncTask with modifiers "public static final"
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.getCompatAsyncTaskThreadPool(ThreadPoolExecutorExtractor.java:4)
            at androidx.test.espresso.base.BaseLayerModule.provideCompatAsyncTaskMonitor(BaseLayerModule.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.provideCompatAsyncTaskMonitor(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.get(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:1)
            at androidx.test.espresso.base.BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.get(BaseLayerModule_ProvideCompatAsyncTaskMonitorFactory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.base.UiControllerImpl_Factory.get(UiControllerImpl_Factory.java:1)
            at androidx.test.espresso.base.UiControllerImpl_Factory.get(UiControllerImpl_Factory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.base.UiControllerModule_ProvideUiControllerFactory.get(UiControllerModule_ProvideUiControllerFactory.java:1)
            at androidx.test.espresso.base.UiControllerModule_ProvideUiControllerFactory.get(UiControllerModule_ProvideUiControllerFactory.java:2)
            at androidx.test.espresso.core.internal.deps.dagger.internal.DoubleCheck.get(DoubleCheck.java:6)
            at androidx.test.espresso.DaggerBaseLayerComponent$ViewInteractionComponentImpl.viewInteraction(DaggerBaseLayerComponent.java:1)
            at androidx.test.espresso.Espresso.onView(Espresso.java:1)
            at com.example.bazel.MainActivityTest.checkTextIsBazel(MainActivityTest.java:38)
            at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
            at java.base/java.lang.reflect.Method.invoke(Method.java:577)
            at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
            at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
            at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
            at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
            at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
            at org.junit.rules.ExternalResource$1.evaluate(ExternalResource.java:48)
            at org.junit.rules.RunRules.evaluate(RunRules.java:20)
            at org.robolectric.RobolectricTestRunner$HelperTestRunner$1.evaluate(RobolectricTestRunner.java:580)
            at org.robolectric.internal.SandboxTestRunner$2.lambda$evaluate$2(SandboxTestRunner.java:287)
            at org.robolectric.internal.bytecode.Sandbox.lambda$runOnMainThread$0(Sandbox.java:99)
            at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
            at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
            at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
            at java.base/java.lang.Thread.run(Thread.java:833)
        Caused by: java.lang.IllegalAccessException: class androidx.test.espresso.base.ThreadPoolExecutorExtractor$2 cannot access a member of class androidx.loader.content.ModernAsyncTask with modifiers "public static final"
            at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:394)
            at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:674)
            at java.base/java.lang.reflect.Field.checkAccess(Field.java:1140)
            at java.base/java.lang.reflect.Field.get(Field.java:425)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor$2.call(ThreadPoolExecutorExtractor.java:3)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor$2.call(ThreadPoolExecutorExtractor.java:6)
            at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.runOnMainThread(ThreadPoolExecutorExtractor.java:9)
            at androidx.test.espresso.base.ThreadPoolExecutorExtractor.getCompatAsyncTaskThreadPool(ThreadPoolExecutorExtractor.java:1)
            ... 30 more

        FAILURES!!!
        Tests run: 2,  Failures: 2


        BazelTestRunner exiting with a return value of 1
        JVM shutdown hooks (if any) will run now.
        The JVM will exit once they complete.

        -- JVM shutdown starting at 2022-10-26 05:56:40 --

        ================================================================================
        Target //src/androidTest:test_app up-to-date:
        bazel-bin/src/androidTest/test_app.jar
        bazel-bin/src/androidTest/test_app
        INFO: Elapsed time: 3.486s, Critical Path: 3.39s
        INFO: 2 processes: 1 internal, 1 darwin-sandbox.
        INFO: Build completed, 1 test FAILED, 2 total actions
        //src/androidTest:test_app                                               FAILED in 3.3s
        /private/var/tmp/_bazel_mawinda/8f9dc2d2b28831da7713ee0b0a7afa55/execroot/__main__/bazel-out/darwin_arm64-fastbuild/testlogs/src/androidTest/test_app/test.log

        INFO: Build completed, 1 test FAILED, 2 total actions
        ```
    - **explanation** -> 
       it is a known issue as a Roboelectric dissabling instrumenting in androidx by default. Temporary fix is to add instrumentedPackages=androidx.loader.content, but since am new to bazel, couldn't find properties, Doing more research on the same, i got the issue was fixed in later dependecies, i just upgraded 

    - **solution** -> 
      Error was due to espresso dependecy 
       
       upgraded dependency to `androidx.test.espresso:espresso-core:3.5.0-alpha03`

    - **result** -> 
        Test was successful
        ```
           INFO: Analyzed target //src/androidTest:test_app (1 packages loaded, 187 targets configured).
            INFO: Found 1 test target...
            Target //src/androidTest:test_app up-to-date:
            bazel-bin/src/androidTest/test_app.jar
            bazel-bin/src/androidTest/test_app
            INFO: Elapsed time: 88.149s, Critical Path: 11.93s
            INFO: 81 processes: 4 internal, 75 darwin-sandbox, 2 worker.
            INFO: Build completed successfully, 81 total actions
            //src/androidTest:test_app                                               PASSED in 3.8s

            Executed 1 out of 1 test: 1 test passes.
            INFO: Build completed successfully, 81 total actions 
        ```
