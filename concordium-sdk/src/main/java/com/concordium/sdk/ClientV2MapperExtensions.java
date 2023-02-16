package com.concordium.sdk;

import com.concordium.grpc.v2.AccountInfo;
import com.concordium.grpc.v2.Commitment;
import com.concordium.grpc.v2.CredentialPublicKeys;
import com.concordium.grpc.v2.Policy;
import com.concordium.grpc.v2.ReleaseSchedule;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.BlockInfoV2;
import com.concordium.sdk.responses.accountinfo.BakerPoolInfo;
import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.accountinfo.*;
import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.responses.accountinfo.credential.*;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.Description;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.UInt64;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.protobuf.ByteString;
import lombok.var;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.concordium.sdk.responses.nodeinfo.NodeInfo.UTC_ZONE;
import static com.google.common.collect.ImmutableList.copyOf;

/**
 * Object Mapping Extensions. Maps from GRPC types to client types and vice versa.
 */
interface ClientV2MapperExtensions {
    double HUNDRED_THOUSAND = 100_000D;

    static com.concordium.grpc.v2.BlockHashInput to(final BlockHashInput input) {
        var builder = com.concordium.grpc.v2.BlockHashInput.newBuilder();
        switch (input.getType()) {
            case BEST:
                builder.setBest(Empty.getDefaultInstance());
                break;
            case LAST_FINAL:
                builder.setLastFinal(Empty.getDefaultInstance());
                break;
            case GIVEN:
                if (Objects.isNull(input.getBlockHash())) {
                    throw new IllegalArgumentException("Block Hash should be set if type is GIVEN");
                }

                builder.setGiven(to(input.getBlockHash()));
                break;
            default:
                throw new IllegalArgumentException("Invalid type");
        }

        return builder.build();
    }

    static com.concordium.grpc.v2.BlockHash to(final Hash blockHash) {
        return com.concordium.grpc.v2.BlockHash.newBuilder().setValue(to(blockHash.getBytes())).build();
    }

    static ByteString to(final byte[] bytes) {
        return ByteString.copyFrom(bytes);
    }

    static AnonymityRevokerInfo to(final ArInfo arInfo) {
        return AnonymityRevokerInfo.builder()
                .arIdentity(to(arInfo.getIdentity()))
                .description(to(arInfo.getDescription()))
                .arPublicKey(to(arInfo.getPublicKey()))
                .build();
    }

    static ElgamalPublicKey to(final ArInfo.ArPublicKey publicKey) {
        return ElgamalPublicKey.from(publicKey.getValue().toByteArray());
    }

    static Description to(final com.concordium.grpc.v2.Description description) {
        return Description.builder()
                .description(description.getDescription())
                .url(description.getUrl())
                .name(description.getName())
                .build();
    }

    static int to(final ArInfo.ArIdentity identity) {
        return identity.getValue();
    }

    static <T1, T2> Iterator<T2> to(final Iterator<T1> iterator, final Function<? super T1, ? extends T2> to) {
        return Iterators.transform(iterator, to);
    }

    static BlockInfoV2 to(com.concordium.grpc.v2.ArrivedBlockInfo arrivedBlockInfo) {
        return BlockInfoV2.builder()
                .blockHash(to(arrivedBlockInfo.getHash()))
                .blockHeight(to(arrivedBlockInfo.getHeight()))
                .build();
    }

    static UInt64 to(AbsoluteBlockHeight height) {
        return UInt64.from(height.getValue());
    }

    static Hash to(BlockHash hash) {
        return Hash.from(hash.getValue().toByteArray());
    }

    static BlockInfoV2 to(FinalizedBlockInfo finalizedBlockInfo) {
        return BlockInfoV2.builder()
                .blockHash(to(finalizedBlockInfo.getHash()))
                .blockHeight(to(finalizedBlockInfo.getHeight()))
                .build();
    }

    static com.concordium.grpc.v2.AccountIdentifierInput to(AccountRequest accountIdentifier) {
        var builder = AccountIdentifierInput.newBuilder();

        switch (accountIdentifier.getType()) {
            case ADDRESS:
                builder.setAddress(to(accountIdentifier.getAddress()));
                break;
            case INDEX:
                builder.setAccountIndex(to(accountIdentifier.getIndex()));
                break;
            case CREDENTIAL_REGISTRATION_ID:
                builder.setCredId(to(accountIdentifier.getRegistrationId()));
                break;
        }

        return builder.build();
    }

