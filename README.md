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

### Chain Queries

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

- `getModuleSource`
```java
ModuleSource getModuleSource(ModuleRef moduleRef, Hash blockHash) throws ModuleNotFoundException
```
Get the source of a smart contract module.
Throws `ModuleNotFoundException` if the module could not be found for the given block.

- `getModuleList`
```java
ImmutableList<ModuleRef> getModuleList(final Hash blockHash) throws BlockNotFoundException
```
Get a list of smart contract modules that exist in the state after the given block.

- `getBirkParameters`
```java
BirkParameters getBirkParameters(Hash blockHash) throws BlockNotFoundException
```
Get an overview of the parameters used for baking for a given block.

- `getInstanceInfo`
```java
InstanceInfo getInstanceInfo(final ContractAddress contractAddress, final Hash blockHash) throws ContractInstanceNotFoundException
```
Get the smart contract instance information given the `ContractAddress` for the provided block.
Throws `ContractInstanceNotFoundexception` if the instance could not be found for the given block.

- `getInstances`
```java
ImmutableList<ContractAddress> getInstances(Hash blockHash) throws BlockNotFoundException
```
Get the list of smart contract instances in a given block at block commitment.
Throws a `BlockNotFoundException` if an invalid block hash was given.

- `getAccountList`
```java
ImmutableList<AccountAddress> getAccountList(Hash blockHash) throws BlockNotFoundException
```
Get the list of accounts in the given block.
Throws a `BlockNotFoundException` if an invalid block hash was given.

- `getAncestors`
```java
ImmutableList<Hash> getAncestors(Hash blockHash, long num) throws BlockNotFoundException
```
Get a list of block hashes that preceding the provided block hash and with a maximum size of the provided number.

- `getRewardStatus`
```java
RewardsOverview getRewardStatus(final Hash blockHash) throws BlockNotFoundException
```
Get the information about total amount of CCD and the state of various administrative accounts.

- `getBranches`
```java
Branch getBranches()
```
Get the branches of the node's tree. Branches are all live blocks that
are successors of the last finalized block. In particular this means
that blocks which do not have a parent are not included in this
response.

- `getIdentityProviders`
```java
ImmutableList<IdentityProviderInfo> getIdentityProviders(Hash blockHash) throws BlockNotFoundException
```
Get the list of identity providers in the given block.

- `getAnonymityRevokers`
```java
ImmutableList<AnonymityRevokerInfo> getAnonymityRevokers(Hash blockHash) throws BlockNotFoundException
```
Get the list of anonymity revokers in the given block.

- `getBakerList`
```java
ImmutableList<BakerId> getBakerList(Hash blockHash) throws BlockNotFoundException
```
Get the IDs of the bakers registered in the given block.

- `getTransactionStatusInBlock`
```java
    TransactionStatusInBlock getTransactionStatusInBlock(
            Hash transactionHash,
            Hash blockHash) throws TransactionNotFoundInBlockException
```

- `getAccountNonFinalizedTransactions`
```java
ImmutableList<Hash> getAccountNonFinalizedTransactions(AccountAddress address)
```
Get the list of transactions hashes for transactions that claim to be from the given account, 
but which are not yet finalized. They are either committed to a block or still pending.


- `getPoolStatus`
```java
PoolStatus getPoolStatus(Hash blockHash, BakerId bakerId) throws PoolNotFoundException
```
Get the status of a given baker pool.
Throws `PoolNotFoundException` if the pool was not found.

- `getPassivePoolStatus`
```java
PoolStatus getPassivePoolStatus(Hash blockHash) throws PoolNotFoundException
```
Get the status of passive delegation at the given block.

### Node & P2P Queries

- `getUptime`
```java
Duration getUptime()
```
Retrives the uptime of node. Duration since the node was started.

- `getTotalSent`
```java
long getTotalSent()
```
Retrives total number of packets sent from the node.

- `getPeerStatistics`
```java
PeerStatistics getPeerStatistics(final boolean includeBootstrappers)
```
Retrieves the statistics of the peers that the node is connected to.

- `getPeerList`
```java
ImmutableList<Peer> getPeerList(boolean includeBootstrappers) throws UnknownHostException
```
Retrieves the peers that the node is connected to.
The boolean flag `includeBootstrappers` indicates whether bootstrapper nodes 
should be included in the response.

- `getVersion`
```java
SemVer getVersion()
```
Retrives the node software version.

- `getNodeInfo`
```java
NodeInfo getNodeInfo()
```
Retrieve meta information about the node e.g. if it's baking or not, the local time of the node etc.

- `shutdown`
```java
boolean shutdown()
```
Shut down the node.

