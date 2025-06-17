# Releasing

In order to release the SDK on Maven Central, do the following.

1. Change all the SDK artifacts version to `X.Y.Z` where it previously was `X.Y.Z-SNAPSHOT`:
   from the root of the repository invoke
   ```
   mvn versions:set -DnewVersion=$VERSION
   ```
   where `$VERSION` is in the following format: `X.Y.Z`.
   This should set the desired version in the various [pom.xml](pom.xml) files, including
   the [pom.xml](concordium-sdk/pom.xml) in [concordium-sdk](concordium-sdk) and
   the [pom.xml](concordium-android-sdk/pom.xml) in [concordium-android-sdk/](concordium-android-sdk)
2. Update the Changelog
3. Commit the version and changelog update
4. Tag the commit in the following format: `v.X.Y.Z`
5. Manually trigger the `Release` workflow for the tag.
   [Ask for approval](https://github.com/orgs/Concordium/teams/java-sdk-publishers) for the last part of the workflow
6. Merge (squash) the release branch into `main`
7. Wait for the version to appear under https://repo1.maven.org/maven2/com/concordium/sdk/
8. Manually add Changelog entries of this version to the GitHub release,
   which was automatically created by the `Release` workflow

The released artifacts must become available here:

- [java sdk](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-sdk)
- [android archive](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-android-sdk)

## Javadoc

The `Relase` workflow also creates the branch `javadoc` if it does not already exist.
This branch contains the generated javadoc which is visible
at: [https://concordium.github.io/concordium-java-sdk/javadoc/concordium-sdk/apidocs/](https://concordium.github.io/concordium-java-sdk/javadoc/concordium-sdk/apidocs/)

## Useful links

[Maven – Releasing the deployment](https://central.sonatype.org/publish/release/)
