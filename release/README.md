# Releasing


In order to release the code, do the following.


1. Bump the version to the new desired version by invoking ([from the root of the repository](../))
```
mvn versions:set -DnewVersion=$VERSION
```
where `$VERSION` is in the following format: `x.y.z`
This should set the desired version in the various [pom.xml](../pom.xml) files, including the [pom.xml](../concordium-sdk/pom.xml) in [concordium-sdk](../concordium-sdk) and the [pom.xml](../concordium-android-sdk/pom.xml) in [concordium-android-sdk/](../concordium-android-sdk).
This should change the [pom.xml](../concordium-sdk-examples/pom.xml) in [examples](../concordium-sdk-examples) to use this new version.

1. Commit and push this new version.
1. Publish a release via Github (the tagged version and the $VERSION just set should be the same)
1. Invoke the `Release` workflow on the newly released tag
1. Set a new `SNAPSHOT` version via:

```
mvn versions:set -DnewVersion=$VERSION-SNAPSHOT
```
where `$VERSION` is the just released version.
This should also change the [pom.xml](../concordium-sdk-examples/pom.xml) in [examples](../concordium-sdk-examples) to use this new version.
1. Commit and push the `SNAPSHOT` version.

## Release workflow

Invoking the `Release` workflow generates and attaches the following to the release.
1. A jar of the compiled SDK including native dependencies for windows, ubuntu and macos.
2. A jar-with-dependencies containing all of the above as well as all maven dependencies.
3. A jar with javadoc that can be imported to the users IDE to add documentation.

### Maven central
Two artifacts are automatically being uploaded and staged to maven central
1. The Java sdk with precompiled binaries for ubuntu, macos and windows. 
2. The Android archive to use in Android apps.
   
See the [Makefile](../Makefile) for specific targets.

One must login to https://s01.oss.sonatype.org/ (credentials can be found in Bitwarden) publish the release.
This is done by first closing the staged release followed by a click on the "release" button.

Lastly the released artifiacts are available here: 
- [java sdk](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-sdk)
- [android archive](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-android-sdk)

### Javadoc
The `Relase` workflow also creates the branch `javadoc` if it does not already exist.
This branch contains the generated javadoc which is visible at: [https://concordium.github.io/concordium-java-sdk/javadoc/concordium-sdk/apidocs/](https://concordium.github.io/concordium-java-sdk/javadoc/concordium-sdk/apidocs/)



