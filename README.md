# Concordium Java SDK
The Concordium Java SDK provides an interface to communicating with a Concordium node.


# Prerequisites
1. Java 1.8
1. Maven
1. Rust
1. `make`

- Java has been tested with OpenJDK v.1.8.0_292.
- Maven has been tested with v.3.6.3.
- Rust has been tested with v.1.53.0.


# Build and usage
To build the native library and put it into the correct folders all one has to do the following:

1. Clone this repositry and remember to update submodules, for example when cloning via
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
1. The rust [implementation](./ed25519-jni) of which the Java native binds to.

## The Java part
The SDK uses JNI to perform EDDSA_ED25519 signatures.
The JNI entrypoint is located in the [EDD25519.java](./concordium-sdk/src/main/java/com/concordium/sdk/crypto/ed25519/ED25519.java)
One can create a new header file by using the command: `javac -h . ED25519.java` from the root of [ed25519](./concordium-sdk/src/main/java/com/concordium/sdk/crypto/ed25519) folder.

## The Rust part

The output will be a header file, the contents hereof must be matched appropriately as described in the [documentation](https://docs.rs/jni/0.19.0/jni/) into the [lib.rs](./ed25519-jni/src/lib.rs) rust source file.


# Usage
The [`Client`](./concordium-java-sdk/blob/main/concordium-sdk/src/main/java/com/concordium/sdk/Client.java) is the main entrypoint for the SDK.
It facilitates an API for communicating with the [node](https://github.com/Concordium/concordium-node).
The `Client` must be initialized with a `Connection` which holds information of the node URL, node port, password etc.

## Example of instantiating the Client
```java
Connection connection = Connection.builder()
                .credentials(Credentials.from(${password}))
                .host(${node_url})
                .port(${node_port})
                .timeout(${timeout})
                .build();
Client client = Client.from(connection);
```

where

- `password` is the password to use
- `node_url`  is the url of the node
- `node_port` is the nodes rpc port
- `timeout` is the timeout for the GRPC connection (default is 15000 ms)

### Configuring the connection

One can also provide extra HTTP headers to the grpc calls by setting up the client as the following:

```java
Connection connection = Connection.builder()
                .credentials(Credentials.builder()
                        .authenticationToken(${password})
                        .withAdditionalHeader(Header.from("HEADER1", "VALUE1"))
                        .withAdditionalHeader(Header.from("HEADER2", "VALUE2"))
                        .build())
                .host(${node_url})
                .port(${node_port})
                .timeout(${timeout})
                .build();
Client client = Client.from(connection);
```

Note. One cannot provide an additional `Header` 'Authentication' as this is already used for the ${password}.

#### Enforcing TLS

It is also possible to enforce TLS to be used in the underlying connection e.g.

```java
Connection connection = Connection.builder()
                .credentials(Credentials.builder()
                        .authenticationToken(${password})
                        .withAdditionalHeader(Header.from("HEADER1", "VALUE1"))
                        .withAdditionalHeader(Header.from("HEADER2", "VALUE2"))
                        .build())
                .host(${node_url})
                .port(${node_port})
                .timeout(${timeout})
                .useTLS(TLSConfig.from(new File("/a/path/to/the/servers/certificate.pem")))
                .build();
Client client = Client.from(connection);
```


Further the `Client` exposes a `close()` function which should be called when finished using the client in order to
perform an orderly shutdown of the underlying grpc connection.

# API Overview

## Queries

- `getAccountInfo`
```java
AccountInfo getAccountInfo(AccountRequest request, Hash blockHash) throws AccountNotFoundException
```
Retrieves the [AccountInfo](./concordium-java-sdk/blob/main/concordium-sdk/src/main/java/com/concordium/sdk/responses/accountinfo/AccountInfo.java) given an `AccountRequest` and a block `Hash`.
Throws a `AccountNotFoundException` if the account was not found.

- `getNextAccountNonce`
```java
AccountNonce getNextAccountNonce(AccountAddress address)
```
Retrieves the next `AccountNonce` for the associated `AccountAddress`.

- `getTransactionStatus`
```java
TransactionStatus getTransactionStatus(Hash transactionHash) throws TransactionNotFoundException
```
Retrieves the `TransactionStatus` of a transaction given by the provided transaction `Hash`.
Throws a `TransactionNotFoundException` if the transaction was not found.

- `getConsensusStatus`
```java
ConsensusStatus getConsensusStatus()
```
Retrieves the `ConsensusStatus` which contains the summary of the current state of the chain from the perspective of the ndoe.

- `getBlockSummary`
```java
BlockSummary getBlockSummary(Hash blockHash) throws BlockNotFoundException
```
Retrieves the `BlockSummary` of a block given by the provided block `hash`.
Throws a `BlockNotFoundException` if the block was not found.

- `getBlocksAtHeight`
```java
BlocksAtHeight getBlocksAtHeight(BlocksAtHeightRequest height) throws BlockNotFoundException
```
Retrieves the `BlocksAtHeight` at the given height if one or more was found.
Throws a `BlockNotFoundException` if no blocks were found.

## Transactions

- `Hash sendTransaction(Transaction transaction) throws TransactionRejectionException`
Sends a transaction to the node. If it suceeds the `sendTransaction` function will 
return the associated transation `Hash`.

If the transaction was rejected by the node, then `sendTransaction` will throw a checked `TransactionException` (see below for details of common errors).

### Supported Transactions

- TransferTransaction 
Sends funds from one account to another.

- TransferWithMemo
Sends funds from one account to another with an associated `Memo`.


## Exceptions and general error handling

- `TransactionCreationException`
A checked exception which contains an inner exception with the reason for why the `Transaction` could not be created.

This could for an example happen when building a `TransferTransaction`:

```java
try {
    Transaction transfer = TransferTransaction
                            .builder()
                            .sender(sender)
                            .receiver(receiver)
                            .amount(amount)
                            .nonce(accountNonce)
                            .expiry(expiry)
                            .signer(signer)
                            .build();
} catch (TransactionCreationException e) {
    Log.err(e.getMessage());
}
```

- `TransactionRejectedException`
A checked exception which is being thrown when the `Transaction` was successfully created but was rejected by the node.
This exception contains the `Transaction` that failed.

- `TransactionNotFoundException`
A checked exception which is being thrown if `getTransactionStatus` was called with a non-existent transaction hash.
The exception contains the non existant hash.

- `BlockNotFoundException`
A checked exception which is being thrown if `getBlockSummary` was called with a non-existent block hash.
The exception contains the non-existent hash.

- `AccountNotFoundException`
A checked exception which is being thrown if `getAccountInfo` was called with a non existant block hash.
The exception contains the account address and the block hash that could not be found.

# Code Examples

## Creating the Client

```java
Connection connection = Connection.builder()
                .credentials(Credentials.from("password"))
                .host("127.0.0.1")
                .port(10000)
                .build();
Client client = Client.from(connection);
```

## Queries

### getAccountInfo

```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
AccountRequest accountRequest = AccountRequest.from(AccountAddress.from("3uyRpq2NPSY4VJn8Qd1uWGcUQTGpCAarHTtqpneiSGqv36Uhna"));
try {
    AccountInfo accountInfo = client.getAccountInfo(accountRequest, blockHash);
} catch(AccountNotFoundException e) {
    Log.err(e.getMessage);
}
```

### getNextAccountNonce

```java
AccountAddress accountAddress = AccountAddress.from("3uyRpq2NPSY4VJn8Qd1uWGcUQTGpCAarHTtqpneiSGqv36Uhna");
AccountNonce accountNonce = client.getNextAccountNonce(accountAddress);
```

### getTransactionStatus

```java
try {
    Hash transactionHash = Hash.from("78674107c228958752170db61c5a74929e990440d5da25975c6c6853f98db674");
    TransactionStatus transactionStatus = client.getTransactionStatus(transactionHash);
} catch (TransactionNotFoundException e) {
    Log.err(e.getMessage());
}
```

### getConsensusStatus

```java
ConsensusStatus consensusStatus = client.getConsensusStatus();
```

### getBlockInfo
```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
try {
    BlockInfo blockInfo = client.getBlockInfo(blockHash);
} catch(BlockNotFoundException e) {
    Log.Err(e.getMessage());
}
```

### getBlockSummary
```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
try {
    BlockSummary blockSummary = client.getBlockSummary(blockHash);
} catch(BlockNotFoundException e) {
    Log.Err(e.getMessage());
}
```

## Transactions

The [`TransactionFactory`](./concordium-sdk/src/main/java/com/concordium/sdk/transactions/TransactionFactory.java) provides a
overview of the supported `Transaction` types and a convenient way of obtaining the required `builder`.

### sendTransaction

```java
AccountAddress sender = AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc");
AccountAddress receiver = AccountAddress.from("3hYXYEPuGyhFcVRhSk2cVgKBhzVcAryjPskYk4SecpwGnoHhuM");
GTUAmount amount = GTUAmount.asMicro(17);
AccountNonce accountNonce = AccountNonce.from(78910);
Expiry expiry = Expiry.createNew().addMinutes(5);

ED25519SecretKey firstSecretKey = ED25519SecretKey.from("8f6494e89bb984dfd80bf9e5e9df9573754c9c245ed0981e95785406ca4969e7");
ED25519SecretKey secondSecretKey = ED25519SecretKey.from("d34c74af5db462c96b30360e74657bc74bf73a64ef7c43679e748e3f2de38f01");

TransactionSigner signer = TransactionSigner.from(
                SignerEntry.from(Index.from(0), Index.from(0),
                        firstSecretKey),
                SignerEntry.from(Index.from(0), Index.from(1),
                        secondSecretKey))

try {
    Transaction transaction = TransactionFactory.newTransfer()
                            .sender(sender)
                            .receiver(receiver)
                            .amount(amount)
                            .nonce(accountNonce)
                            .expiry(expiry)
                            .signer(signer)
                            .build();

    Hash transactionHash  = client.sendTransaction(transaction);
    // Do something with the succesfully sent transaction hash.
    // Here we simply log it.
    Log.Info("Transaction "+ transactionHash.asHex() +" was sent to node.");
} catch (TransactionRejectionException e) {
    // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
} catch (TransactionCreationException e) {
    // Handle creation failure, here we simply log it.
    Log.err(e.getMessage());
}

```

#### Sending a raw transaction
It is also possible to send a `raw` transaction via the client. 
Use `RawTransaction.from(rawTransactionBytes)` in order to retrieve a `Transaction` object which 
can then be passed to the `Client.sendTransaction`.

```java
try{
    RawTransaction rawTransaction = RawTransaction.from(transfer.getBytes());
    Hash transactionHash  = client.sendTransaction(transaction);
    // Do something with the succesfully sent transaction hash.
    // Here we simply log it.
    Log.Info("Transaction "+ transactionHash.asHex() +" was sent to node.");
} catch (TransactionRejectionException e) {
    // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}

```


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
