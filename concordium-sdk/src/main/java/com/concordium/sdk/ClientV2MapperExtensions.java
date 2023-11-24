package com.concordium.sdk;

import com.concordium.grpc.v2.AccessStructure;
import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountIndex;
import com.concordium.grpc.v2.AccountInfo;
import com.concordium.grpc.v2.AuthorizationsV1;
import com.concordium.grpc.v2.BakerId;
import com.concordium.grpc.v2.BlockItem;
import com.concordium.grpc.v2.Commitment;
import com.concordium.grpc.v2.ContractAddress;
import com.concordium.grpc.v2.CredentialPublicKeys;
import com.concordium.grpc.v2.CredentialRegistrationId;
import com.concordium.grpc.v2.DelegatorInfo;
import com.concordium.grpc.v2.DelegatorRewardPeriodInfo;
import com.concordium.grpc.v2.EncryptedAmount;
import com.concordium.grpc.v2.GasRewards;
import com.concordium.grpc.v2.HigherLevelKeys;
import com.concordium.grpc.v2.Memo;
import com.concordium.grpc.v2.NextUpdateSequenceNumbers;
import com.concordium.grpc.v2.Policy;
import com.concordium.grpc.v2.ProtocolVersion;
import com.concordium.grpc.v2.ReleaseSchedule;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.requests.EpochQuery;
import com.concordium.sdk.responses.Epoch;
import com.concordium.sdk.responses.Round;
import com.concordium.sdk.responses.TimeoutParameters;
import com.concordium.sdk.responses.*;
import com.concordium.sdk.responses.accountinfo.BakerPoolInfo;
import com.concordium.sdk.responses.accountinfo.CommissionRates;
import com.concordium.sdk.responses.accountinfo.*;
import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.responses.accountinfo.credential.*;
import com.concordium.sdk.responses.bakersrewardperiod.BakerInfo;
import com.concordium.sdk.responses.blockitemsummary.AccountCreationDetails;
import com.concordium.sdk.responses.blockitemsummary.*;
import com.concordium.sdk.responses.blocksummary.FinalizationData;
import com.concordium.sdk.responses.blocksummary.Finalizer;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.*;
import com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate;
import com.concordium.sdk.responses.blocksummary.updates.keys.*;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.PendingUpdateType;
import com.concordium.sdk.responses.branch.Branch;
import com.concordium.sdk.responses.chainparameters.ChainParametersV0;
import com.concordium.sdk.responses.chainparameters.ChainParametersV1;
import com.concordium.sdk.responses.chainparameters.ChainParametersV2;
import com.concordium.sdk.responses.chainparameters.CooldownParametersCpv1;
import com.concordium.sdk.responses.chainparameters.*;
import com.concordium.sdk.responses.consensusstatus.ConsensusStatus;
import com.concordium.sdk.responses.election.ElectionInfoBaker;
import com.concordium.sdk.responses.poolstatus.BakerPoolStatus;
import com.concordium.sdk.responses.poolstatus.CurrentPaydayStatus;
import com.concordium.sdk.responses.poolstatus.PendingChangeReduceBakerCapital;
import com.concordium.sdk.responses.poolstatus.PendingChangeRemovePool;
import com.concordium.sdk.responses.rewardstatus.RewardsOverview;
import com.concordium.sdk.responses.smartcontracts.ContractVersion;
import com.concordium.sdk.responses.transactionstatus.DelegationTarget;
import com.concordium.sdk.responses.transactionstatus.OpenStatus;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.AccountTransaction;
import com.concordium.sdk.transactions.InitContractPayload;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Signature;
import com.concordium.sdk.transactions.TransferPayload;
import com.concordium.sdk.transactions.TransferWithMemoPayload;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.UpdateContract;
import com.concordium.sdk.transactions.smartcontracts.WasmModule;
import com.concordium.sdk.transactions.smartcontracts.WasmModuleVersion;
import com.concordium.sdk.types.Timestamp;
import com.concordium.sdk.types.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.protobuf.ByteString;
import lombok.NonNull;
import lombok.val;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.copyOf;

/**
 * Object Mapping Extensions. Maps from GRPC types to client types and vice versa.
 */
interface ClientV2MapperExtensions {

