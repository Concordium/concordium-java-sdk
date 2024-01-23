# Wallet example for Android applications

This folder contains a small project for an Android app that uses the Concordium Android SDK.

For the app to build, one must add the Android library (AAR file), to the `app/libs` folder. This can either be done by downloading it from the repository's releases, or building the [concordium-android-sdk](../concordium-android-sdk/) with `mvn install`. (This command will copy the library on `app/libs`)

The app is able to take a BIP39 seed phrase, and use that to create/recover an identity, create an account and send transfers using the created account.