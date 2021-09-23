# Releasing


In order to release the code, do the following.


1. Bump the version to the new desired version by invoking

```
mvn versions:set -DnewVersion=$VERSION
```
where `$VERSION` is in the following format: `x.y.z`
This should set the desired version in the [pom.xml](../concordium-sdk/pom.xml).

1. Commit and push this new version.
1. Do a release via Github (the tagged version and the $VERSION just set should be the same)
1. Set a new `SNAPSHOT` version via:
```
mvn versions:set -DnewVersion=$VERSION-SNAPSHOT
```
where `$VERSION` is the just released version.

1. Commit and push the `SNAPSHOT` version.

