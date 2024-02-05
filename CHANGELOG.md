# Changelog

## Unreleased changes

- Introduced Cis2Client for interfacing with CIS2 compliant smart contracts.
- Support for deserializing contract update transactions.

## 6.1.0
- Purge remaining usages of V1 GRPC API.
- Added support for android through an AAR artifact.
- Added `ConcordiumHdWallet` class for deriving Concordium specific keys and randomness from a seed phrase.
- Added `Identity` class with methods `createIdentityRequest` for creating identity requests and `createIdentityRecoveryRequest` for creating identity recovery requests.
- Added `Credential` class exposing methods required to create, sign and sending a credential deployment transaction.
- Added release on maven central.

## 6.0.0
- Added method `waitUntilFinalized` for waiting until a given transaction is finalized.
- Removed deprecated V1 API from the SDK. Consumers of the (now removed) `getBlockSummary` endpoint should refer to `GetBlockTransactionEvents`, `GetBlockSpecialEvents` and `GetBlockPendingUpdates`.
- Added support for GRPC V2 `GetWinningBakersEpoch` for getting a list of bakers that won the lottery in a particular historical epoch. Only available when querying a node with version at least 6.1.
- Added support for GRPC V2 `GetFirstBlockEpoch` for getting the block hash of the first finalized block in a specified epoch. Only available when querying a node with version at least 6.1.
- Added support for GRPC V2 `GetBakerEarliestWinTime` for getting the projected earliest time at which a particular baker will be required to bake a block. Only available when querying a node woth version at least 6.1.
- Added support for GRPC V2 `GetBakerRewardPeriodInfo` for getting all the bakers in the reward period of a block. Only available when querying a node with version at least 6.1.
- Added support for GRPC V2 `GetBlockCertificates` for retrieving certificates for a block supporting ConcordiumBF, i.e. a node with at least version 6.1.
- Extended `CurrentPaydayStatus` with `CommissionRates` that apply for the current reward period. Requires at least node version 6.1.
- Implemented custom JSON serialization of `AbstractAddress` to enable `AbstractAddress` as a smart contract parameter, and added class `ListParam` for conveniently using lists of objects, `AbstractAddress`, `ContractAddress` and `AccountAddress` as smart contract parameters.
- Added support for creating and serializing smart contract parameters using the abstract class `SchemaParameter` and a provided `Schema`.

## 5.1.0
- Fixed a regression that made it harder to deserialize transactions from bytes.
- toString on CCD amount now displays the amount as micro CCD.
- Fix bug where `Details` type was not correctly set when querying status of a block item.

## 5.0.2
- Remove an unused import from `ClientV2` which could make compilation fail on some jdks.

## 5.0.1

- Fix a bug that caused custom http headers to be set for `ClientV2`.

## 5.0.0

- Added typing for baker keys and moved `createBakerKeys` to `BakerKeys`. Likewise the `TransactionFactory.newUpdateBakerKeys` takes in
  the generated baker keys.
