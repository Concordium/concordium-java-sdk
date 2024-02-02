# Concordium Java SDK
The Concordium Java SDK provides an interface to communicating with a Concordium node.

The package is released on [Maven Central](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-sdk).
The Android compatible package is also released on [Maven Central](https://central.sonatype.com/artifact/com.concordium.sdk/concordium-android-sdk).

# Prerequisites
1. Java 1.8
1. Maven
1. Rust
1. `make`

- Java has been tested with OpenJDK v.1.8.0_292.
- Maven has been tested with v.3.6.3.
- Rust has been tested with v.1.69


# Build and usage
To build the native library and put it into the correct folders all one has to do the following:

1. Clone this repository and remember to update git submodules, for example when cloning via
```bash
git clone https://github.com/Concordium/concordium-java-sdk.git --recurse-submodules
```
2. Run `make` from the root of this repository.
3. Run `mvn install` from the root of the [concordium-sdk](./concordium-sdk) folder.

The first step builds the native dependencies and the latter builds the resulting `.jar` file.

The two steps builds results in the following two `.jar` files.
1. A regular jar file called `concordium-sdk-{VERSION}.jar`.
2. A `fat` jar file called `concordium-sdk-{VERSION}-jar-with-dependencies.jar` which embeds the dependencies into one jar.

The "normal" jar can then be used from another project by adding it to the `pom.xml`:

```xml
 <dependencies>
        <dependency>
            <groupId>com.concordium.sdk</groupId>
            <artifactId>concordium-sdk</artifactId>
            <version>${VERSION}</version>
        </dependency>
 </dependencies>
```

The `fat` jar can then be used from another project by adding it to the `pom.xml`:

```xml
 <dependencies>
        <dependency>
            <groupId>com.concordium.sdk</groupId>
            <artifactId>concordium-sdk</artifactId>
            <version>${VERSION}</version>
            <classifier>jar-with-dependencies</classifier>
        </dependency>
 </dependencies>
```

## JNI
When doing changes wrt. the JNI two things have to be taken care of.
1. The Java native calls
1. The rust [implementation](./crypto-jni) of which the Java native binds to.

## The Java part
The SDK uses JNI to perform EDDSA_ED25519 signatures.
The JNI entrypoint is located in the [EDD25519.java](./concordium-sdk/src/main/java/com/concordium/sdk/crypto/ed25519/ED25519.java)
One can create a new header file by using the command: `javac -h . ED25519.java` from the root of [ed25519](./concordium-sdk/src/main/java/com/concordium/sdk/crypto/ed25519) folder.

## The Rust part

The output will be a header file, the contents hereof must be matched appropriately as described in the [documentation](https://docs.rs/jni/0.19.0/jni/) into the [lib.rs](crypto-jni/src/lib.rs) rust source file.

## Android
To build the android AAR package, one has to do the following:

1. Clone this repository and remember to update git submodules, for example when cloning via
```bash
git clone https://github.com/Concordium/concordium-java-sdk.git --recurse-submodules
```
2. Set the ANDROID_HOME environment variable to the path to your Android SDK installation.
3. Run `make add-android-targets` from the root of this repository.
4. Run `make android` from the root of this repository.
4. Run `mvn install -N` from the root of the repository.
5. Run `mvn install` from the root of the [concordium-android-sdk](./concordium-android-sdk) folder.

`make add-android-targets` adds the rust targets that the native libraries will be built for.
`make android` builds the native libraries for various targets using cargo-ndk.
And the final step builds the aar file `concordium-android-sdk.aar` in the [target](./concordium-android-sdk/target)  folder, which can be used in android projects.
Note that this uses the [Android Maven Plugin](http://simpligility.github.io/android-maven-plugin/), which is what requires the Android SDK, the specific version can be seen in its documentation.

Note that the minimum android SDK version for this package is 26.

# Usage
The [`ClientV2`](./concordium-java-sdk/blob/main/concordium-sdk/src/main/java/com/concordium/sdk/ClientV2.java) is the main entrypoint for the SDK.
It facilitates an API for communicating with the [node](https://github.com/Concordium/concordium-node) through the V2 API.
The `ClientV2` must be initialized with a `Connection` which holds information of the node URL, timeout duration etc. for the connection.

## Example of instantiating the Client
```java
Connection connection = Connection.newBuilder()
                .host(${node_host})
                .port(${node_port})
                .build();
ClientV2 client = ClientV2.from(connection);
```

where

- `node_host`  is the host of the node e.g. `127.0.0.1`
- `node_port` is the nodes rpc port e.g. `20000`

### Configuring the connection

One can also provide extra HTTP headers to the grpc calls by setting up the client as the following:

```java
Connection connection = Connection.newbuilder()
                .credentials(Credentials.builder()
                        .withAdditionalHeader(Header.from("HEADER1", "VALUE1"))
                        .withAdditionalHeader(Header.from("HEADER2", "VALUE2"))
                        .build())
                .host(${node_url})
                .port(${node_port})
                .timeout(${timeout})
                .build();
ClientV2 client = ClientV2.from(connection);
```

#### Enforcing TLS

It is also possible to enforce TLS to be used in the underlying connection e.g.

```java
Connection connection = Connection.newbuilder()
                .host(${node_url})
                .port(${node_port})
                .timeout(${timeout})
                .useTLS(TLSConfig.from(new File("/a/path/to/the/servers/certificate.pem")))
                .build();
ClientV2 client = ClientV2.from(connection);
```

Further the `Client` exposes a `close()` function which should be called when finished using the client in order to
perform an orderly shutdown of the underlying grpc connection.

# API Overview

The node API is exposed via `ClientV2` and transactions can be created using the `TransactionFactory` e.g. `TransactionFactory.newTransfer()`.

See the [examples](./concordium-sdk-examples/src/main/java/com/concordium/sdk/examples) for example usages of the API.

#### Sending a raw transaction
It is also possible to send a `raw` transaction via the client e.g., if the transaction has been generated somewhere else.
This can be done via (`AccountTransaction.fromBytes(the_raw_transaction_bytes)`.


# Custom Signing
By default the java library supports ED25519 signing via the native binding [ED25519SecretKey](./concordium-sdk/src/main/java/com/concordium/sdk/crypto/ed25519/ED25519SecretKey.java).

However the library also supports external signing services such as HSM's or other `secret managers` and the like.
In order to use an external signer one must implement the [Signer](./concordium-sdk/src/main/java/com/concordium/sdk/transactions/Signer.java) interface.

The interface exposes one function:

```java
byte[] sign(byte[] message) throws ED25519Exception;
```

A signer is then added to a `TransactionSigner` as follows:

```java
 TransactionSignerImpl signer = TransactionSigner.from(SignerEntry.from(Index.from(0), Index.from(1), new Signer() {
                    // The custom signer
                    @Override
                    public byte[] sign(byte[] bytes) throws ED25519Exception {
                        return new byte[0];
                    }
                }), ... );

```