- `joinNetwork`
```java
Boolean joinNetwork(final UInt16 networkId)
```
Ask the node to join the specified network.

- `leaveNetwork`
```java
boolean leaveNetwork(final UInt16 networkId)
```
Ask the node to leave the specified network.

- `getBannedPeers`
```java
ImmutableList<Peer> getBannedPeers() throws UnknownHostException
```
Get a list of the banned peers.

- `getTotalReceived`
```java
long getTotalReceived()
```
Query for the total number of packets that the node has received thus far.

- `peerConnect`
```java
boolean peerConnect(InetSocketAddress address)
```
Instruct the node to try to connect to the given peer. This also adds the address to the list of trusted addresses. 
These are addresses to which the node will try to keep connected to at all times.

- `startBaker`
```java
boolean startBaker()
```
Start the baker.

- `stopBaker`
```java
boolean stopBaker()
```
Stop the baker.

- `banNode`
```java
boolean banNode(final BanNodeRequest request)
```
Ban a specific node by Id or Ip address. Returns true if specified node was banned false otherwise.

- `unBanNode`
```java
boolean unBanNode(final InetAddress ip)
```
Unban a specific node by Ip address. Returns true if specified node was unbanned false otherwise.

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

- Register Data
Registers a maximum of 256 bytes on the chain.

- Initialize Contract
Initialize a smart contract from an already deployed module.

- Deploy Contract
Deploy a smart contract module.

- Update Contract
Update a smart contract.

- TransferScheduleTransaction
Send funds from one account to another with an attached schedule.

- TransferScheduleWithMemoTransaction
Send funds from one account to another with an attached schedule with an associated `Memo`.

- UpdateCredentialKeysTransaction
Updates signing keys of a specific credential.

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

### Chain Queries

#### getAccountInfo

```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
AccountRequest accountRequest = AccountRequest.from(AccountAddress.from("3uyRpq2NPSY4VJn8Qd1uWGcUQTGpCAarHTtqpneiSGqv36Uhna"));
try {
    AccountInfo accountInfo = client.getAccountInfo(accountRequest, blockHash);
} catch(AccountNotFoundException e) {
    Log.err(e.getMessage);
}
```

#### getNextAccountNonce

```java
AccountAddress accountAddress = AccountAddress.from("3uyRpq2NPSY4VJn8Qd1uWGcUQTGpCAarHTtqpneiSGqv36Uhna");
AccountNonce accountNonce = client.getNextAccountNonce(accountAddress);
```

#### getTransactionStatus

```java
try {
    Hash transactionHash = Hash.from("78674107c228958752170db61c5a74929e990440d5da25975c6c6853f98db674");
    TransactionStatus transactionStatus = client.getTransactionStatus(transactionHash);
} catch (TransactionNotFoundException e) {
    Log.err(e.getMessage());
}
```

#### getConsensusStatus

```java
ConsensusStatus consensusStatus = client.getConsensusStatus();
```

#### getBlockInfo
```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
try {
    BlockInfo blockInfo = client.getBlockInfo(blockHash);
} catch(BlockNotFoundException e) {
    Log.Err(e.getMessage());
}
```

#### getBlockSummary
```java
Hash blockHash = Hash.from("3d52e63350bfd21676ecbf6ce29688e3be6bff86cbacfe138aac107b64d29ba1");
try {
    BlockSummary blockSummary = client.getBlockSummary(blockHash);
} catch(BlockNotFoundException e) {
    Log.Err(e.getMessage());
}
```

#### getModuleSource
```java
ModuleSource moduleSource = client.getModuleSource(
        Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c08"),
        Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"));
```

#### getModuleList
```java
ImmutableList<ModuleRef> list = client
        .getModuleList(Hash.from("a7ddcc750d6e2a5d72c8d3eedee1453269b1712f8dd36f1d94d5e606df92e7fe"));
```

#### getBirkParameters
```java
BirkParameters birkParams = client
        .getBirkParameters(Hash.from("a7ddcc750d6e2a5d72c8d3eedee1453269b1712f8dd36f1d94d5e606df92e7fe"));
```

#### getInstanceInfo
```java
InstanceInfo instanceInfo = client.getInstanceInfo(
                new ContractAddress(0, 0),
                Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
```

#### getInstances
```java
ImmutableList<ContractAddress> contractInstances = client
                .getInstances(Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
```

#### getAccountList
```java
ImmutableList<AccountAddress> accounts = client
                .getAccountList(Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"));
```

#### getAncestors
```java
ImmutableList<Hash> ancestors = client.getAncestors(Hash.from("9741d166fdc9b70a183d6c22f79e6f87c236f56c545c9b5f1114847fecc7ba39"), 10);
```