    static CredentialRegistrationId to(com.concordium.sdk.transactions.CredentialRegistrationId registrationId) {
        return CredentialRegistrationId.newBuilder()
                .setValue(to(registrationId.getRegId()))
                .build();
    }

    static AccountIndex to(com.concordium.sdk.responses.AccountIndex index) {
        return AccountIndex.newBuilder().setValue(index.getIndex().getValue()).build();
    }

    static AccountAddress to(com.concordium.sdk.transactions.AccountAddress address) {
        return AccountAddress.newBuilder()
                .setValue(to(address.getBytes()))
                .build();
    }

    static com.concordium.sdk.responses.accountinfo.AccountInfo to(AccountInfo account) {
        var builder = com.concordium.sdk.responses.accountinfo.AccountInfo.builder()
                .accountNonce(to(account.getSequenceNumber()))
                .accountAmount(to(account.getAmount()))
                .accountReleaseSchedule(to(account.getSchedule()))
                .accountCredentials(ImmutableMap.copyOf(to(
                        account.getCredsMap(),
                        k -> Index.from(k),
                        ClientV2MapperExtensions::to)))
                .accountThreshold(account.getThreshold().getValue())
                .accountEncryptedAmount(to(account.getEncryptedBalance()))
                .accountEncryptionKey(ElgamalPublicKey.from(account.getEncryptionKey().getValue().toByteArray()))
                .accountIndex(to(account.getIndex()))
                .accountAddress(to(account.getAddress()));

        if (account.hasStake()) {
            if (account.getStake().hasBaker()) {
                builder.bakerInfo(to(account.getStake().getBaker()));
            } else if (account.getStake().hasDelegator()) {
                builder.delegation(to(account.getStake().getDelegator()));
            } else {
                throw new IllegalArgumentException();
            }
        }

        return builder.build();
    }

    static com.concordium.sdk.transactions.AccountAddress to(AccountAddress address) {
        return com.concordium.sdk.transactions.AccountAddress.from(address.getValue().toByteArray());
    }

    @Nullable
    static AccountDelegation to(com.concordium.grpc.v2.AccountStakingInfo.Delegator stake) {
        return AccountDelegation.builder()
                .pendingChange(to(stake.getPendingChange()))
                .restakeEarnings(stake.getRestakeEarnings())
                .stakedAmount(to(stake.getStakedAmount()))
                .target(to(stake.getTarget()))
                .build();
    }

    static DelegationTarget to(com.concordium.grpc.v2.DelegationTarget target) {
        if (target.hasBaker()) {
            return DelegationTarget.newBakerDelegationTarget(to(target.getBaker()));
        } else if (target.hasPassive()) {
            return DelegationTarget.newPassiveDelegationTarget();
        } else
            throw new IllegalArgumentException();
    }

    static Baker to(com.concordium.grpc.v2.AccountStakingInfo.Baker stake) {
        return Baker.builder()
                .pendingChange(to(stake.getPendingChange()))
                .restakeEarnings(stake.getRestakeEarnings())
                .stakedAmount(to(stake.getStakedAmount()))
                .bakerPoolInfo(to(stake.getPoolInfo()))
                .bakerId(to(stake.getBakerInfo().getBakerId()))
                .bakerAggregationVerifyKey(ED25519PublicKey.from(stake.getBakerInfo().getAggregationKey().getValue().toByteArray()))
                .bakerElectionVerifyKey(ED25519PublicKey.from(stake.getBakerInfo().getElectionKey().getValue().toByteArray()))
                .bakerSignatureVerifyKey(ED25519PublicKey.from(stake.getBakerInfo().getSignatureKey().getValue().toByteArray()))
                .build();
    }

    static com.concordium.sdk.responses.AccountIndex to(BakerId bakerId) {
        return com.concordium.sdk.responses.AccountIndex.from(bakerId.getValue());
    }

    static PendingChange to(StakePendingChange pendingChange) {
        if (pendingChange.hasReduce()) {
            var reduce = pendingChange.getReduce();
            return ReduceStakeChange.builder()
                    .effectiveTime(to(to(reduce.getEffectiveTime())))
                    .newStake(to(reduce.getNewStake()))
                    .build();
        } else if (pendingChange.hasRemove()) {
            return RemoveStakeChange.builder()
                    .effectiveTime(to(to(pendingChange.getRemove())))
                    .build();
        } else {
            throw new IllegalArgumentException();
        }
    }

