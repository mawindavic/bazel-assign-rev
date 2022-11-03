This is a barebones Android app for the [Bazel Android tutorial](https://docs.bazel.build/versions/master/tutorial/android-app.html).

### Building Project
 ```
bazel build //src/main:app --verbose_failures --sandbox_debug --experimental_strict_java_deps=off
 ```

### Installing to Emulator
  ```
 bazel mobile-install //src/main:app --start_app --verbose_failures --sandbox_debug --experimental_strict_java_deps=off
 ```
### Running tests
  ```
bazel test  //src/androidTest:test_app --jvmopt=-Djava.security.manager=allow --experimental_strict_java_deps=off  --verbose_failures --test_output=errors
 ```# bazel-assignment