#### getRewardStatus
```java
RewardsOverview rewardsStatus = client
        .getRewardStatus(Hash.from("a7ddcc750d6e2a5d72c8d3eedee1453269b1712f8dd36f1d94d5e606df92e7fe"));
```

#### getBranches
```java
Branch branch = client.getBranches();
```

#### getIdentityProviders
```java
ImmutableList<IdentityProviderInfo> identityProviders = client.getIdentityProviders(
        Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"));
```

#### getAnonymityRevokers
```java
ImmutableList<AnonymityRevokerInfo> anonymityRevokers = client
                .getAnonymityRevokers(Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"));
```

#### getBakerList
```java
ImmutableList<BakerId> bakerList = client
                .getBakerList(Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"));
```

#### getPoolStatus
```java
PoolStatus poolStatusPassiveDeletation = client
        .getPassivePoolStatus(
                Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"));

PoolStatus bakerPoolStatus = client.getPoolStatus(
        Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb"), 
                BakerId.from(1));
```

#### getTransactionStatusInBlock
```java
TransactionStatusInBlock ret = client.getTransactionStatusInBlock(
                Hash.from("ea88c209c40f5828aeedf3326f314f66b7adf49e754a94f29b72e9d334d82eb7"),
                Hash.from("2f15e174a42ec63d68abd8597e69573cf83199aacbfb9dae03c255d35b84aafb")
        );
```
Get the status of a transaction in a given block.

#### getAccountNonFinalizedTransactions
```java
ImmutableList<Hash> ret = client
                .getAccountNonFinalizedTransactions(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"));
```

### Node & P2P Queries

#### getUptime
```java
Duration uptime = client.getUptime();
```

#### getTotalSent
```java
long sentPackets = client.getTotalSent();
```
#### getPeerStatistics
```java
boolean shouldIncludeBootstrapperNodes = true;
PeerStatistics peerStatistics = client.getPeerStatistics(shouldIncludeBootstrapperNodes);
```

#### getPeerList
```java
ImmutableList<Peer> peers = client.getPeerList(true);
```

#### getVersion
```java
SemVer version = client.getVersion();
```

#### getNodeInfo
```java
NodeInfo = client.getNodeInfo();
```

#### peerConnect
```java
boolean ret = client.peerConnect(InetSocketAddress.createUnresolved("127.0.0.1", 8080));
```

#### startBaker
```java
client.startBaker();
```

#### stopBaker
```java
client.stopBaker();
```

#### banNode
```java
client.banNode(BanNodeRequest.from("NodeId"))
```

```java
client.banNode(BanNodeRequest.from(InetAddress.getByName("127.0.0.1")))
```

#### unBanNode
```java
client.unBanNode(InetAddress.getByName("127.0.0.1"));
```

#### getTotalReceived
```java
long totalReceived = client.getTotalReceived();
```

#### shutdown
```java
client.shutdown();
```

#### joinNetwork
```java
client.joinNetwork(UInt16.from(200));
```

#### leaveNetwork
```java
client.leaveNetwork(UInt16.from(200));
```