    static com.concordium.grpc.v2.BlockHashInput to(final BlockQuery input) {
        BlockHashInput.Builder builder = BlockHashInput.newBuilder();
        switch (input.getType()) {
            case BEST:
                builder.setBest(Empty.getDefaultInstance());
                break;
            case LAST_FINAL:
                builder.setLastFinal(Empty.getDefaultInstance());
                break;
            case GIVEN:
                if (Objects.isNull(input.getBlockHash())) {
                    throw new IllegalArgumentException("Block Hash must be set when querying by a block hash.");
                }
                builder.setGiven(to(input.getBlockHash()));
                break;
            case HEIGHT:
                if (Objects.isNull(input.getHeight())) {
                    throw new IllegalArgumentException("Block height must be set when querying by height.");
                }
                switch (input.getHeight().getType()) {
                    case ABSOLUTE:
                        builder
                                .setAbsoluteHeight(AbsoluteBlockHeight
                                        .newBuilder()
                                        .setValue(input.getHeight().getHeight())
                                        .build());
                        break;
                    case RELATIVE:
                        builder
                                .setRelativeHeight(com.concordium.grpc.v2.BlockHashInput.RelativeHeight
                                        .newBuilder()
                                        .setGenesisIndex(GenesisIndex.newBuilder().setValue(input.getHeight().getGenesisIndex()).build())
                                        .setHeight(BlockHeight.newBuilder().setValue(input.getHeight().getHeight()).build())
                                        .setRestrict(input.getHeight().isRestrictedToGenesisIndex())
                                        .build());
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid type");
        }

        return builder.build();
    }

    static com.concordium.grpc.v2.BlockHash to(final Hash blockHash) {
        return com.concordium.grpc.v2.BlockHash.newBuilder().setValue(to(blockHash.getBytes())).build();
    }

    static ModuleRef to(final com.concordium.sdk.responses.modulelist.ModuleRef moduleRef) {
        return ModuleRef.newBuilder()
                .setValue(to(moduleRef.getBytes()))
                .build();
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

    static com.concordium.sdk.responses.blocksummary.updates.queues.Description to(
            final com.concordium.grpc.v2.Description description) {
        return com.concordium.sdk.responses.blocksummary.updates.queues.Description.builder()
                .description(description.getDescription())
                .url(description.getUrl())
                .name(description.getName())
                .build();
    }

    static com.concordium.sdk.responses.chainparameters.ChainParameters to(final com.concordium.grpc.v2.ChainParameters parameters) {
        switch (parameters.getParametersCase()) {
            case V0:
                val v0Params = parameters.getV0();
                return ChainParametersV0.from(v0Params);
            case V1:
                val v1Params = parameters.getV1();
                return ChainParametersV1.from(v1Params);
            case V2:
                val v2Params = parameters.getV2();
                return ChainParametersV2.from(v2Params);
            case PARAMETERS_NOT_SET:
                throw new IllegalArgumentException("Unexpected parameters version");

        }
        throw new IllegalArgumentException("Parameters version not set");
    }

    static int to(final ArInfo.ArIdentity identity) {
        return identity.getValue();
    }

    static int to(final IpIdentity identity) {
        return identity.getValue();
    }

    static PSPublicKey to(final IpInfo.IpVerifyKey verifyKey) {
        return PSPublicKey.from(verifyKey.getValue().toByteArray());
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

    static com.concordium.grpc.v2.AccountIdentifierInput to(AccountQuery accountIdentifier) {
        AccountIdentifierInput.Builder builder = AccountIdentifierInput.newBuilder();
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

    static AccountAddress to(com.concordium.sdk.types.AccountAddress address) {
        return AccountAddress.newBuilder()
                .setValue(to(address.getBytes()))
                .build();
    }

    static com.concordium.sdk.responses.accountinfo.AccountInfo to(AccountInfo account) {
        com.concordium.sdk.responses.accountinfo.AccountInfo.AccountInfoBuilder builder = com.concordium.sdk.responses.accountinfo.AccountInfo.builder();
        builder
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

    static com.concordium.sdk.types.AccountAddress to(AccountAddress address) {
        return com.concordium.sdk.types.AccountAddress.from(address.getValue().toByteArray());
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
                .bakerInfo(BakerInfo.from(stake.getBakerInfo()))
                .build();
    }

    static com.concordium.sdk.responses.BakerId to(BakerId bakerId) {
        return com.concordium.sdk.responses.BakerId.from(bakerId.getValue());
    }

    static PendingChange to(StakePendingChange pendingChange) {
        switch (pendingChange.getChangeCase()) {
            case REDUCE:
                StakePendingChange.Reduce reduce = pendingChange.getReduce();
                return ReduceStakeChange.builder()
                        .effectiveTime(Timestamp.from(reduce.getEffectiveTime()))
                        .newStake(to(reduce.getNewStake()))
                        .build();
            case REMOVE:
                return RemoveStakeChange.builder()
                        .effectiveTime(Timestamp.from(pendingChange.getRemove()))
                        .build();
            default:
            case CHANGE_NOT_SET:
                throw new IllegalArgumentException();
        }
    }

    static BakerPoolInfo to(com.concordium.grpc.v2.BakerPoolInfo poolInfo) {
        return BakerPoolInfo.builder()
                .metadataUrl(poolInfo.getUrl())
                .openStatus(to(poolInfo.getOpenStatus()))
                .commissionRates(CommissionRates.from(poolInfo.getCommissionRates()))
                .build();
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

    static java.time.YearMonth to(com.concordium.grpc.v2.YearMonth validTo) {
        return java.time.YearMonth.of(validTo.getYear(), validTo.getMonth());
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
                .timestamp(Timestamp.from(s.getTimestamp()))
                .transactions(to(s.getTransactionsList(), ClientV2MapperExtensions::to))
                .build();
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
        val builder = CredentialDeploymentTransaction
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
                        .payload(DeployModule.builder().module(deployModulePayload).build())
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
                final UpdateContract updateContractPayload = to(payload.getUpdateContract());
                return UpdateContractTransaction.builderAccountTransactionBlockItem()
                        .header(to(transaction.getHeader(), updateContractPayload.getRawPayloadBytes().length))
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
                        .payload(RawPayload.from(rawPayloadBytes))
                        .build();
            default:
            case PAYLOAD_NOT_SET:
                throw new IllegalArgumentException("Cannot parse account transaction as payload tag was not set.");
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

    static UpdateContract to(com.concordium.grpc.v2.UpdateContractPayload updateContract) {
        return UpdateContract.from(
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

    static com.concordium.sdk.responses.modulelist.ModuleRef to(ModuleRef moduleRef) {
        return com.concordium.sdk.responses.modulelist.ModuleRef.from(moduleRef.getValue().toByteArray());
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
        val builder = TransactionSignature.builder();

        for (val credentialIndex : signatures.getSignaturesMap().keySet()) {
            val builderInternal
                    = TransactionSignatureAccountSignatureMap.builder();
            for (val index : signatures.getSignaturesMap().get(credentialIndex).getSignaturesMap().keySet()) {
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
        val ret = TransactionHeader.builder()
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
    static java.time.Duration to(Duration slotDuration) {
        return java.time.Duration.ofMillis(slotDuration.getValue());
    }

    // Convert a ProtocolVersion object to the corresponding com.concordium.sdk.responses.ProtocolVersion object
    static com.concordium.sdk.responses.ProtocolVersion to(ProtocolVersion protocolVersion) {
        return com.concordium.sdk.responses.ProtocolVersion.forValue(protocolVersion.getNumber() + 1);
    }

    // Convert a ConsensusInfo object to a ConsensusStatus object
    static ConsensusStatus to(ConsensusInfo consensusInfo) {
        val builder = ConsensusStatus.builder()
                .bestBlock(to(consensusInfo.getBestBlock()))
                .genesisBlock(to(consensusInfo.getGenesisBlock()))
                .genesisTime(Timestamp.from(consensusInfo.getGenesisTime()))
                .slotDuration(to(consensusInfo.getSlotDuration()))
                .epochDuration(to(consensusInfo.getEpochDuration()))
                .lastFinalizedBlock(to(consensusInfo.getLastFinalizedBlock()))
                .bestBlockHeight(to(consensusInfo.getBestBlockHeight()).getValue())
                .lastFinalizedBlockHeight(to(consensusInfo.getLastFinalizedBlockHeight()).getValue())
                .blocksReceivedCount(consensusInfo.getBlocksReceivedCount())
                .blockLastReceivedTime(Timestamp.from(consensusInfo.getBlockLastReceivedTime()))
                .blockReceiveLatencyEMA(consensusInfo.getBlockReceiveLatencyEma())
                .blockReceiveLatencyEMSD(consensusInfo.getBlockReceiveLatencyEmsd())
                .blockReceivePeriodEMA(consensusInfo.getBlockReceivePeriodEma())
                .blockReceivePeriodEMSD(consensusInfo.getBlockReceivePeriodEmsd())
                .blocksVerifiedCount(consensusInfo.getBlocksVerifiedCount())
                .blockLastArrivedTime(Timestamp.from(consensusInfo.getBlockLastArrivedTime()))
                .blockArriveLatencyEMA(consensusInfo.getBlockArriveLatencyEma())
                .blockArriveLatencyEMSD(consensusInfo.getBlockArriveLatencyEmsd())
                .blockArrivePeriodEMA(consensusInfo.getBlockArrivePeriodEma())
                .blockArrivePeriodEMSD(consensusInfo.getBlockArrivePeriodEmsd())
                .transactionsPerBlockEMA(consensusInfo.getTransactionsPerBlockEma())
                .transactionsPerBlockEMSD(consensusInfo.getTransactionsPerBlockEmsd())
                .finalizationCount(consensusInfo.getFinalizationCount())
                .lastFinalizedTime(Timestamp.from(consensusInfo.getLastFinalizedTime()))
                .finalizationPeriodEMA(consensusInfo.getFinalizationPeriodEma())
                .finalizationPeriodEMSD(consensusInfo.getFinalizationPeriodEmsd())
                .protocolVersion(to(consensusInfo.getProtocolVersion()))
                .genesisIndex(consensusInfo.getGenesisIndex().getValue())
                .currentEraGenesisBlock(to(consensusInfo.getCurrentEraGenesisBlock()))
                .currentEraGenesisTime(Timestamp.from(consensusInfo.getCurrentEraGenesisTime()))
                .currentRound(to(consensusInfo.getCurrentRound()))
                .currentEpoch(to(consensusInfo.getCurrentEpoch()))
                .triggerBlockTime(Timestamp.from(consensusInfo.getTriggerBlockTime()));
        return builder.build();
    }

    static Epoch to(com.concordium.grpc.v2.Epoch currentEpoch) {
        return Epoch.from(currentEpoch.getValue());
    }

    static Round to(com.concordium.grpc.v2.Round currentRound) {
        return Round.from(currentRound.getValue());
    }

    static SendBlockItemRequest to(AccountTransaction accountTransaction) {
        return SendBlockItemRequest.newBuilder()
                .setAccountTransaction(com.concordium.grpc.v2.AccountTransaction.newBuilder()
                        .setHeader(to(accountTransaction.getHeader()))
                        .setPayload(AccountTransactionPayload.newBuilder()
                                .setRawPayload(ByteString.copyFrom(accountTransaction.getPayload().getBytes()))
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
        val builder = com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters.builder()
                .bulletproofGenerators(BulletproofGenerators.from(grpcOutput.getBulletproofGenerators().toByteArray()))
                .onChainCommitmentKey(PedersenCommitmentKey.from(grpcOutput.getOnChainCommitmentKey().toByteArray()))
                .genesisString(grpcOutput.getGenesisString());

        return builder.build();

    }

    static RewardsOverview to(TokenomicsInfo tokenomicsInfo) {
        RewardsOverview.RewardsOverviewBuilder builder = RewardsOverview.builder();
        if (tokenomicsInfo.hasV0()) {
            builder = builder.totalAmount(to(tokenomicsInfo.getV0().getTotalAmount()))
                    .totalEncryptedAmount(to(tokenomicsInfo.getV0().getTotalEncryptedAmount()))
                    .bakingRewardAccount(to(tokenomicsInfo.getV0().getBakingRewardAccount()))
                    .finalizationRewardAccount(to(tokenomicsInfo.getV0().getFinalizationRewardAccount()))
                    .gasAccount(to(tokenomicsInfo.getV0().getGasAccount()))
                    .protocolVersion(to(tokenomicsInfo.getV0().getProtocolVersion()));
        } else if (tokenomicsInfo.hasV1()) {
            builder = builder.totalAmount(to(tokenomicsInfo.getV1().getTotalAmount()))
                    .totalEncryptedAmount(to(tokenomicsInfo.getV1().getTotalEncryptedAmount()))
                    .bakingRewardAccount(to(tokenomicsInfo.getV1().getBakingRewardAccount()))
                    .finalizationRewardAccount(to(tokenomicsInfo.getV1().getFinalizationRewardAccount()))
                    .gasAccount(to(tokenomicsInfo.getV1().getGasAccount()))
                    .foundationTransactionRewards(to(tokenomicsInfo.getV1().getFoundationTransactionRewards()))
                    .nextPaydayTime(Timestamp.from(tokenomicsInfo.getV1().getNextPaydayTime()))
                    .nextPaydayMintRate(to(tokenomicsInfo.getV1().getNextPaydayMintRate()))
                    .totalStakedCapital(to(tokenomicsInfo.getV1().getTotalStakedCapital()))
                    .protocolVersion(to(tokenomicsInfo.getV1().getProtocolVersion()));
        }

        return builder.build();
    }

    static double to(MintRate mintRate) {
        return mintRate.getMantissa() * Math.pow(10, -1 * mintRate.getExponent());
    }


    static Summary to(BlockItemSummary blockItemSummary) {
        val summary = Summary.builder()
                .transactionIndex(UInt64.from(blockItemSummary.getIndex().getValue()))
                .transactionHash(to(blockItemSummary.getHash()))
                .energyCost(com.concordium.sdk.requests.smartcontracts.Energy.from(blockItemSummary.getEnergyCost()));
        switch (blockItemSummary.getDetailsCase()) {
            case ACCOUNT_TRANSACTION:
                val tx = blockItemSummary.getAccountTransaction();
                summary.details(
                        Details
                                .builder()
                                .type(Type.ACCOUNT_TRANSACTION)
                                .accountTransactionDetails(com.concordium.sdk.responses.blockitemsummary.AccountTransactionDetails.from(tx))
                                .build());
                break;
            case ACCOUNT_CREATION:
                val accountCreation = blockItemSummary.getAccountCreation();
                summary.details(Details
                        .builder()
                        .type(Type.ACCOUNT_CREATION)
                        .accountCreationDetails(AccountCreationDetails.from(accountCreation))
                        .build());
                break;
            case UPDATE:
                val details = ChainUpdateDetails.from(blockItemSummary.getUpdate());
                summary.details(Details
                        .builder()
                        .type(Type.CHAIN_UPDATE)
                        .chainUpdateDetails(details)
                        .build());
                break;
            case DETAILS_NOT_SET:
                throw new IllegalArgumentException("Unrecognized block item summary");
        }

        return summary.build();
    }

    static Hash to(StateHash stateHash) {
        return Hash.from(stateHash.getValue().toByteArray());
    }

    static com.concordium.sdk.responses.blockinfo.BlockInfo to(BlockInfo blockInfo) {
        return com.concordium.sdk.responses.blockinfo.BlockInfo.builder()
                .blockHash(to(blockInfo.getHash()))
                .blockHeight(to(blockInfo.getHeight()))
                .transactionEnergyCost((int) blockInfo.getTransactionsEnergyCost().getValue())
                .blockBaker(to(blockInfo.getBaker().getValue()))
                .blockStateHash(to(blockInfo.getStateHash()))
                .blockTime(Timestamp.from(blockInfo.getSlotTime()))
                .blockParent(to(blockInfo.getParentBlock()))
                .blockReceiveTime(Timestamp.from(blockInfo.getReceiveTime()))
                .genesisIndex(blockInfo.getGenesisIndex().getValue())
                .blockSlot((int) blockInfo.getSlotNumber().getValue())
                .finalized(blockInfo.getFinalized())
                .eraBlockHeight((int) blockInfo.getEraBlockHeight().getValue())
                .blockLastFinalized(to(blockInfo.getLastFinalizedBlock()))
                .transactionsSize(blockInfo.getTransactionsSize())
                .transactionCount(blockInfo.getTransactionCount())
                .blockArriveTime(Timestamp.from(blockInfo.getArriveTime()))
                .protocolVersion(to(blockInfo.getProtocolVersion()))
                .round(to(blockInfo.getRound()))
                .epoch(to(blockInfo.getEpoch()))
                .build();
    }

    static com.concordium.sdk.responses.BakerId to(long value) {
        return com.concordium.sdk.responses.BakerId.from(value);
    }

    static Optional<FinalizationData> to(BlockFinalizationSummary finalizationSummary) {
        if (finalizationSummary.hasNone()) {
            return Optional.empty();
        } //There is no finalization data in the block
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
                        .foundationAccount(com.concordium.sdk.types.AccountAddress.from(mint.getFoundationAccount().getValue().toByteArray()))
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
                        .baker(com.concordium.sdk.types.AccountAddress.from(blockReward.getBaker().getValue().toByteArray()))
                        .foundationAccount(com.concordium.sdk.types.AccountAddress.from(blockReward.getFoundationAccount().getValue().toByteArray()))
                        .build();
            }
            case PAYDAY_FOUNDATION_REWARD: {
                val paydayFoundationReward = event.getPaydayFoundationReward();
                return PaydayFoundationReward.builder()
                        .foundationAccount(com.concordium.sdk.types.AccountAddress.from(paydayFoundationReward.getFoundationAccount().getValue().toByteArray()))
                        .developmentCharge(CCDAmount.fromMicro(paydayFoundationReward.getDevelopmentCharge().getValue()))
                        .build();
            }
            case PAYDAY_ACCOUNT_REWARD: {
                val paydayAccountReward = event.getPaydayAccountReward();
                return PaydayAccountReward.builder()
                        .account(com.concordium.sdk.types.AccountAddress.from(paydayAccountReward.getAccount().getValue().toByteArray()))
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
                if (paydayPoolReward.hasPoolOwner()) {
                    result.poolOwner(paydayPoolReward.getPoolOwner().getValue());
                }
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
                        .address(com.concordium.sdk.types.AccountAddress.from(e.getAccount().getValue().toByteArray()))
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
                .electionDifficulty(PartsPerHundredThousand.from(grpcOutput.getElectionDifficulty().getValue().getPartsPerHundredThousand()).asDouble())
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

    static BakerId to(com.concordium.sdk.responses.BakerId bakerId) {
        return BakerId.newBuilder().setValue(bakerId.toLong()).build();
    }

    static BakerPoolStatus to(PoolInfoResponse grpcOutput) {
        return BakerPoolStatus.builder()
                .bakerId(to(grpcOutput.getBaker()))
                .bakerAddress(to(grpcOutput.getAddress()))
                .bakerEquityCapital(to(grpcOutput.getEquityCapital()))
                .delegatedCapital(to(grpcOutput.getDelegatedCapital()))
                .delegatedCapitalCap(to(grpcOutput.getDelegatedCapitalCap()))
                .poolInfo(to(grpcOutput.getPoolInfo()))
                .bakerStakePendingChange(grpcOutput.hasEquityPendingChange()
                        ? to(grpcOutput.getEquityPendingChange())
                        : null)
                .currentPaydayStatus(grpcOutput.hasCurrentPaydayInfo() ? to(grpcOutput.getCurrentPaydayInfo()) : null)
                .allPoolTotalCapital(to(grpcOutput.getAllPoolTotalCapital()))
                .build();
    }

    static com.concordium.sdk.responses.poolstatus.PendingChange to(PoolPendingChange equityPendingChange) {
        switch (equityPendingChange.getChangeCase()) {
            case REDUCE:
                return PendingChangeReduceBakerCapital.builder()
                        .effectiveTime(Timestamp.from(equityPendingChange.getReduce().getEffectiveTime()))
                        .bakerEquityCapital(to(equityPendingChange.getReduce().getReducedEquityCapital()))
                        .build();
            case REMOVE:
                return PendingChangeRemovePool.builder()
                        .effectiveTime(Timestamp.from(equityPendingChange.getRemove().getEffectiveTime()))
                        .build();
            case CHANGE_NOT_SET:
            default:
                return null;
        }
    }

    static @NonNull CurrentPaydayStatus to(PoolCurrentPaydayInfo currentPaydayInfo) {
        return CurrentPaydayStatus.builder()
                .bakerEquityCapital(to(currentPaydayInfo.getBakerEquityCapital()))
                .blocksBaked(UInt64.from(currentPaydayInfo.getBlocksBaked()))
                .delegatedCapital(to(currentPaydayInfo.getDelegatedCapital()))
                .effectiveStake(to(currentPaydayInfo.getEffectiveStake()))
                .finalizationLive(currentPaydayInfo.getFinalizationLive())
                .lotteryPower(currentPaydayInfo.getLotteryPower())
                .transactionFeesEarned(to(currentPaydayInfo.getTransactionFeesEarned()))
                .commissionRates(CommissionRates.from(currentPaydayInfo.getCommissionRates()))
                .build();
    }

    static com.concordium.sdk.responses.intanceinfo.InstanceInfo to(com.concordium.grpc.v2.InstanceInfo instanceInfo) {
        switch (instanceInfo.getVersionCase()) {
            default:
            case VERSION_NOT_SET:
                throw new IllegalArgumentException("Invalid Version");
            case V0:
                val v0 = instanceInfo.getV0();
                return com.concordium.sdk.responses.intanceinfo.InstanceInfo.builder()
                        .amount(to(v0.getAmount()))
                        .methods(ImmutableList.copyOf(
                                to(v0.getMethodsList(), com.concordium.grpc.v2.ReceiveName::getValue)))
                        .owner(to(v0.getOwner()))
                        .version(ContractVersion.V0)
                        .sourceModule(to(v0.getSourceModule()))
                        .name(v0.getName().getValue())
                        .build();
            case V1:
                val v1 = instanceInfo.getV1();
                return com.concordium.sdk.responses.intanceinfo.InstanceInfo.builder()
                        .amount(to(v1.getAmount()))
                        .methods(ImmutableList.copyOf(
                                to(v1.getMethodsList(), com.concordium.grpc.v2.ReceiveName::getValue)))
                        .owner(to(v1.getOwner()))
                        .version(ContractVersion.V1)
                        .sourceModule(to(v1.getSourceModule()))
                        .name(v1.getName().getValue())
                        .build();
        }
    }

    static com.concordium.sdk.responses.DelegatorRewardPeriodInfo to(DelegatorRewardPeriodInfo i) {
        return com.concordium.sdk.responses.DelegatorRewardPeriodInfo.builder()
                .account(to(i.getAccount()))
                .stake(to(i.getStake()))
                .build();
    }

    static com.concordium.sdk.responses.NextUpdateSequenceNumbers to(NextUpdateSequenceNumbers grpcOutput) {
        return com.concordium.sdk.responses.NextUpdateSequenceNumbers.builder()
                .rootKeys(to(grpcOutput.getRootKeys()))
                .level1Keys(to(grpcOutput.getLevel1Keys()))
                .level2Keys(to(grpcOutput.getLevel2Keys()))
                .protocol(to(grpcOutput.getProtocol()))
                .electionDifficulty(to(grpcOutput.getElectionDifficulty()))
                .euroPerEnergy(to(grpcOutput.getEuroPerEnergy()))
                .microCcdPerEuro(to(grpcOutput.getMicroCcdPerEuro()))
                .foundationAccount(to(grpcOutput.getFoundationAccount()))
                .mintDistribution(to(grpcOutput.getMintDistribution()))
                .transactionFeesDistribution(to(grpcOutput.getTransactionFeeDistribution()))
                .gasRewards(to(grpcOutput.getGasRewards()))
                .poolParameters(to(grpcOutput.getPoolParameters()))
                .addAnonymityRevoker(to(grpcOutput.getAddAnonymityRevoker()))
                .addIdentityProvider(to(grpcOutput.getAddIdentityProvider()))
                .cooldownParameters(to(grpcOutput.getCooldownParameters()))
                .timeParameters(to(grpcOutput.getTimeParameters()))
                .timeoutParameters(to(grpcOutput.getTimeoutParameters()))
                .minBlockTime(to(grpcOutput.getMinBlockTime()))
                .blockEnergyLimit(to(grpcOutput.getBlockEnergyLimit()))
                .finalizationCommitteeParameters(to(grpcOutput.getFinalizationCommitteeParameters()))
                .build();
    }

    static PendingUpdateV2 to(PendingUpdate u) {
        switch (u.getEffectCase()) {
            case ROOT_KEYS:
                return PendingUpdateV2.<KeysUpdate>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.RootKeys)
                        .update(to(u.getRootKeys()))
                        .build();
            case LEVEL1_KEYS:
                return PendingUpdateV2.<KeysUpdate>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.Level1Keys)
                        .update(to(u.getLevel1Keys()))
                        .build();
            case LEVEL2_KEYS_CPV_0:
                return PendingUpdateV2.<Level2KeysUpdates>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.Level2CpV0Keys)
                        .update(to(u.getLevel2KeysCpv0()))
                        .build();
            case LEVEL2_KEYS_CPV_1:
                return PendingUpdateV2.<Level2KeysUpdates>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.Level2CpV1Keys)
                        .update(to(u.getLevel2KeysCpv1()))
                        .build();
            case PROTOCOL:
                return PendingUpdateV2.<ProtocolUpdate>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.Protocol)
                        .update(to(u.getProtocol()))
                        .build();
            case ELECTION_DIFFICULTY:
                return PendingUpdateV2.<Double>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.ElectionDifficulty)
                        .update(PartsPerHundredThousand.from(u.getElectionDifficulty().getValue().getPartsPerHundredThousand()).asDouble())
                        .build();
            case EURO_PER_ENERGY:
                return PendingUpdateV2.<Fraction>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.EuroPerEnergy)
                        .update(to(u.getEuroPerEnergy().getValue()))
                        .build();
            case MICRO_CCD_PER_EURO:
                return PendingUpdateV2.<Fraction>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.MicroCcdPerEuro)
                        .update(to(u.getMicroCcdPerEuro().getValue()))
                        .build();
            case FOUNDATION_ACCOUNT:
                return PendingUpdateV2.<com.concordium.sdk.types.AccountAddress>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.FoundationAccount)
                        .update(to(u.getFoundationAccount()))
                        .build();
            case MINT_DISTRIBUTION_CPV_0:
                return PendingUpdateV2.<MintDistributionCpV0>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.MintDistributionCpV0)
                        .update(to(u.getMintDistributionCpv0()))
                        .build();
            case MINT_DISTRIBUTION_CPV_1:
                return PendingUpdateV2.<MintDistributionCpV1>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.MintDistributionCpV1)
                        .update(to(u.getMintDistributionCpv1()))
                        .build();
            case TRANSACTION_FEE_DISTRIBUTION:
                return PendingUpdateV2.<com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.TransactionFeeDistribution)
                        .update(com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution.from(u.getTransactionFeeDistribution()))
                        .build();
            case GAS_REWARDS:
                return PendingUpdateV2.<com.concordium.sdk.responses.chainparameters.GasRewards>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.GasRewards)
                        .update(to(u.getGasRewards()))
                        .build();
            case POOL_PARAMETERS_CPV_0:
                return PendingUpdateV2.<CCDAmount>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.PoolParametersCpV0)
                        .update(to(u.getPoolParametersCpv0().getBakerStakeThreshold()))
                        .build();
            case POOL_PARAMETERS_CPV_1:
                return PendingUpdateV2.<PoolParameters>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.PoolParametersCpV1)
                        .update(to(u.getPoolParametersCpv1()))
                        .build();
            case ADD_ANONYMITY_REVOKER:
                return PendingUpdateV2.<AnonymityRevokerInfo>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.AddAnonymityRevoker)
                        .update(to(u.getAddAnonymityRevoker()))
                        .build();
            case ADD_IDENTITY_PROVIDER:
                return PendingUpdateV2.<IdentityProviderInfo>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.AddIdentityProvider)
                        .update(to(u.getAddIdentityProvider()))
                        .build();
            case COOLDOWN_PARAMETERS:
                return PendingUpdateV2.<CooldownParametersCpv1>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.CoolDownParameters)
                        .update(to(u.getCooldownParameters()))
                        .build();
            case TIME_PARAMETERS:
                return PendingUpdateV2.<TimeParameters>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.TimeParameters)
                        .update(to(u.getTimeParameters()))
                        .build();
            case GAS_REWARDS_CPV_2:
                return PendingUpdateV2.<GasRewardsCpV2>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.GasRewardsCpV2)
                        .update(to(u.getGasRewardsCpv2()))
                        .build();
            case TIMEOUT_PARAMETERS:
                return PendingUpdateV2.<com.concordium.sdk.responses.TimeoutParameters>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.TimeoutParameters)
                        .update(to(u.getTimeoutParameters()))
                        .build();
            case MIN_BLOCK_TIME:
                return PendingUpdateV2.<java.time.Duration>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.MinBlockTime)
                        .update(to(u.getMinBlockTime()))
                        .build();
            case BLOCK_ENERGY_LIMIT:
                return PendingUpdateV2.<UInt64>builder()
                        .effectiveTime(to(u.getEffectiveTime()))
                        .type(PendingUpdateType.BlockEnergyLimit)
                        .update(to(u.getBlockEnergyLimit()))
                        .build();
            default:
            case EFFECT_NOT_SET:
                throw new IllegalArgumentException("Unexpected effect case");
        }
    }

    static TimeoutParameters to(com.concordium.grpc.v2.TimeoutParameters timeoutParameters) {
        return TimeoutParameters.builder()
                .timeoutBase(to(timeoutParameters.getTimeoutBase()))
                .timeoutIncrease(to(timeoutParameters.getTimeoutIncrease()))
                .timeoutDecrease(to(timeoutParameters.getTimeoutDecrease()))
                .build();
    }

    static GasRewardsCpV2 to(GasRewardsCpv2 gasRewardsCpv2) {
        return GasRewardsCpV2.builder()
                .accountCreation(PartsPerHundredThousand.from(gasRewardsCpv2.getAccountCreation().getPartsPerHundredThousand()).asDouble())
                .baker(PartsPerHundredThousand.from(gasRewardsCpv2.getBaker().getPartsPerHundredThousand()).asDouble())
                .chainUpdate(PartsPerHundredThousand.from(gasRewardsCpv2.getChainUpdate().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    static TimeParameters to(TimeParametersCpv1 timeParameters) {
        return TimeParameters.builder()
                .mintPerPayday(to(timeParameters.getMintPerPayday()))
                .rewardPeriodLength(timeParameters.getRewardPeriodLength().getValue().getValue())
                .build();
    }

    static CooldownParametersCpv1 to(com.concordium.grpc.v2.CooldownParametersCpv1 cooldownParameters) {
        return CooldownParametersCpv1.builder()
                .delegatorCooldown(cooldownParameters.getDelegatorCooldown().getValue())
                .poolOwnerCooldown(cooldownParameters.getPoolOwnerCooldown().getValue())
                .build();
    }

    static PoolParameters to(PoolParametersCpv1 poolParametersCpv1) {
        return PoolParameters.builder()
                .bakingCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getBaking()))
                .capitalBound(PartsPerHundredThousand.from(poolParametersCpv1.getCapitalBound().getValue().getPartsPerHundredThousand()).asDouble())
                .finalizationCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getFinalization()))
                .leverageBound(to(poolParametersCpv1.getLeverageBound().getValue()))
                .minimumEquityCapital(to(poolParametersCpv1.getMinimumEquityCapital()))
                .passiveTransactionCommission(PartsPerHundredThousand.from(poolParametersCpv1.getPassiveTransactionCommission().getPartsPerHundredThousand()).asDouble())
                .transactionCommissionRange(Range.from(poolParametersCpv1.getCommissionBounds().getTransaction()))
                .passiveBakingCommission(PartsPerHundredThousand.from(poolParametersCpv1.getPassiveBakingCommission().getPartsPerHundredThousand()).asDouble())
                .passiveFinalizationCommission(PartsPerHundredThousand.from(poolParametersCpv1.getPassiveFinalizationCommission().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    static com.concordium.sdk.responses.chainparameters.GasRewards to(GasRewards gasRewards) {
        val builder = com.concordium.sdk.responses.chainparameters.GasRewards.builder()
                .accountCreation(PartsPerHundredThousand.from(gasRewards.getAccountCreation().getPartsPerHundredThousand()).asDouble())
                .baker(PartsPerHundredThousand.from(gasRewards.getBaker().getPartsPerHundredThousand()).asDouble())
                .chainUpdate(PartsPerHundredThousand.from(gasRewards.getChainUpdate().getPartsPerHundredThousand()).asDouble());
        if (gasRewards.hasFinalizationProof()) {
            builder.finalizationProof(PartsPerHundredThousand.from(gasRewards.getFinalizationProof().getPartsPerHundredThousand()).asDouble());
        }
        return builder.build();
    }

    static MintDistributionCpV1 to(MintDistributionCpv1 mintDistribution) {
        return MintDistributionCpV1.builder()
                .bakingReward(PartsPerHundredThousand.from(mintDistribution.getBakingReward().getPartsPerHundredThousand()).asDouble())
                .finalizationReward(PartsPerHundredThousand.from(mintDistribution.getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    static MintDistributionCpV0 to(MintDistributionCpv0 mintDistribution) {
        return MintDistributionCpV0.builder()
                .mintPerSlot(to(mintDistribution.getMintPerSlot()))
                .bakingReward(PartsPerHundredThousand.from(mintDistribution.getBakingReward().getPartsPerHundredThousand()).asDouble())
                .finalizationReward(PartsPerHundredThousand.from(mintDistribution.getFinalizationReward().getPartsPerHundredThousand()).asDouble())
                .build();
    }

    static ProtocolUpdate to(com.concordium.grpc.v2.ProtocolUpdate protocol) {
        return ProtocolUpdate.builder()
                .message(protocol.getMessage())
                .specificationURL(protocol.getSpecificationUrl())
                .specificationHash(to(protocol.getSpecificationHash()))
                .specificationAuxiliaryData(protocol.getSpecificationAuxiliaryData().toByteArray())
                .build();
    }

    static Hash to(Sha256Hash specificationHash) {
        return Hash.from(specificationHash.getValue().toByteArray());
    }

    static Level2KeysUpdates to(AuthorizationsV1 level2Keys) {
        return Level2KeysUpdates.builder()
                .addAnonymityRevoker(to(level2Keys.getV0().getAddAnonymityRevoker()))
                .electionDifficulty(to(level2Keys.getV0().getParameterConsensus()))
                .euroPerEnergy(to(level2Keys.getV0().getParameterEuroPerEnergy()))
                .protocol(to(level2Keys.getV0().getProtocol()))
                .poolParameters(to(level2Keys.getV0().getPoolParameters()))
                .transactionFeeDistribution(to(level2Keys.getV0().getParameterTransactionFeeDistribution()))
                .mintDistribution(to(level2Keys.getV0().getParameterMintDistribution()))
                .microGTUPerEuro(to(level2Keys.getV0().getParameterMicroCCDPerEuro()))
                .paramGASRewards(to(level2Keys.getV0().getParameterGasRewards()))
                .foundationAccount(to(level2Keys.getV0().getParameterFoundationAccount()))
                .emergency(to(level2Keys.getV0().getEmergency()))
                .addIdentityProvider(to(level2Keys.getV0().getAddIdentityProvider()))
                .verificationKeys(to(level2Keys.getV0().getKeysList(), ClientV2MapperExtensions::to))
                .addAnonymityRevoker(to(level2Keys.getV0().getAddAnonymityRevoker()))
                .cooldownParameters(to(level2Keys.getParameterCooldown()))
                .timeParameters(to(level2Keys.getParameterTime()))
                .build();
    }

    static Level2KeysUpdates to(com.concordium.grpc.v2.AuthorizationsV0 level2Keys) {
        return Level2KeysUpdates.builder()
                .addAnonymityRevoker(to(level2Keys.getAddAnonymityRevoker()))
                .electionDifficulty(to(level2Keys.getParameterConsensus()))
                .euroPerEnergy(to(level2Keys.getParameterEuroPerEnergy()))
                .protocol(to(level2Keys.getProtocol()))
                .transactionFeeDistribution(to(level2Keys.getParameterTransactionFeeDistribution()))
                .mintDistribution(to(level2Keys.getParameterMintDistribution()))
                .microGTUPerEuro(to(level2Keys.getParameterMicroCCDPerEuro()))
                .paramGASRewards(to(level2Keys.getParameterGasRewards()))
                .foundationAccount(to(level2Keys.getParameterFoundationAccount()))
                .emergency(to(level2Keys.getEmergency()))
                .addIdentityProvider(to(level2Keys.getAddIdentityProvider()))
                .verificationKeys(to(level2Keys.getKeysList(), ClientV2MapperExtensions::to))
                .addAnonymityRevoker(to(level2Keys.getAddAnonymityRevoker()))
                .poolParameters(to(level2Keys.getPoolParameters()))
                .build();
    }

    static Authorization to(AccessStructure accessStructure) {
        return Authorization.builder()
                .threshold((byte) accessStructure.getAccessThreshold().getValue())
                .authorizedKeys(to(accessStructure.getAccessPublicKeysList(), (t) -> t.getValue()))
                .build();
    }

    static Fraction to(Ratio value) {
        return Fraction.from(value);
    }

    static KeysUpdate to(HigherLevelKeys rootKeys) {
        return KeysUpdate.builder()
                .threshold(rootKeys.getThreshold().getValue())
                .verificationKeys(to(rootKeys.getKeysList(), ClientV2MapperExtensions::to))
                .build();
    }

    static VerificationKey to(UpdatePublicKey k) {
        return VerificationKey.builder()
                .verifyKey(k.getValue().toByteArray())
                .signingScheme(SigningScheme.ED25519)
                .build();
    }

    static ContractAddress to(com.concordium.sdk.types.ContractAddress contractAddress) {
        return ContractAddress.newBuilder()
                .setIndex(contractAddress.getIndex())
                .setSubindex(contractAddress.getSubIndex())
                .build();
    }

    static KeyValurPair to(InstanceStateKVPair instanceStateKVPair) {
        return KeyValurPair.builder()
                .key(instanceStateKVPair.getKey().toByteArray())
                .value(instanceStateKVPair.getValue().toByteArray())
                .build();
    }

    static Address to(AbstractAddress address) {
        val builder = Address.newBuilder();
        switch (address.getType()) {
            case ADDRESS_ACCOUNT:
                com.concordium.sdk.types.AccountAddress account = (com.concordium.sdk.types.AccountAddress) address;
                builder.setAccount(AccountAddress.newBuilder().setValue(ByteString.copyFrom(account.getBytes())));
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

    static com.concordium.grpc.v2.ReceiveName to(ReceiveName receiveName) {
        return com.concordium.grpc.v2.ReceiveName.newBuilder()
                .setValue(receiveName.getContractName() + "." + receiveName.getMethod())
                .build();

    }

    static com.concordium.grpc.v2.Parameter to(Parameter parameter) {
        return com.concordium.grpc.v2.Parameter.newBuilder()
                .setValue(ByteString.copyFrom(parameter.getBytes()))
                .build();
    }

    static EpochRequest to(EpochQuery query) {
        switch (query.getType()) {
            case BLOCK_HASH:
                return EpochRequest.newBuilder()
                        .setBlockHash(to(query.getBlockHashInput())).build();
            case RELATIVE_EPOCH:
                return EpochRequest.newBuilder()
                        .setRelativeEpoch(
                                EpochRequest.RelativeEpoch.newBuilder()
                                        .setGenesisIndex(GenesisIndex.newBuilder().setValue(query.getGenesisIndex()).build())
                                        .setEpoch(com.concordium.grpc.v2.Epoch.newBuilder().setValue(query.getEpoch().getValue().getValue()).build())
                                        .build()
                        ).build();
            default:
                throw new IllegalArgumentException("Unexpected EpochQuery");
        }
    }
}
