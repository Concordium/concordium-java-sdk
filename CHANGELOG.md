# Changelog

## Unreleased changes
- Added support for GRPC V2 `GetBlockSpecialEvents` for retrieving a list of events not generated directly by any transaction
- Added support for GRPC V2 `GetAccountInfo`
- Added support for GRPC V2 `GetAccountList`
- Added support for GRPC V2 `GetNextAccountNonceNumber`
- Added support for GRPC V2 `GetBlockItems`
- Added support for GRPC V2 `GetConsensusStatus`
- Added support for GRPC V2 `GetCryptographic Parameters`
- Added support for GRPC V2 `GetIdentityProviders`
- Added support for GRPC V2 `SendBlockItem` for sending a transaction.
- Added support for GRPC V2 `GetBlockItemStatus` for retrieving the status of an individual transaction
- Fixed incorrect transaction event with respect to a transaction status for when a delegator decreased its stake.
- Added support for GRPC V2 `GetBlockInfo` for retrieving information of the block being queried.
- Added support for GRPC V2 `GetTokenomicsInfo` for acquiring various tokenomics related information for a block.
- Added support for GRPC V2 `GetElectionInfo`

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