    static OffsetDateTime to(Timestamp to) {
        return OffsetDateTime.ofInstant(to.getDate().toInstant(), UTC_ZONE);
    }

    static BakerPoolInfo to(com.concordium.grpc.v2.BakerPoolInfo poolInfo) {
        return BakerPoolInfo.builder()
                .metadataUrl(poolInfo.getUrl())
                .openStatus(to(poolInfo.getOpenStatus()))
                .commissionRates(to(poolInfo.getCommissionRates()))
                .build();
    }

    static CommissionRates to(com.concordium.grpc.v2.CommissionRates commissionRates) {
        return CommissionRates.builder()
                .transactionCommission(to(commissionRates.getTransaction()))
                .finalizationCommission(to(commissionRates.getFinalization()))
                .bakingCommission(to(commissionRates.getBaking()))
                .build();
    }

    static double to(AmountFraction frac) {
        return frac.getPartsPerHundredThousand() / HUNDRED_THOUSAND;
    }

    static OpenStatus to(com.concordium.grpc.v2.OpenStatus openStatus) {
        switch (openStatus) {
            case OPEN_STATUS_OPEN_FOR_ALL:
                return OpenStatus.OPEN_FOR_ALL;
            case OPEN_STATUS_CLOSED_FOR_NEW:
                return OpenStatus.CLOSED_FOR_NEW;
            case OPEN_STATUS_CLOSED_FOR_ALL:
                return OpenStatus.CLOSED_FOR_ALL;
            default:
                throw new IllegalArgumentException();
        }
    }

    static com.concordium.sdk.responses.AccountIndex to(AccountIndex index) {
        return com.concordium.sdk.responses.AccountIndex.from(index.getValue());
    }

    static AccountEncryptedAmount to(EncryptedBalance encryptedBalance) {
        return AccountEncryptedAmount.builder()
                .startIndex(EncryptedAmountIndex.from(encryptedBalance.getStartIndex()))
                .selfAmount(to(encryptedBalance.getSelfAmount()))
                .incomingAmounts(to(encryptedBalance.getIncomingAmountsList(), ClientV2MapperExtensions::to))
                .numAggregated(
                        encryptedBalance.hasNumAggregated()
                                ? Optional.of(encryptedBalance.getNumAggregated())
                                : Optional.empty())
                .aggregatedAmount(
                        encryptedBalance.hasAggregatedAmount()
                                ? Optional.of(to(encryptedBalance.getAggregatedAmount()))
                                : Optional.empty())
                .build();
    }

    static com.concordium.sdk.transactions.EncryptedAmount to(EncryptedAmount amount) {
        return com.concordium.sdk.transactions.EncryptedAmount.from(amount.getValue().toByteArray());
    }

    static Credential to(AccountCredential v) {
        if (v.hasInitial()) {
            return to(v.getInitial());
        } else if (v.hasNormal()) {
            return to(v.getNormal());
        }

        throw new IllegalArgumentException();
    }

    static Credential to(InitialCredentialValues creds) {
        return Credential.builder()
                .version(0)
                .type(CredentialType.INITIAL)
                .policy(to(creds.getPolicy()))
                .credId(to(creds.getCredId()))
                .ipIdentity(creds.getIpId().getValue())
                .credentialPublicKeys(to(creds.getKeys()))
                .build();
    }

    static Credential to(NormalCredentialValues creds) {
        return Credential.builder()
                .version(0)
                .type(CredentialType.NORMAL)
                .arData(ImmutableMap.copyOf(to(creds.getArDataMap(), Index::from, ClientV2MapperExtensions::to)))
                .revocationThreshold(creds.getArThreshold().getValue())
                .commitments(to(creds.getCommitments()))
                .policy(to(creds.getPolicy()))
                .credId(to(creds.getCredId()))
                .ipIdentity(creds.getIpId().getValue())
                .credentialPublicKeys(to(creds.getKeys()))
                .build();
    }

    static com.concordium.sdk.responses.accountinfo.credential.CredentialPublicKeys to(CredentialPublicKeys keys) {
        return com.concordium.sdk.responses.accountinfo.credential.CredentialPublicKeys.builder()
                .threshold(keys.getThreshold().getValue())
                .keys(ImmutableMap.copyOf(to(keys.getKeysMap(), Index::from, ClientV2MapperExtensions::to)))
                .build();
    }