#### getBannedPeers
```java
ImmutableList<Peer> bannedPeers = client.getBannedPeers();
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


### Initialising a smart contract transaction 
The following example demonstrates how to initialize a smart contract from a module, which has already been deployed.
The name of the contract is "CIS2-NFT".
In this example, the contract does not take any parameters, so we can leave parameters as an empty buffer.

```java
try{
    byte[] paramBytes = new byte[0];
    Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));
    InitContractTransaction transaction = TransactionFactory.newInitContract()
        .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
        .nonce(AccountNonce.from(nonceValue))
        .expiry(Expiry.from(expiry))
        .signer(signer)
        .payload(InitContractPayload.from(0, moduleRef.getBytes(), "init_CIS2-NFT", paramBytes))
        .maxEnergyCost(UInt64.from(3000))
        .build();
    client.sendTransaction(transaction);

} catch (TransactionRejectionException e) {
    // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}

```

#### Deploy module transaction
The following example demonstrates how to construct a "deployModule" transaction, which is used to deploy a smart contract module.

 ```java
 try{
    byte[] array = Files.readAllBytes(Paths.get("path_to_wasm_file"));
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));

    DeployModuleTransaction transaction = TransactionFactory.newDeployModule()
        .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
        .nonce(AccountNonce.from(nonceValue))
        .expiry(Expiry.from(expiry))
        .signer(signer)
        .module(WasmModule.from(array, 1))
        .maxEnergyCost(UInt64.from(10000))
        .build();
    client.sendTransaction(transaction);
} catch (TransactionRejectionException e) {
     // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}
```

#### Update contract transaction
The following example demonstrates how to construct a "updateContract" transaction, which is used to update a smart contract.

```java
try{
    byte[] paramBytes = new byte[0];
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));

    UpdateContractTransaction transaction = TransactionFactory.newUpdateContract()
        .sender(AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e"))
        .nonce(AccountNonce.from(nonceValue))
        .expiry(Expiry.from(expiry))
        .signer(signer)
        .payload(UpdateContractPayload.from(
            10,
            ContractAddress.from(789, 0),
            "CIS2NFT",
            "mint",
            paramBytes)
        )
        .maxEnergyCost(UInt64.from(3000))
        .build();
    client.sendTransaction(transaction);
 } catch (TransactionRejectionException e) {
    // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}
```
#### Transfer schedule transaction
The following example demonstrates how to construct a "transferSchedule" transaction, which is used to transfer CCD with schedule.

 ```java
try{
    Schedule[] schedule = new Schedule[1];
    schedule[0] = Schedule.from(1662869154000L, 10);
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));
    TransactionScheduleTransaction transaction = TransactionFactory.newScheduledTransfer()
        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
        .nonce(AccountNonce.from(78910))
        .expiry(Expiry.from(123456))
        .signer(signer)
        .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
        .schedule(schedule)
        .build();
    client.sendTransaction(transaction);
} catch (TransactionRejectionException e) {
    // Handle the rejected transaction, here we simply log it.
    Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}
```
#### Transfer schedule with memo transaction
The following example demonstrates how to construct a "transferScheduleWithMemo" transaction, which is used to transfer CCD with schedule and memo.

 ```java
try{
    Schedule[] schedule = new Schedule[1];
    schedule[0] = Schedule.from(1662869154000L, 10);
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));
    TransactionScheduleWithMemoTransaction transaction = TransactionFactory.newScheduledTransferWithMemo()
        .sender(AccountAddress.from("3JwD2Wm3nMbsowCwb1iGEpnt47UQgdrtnq2qT6opJc3z2AgCrc"))
        .nonce(AccountNonce.from(78910))
        .expiry(Expiry.from(123456))
        .signer(signer)
        .to(AccountAddress.from("3bzmSxeKVgHR4M7pF347WeehXcu43kypgHqhSfDMs9SvcP5zto"))
        .schedule(schedule)
        .memo(Memo.from(new byte[]{1, 2, 3, 4, 5}))
        .build();
    client.sendTransaction(transaction);
 } catch (TransactionRejectionException e) {
     // Handle the rejected transaction, here we simply log it.
     Transaction rejectedTransaction =  e.getTransaction();
    Hash rejectedTransactionHash = rejectedTransaction.getHash();
    String rejectedTransactionHashHex = rejectedTransactionHash.asHex();
    Log.err("Transaction " + rejectedTransactionHashHex + " was rejected");
}
```

#### Update Credential Keys transaction
The following example demonstrates how to construct a "UpdateCredentialKeys" transaction, which is used to 
update signing keys of a specific credential.

 ```java
 try{
    TransactionSigner signer = TransactionSigner.from(
        SignerEntry.from(Index.from(0), Index.from(0),
            firstSecretKey),
        SignerEntry.from(Index.from(0), Index.from(1),
            secondSecretKey));

    Map<Index, Key> keys =  new HashMap<>();
    Key newKey = new Key("ad6591a2deb03c32357615d73e144e01a49abad49671428d46db58cf2d4e4d87", "Ed25519");
    keys.put(Index.from(0), newKey);

    CredentialRegistrationId regId = CredentialRegistrationId.fromBytes(new byte[]{-90, 67, -42, 8, 42, -113, -128, 70, 15, -1, 39, -13, -1, 39, -2, -37, -3, -58, 0, 57, 82, 116, 2, -72, 24, -113, -56, 69, -88, 73, 66, -117, 84, -124, -56, 42, 21, -119, -54, -73, 96, 76, 26, 43, -23, 120, -61, -100});
    CredentialPublicKeys credentialPublicKeys = CredentialPublicKeys.from(keys, 1);
    
    UpdateCredentialKeysTransaction transaction = TransactionFactory.newUpdateCredentialKeys()
        .nonce(AccountNonce.from(525))
        .expiry(Expiry.from(1669466666))
        .signer(TransactionTestHelper.getValidSigner())
        .numExistingCredentials(UInt16.from(5))
        .keys(credentialPublicKeys)
        .credentialRegistrationID(regId)
        .build();
    client.sendTransaction(transaction);
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
