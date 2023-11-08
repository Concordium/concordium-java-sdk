# Releasing


In order to release the code, do the following.


1. Bump the version to the new desired version by invoking ([from the root of the java sdk](../concordium-sdk/))
```
mvn versions:set -DnewVersion=$VERSION
```
where `$VERSION` is in the following format: `x.y.z`
This should set the desired version in the [pom.xml](../concordium-sdk/pom.xml).
The [pom.xml](../concordium-sdk-examples/pom.xml) in [examples](../concordium-sdk-examples) should also be changed to use this new version

1. Commit and push this new version.
1. Do a release via Github (the tagged version and the $VERSION just set should be the same)
1. Set a new `SNAPSHOT` version via:

Publishing the release invokes the `Release` workflow. The `Release` workflow generates and attaches the following to the release.
1. A jar of the compiled SDK including native dependencies for windows, ubuntu and macos.
2. A jar-with-dependencies containing all of the above as well as all maven dependencies.
3. A jar with javadoc that can be imported to the users IDE to add documentation.

Javadoc is also uploaded to https://concordium.github.io/concordium-java-sdk/javadoc/concordium-sdk/apidocs/

```
mvn versions:set -DnewVersion=$VERSION-SNAPSHOT
```
where `$VERSION` is the just released version.
The [pom.xml](../concordium-sdk-examples/pom.xml) in [examples](../concordium-sdk-examples) should also be changed to use this new snapshot version
1. Commit and push the `SNAPSHOT` version.