- Added support for `getBlockTransactionEvents` for retrieving events emitted by transactions.
- Added support for `InvokeInstance` for executing a smart contract instance locally.
- Removed `Account` instead one should simply use `AccountAddress`.
- All timestamps are now exposed as `Timestamp`s with convenience method for getting ZonedDateTime.
- Renamed `AccountRequest` to `AccountQuery`.
- Added support for GRPC V2 `GetBlockChainParameters` for retrieving the parameters of the chain.
- Deprecated the `Client`. Instead one should use `ClientV2` which leverages the GRPCv2 API of the node.
- Support for protocol V6.
- Added support for GRPC V2 `GetPeersInfo` for retrieving information of the peers that the node holds.
- Added support for GRPC V2 `GetNodeInfo` for retrieving various information of the node queried.
- Added support for GRPC V2 `GetBannedPeers` for retrieving a list of the banned peers.
- Added support for GRPC V2 `BanPeer` for banning a peer,
- Added support for GRPC V2 `UnbanPeer` for unbanning a peer
- Added support for GRPC V2 `GetBlockSpecialEvents` for retrieving a list of events not generated directly by any transaction
- Added support for GRPC V2 `GetBlockFinalizationSummary` for retrieving summary of the finalization data in a given block
- Added support for importing mobile & browser extension wallet exports as well as exports in the genesis format
- Added support for GRPC V2 `GetAccountInfo`
- Added support for GRPC V2 `GetAccountList`
- Added support for GRPC V2 `GetNextAccountNonceNumber`
- Added support for GRPC V2 `GetBlockItems`
- Added support for GRPC V2 `GetConsensusStatus`
- Added support for GRPC V2 `GetCryptographic Parameters`
- Added support for GRPC V2 `GetIdentityProviders`
- Added support for GRPC V2 `SendBlockItem` for sending a transaction.
- Added support for GRPC V2 `PeerConnect` for instructing the node to connect to a specific peer.
- Added support for GRPC V2 `PeerDisconnect`  for instructing the node to drop a peer.
- Added support for GRPC V2 `GetPeersInfo` for acquiring information of the peers that the node is connected to.
- Added support for GRPC V2 `GetBlockItemStatus` for retrieving the status of an individual transaction
- Added support for GRPC V2 `DumpStart` which instructs the node to start dumping network packages at a specified path.
- Added support for GRPC V2 `DumpStop` which instructs the node to stop dumping packages.
- Fixed incorrect transaction event with respect to a transaction status for when a delegator decreased its stake.
- Added support for GRPC V2 `GetBlockInfo` for retrieving information of the block being queried.
- Added support for GRPC V2 `GetTokenomicsInfo` for acquiring various tokenomics related information for a block.
- Added support for GRPC V2 `GetInstanceList` for retrieving the contract instances at a certain block.
- Added support for GRPC V2 `GetAncestors` for getting specified no of ancestor blocks for the input block.
- Added support for GRPC V2 `GetBakerList` for retrieving the bakers at a certain block.
- Added support for GRPC V2 `GetAccountNonFinalizedTransactions` for querying transactions from a particular account that have not yet been finalized.
- Added support for GRPC V2 `GetBranches` for gettting branches of the chain (i.e. decendants of the last finalized block).
- Added support for GRPC V2 `GetPassiveDelegators` for getting the passive delegators at a certain block.
- Added support for GRPC V2 `GetPoolDelegators` for getting the delegators for a specified baking pool at a certain block.
- Added support for GRPC V2 `GetElectionInfo` for getting the bakers at a certain block and other metadata of the election.
- Added support for GRPC V2 `GetInstanceState` for getting state of any Contract Instance
- Added support for GRPC V2 `InstanceStateLookup` for getting value of a particular key in Contract Instance.
- Added support for GRPC V2 `GetPoolInfo` for gettting information about a given pool at the end of a given block.
- Added support for GRPC V2 `GetModuleList` for getting list of smart contract module at the end of a given block.
- Added support for GRPC V2 `GetInstanceInfo` for getting information about a contract instance at the end of a given block.
- Added support for GRPC V2 `GetPoolDelegatorsRewardPeriod` for getting fixed list of Pool Delegators for a given pool at the end of a given block.
- Added support for GRPC V2 `GetPassiveDelegatorsRewardPeriod` for getting fixed list of Passive Delegators for a given pool at the end of a given block.
- Added support for GRPC V2 `GetNextUpdateSequenceNumbers` for getting next available sequence numbers for updating chain parameters after a given block.
- Added support for GRPC V2 `GetBlockPendingUpdates` for getting the pending updates to chain parameters at the end of a given block.
- Added support for GRPC V2 `Shutdown` For shutting down the network layer of the Node.
- Added support for GRPC V2 `GetModuleSource` for getting binary source of Smart Contract Module.

## 4.2.0
- Added initial support for GRPC V2
- Added initial support for GRPC V2 query GetAnonymityRevokers
- Added CLI example for GRPC V2 query GetAnonymityRevokers
- Added support for GRPC V2 GetBlocks
- Added support for GRPC V2 GetFinalizedBlocks
- Added support for M1/M2 Mac

## 4.1.0
- Added support for Transfer With Schedule.
- Added support for Transfer With Schedule With Memo.
- Added support for Update Credentials Keys.
- Added support for Transfer to public wallet.
- Added support for Transfer To Encrypted wallet.
- Added support for Encrypted Transfer.
- Added support for Encrypted Transfer with Memo.
- Added support for Configuring a baker.
- Added support for Configuring the account as a Delegator.
- A new native dependency has been introduced in order to support the new transaction types mentioned above,
  As a result native dependencies need to be rebuilt before building the Java project.

