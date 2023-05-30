package com.concordium.sdk;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountInfo;
import com.concordium.grpc.v2.BlockItem;
import com.concordium.grpc.v2.Commitment;
import com.concordium.grpc.v2.ContractAddress;
import com.concordium.grpc.v2.CredentialPublicKeys;
import com.concordium.grpc.v2.CredentialRegistrationId;
import com.concordium.grpc.v2.EncryptedAmount;
import com.concordium.grpc.v2.Memo;
import com.concordium.grpc.v2.Policy;
import com.concordium.grpc.v2.ReleaseSchedule;
import com.concordium.grpc.v2.*;
import com.concordium.grpc.v2.Timestamp;
import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.requests.BlockHashInput;
import com.concordium.sdk.requests.getaccountinfo.AccountRequest;
import com.concordium.sdk.responses.BlockIdentifier;
import com.concordium.sdk.responses.accountinfo.BakerPoolInfo;
import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.accountinfo.*;
import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.responses.accountinfo.credential.*;
import com.concordium.sdk.responses.blocksummary.FinalizationData;
import com.concordium.sdk.responses.blocksummary.Finalizer;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.*;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.Description;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.branch.Branch;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.election.ElectionInfoBaker;
import com.concordium.sdk.responses.nodeinfo.BakingCommitteeDetails;
import com.concordium.sdk.responses.nodeinfo.BakingStatus;
import com.concordium.sdk.responses.nodeinfo.ConsensusState;
import com.concordium.sdk.responses.nodeinfo.PeerType;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.responses.transactionstatus.*;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.transactions.AccountTransaction;
import com.concordium.sdk.responses.transactionstatus.RejectReason;
import com.concordium.sdk.responses.transactionstatus.TransactionType;
import com.concordium.sdk.transactions.AccountNonce;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.transactions.InitContractPayload;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Signature;
import com.concordium.sdk.transactions.TransferPayload;
import com.concordium.sdk.transactions.TransferWithMemoPayload;
import com.concordium.sdk.transactions.UpdateContractPayload;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.*;
import com.concordium.sdk.types.Timestamp;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.protobuf.ByteString;
import lombok.val;
import lombok.var;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.concordium.grpc.v2.RejectReason.ReasonCase;
import static com.concordium.sdk.Constants.UTC_ZONE;
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

    static com.concordium.grpc.v2.TransactionHash toTransactionHash(final Hash blockHash) {
        return com.concordium.grpc.v2.TransactionHash.newBuilder().setValue(to(blockHash.getBytes())).build();
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

    static IdentityProviderInfo to(final IpInfo ipInfo) {
        return IdentityProviderInfo.builder()
                .ipIdentity(to(ipInfo.getIdentity()))
                .description(to(ipInfo.getDescription()))
                .ipCdiVerifyKey(to(ipInfo.getCdiVerifyKey()))
                .ipVerifyKey(to(ipInfo.getVerifyKey()))
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
    static int to(final IpIdentity identity) {
        return identity.getValue();
    }

    static PSPublicKey to(final IpInfo.IpVerifyKey verifyKey) {
        return new PSPublicKey(verifyKey.getValue().toByteArray());
    }

    static ED25519PublicKey to(final IpInfo.IpCdiVerifyKey cdiVerifyKey) {
        return ED25519PublicKey.from(cdiVerifyKey.getValue().toByteArray());
    }

    static <T1, T2> Iterator<T2> to(final Iterator<T1> iterator, final Function<? super T1, ? extends T2> to) {
        return Iterators.transform(iterator, to);
    }

    static BlockIdentifier to(com.concordium.grpc.v2.ArrivedBlockInfo arrivedBlockInfo) {
        return BlockIdentifier.builder()
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

    static BlockIdentifier to(FinalizedBlockInfo finalizedBlockInfo) {
        return BlockIdentifier.builder()
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
                        Index::from,
                        ClientV2MapperExtensions::to)))
                .accountThreshold(account.getThreshold().getValue())
                .accountEncryptedAmount(to(account.getEncryptedBalance()))
                .accountEncryptionKey(ElgamalPublicKey.from(account.getEncryptionKey().getValue().toByteArray()))
                .accountIndex(to(account.getIndex()))
                .accountAddress(to(account.getAddress()));

        if (account.hasStake()) {
            switch (account.getStake().getStakingInfoCase()) {
                case BAKER:
                    builder.bakerInfo(to(account.getStake().getBaker()));
                    break;
                case DELEGATOR:
                    builder.delegation(to(account.getStake().getDelegator()));
                    break;
                case STAKINGINFO_NOT_SET:
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

    static com.concordium.sdk.responses.BakerId toBakerId(BakerId bakerId) {
        return com.concordium.sdk.responses.BakerId.from(bakerId.getValue());
    }

    static PendingChange to(StakePendingChange pendingChange) {
        switch (pendingChange.getChangeCase()) {
            case REDUCE:
                var reduce = pendingChange.getReduce();
                return ReduceStakeChange.builder()
                        .effectiveTime(to(to(reduce.getEffectiveTime())))
                        .newStake(to(reduce.getNewStake()))
                        .build();
            case REMOVE:
                return RemoveStakeChange.builder()
                        .effectiveTime(to(to(pendingChange.getRemove())))
                        .build();
            default:
            case CHANGE_NOT_SET:
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

    static double to(AmountFraction amountFraction) {
        return amountFraction.getPartsPerHundredThousand() / HUNDRED_THOUSAND;
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
                        ImmutableMap.copyOf(to(policy.getAttributesMap(), ClientV2MapperExtensions::to, ByteString::toStringUtf8)))
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

    static AccountNonce to(NextAccountSequenceNumber nextAccountSequenceNumber) {
        return AccountNonce.from(to(nextAccountSequenceNumber.getSequenceNumber()));
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

    static com.concordium.sdk.transactions.BlockItem to(BlockItem blockItem) {
        switch (blockItem.getBlockItemCase()) {
            case ACCOUNT_TRANSACTION:
                return to(blockItem.getAccountTransaction());
            case CREDENTIAL_DEPLOYMENT:
                return to(blockItem.getCredentialDeployment());
            case UPDATE_INSTRUCTION:
                return to(blockItem.getUpdateInstruction());
        }

        throw new IllegalArgumentException();
    }

    static com.concordium.sdk.transactions.BlockItem to(UpdateInstruction updateInstruction) {
        return UpdateInstructionTransaction.builderBlockItem()
                .header(to(updateInstruction.getHeader()))
                .payload(to(updateInstruction.getPayload()))
                .signature(UpdateInstructionTransactionSignature.builder()
                        .signatures(ImmutableMap.copyOf(to(
                                updateInstruction.getSignatures().getSignaturesMap(),
                                Index::from,
                                v -> Signature.from(v.getValue().toByteArray()))))
                        .build())
                .build();
    }

    static UpdateInstructionTransactionPayload to(UpdateInstructionPayload payload) {
        switch (payload.getPayloadCase()) {
            case RAW_PAYLOAD:
                return UpdateInstructionTransactionPayload.from(payload.getRawPayload().toByteArray());
            case PAYLOAD_NOT_SET:
                return UpdateInstructionTransactionPayload.from(new byte[0]);
            default:
                throw new IllegalArgumentException();
        }
    }

    static UpdateInstructionTransactionHeader to(UpdateInstructionHeader header) {
        return UpdateInstructionTransactionHeader.builder()
                .effectiveTime(to(header.getEffectiveTime()))
                .timeout(to(header.getTimeout()))
                .sequenceNumber(to(header.getSequenceNumber()))
                .build();
    }

    static UInt64 to(UpdateSequenceNumber sequenceNumber) {
        return UInt64.from(sequenceNumber.getValue());
    }

    static CredentialDeploymentTransaction to(CredentialDeployment transaction) {
        var builder = com.concordium.sdk.transactions
                .CredentialDeploymentTransaction
                .builderBlockItem()
                .expiry(to(transaction.getMessageExpiry()));
        switch (transaction.getPayloadCase()) {
            case RAW_PAYLOAD:
                return builder.payloadBytes(transaction.getRawPayload().toByteArray()).build();
            case PAYLOAD_NOT_SET:
                return builder.payloadBytes(new byte[0]).build();
            default:
                throw new IllegalArgumentException();
        }
    }

    static AccountTransaction to(com.concordium.grpc.v2.AccountTransaction transaction) {
        val payload = transaction.getPayload();

        switch (payload.getPayloadCase()) {
            case DEPLOY_MODULE: {
                final WasmModule deployModulePayload = to(payload.getDeployModule());
                return DeployModuleTransaction
                        .builderBlockItem()
                        .header(to(transaction.getHeader(), deployModulePayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(deployModulePayload)
                        .build();
            }
            case INIT_CONTRACT: {
                final InitContractPayload initContractPayload = to(payload.getInitContract());
                return InitContractTransaction.builderBlockItem()
                        .header(to(transaction.getHeader(), initContractPayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(initContractPayload)
                        .build();
            }
            case UPDATE_CONTRACT:
                final UpdateContractPayload updateContractPayload = to(payload.getUpdateContract());
                return UpdateContractTransaction
                        .builderBlockItem()
                        .header(to(transaction.getHeader(), updateContractPayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(updateContractPayload)
                        .build();
            case REGISTER_DATA:
                final Data registerDataPayload = to(payload.getRegisterData());
                return RegisterDataTransaction
                        .builderBlockItem()
                        .header(to(transaction.getHeader(), registerDataPayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(registerDataPayload)
                        .build();
            case TRANSFER:
                final TransferPayload transferPayload = to(payload.getTransfer());
                return TransferTransaction
                        .builderBlockItem()
                        .header(to(transaction.getHeader(), transferPayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(transferPayload)
                        .build();
            case TRANSFER_WITH_MEMO:
                final TransferWithMemoPayload transferWithMemoPayload = to(payload.getTransferWithMemo());
                return TransferWithMemoTransaction
                        .builderBlockItem()
                        .header(to(transaction.getHeader(), transferWithMemoPayload.getBytes().length))
                        .signature(to(transaction.getSignature()))
                        .payload(transferWithMemoPayload)
                        .build();
            case RAW_PAYLOAD:
                final byte[] rawPayloadBytes = payload.getRawPayload().toByteArray();
                return com.concordium.sdk.transactions.AccountTransaction
                        .builderAccountTransactionBlockItem()
                        .header(to(transaction.getHeader(), rawPayloadBytes.length))
                        .signature(to(transaction.getSignature()))
                        .payloadBytes(rawPayloadBytes)
                        .build();
            default:
            case PAYLOAD_NOT_SET:
                return com.concordium.sdk.transactions.AccountTransaction
                        .builderAccountTransactionBlockItem()
                        .header(to(transaction.getHeader(), 0))
                        .signature(to(transaction.getSignature()))
                        .payloadBytes(new byte[0])
                        .build();
        }
    }

    static TransferWithMemoPayload to(com.concordium.grpc.v2.TransferWithMemoPayload transferWithMemo) {
        return TransferWithMemoPayload.from(to(transferWithMemo.getReceiver()),
                to(transferWithMemo.getAmount()),
                to(transferWithMemo.getMemo()));
    }

    static TransferPayload to(com.concordium.grpc.v2.TransferPayload transfer) {
        return TransferPayload.from(to(transfer.getReceiver()), to(transfer.getAmount()));
    }

    static Data to(RegisteredData registerData) {
        return Data.from(registerData.getValue().toByteArray());
    }

    static UpdateContractPayload to(com.concordium.grpc.v2.UpdateContractPayload updateContract) {
        return UpdateContractPayload.from(
                to(updateContract.getAmount()),
                to(updateContract.getAddress()),
                ReceiveName.parse(updateContract.getReceiveName().getValue()),
                updateContract.hasParameter() ? to(updateContract.getParameter()) : Parameter.EMPTY);
    }

    static InitContractPayload to(com.concordium.grpc.v2.InitContractPayload initContract) {
        return InitContractPayload.from(to(initContract.getAmount()),
                to(initContract.getModuleRef()),
                to(initContract.getInitName()),
                initContract.hasParameter() ? to(initContract.getParameter()) : Parameter.EMPTY);
    }

    static Parameter to(com.concordium.grpc.v2.Parameter parameter) {
        return Parameter.from(parameter.getValue().toByteArray());
    }

    static InitName to(com.concordium.grpc.v2.InitName initName) {
        return InitName.from(initName.getValue());
    }

    static Hash to(ModuleRef moduleRef) {
        return Hash.from(moduleRef.getValue().toByteArray());
    }

    static com.concordium.sdk.transactions.Memo to(Memo memo) {
        return com.concordium.sdk.transactions.Memo.from(memo.getValue().toByteArray());
    }

    static com.concordium.sdk.types.ContractAddress to(ContractAddress address) {
        return com.concordium.sdk.types.ContractAddress.from(address.getIndex(), address.getSubindex());
    }

    static WasmModule to(VersionedModuleSource deployModule) {
        switch (deployModule.getModuleCase()) {
            case V1:
                return WasmModule.from(deployModule.getV1().getValue().toByteArray(), WasmModuleVersion.V1);
            case V0:
                return WasmModule.from(deployModule.getV0().getValue().toByteArray(), WasmModuleVersion.V0);
            case MODULE_NOT_SET:
                return WasmModule.from(new byte[0]);
            default:
                throw new IllegalArgumentException();
        }
    }

    static TransactionSignature to(AccountTransactionSignature signatures) {
        var builder = TransactionSignature.builder();

        for (var credentialIndex : signatures.getSignaturesMap().keySet()) {
            var builderInternal
                    = TransactionSignatureAccountSignatureMap.builder();
            for (var index : signatures.getSignaturesMap().get(credentialIndex).getSignaturesMap().keySet()) {
                val signature = signatures
                        .getSignaturesMap()
                        .get(credentialIndex)
                        .getSignaturesMap()
                        .get(index);

                builderInternal.signature(Index.from(index), Signature.from(signature.getValue().toByteArray()));
            }

            builder.signature(Index.from(credentialIndex), builderInternal.build());
        }

        return builder.build();
    }

    static TransactionHeader to(AccountTransactionHeader header, int payloadSize) {
        var ret = TransactionHeader.builder()
                .sender(to(header.getSender()))
                .expiry(to(header.getExpiry()))
                .accountNonce(to(header.getSequenceNumber()))
                .build();
        ret.setMaxEnergyCost(to(header.getEnergyAmount()));
        ret.setPayloadSize(UInt32.from(payloadSize));

        return ret;
    }

    static UInt64 to(Energy energyAmount) {
        return UInt64.from(energyAmount.getValue());
    }

    static UInt64 to(TransactionTime expiry) {
        return UInt64.from(expiry.getValue());
    }

    // Convert a Duration object to a long value
    static long to(Duration slotDuration) {
        return slotDuration.getValue();
    }

    // Convert a ProtocolVersion object to the corresponding com.concordium.sdk.responses.ProtocolVersion object
    static com.concordium.sdk.responses.ProtocolVersion to(ProtocolVersion protocolVersion) {
        return com.concordium.sdk.responses.ProtocolVersion.forValue(protocolVersion.getNumber() + 1);
    }

    // Convert a ConsensusInfo object to a ConsensusStatus object
    static ConsensusStatus to(ConsensusInfo consensusInfo) {
        var builder = ConsensusStatus.builder()
                .bestBlock(to(consensusInfo.getBestBlock()))
                .genesisBlock(to(consensusInfo.getGenesisBlock()))
                .genesisTime(to(consensusInfo.getGenesisTime()).getDate())
                .slotDuration(to(consensusInfo.getSlotDuration()))
                .epochDuration(to(consensusInfo.getEpochDuration()))
                .lastFinalizedBlock(to(consensusInfo.getLastFinalizedBlock()))
                .bestBlockHeight(to(consensusInfo.getBestBlockHeight()).getValue())
                .lastFinalizedBlockHeight(to(consensusInfo.getLastFinalizedBlockHeight()).getValue())
                .blocksReceivedCount(consensusInfo.getBlocksReceivedCount())
                .blockLastReceivedTime(to(consensusInfo.getBlockLastReceivedTime()).toString())
                .blockReceiveLatencyEMA(consensusInfo.getBlockReceiveLatencyEma())
                .blockReceiveLatencyEMSD(consensusInfo.getBlockReceiveLatencyEmsd())
                .blockReceivePeriodEMA(consensusInfo.getBlockReceivePeriodEma())
                .blockReceivePeriodEMSD(consensusInfo.getBlockReceivePeriodEmsd())
                .blocksVerifiedCount(consensusInfo.getBlocksVerifiedCount())
                .blockLastArrivedTime(to(consensusInfo.getBlockLastArrivedTime()).toString())
                .blockArriveLatencyEMA(consensusInfo.getBlockArriveLatencyEma())
                .blockArriveLatencyEMSD(consensusInfo.getBlockArriveLatencyEmsd())
                .blockArrivePeriodEMA(consensusInfo.getBlockArrivePeriodEma())
                .blockArrivePeriodEMSD(consensusInfo.getBlockArrivePeriodEmsd())
                .transactionsPerBlockEMA(consensusInfo.getTransactionsPerBlockEma())
                .transactionsPerBlockEMSD(consensusInfo.getTransactionsPerBlockEmsd())
                .finalizationCount(consensusInfo.getFinalizationCount())
                .lastFinalizedTime(to(consensusInfo.getLastFinalizedTime()).toString())
                .finalizationPeriodEMA(consensusInfo.getFinalizationPeriodEma())
                .finalizationPeriodEMSD(consensusInfo.getFinalizationPeriodEmsd())
                .protocolVersion(to(consensusInfo.getProtocolVersion()))
                .genesisIndex(consensusInfo.getGenesisIndex().getValue())
                .currentEraGenesisBlock(to(consensusInfo.getCurrentEraGenesisBlock()).toString())
                .currentEraGenesisTime(to(consensusInfo.getCurrentEraGenesisTime()).getDate());

        return builder.build();
    }

    static SendBlockItemRequest to(AccountTransaction accountTransaction) {
        return SendBlockItemRequest.newBuilder()
                .setAccountTransaction(com.concordium.grpc.v2.AccountTransaction.newBuilder()
                        .setHeader(to(accountTransaction.getHeader()))
                        .setPayload(AccountTransactionPayload.newBuilder()
                                .setRawPayload(ByteString.copyFrom(accountTransaction.getPayloadBytes()))
                                .build())
                        .setSignature(to(accountTransaction.getSignature()))
                        .build())
                .build();
    }

    static AccountTransactionSignature to(TransactionSignature signature) {
        return AccountTransactionSignature.newBuilder()
                .putAllSignatures(to(
                        signature.getSignatures(),
                        ClientV2MapperExtensions::to,
                        ClientV2MapperExtensions::to))
                .build();
    }

    static AccountSignatureMap to(TransactionSignatureAccountSignatureMap v) {
        return AccountSignatureMap.newBuilder().putAllSignatures(to(
                        v.getSignatures(),
                        ClientV2MapperExtensions::to,
                        ClientV2MapperExtensions::to))
                .build();
    }

    static Integer to(Index index) {
        return (int) index.getValue();
    }

    static com.concordium.grpc.v2.Signature to(Signature signature) {
        return com.concordium.grpc.v2.Signature.newBuilder()
                .setValue(ByteString.copyFrom(signature.getBytes()))
                .build();
    }

    static AccountTransactionHeader to(TransactionHeader header) {
        return AccountTransactionHeader.newBuilder()
                .setSequenceNumber(SequenceNumber.newBuilder()
                        .setValue(to(header.getAccountNonce()))
                        .build())
                .setSender(to(header.getSender()))
                .setExpiry(to(header.getExpiry()))
                .setEnergyAmount(toEnergy(header.getMaxEnergyCost()))
                .build();
    }

    static Energy toEnergy(UInt64 maxEnergyCost) {
        return Energy.newBuilder().setValue(maxEnergyCost.getValue()).build();
    }

    static TransactionTime to(UInt64 expiry) {
        return TransactionTime.newBuilder().setValue(expiry.getValue()).build();
    }

    static long to(Nonce accountNonce) {
        return accountNonce.getValue().getValue();
    }

    static com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters to(CryptographicParameters grpcOutput) {
        var builder = com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters.builder()
                .bulletproofGenerators(BulletproofGenerators.from(grpcOutput.getBulletproofGenerators().toByteArray()))
                .onChainCommitmentKey(PedersenCommitmentKey.from(grpcOutput.getOnChainCommitmentKey().toByteArray()))
                .genesisString(grpcOutput.getGenesisString());

        return builder.build();

    }

    static RewardsOverview to(TokenomicsInfo tokenomicsInfo) {
        var builder = RewardsOverview.builder();
        if (tokenomicsInfo.hasV0()) {
            builder = builder.totalAmount(to(tokenomicsInfo.getV0().getTotalAmount()))
                    .totalEncryptedAmount(to(tokenomicsInfo.getV0().getTotalEncryptedAmount()))
                    .bakingRewardAccount(to(tokenomicsInfo.getV0().getBakingRewardAccount()))
                    .finalizationRewardAccount(to(tokenomicsInfo.getV0().getFinalizationRewardAccount()))
                    .gasAccount(to(tokenomicsInfo.getV0().getGasAccount()))
                    .protocolVersion(to(tokenomicsInfo.getV0().getProtocolVersion()));
        }
        else if (tokenomicsInfo.hasV1()) {
            builder = builder.totalAmount(to(tokenomicsInfo.getV1().getTotalAmount()))
                    .totalEncryptedAmount(to(tokenomicsInfo.getV1().getTotalEncryptedAmount()))
                    .bakingRewardAccount(to(tokenomicsInfo.getV1().getBakingRewardAccount()))
                    .finalizationRewardAccount(to(tokenomicsInfo.getV1().getFinalizationRewardAccount()))
                    .gasAccount(to(tokenomicsInfo.getV1().getGasAccount()))
                    .foundationTransactionRewards(to(tokenomicsInfo.getV1().getFoundationTransactionRewards()))
                    .nextPaydayTime(to(tokenomicsInfo.getV1().getNextPaydayTime()).getDate())
                    .nextPaydayMintRate(to(tokenomicsInfo.getV1().getNextPaydayMintRate()))
                    .totalStakedCapital(to(tokenomicsInfo.getV1().getTotalStakedCapital()))
                    .protocolVersion(to(tokenomicsInfo.getV1().getProtocolVersion()));
        }

        return builder.build();
    }

    // Convert a com.concordium.grpc.v2.BlockItemStatus object to the corresponding com.concordium.sdk.responses.TransactionStatus object
    static TransactionStatus to(com.concordium.grpc.v2.BlockItemStatus blockItemStatus) {
        var builder = TransactionStatus.builder();
        var statusCase = blockItemStatus.getStatusCase();
        switch (statusCase) {
            case FINALIZED:
                builder = builder.status(Status.FINALIZED)
                        .outcomes(to(blockItemStatus.getFinalized().getOutcome()));
                break;
            case COMMITTED:
                builder = builder.status(Status.COMMITTED)
                        .outcomes(to(blockItemStatus.getCommitted().getOutcomesList()));
                break;
            case RECEIVED:
                builder = builder.status(Status.RECEIVED);
                break;
            default:
                builder = builder.status(Status.ABSENT);
                break;
        }

        return builder.build();
    }

    static double to(MintRate mintRate) {
        double rate = mintRate.getMantissa() * Math.pow(10, -1 * mintRate.getExponent());
        return rate;
    }

    static Map<Hash, TransactionSummary> to(List<com.concordium.grpc.v2.BlockItemSummaryInBlock> blockItemSummaries) {
        Map<Hash, TransactionSummary> outcomes = new HashMap<>();

        for (com.concordium.grpc.v2.BlockItemSummaryInBlock outcome : blockItemSummaries) {
            outcomes.put(to(outcome.getBlockHash()), to(outcome.getOutcome()));
        }

        return outcomes;
    }
    static Map<Hash, TransactionSummary> to(BlockItemSummaryInBlock outcome) {
        Map<Hash, TransactionSummary> result = new HashMap<>();
        result.put(to(outcome.getBlockHash()), to(outcome.getOutcome()));
        return result;
    }

    static TransactionSummary to(BlockItemSummary blockItemSummary) {
        var summary = TransactionSummary.builder()
                .index((int) blockItemSummary.getIndex().getValue())
                .hash(to(blockItemSummary.getHash()))
                .energyCost((int) blockItemSummary.getEnergyCost().getValue())
                .type(toTransactionType(blockItemSummary));
        if (blockItemSummary.getDetailsCase() == BlockItemSummary.DetailsCase.ACCOUNT_TRANSACTION) {
            summary = summary.sender(to(blockItemSummary.getAccountTransaction().getSender()))
                    .cost(to(blockItemSummary.getAccountTransaction().getCost()))
                    .result(toTransactionResult(blockItemSummary.getAccountTransaction()));
        }

        return summary.build();
    }

    static Map<ReasonCase, RejectReasonType> initializeReasonCaseToRejectReasonType() {
        Map<ReasonCase, RejectReasonType> map = new HashMap<>();
        map.put(ReasonCase.MODULE_NOT_WF, RejectReasonType.MODULE_NOT_WF);
        map.put(ReasonCase.MODULE_HASH_ALREADY_EXISTS, RejectReasonType.MODULE_HASH_ALREADY_EXISTS);
        map.put(ReasonCase.INVALID_ACCOUNT_REFERENCE, RejectReasonType.INVALID_ACCOUNT_REFERENCE);
        map.put(ReasonCase.INVALID_INIT_METHOD, RejectReasonType.INVALID_INIT_METHOD);
        map.put(ReasonCase.INVALID_RECEIVE_METHOD, RejectReasonType.INVALID_RECEIVE_METHOD);
        map.put(ReasonCase.INVALID_MODULE_REFERENCE, RejectReasonType.INVALID_MODULE_REFERENCE);
        map.put(ReasonCase.INVALID_CONTRACT_ADDRESS, RejectReasonType.INVALID_CONTRACT_ADDRESS);
        map.put(ReasonCase.RUNTIME_FAILURE, RejectReasonType.RUNTIME_FAILURE);
        map.put(ReasonCase.AMOUNT_TOO_LARGE, RejectReasonType.AMOUNT_TOO_LARGE);
        map.put(ReasonCase.SERIALIZATION_FAILURE, RejectReasonType.SERIALIZATION_FAILURE);
        map.put(ReasonCase.OUT_OF_ENERGY, RejectReasonType.OUT_OF_ENERGY);
        map.put(ReasonCase.REJECTED_INIT, RejectReasonType.REJECTED_INIT);
        map.put(ReasonCase.REJECTED_RECEIVE, RejectReasonType.REJECTED_RECEIVE);
        map.put(ReasonCase.INVALID_PROOF, RejectReasonType.INVALID_PROOF);
        map.put(ReasonCase.ALREADY_A_BAKER, RejectReasonType.ALREADY_A_BAKER);
        map.put(ReasonCase.NOT_A_BAKER, RejectReasonType.NOT_A_BAKER);
        map.put(ReasonCase.INSUFFICIENT_BALANCE_FOR_BAKER_STAKE, RejectReasonType.INSUFFICIENT_BALANCE_FOR_BAKER_STAKE);
        map.put(ReasonCase.STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING, RejectReasonType.STAKE_UNDER_MINIMUM_THRESHOLD_FOR_BAKING);
        map.put(ReasonCase.BAKER_IN_COOLDOWN, RejectReasonType.BAKER_IN_COOLDOWN);
        map.put(ReasonCase.DUPLICATE_AGGREGATION_KEY, RejectReasonType.DUPLICATE_AGGREGATION_KEY);
        map.put(ReasonCase.NON_EXISTENT_CREDENTIAL_ID, RejectReasonType.NON_EXISTENT_CREDENTIAL_ID);
        map.put(ReasonCase.KEY_INDEX_ALREADY_IN_USE, RejectReasonType.KEY_INDEX_ALREADY_IN_USE);
        map.put(ReasonCase.INVALID_ACCOUNT_THRESHOLD, RejectReasonType.INVALID_ACCOUNT_THRESHOLD);
        map.put(ReasonCase.INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD, RejectReasonType.INVALID_CREDENTIAL_KEY_SIGN_THRESHOLD);
        map.put(ReasonCase.INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF, RejectReasonType.INVALID_ENCRYPTED_AMOUNT_TRANSFER_PROOF);
        map.put(ReasonCase.INVALID_TRANSFER_TO_PUBLIC_PROOF, RejectReasonType.INVALID_TRANSFER_TO_PUBLIC_PROOF);
        map.put(ReasonCase.ENCRYPTED_AMOUNT_SELF_TRANSFER, RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER);
        map.put(ReasonCase.INVALID_INDEX_ON_ENCRYPTED_TRANSFER, RejectReasonType.INVALID_INDEX_ON_ENCRYPTED_TRANSFER);
        map.put(ReasonCase.ZERO_SCHEDULEDAMOUNT, RejectReasonType.ZERO_SCHEDULED_AMOUNT);
        map.put(ReasonCase.NON_INCREASING_SCHEDULE, RejectReasonType.NON_INCREASING_SCHEDULE);
        map.put(ReasonCase.FIRST_SCHEDULED_RELEASE_EXPIRED, RejectReasonType.FIRST_SCHEDULED_RELEASE_EXPIRED);
        map.put(ReasonCase.SCHEDULED_SELF_TRANSFER, RejectReasonType.SCHEDULED_SELF_TRANSFER);
        map.put(ReasonCase.INVALID_CREDENTIALS, RejectReasonType.INVALID_CREDENTIALS);
        map.put(ReasonCase.DUPLICATE_CRED_IDS, RejectReasonType.DUPLICATE_CRED_IDS);
        map.put(ReasonCase.NON_EXISTENT_CRED_IDS, RejectReasonType.NON_EXISTENT_CRED_IDS);
        map.put(ReasonCase.REMOVE_FIRST_CREDENTIAL, RejectReasonType.REMOVE_FIRST_CREDENTIAL);
        map.put(ReasonCase.CREDENTIAL_HOLDER_DID_NOT_SIGN, RejectReasonType.CREDENTIAL_HOLDER_DID_NOT_SIGN);
        map.put(ReasonCase.NOT_ALLOWED_MULTIPLE_CREDENTIALS, RejectReasonType.NOT_ALLOWED_MULTIPLE_CREDENTIALS);
        map.put(ReasonCase.NOT_ALLOWED_TO_RECEIVE_ENCRYPTED, RejectReasonType.NOT_ALLOWED_TO_RECEIVE_ENCRYPTED);
        map.put(ReasonCase.NOT_ALLOWED_TO_HANDLE_ENCRYPTED, RejectReasonType.NOT_ALLOWED_TO_HANDLE_ENCRYPTED);
        map.put(ReasonCase.MISSING_BAKER_ADD_PARAMETERS, RejectReasonType.MISSING_BAKER_ADD_PARAMETERS);
        map.put(ReasonCase.FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE, RejectReasonType.FINALIZATION_REWARD_COMMISSION_NOT_IN_RANGE);
        map.put(ReasonCase.BAKING_REWARD_COMMISSION_NOT_IN_RANGE, RejectReasonType.BAKING_REWARD_COMMISSION_NOT_IN_RANGE);
        map.put(ReasonCase.TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE, RejectReasonType.TRANSACTION_FEE_COMMISSION_NOT_IN_RANGE);
        map.put(ReasonCase.ALREADY_A_DELEGATOR, RejectReasonType.ALREADY_A_DELEGATOR);
        map.put(ReasonCase.INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE, RejectReasonType.INSUFFICIENT_BALANCE_FOR_DELEGATION_STAKE);
        map.put(ReasonCase.MISSING_DELEGATION_ADD_PARAMETERS, RejectReasonType.MISSING_DELEGATION_ADD_PARAMETERS);
        map.put(ReasonCase.INSUFFICIENT_DELEGATION_STAKE, RejectReasonType.INSUFFICIENT_DELEGATION_STAKE);
        map.put(ReasonCase.DELEGATOR_IN_COOLDOWN, RejectReasonType.DELEGATOR_IN_COOLDOWN);
        map.put(ReasonCase.NOT_A_DELEGATOR, RejectReasonType.NOT_A_DELEGATOR);
        map.put(ReasonCase.DELEGATION_TARGET_NOT_A_BAKER, RejectReasonType.DELEGATION_TARGET_NOT_A_BAKER);
        map.put(ReasonCase.STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL, RejectReasonType.STAKE_OVER_MAXIMUM_THRESHOLD_FOR_POOL);
        map.put(ReasonCase.POOL_WOULD_BECOME_OVER_DELEGATED, RejectReasonType.POOL_WOULD_BECOME_OVER_DELEGATED);
        map.put(ReasonCase.POOL_CLOSED, RejectReasonType.POOL_CLOSED);

        return map;
    }

    static Map<AccountTransactionEffects.EffectCase, TransactionResultEventType> initializeEffectCaseTransactionResultTypeMap() {
        Map<AccountTransactionEffects.EffectCase, TransactionResultEventType> map = new HashMap<>();
        map.put(AccountTransactionEffects.EffectCase.MODULE_DEPLOYED, TransactionResultEventType.MODULE_DEPLOYED);
        map.put(AccountTransactionEffects.EffectCase.CONTRACT_INITIALIZED, TransactionResultEventType.CONTRACT_INITIALIZED);
        map.put(AccountTransactionEffects.EffectCase.CONTRACT_UPDATE_ISSUED, TransactionResultEventType.CONTRACT_UPDATED);
        map.put(AccountTransactionEffects.EffectCase.ACCOUNT_TRANSFER, TransactionResultEventType.TRANSFERRED);
        map.put(AccountTransactionEffects.EffectCase.BAKER_ADDED, TransactionResultEventType.BAKER_ADDED);
        map.put(AccountTransactionEffects.EffectCase.BAKER_REMOVED, TransactionResultEventType.BAKER_REMOVED);
        map.put(AccountTransactionEffects.EffectCase.BAKER_STAKE_UPDATED, TransactionResultEventType.BAKER_STAKE_UPDATED);
        map.put(AccountTransactionEffects.EffectCase.BAKER_RESTAKE_EARNINGS_UPDATED, TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS);
        map.put(AccountTransactionEffects.EffectCase.BAKER_KEYS_UPDATED, TransactionResultEventType.BAKER_KEYS_UPDATED);
        map.put(AccountTransactionEffects.EffectCase.ENCRYPTED_AMOUNT_TRANSFERRED, TransactionResultEventType.ENCRYPTED_AMOUNT_TRANSFERRED);
        map.put(AccountTransactionEffects.EffectCase.TRANSFERRED_TO_ENCRYPTED, TransactionResultEventType.TRANSFERRED_TO_ENCRYPTED);
        map.put(AccountTransactionEffects.EffectCase.TRANSFERRED_TO_PUBLIC, TransactionResultEventType.TRANSFERRED_TO_PUBLIC);
        map.put(AccountTransactionEffects.EffectCase.TRANSFERRED_WITH_SCHEDULE, TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE);
        map.put(AccountTransactionEffects.EffectCase.CREDENTIAL_KEYS_UPDATED, TransactionResultEventType.CREDENTIAL_KEYS_UPDATED);
        map.put(AccountTransactionEffects.EffectCase.CREDENTIALS_UPDATED, TransactionResultEventType.CREDENTIALS_UPDATED);
        map.put(AccountTransactionEffects.EffectCase.DATA_REGISTERED, TransactionResultEventType.DATA_REGISTERED);
        map.put(AccountTransactionEffects.EffectCase.BAKER_CONFIGURED, TransactionResultEventType.BAKER_CONFIGURED);
        map.put(AccountTransactionEffects.EffectCase.DELEGATION_CONFIGURED, TransactionResultEventType.DELEGATION_CONFIGURED);

        return map;
    }

    Map<ReasonCase, RejectReasonType> REASON_CASE_TO_REJECT_REASON_TYPE = initializeReasonCaseToRejectReasonType();
    Map<AccountTransactionEffects.EffectCase, TransactionResultEventType> EFFECT_CASE_TRANSACTION_RESULT_EVENT_TYPE_MAP = initializeEffectCaseTransactionResultTypeMap();

    static TransactionResult toTransactionResult(AccountTransactionDetails transactionDetails) {
        var result = TransactionResult.builder();
        if (transactionDetails.getEffects().hasNone()) {
            result = result.outcome(Outcome.REJECT);
            var rejectReason = transactionDetails.getEffects().getNone().getRejectReason().getReasonCase();

            for (Map.Entry<ReasonCase, RejectReasonType> entry : REASON_CASE_TO_REJECT_REASON_TYPE.entrySet()) {
                if (rejectReason == entry.getKey()) {
                    RejectReasonType rejectReasonType = entry.getValue();
                    result = result.rejectReason(new RejectReason() {
                        @Override
                        public RejectReasonType getType() {
                            return rejectReasonType;
                        }
                    });
                    break;
                }
            }
        } else {
            var effectCase = transactionDetails.getEffects().getEffectCase();
            List<TransactionResultEvent> eventList = new ArrayList<>();

            for (Map.Entry<AccountTransactionEffects.EffectCase, TransactionResultEventType> entry : EFFECT_CASE_TRANSACTION_RESULT_EVENT_TYPE_MAP.entrySet()) {
                if (effectCase == entry.getKey()) {
                    TransactionResultEventType transactionResultEventType = entry.getValue();
                    eventList.add(new TransactionResultEvent() {
                        @Override
                        public TransactionResultEventType getType() {
                            return transactionResultEventType;
                        }
                    });
                }
            }
            if (eventList.isEmpty()) {
                eventList = null;
            }


            result = result.outcome(Outcome.SUCCESS)
                    .events(eventList);

        }

        return result.build();
    }

    static TransactionTypeInfo toTransactionType(BlockItemSummary summary) {
        var builder = TransactionTypeInfo.builder();

        var detailsCase = summary.getDetailsCase();
        switch (detailsCase) {
            case ACCOUNT_TRANSACTION:
                builder = builder.type(TransactionType.ACCOUNT_TRANSACTION);
                var effects = summary.getAccountTransaction().getEffects().getEffectCase();
                switch (effects) {
                    case MODULE_DEPLOYED:
                        builder = builder.contents(TransactionContents.DEPLOY_MODULE);
                        break;
                    case CONTRACT_INITIALIZED:
                        builder = builder.contents(TransactionContents.CONTRACT_INITIALIZED);
                        break;
                    case CONTRACT_UPDATE_ISSUED:
                        builder = builder.contents(TransactionContents.CONTRACT_UPDATED);
                        break;
                    case ACCOUNT_TRANSFER:
                        builder = builder.contents(TransactionContents.TRANSFER);
                        break;
                    case BAKER_ADDED:
                        builder = builder.contents(TransactionContents.ADD_BAKER);
                        break;
                    case BAKER_REMOVED:
                        builder = builder.contents(TransactionContents.REMOVE_BAKER);
                        break;
                    case BAKER_STAKE_UPDATED:
                        builder = builder.contents(TransactionContents.UPDATE_BAKER_STAKE);
                        break;
                    case BAKER_RESTAKE_EARNINGS_UPDATED:
                        builder = builder.contents(TransactionContents.UPDATE_BAKER_RESTAKE_EARNINGS);
                        break;
                    case BAKER_KEYS_UPDATED:
                        builder = builder.contents(TransactionContents.UPDATE_BAKER_KEYS);
                        break;
                    case ENCRYPTED_AMOUNT_TRANSFERRED:
                        builder = builder.contents(TransactionContents.ENCRYPTED_AMOUNT_TRANSFER);
                        break;
                    case TRANSFERRED_TO_ENCRYPTED:
                        builder = builder.contents(TransactionContents.TRANSFER_TO_ENCRYPTED);
                        break;
                    case TRANSFERRED_TO_PUBLIC:
                        builder = builder.contents(TransactionContents.TRANSFER_TO_PUBLIC);
                        break;
                    case TRANSFERRED_WITH_SCHEDULE:
                        builder = builder.contents(TransactionContents.TRANSFER_WITH_SCHEDULE);
                        break;
                    case CREDENTIAL_KEYS_UPDATED:
                        builder = builder.contents(TransactionContents.UPDATE_CREDENTIAL_KEYS);
                        break;
                    case DATA_REGISTERED:
                        builder = builder.contents(TransactionContents.REGISTER_DATA);
                        break;
                    case BAKER_CONFIGURED:
                        builder = builder.contents(TransactionContents.CONFIGURE_BAKER);
                        break;
                    case DELEGATION_CONFIGURED:
                        builder = builder.contents(TransactionContents.CONFIGURE_DELEGATION);
                        break;
                }
                break;
            case ACCOUNT_CREATION:
                builder = builder.type(TransactionType.CREDENTIAL_DEPLOYMENT_TRANSACTION);
                var credentialType = summary.getAccountCreation().getCredentialType();
                if (credentialType == com.concordium.grpc.v2.CredentialType.CREDENTIAL_TYPE_INITIAL) {
                    builder = builder.contents(TransactionContents.INITIAL);
                } else if (credentialType == com.concordium.grpc.v2.CredentialType.CREDENTIAL_TYPE_NORMAL) {
                    builder = builder.contents(TransactionContents.NORMAL);
                }
                break;
            case UPDATE:
                builder = builder.type(TransactionType.UPDATE_TRANSACTION);
                var updatePayload = summary.getUpdate().getPayload().getPayloadCase();
                switch (updatePayload) {
                    case PROTOCOL_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_PROTOCOL);
                    case ELECTION_DIFFICULTY_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_ELECTION_DIFFICULTY);
                    case EURO_PER_ENERGY_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_EURO_PER_ENERGY);
                    case MICRO_CCD_PER_EURO_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_MICRO_GTU_PER_EURO);
                    case FOUNDATION_ACCOUNT_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_FOUNDATION_ACCOUNT);
                    case MINT_DISTRIBUTION_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_MINT_DISTRIBUTION);
                    case TRANSACTION_FEE_DISTRIBUTION_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_TRANSACTION_FEE_DISTRIBUTION);
                    case GAS_REWARDS_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_GAS_REWARDS);
                    case BAKER_STAKE_THRESHOLD_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_BAKER_STAKE_THRESHOLD);
                    case ROOT_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_ROOT_KEYS);
                    case LEVEL_1_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_LEVEL_1_KEYS);
                    case ADD_ANONYMITY_REVOKER_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_ADD_ANONYMITY_REVOKER);
                    case ADD_IDENTITY_PROVIDER_UPDATE:
                        builder = builder.contents(TransactionContents.ADD_IDENTITY_PROVIDER);
                    case COOLDOWN_PARAMETERS_CPV_1_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_COOLDOWN_PARAMETERS);
                    case POOL_PARAMETERS_CPV_1_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_POOL_PARAMETERS);
                    case TIME_PARAMETERS_CPV_1_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_TIME_PARAMETERS);
                    case MINT_DISTRIBUTION_CPV_1_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_MINT_DISTRIBUTION);
                    case GAS_REWARDS_CPV_2_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_GAS_REWARDS);
                    case TIMEOUT_PARAMETERS_UPDATE:
                        builder = builder.contents(TransactionContents.UPDATE_TIME_PARAMETERS);
                    case MIN_BLOCK_TIME_UPDATE:
                        builder = builder.contents(TransactionContents.MIN_BLOCK_TIME_UPDATE);
                    case BLOCK_ENERGY_LIMIT_UPDATE:
                        builder = builder.contents(TransactionContents.BLOCK_ENERGY_LIMIT_UPDATE);
                }
                break;
        }

        return builder.build();
    }

    /**
     * Converts from Grpc Timestamp to ZonedDateTime
     * @param localtime {@link com.concordium.grpc.v2.Timestamp} timestamp from the Grpc API
     * @return {@link ZonedDateTime} matching ZonedDateTime object
     */
    static ZonedDateTime toZonedDateTime(com.concordium.grpc.v2.Timestamp localtime) {
        return Instant.EPOCH.plusSeconds(localtime.getValue()).atZone(UTC_ZONE);
    }
    /**
     * Converts from Grpc Duration to Java Duration
     * @param duration {@link com.concordium.grpc.v2.Duration} duration from the Grpc API
     * @return {@link java.time.Duration} matching Java Duration object
     */
    static java.time.Duration toDuration(Duration duration) {
        return java.time.Duration.ofMillis(duration.getValue());
    }


    static Hash to(StateHash stateHash) {
        return Hash.from(stateHash.getValue().toByteArray());
    }

    static com.concordium.sdk.responses.blockinfo.BlockInfo to(BlockInfo blockInfo) {
        return com.concordium.sdk.responses.blockinfo.BlockInfo.builder()
                .blockHash(to(blockInfo.getHash()))
                .blockHeight(to(blockInfo.getHeight()))
                .transactionEnergyCost((int) blockInfo.getTransactionsEnergyCost().getValue())
                .blockBaker(to((int) blockInfo.getBaker().getValue()).ordinal())
                .blockStateHash(to(blockInfo.getStateHash()))
                .blockSlotTime(to(to(blockInfo.getSlotTime())))
                .blockParent(to(blockInfo.getParentBlock()))
                .blockReceiveTime(to(to(blockInfo.getReceiveTime())))
                .genesisIndex(blockInfo.getGenesisIndex().getValue())
                .blockSlot((int) blockInfo.getSlotNumber().getValue())
                .finalized(blockInfo.getFinalized())
                .eraBlockHeight((int) blockInfo.getEraBlockHeight().getValue())
                .blockLastFinalized(to(blockInfo.getLastFinalizedBlock()))
                .transactionsSize(blockInfo.getTransactionsSize())
                .transactionCount(blockInfo.getTransactionCount())
                .blockArriveTime(to(to(blockInfo.getArriveTime())))
                .build();
    }

    static Optional<FinalizationData> to(BlockFinalizationSummary finalizationSummary) {
        if (finalizationSummary.hasNone()) {return Optional.empty();} //There is no finalization data in the block
        val finalizationData = finalizationSummary.getRecord();
        val finalizationBlockPointer = to(finalizationData.getBlock()).toString();
        UInt64 finalizationIndex = UInt64.from(finalizationData.getIndex().getValue());
        UInt64 finalizationDelay = UInt64.from(finalizationData.getDelay().getValue());
        val grpcFinalizers = finalizationData.getFinalizersList();
        val finalizers = new ImmutableList.Builder<Finalizer>();
        grpcFinalizers.forEach(f -> finalizers.add(to(f)));

        return Optional.of(FinalizationData.builder()
                .finalizationBlockPointer(finalizationBlockPointer)
                .finalizationIndex(finalizationIndex)
                .finalizationDelay(finalizationDelay)
                .finalizers(finalizers.build()).build());
    }

    static Finalizer to(FinalizationSummaryParty finalizer) {
        return Finalizer.builder()
                .bakerId(com.concordium.sdk.responses.AccountIndex.from(finalizer.getBaker().getValue()))
                .weight(BigInteger.valueOf(finalizer.getWeight()))
                .signed(finalizer.getSigned()).build();
    }

    static ImmutableList<SpecialOutcome> to(Iterator<BlockSpecialEvent> events) {
        val result = new ImmutableList.Builder<SpecialOutcome>();
        events.forEachRemaining(event -> result.add(to(event)));
        return result.build();
    }

    static SpecialOutcome to(BlockSpecialEvent event) {
        switch (event.getEventCase()) {
            case BAKING_REWARDS: {
                val bakingRewards = event.getBakingRewards();
                return BakingRewards.builder()
                        .remainder(CCDAmount.fromMicro(bakingRewards.getRemainder().getValue()))
                        .bakerRewards(toRewardList(bakingRewards.getBakerRewards().getEntriesList()))
                        .build();
            }
            case MINT: {
                val mint = event.getMint();
                return Mint.builder()
                        .mintBakingReward(CCDAmount.fromMicro(mint.getMintBakingReward().getValue()))
                        .mintFinalizationReward(CCDAmount.fromMicro(mint.getMintFinalizationReward().getValue()))
                        .mintPlatformDevelopmentCharge(CCDAmount.fromMicro(mint.getMintPlatformDevelopmentCharge().getValue()))
                        .foundationAccount(com.concordium.sdk.transactions.AccountAddress.from(mint.getFoundationAccount().getValue().toByteArray()))
                        .build();
            }
            case FINALIZATION_REWARDS: {
                val finalizationRewards = event.getFinalizationRewards();
                return FinalizationRewards.builder()
                        .rewards(toRewardList(finalizationRewards.getFinalizationRewards().getEntriesList()))
                        .remainder(CCDAmount.fromMicro(finalizationRewards.getRemainder().getValue()))
                        .build();
            }
            case BLOCK_REWARD: {
                val blockReward = event.getBlockReward();
                return BlockReward.builder()
                        .transactionFees(CCDAmount.fromMicro(blockReward.getTransactionFees().getValue()))
                        .oldGASAccount(CCDAmount.fromMicro(blockReward.getOldGasAccount().getValue()))
                        .newGASAccount(CCDAmount.fromMicro(blockReward.getNewGasAccount().getValue()))
                        .bakerReward(CCDAmount.fromMicro(blockReward.getBakerReward().getValue()))
                        .foundationCharge(CCDAmount.fromMicro(blockReward.getFoundationCharge().getValue()))
                        .baker(com.concordium.sdk.transactions.AccountAddress.from(blockReward.getBaker().getValue().toByteArray()))
                        .foundationAccount(com.concordium.sdk.transactions.AccountAddress.from(blockReward.getFoundationAccount().getValue().toByteArray()))
                        .build();
            }
            case PAYDAY_FOUNDATION_REWARD: {
                val paydayFoundationReward = event.getPaydayFoundationReward();
                return PaydayFoundationReward.builder()
                        .foundationAccount(com.concordium.sdk.transactions.AccountAddress.from(paydayFoundationReward.getFoundationAccount().getValue().toByteArray()))
                        .developmentCharge(CCDAmount.fromMicro(paydayFoundationReward.getDevelopmentCharge().getValue()))
                        .build();
            }
            case PAYDAY_ACCOUNT_REWARD: {
                val paydayAccountReward = event.getPaydayAccountReward();
                return PaydayAccountReward.builder()
                        .account(com.concordium.sdk.transactions.AccountAddress.from(paydayAccountReward.getAccount().getValue().toByteArray()))
                        .transactionFees(CCDAmount.fromMicro(paydayAccountReward.getTransactionFees().getValue()))
                        .bakerReward(CCDAmount.fromMicro(paydayAccountReward.getBakerReward().getValue()))
                        .finalizationReward(CCDAmount.fromMicro(paydayAccountReward.getFinalizationReward().getValue()))
                        .build();
            }
            case BLOCK_ACCRUE_REWARD: {
                val blockAccrueReward = event.getBlockAccrueReward();
                return BlockAccrueReward.builder()
                        .transactionFees(CCDAmount.fromMicro(blockAccrueReward.getTransactionFees().getValue()))
                        .oldGASAccount(CCDAmount.fromMicro(blockAccrueReward.getOldGasAccount().getValue()))
                        .newGASAccount(CCDAmount.fromMicro(blockAccrueReward.getNewGasAccount().getValue()))
                        .bakerReward(CCDAmount.fromMicro(blockAccrueReward.getBakerReward().getValue()))
                        .passiveReward(CCDAmount.fromMicro(blockAccrueReward.getPassiveReward().getValue()))
                        .foundationCharge(CCDAmount.fromMicro(blockAccrueReward.getFoundationCharge().getValue()))
                        .bakerId(com.concordium.sdk.responses.AccountIndex.from(blockAccrueReward.getBaker().getValue()))
                        .build();
            }
            case PAYDAY_POOL_REWARD: {
                val paydayPoolReward = event.getPaydayPoolReward();
                val result = PaydayPoolReward.builder();
                if (paydayPoolReward.hasPoolOwner()) {result.poolOwner(paydayPoolReward.getPoolOwner().getValue());}
                result.transactionFees(CCDAmount.fromMicro(paydayPoolReward.getTransactionFees().getValue()))
                        .bakerReward(CCDAmount.fromMicro(paydayPoolReward.getBakerReward().getValue()))
                        .finalizationReward(CCDAmount.fromMicro(paydayPoolReward.getFinalizationReward().getValue()));
                return result.build();
            }
            default:
                throw new IllegalStateException("Unexpected value: " + event.getEventCase());
        }

    }

    static List<Reward> toRewardList(List<BlockSpecialEvent.AccountAmounts.Entry> entriesList) {
        val result = new ImmutableList.Builder<Reward>();
        entriesList.forEach(e ->
                result.add(Reward.builder()
                        .address(com.concordium.sdk.transactions.AccountAddress.from(e.getAccount().getValue().toByteArray()))
                        .amount(CCDAmount.fromMicro(e.getAmount().getValue())).build())
        );
        return result.build();
    }

    // Note. In extreme cases then the recursion happening below can lead to
    // stack overflows. However, this should not be a problem in reality, as we
    // do not expect that much branching. Default stack size is mostly 1mb and ~ 7_000 nested calls, which
    // is well within the expected branching.
    static Branch to(com.concordium.grpc.v2.Branch branch) {
        return Branch.builder()
                .blockHash(to(branch.getBlockHash()))
                .children(to(branch.getChildrenList(), ClientV2MapperExtensions::to))
                .build();
    }

    static com.concordium.sdk.responses.DelegatorInfo to(DelegatorInfo delegatorInfo) {
        return com.concordium.sdk.responses.DelegatorInfo.builder()
                .account(to(delegatorInfo.getAccount()))
                .stake(to(delegatorInfo.getStake()))
                .pendingChange(delegatorInfo.hasPendingChange()
                        ? Optional.of(to(delegatorInfo.getPendingChange()))
                        : Optional.empty())
                .build();
    }

    static com.concordium.sdk.responses.election.ElectionInfo to(ElectionInfo grpcOutput) {
        return com.concordium.sdk.responses.election.ElectionInfo.builder()
                .electionDifficulty(to(grpcOutput.getElectionDifficulty().getValue()))
                .leadershipElectionNonce(grpcOutput.getElectionNonce().getValue().toByteArray())
                .bakerElectionInfo(ImmutableList.copyOf(to(grpcOutput.getBakerElectionInfoList(), ClientV2MapperExtensions::to)))
                .build();
    }

    static ElectionInfoBaker to(ElectionInfo.Baker i) {
        return ElectionInfoBaker.builder()
                .baker(to(i.getBaker()))
                .account(to(i.getAccount()))
                .lotteryPower(i.getLotteryPower())
                .build();
    }

    static ContractAddress to(com.concordium.sdk.types.ContractAddress address) {
        return ContractAddress.newBuilder()
                .setIndex(address.getIndex())
                .setSubindex(address.getSubIndex()).build();
    }

    static Address to(AbstractAddress address) {
        val builder = Address.newBuilder();
        switch (address.getType()) {
            case ADDRESS_ACCOUNT:
                val account = (Account) address;
                builder.setAccount(to(account.getAddress())).build();
                break;
            case ADDRESS_CONTRACT:
                val contract = (com.concordium.sdk.types.ContractAddress) address;
                builder.setContract(to(contract)).build();
                break;
        }
        return builder.build();
    }

    static Amount to(CCDAmount amount) {
        return Amount.newBuilder().setValue(amount.getValue().getValue()).build();
    }
}
