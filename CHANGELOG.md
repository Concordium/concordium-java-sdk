# Changelog

## Unreleased changes

- Fixed bug in `UpdateEnqueuedResult` which parsed `effectiveTime` in wrong.

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