## 4.0.0
- Support for Protocol 5.
- Added support for initializing smart contracts.
- Added support for Update Contract.
- Added support for Deploy Module transaction.
- Fix a bug where pending changes for delegators were not visible via `getAccountInfo`.
- Removed `BlocksAtHeightRequest.newRelative(long height, ProtocolVersion protocolVersion, boolean restrictedToGenesisIndex)`
  as it was only a valid call on `mainnet` as all protocols exists there. However this may not be true on e.g. `testnet`.
  Note. `BlocksAtHeightRequest.newRelative(long height, int genesisIndex, boolean restrictedToGenesisIndex)` still exists for the purpose
  of getting blocks at a relative height.
- Fix a bug where a `RejectReason` for setting delegation target to a non baker was not correctly parsed.
- Fix a bug where `updateTimeParameters` was not correctly parsed into the `TransactionContents` enum.
- Fix a bug where `updateCooldownParameters` was not correctly parsed into the `TransactionContents` enum.
- Fix a bug where `mintDistributionCPV1` was not correctly parsed into the enqueued chain update.
- Fix a bug where `timeParametersCPV1` was not correctly parsed into the enqueued chain update
- Fix a bug where `cooldownParametersCPV1` was not correctly parsed into the enqueued chain update.
- Fix a bug where `mintPerPayday` was not properly parsed.
- Fix a bug where `updateRootKeys` was not properly parsed.

## 3.1.0

- Fixed bug in `UpdateEnqueuedResult` which parsed `effectiveTime` in wrong.
- Added support for `Uptime` query.
- Added support for Account Transaction Register Data.
- Added support for query `PeerTotalSent`
- Added support for query `PeerList`
- Added support for query `PeerStats`
- Added support for query `PeerVersion`
- Added support for query `GetTransactionStatusInBlock`
- Added support for query `GetAccountNonFinalizedTransactions`
- Added support for query `PeerConnect`
- Added support for query `GetBakerList`
- Added support for query `GetPoolStatus`
- Added support for query `PeerTotalReceived`
- Added support for query `GetModuleSource`
- Added support for query `GetModuleList`
- Added support for query `GetBirkParameters`
- Added support for query `NodeInfo`
- Added support for query `Shutdown`
- Added support for query `GetAncestors`
- Added support for query `JoinNetwork`
- Added support for query `LeaveNetwork`
- Added support for query `GetRewardStatus`
- Added support for query `GetInstanceInfo`
- Added support for query `GetInstances`
- Added support for query `GetAccountList`
- Added support for query `GetBannedPeers`
- Added support for query `GetBranches`
- Added support for query `GetIdentityProviders`
- Added support for query `GetAnonymityRevokers`
- Added support for query `BanNode`
- Added support for query `UnBanNode`
- Added support for query `StartBaker`
- Added support for query `StopBaker`

## 3.0.0
- Stronger typing for various places in the API.
- Renamed GTUAmount to CCDAmount
- New CredentialRegistrationId type.
- BlockSummary API now reflects the underlying types better.
- getAccountInfo can now be queried either via `AccountAddress`, `AccountIndex` or `CredentialRegistrationId` via `AccountRequest`.

## 2.4.0
- Support for `getCryptographicParameters`

## 2.3.0
- Expose more types related to `TransactionSummary`

## 2.2.0
- Support for sending raw transactions.

## 2.1.0
- CredentialRegistrationID type introduced and AccountAddress can be derived from it.

## 2.0.0
- Support for configuring TLS.

## 1.7.0
- Allow consumers to provide extra HTTP headers for the gRPC connection.

## 1.6.1
- Fix a bug which caused the `Client` to use a global absolute timeout from when it was initialized.

## 1.6.0
- `AccountTransaction` now exposes a `getType` function which
   returns the type of the underlying `Payload`.

## 1.5.0
- Exposed more of the `transactions` in the API.

## 1.4.0
- Fix parsing bug for `AccountInfo` where scheduled releases was not included.
- Transaction `Status` is now public.
- `BlockItem` is now public.

## 1.3.0
- Support for transaction deserialization.
- Support for `getBlockInfo`.

## 1.2.0
- Support for `getBlocksAtHeight`

## 1.1.0
- Support for account alias.