    static Key to(AccountVerifyKey verifyKey) {
        if (verifyKey.hasEd25519Key()) {
            return Key.builder()
                    .verifyKey(verifyKey.getEd25519Key().toByteArray())
                    .schemeId(VerificationScheme.Ed25519)
                    .build();
        }

        throw new IllegalArgumentException();
    }

    static int to(SignatureThreshold threshold) {
        return threshold.getValue();
    }

    static com.concordium.sdk.transactions.CredentialRegistrationId to(CredentialRegistrationId credId) {
        return com.concordium.sdk.transactions.CredentialRegistrationId.fromBytes(credId.getValue().toByteArray());
    }

    static com.concordium.sdk.responses.accountinfo.credential.Policy to(Policy policy) {
        return com.concordium.sdk.responses.accountinfo.credential.Policy.builder()
                .createdAt(to(policy.getCreatedAt()))
                .validTo(to(policy.getValidTo()))
                .revealedAttributes(
                        ImmutableMap.copyOf(to(policy.getAttributesMap(), k -> to(k), v -> v.toStringUtf8())))
                .build();
    }

    static YearMonth to(com.concordium.grpc.v2.YearMonth validTo) {
        return YearMonth.of(validTo.getYear(), validTo.getMonth());
    }

    static AttributeType to(Integer ordinal) {
        return AttributeType.values()[ordinal];
    }

    static Commitments to(CredentialCommitments commitments) {
        return Commitments.builder()
                .cmmPrf(to(commitments.getPrf()))
                .cmmCredCounter(to(commitments.getCredCounter()))
                .cmmMaxAccounts(to(commitments.getMaxAccounts()))
                .cmmAttributes(ImmutableMap.copyOf(to(
                        commitments.getAttributesMap(),
                        k -> AttributeType.values()[k],
                        ClientV2MapperExtensions::to)))
                .cmmIdCredSecSharingCoeff(copyOf(to(
                        commitments.getIdCredSecSharingCoeffList(),
                        ClientV2MapperExtensions::to)))
                .build();
    }

    static com.concordium.sdk.responses.accountinfo.credential.Commitment to(Commitment commitment) {
        return com.concordium.sdk.responses.accountinfo.credential.Commitment.from(
                commitment.getValue().toByteArray());
    }

    static ArData to(ChainArData arData) {
        return ArData.builder()
                .encIdCredPubShare(EncIdPubShare.from(arData.getEncIdCredPubShare().toByteArray()))
                .build();
    }

    static com.concordium.sdk.responses.accountinfo.ReleaseSchedule to(ReleaseSchedule schedule) {
        return com.concordium.sdk.responses.accountinfo.ReleaseSchedule.builder()
                .total(to(schedule.getTotal()))
                .schedule(copyOf(
                        to(schedule.getSchedulesList(), ClientV2MapperExtensions::to)))
                .build();
    }

    static <T1, T2> List<T2> to(List<T1> sourceList, Function<T1, T2> map) {
        return sourceList.stream().map(map::apply).collect(Collectors.toList());
    }

    static <K1, V1, K2, V2> Map<K2, V2> to(
            Map<K1, V1> sourceMap,
            Function<K1, K2> mapKey,
            Function<V1, V2> mapValues) {
        return sourceMap.entrySet().stream().collect(Collectors.toMap(
                k1V1Entry -> mapKey.apply(k1V1Entry.getKey()),
                k1V1Entry -> mapValues.apply(k1V1Entry.getValue())));
    }

    static ScheduledRelease to(Release s) {
        return ScheduledRelease.builder()
                .amount(to(s.getAmount()))
                .timestamp(to(s.getTimestamp()))
                .transactions(to(s.getTransactionsList(), ClientV2MapperExtensions::to))
                .build();
    }

    static Timestamp to(com.concordium.grpc.v2.Timestamp timestamp) {
        return Timestamp.newMillis(timestamp.getValue());
    }

    static Hash to(TransactionHash transactionHash) {
        return Hash.from(transactionHash.getValue().toByteArray());
    }

    static CCDAmount to(Amount amount) {
        return CCDAmount.fromMicro(amount.getValue());
    }

    static Nonce to(SequenceNumber sequenceNumber) {
        return Nonce.from(sequenceNumber.getValue());
    }
}
