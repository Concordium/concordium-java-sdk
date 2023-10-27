package com.concordium.sdk;

import com.concordium.grpc.v2.AccessStructure;
import com.concordium.grpc.v2.AuthorizationsV0;
import com.concordium.grpc.v2.AuthorizationsV1;
import com.concordium.grpc.v2.CooldownParametersCpv1;
import com.concordium.grpc.v2.GasRewards;
import com.concordium.grpc.v2.HigherLevelKeys;
import com.concordium.grpc.v2.TransactionFeeDistribution;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.pointchevalsanders.PSPublicKey;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.responses.PendingUpdateV2;
import com.concordium.sdk.responses.blocksummary.updates.keys.*;
import com.concordium.sdk.responses.blocksummary.updates.queues.AnonymityRevokerInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.blocksummary.updates.queues.PendingUpdateType;
import com.concordium.sdk.responses.chainparameters.*;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.NonNull;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

/**
 * Tests Mapping from GRPC response using {@link ClientV2MapperExtensions#to(PendingUpdate)}
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetBlockPendingUpdatesTest {

    private static final com.concordium.sdk.types.AccountAddress FOUNDATION_ACCNT
            = com.concordium.sdk.types.AccountAddress.from(
            "37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");

    private static final String AR_NAME = "ar_name";
    private static final String AR_DESC = "ar desc";
    private static final int AR_IDENTITY = 1;
    private static final String AR_URL = "ar url";
    private static final byte[] AR_PUBLIC_KEY = new byte[]{1, 1, 1};
    private static final ArInfo GRPC_AR_INFO = ArInfo.newBuilder()
            .setDescription(Description.newBuilder()
                    .setName(AR_NAME)
                    .setDescription(AR_DESC)
                    .setUrl(AR_URL)
                    .build())
            .setIdentity(ArInfo.ArIdentity.newBuilder().setValue(AR_IDENTITY).build())
            .setPublicKey(ArInfo.ArPublicKey.newBuilder()
                    .setValue(ByteString.copyFrom(AR_PUBLIC_KEY))
                    .build())
            .build();

    private static final byte[] ROOT_KEY_1 = new byte[]{2, 2, 2};
    private static final int ROOT_KEYS_UPDATE_THRESHOLD = 2;
    private static final HigherLevelKeys GRPC_ROOT_KEYS = HigherLevelKeys.newBuilder()
            .addAllKeys(Lists.newArrayList(toUpdatePublicKey(ROOT_KEY_1)))
            .setThreshold(UpdateKeysThreshold.newBuilder().setValue(ROOT_KEYS_UPDATE_THRESHOLD).build())
            .build();
    private static final String IP_NAME = "ip name";
    private static final String IP_DESC = "ip desc";
    private static final String IP_URL = "ip url";
    private static final int IP_IDENTITY = 33;
    private static final byte[] IP_CDR_VERIFY_KEY = new byte[]{
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private static final byte[] IP_VERIFY_KEY = new byte[]{4, 4, 4};
    private static final IpInfo GRPC_IP_INFO = IpInfo.newBuilder()
            .setDescription(Description.newBuilder()
                    .setName(IP_NAME)
                    .setDescription(IP_DESC)
                    .setUrl(IP_URL)
                    .build())
            .setIdentity(IpIdentity.newBuilder().setValue(IP_IDENTITY).build())
            .setCdiVerifyKey(IpInfo.IpCdiVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(IP_CDR_VERIFY_KEY))
                    .build())
            .setVerifyKey(IpInfo.IpVerifyKey.newBuilder()
                    .setValue(ByteString.copyFrom(IP_VERIFY_KEY))
                    .build())
            .build();
    private static final long BLOCK_ENERGY_LIMIT = 3L;
    private static final long POOL_DELEGATOR_COOLDOWN = 10;
    private static final long POOL_OWNER_COOLDOWN = 11;
    private static final CooldownParametersCpv1 GRPC_COOLDOWN_PARAMS = CooldownParametersCpv1.newBuilder()
            .setDelegatorCooldown(DurationSeconds.newBuilder().setValue(POOL_DELEGATOR_COOLDOWN).build())
            .setPoolOwnerCooldown(DurationSeconds.newBuilder().setValue(POOL_OWNER_COOLDOWN).build())
            .build();
    private static final int GAS_REWARDS_BAKER = 99;
    private static final int GAS_REWARDS_CHAIN = 100;
    private static final int GAS_REWARDS_ACCOUNT_CREATION = 101;
    private static final GasRewardsCpv2 GRPC_GAS_REWARDS_CPV2 = GasRewardsCpv2.newBuilder()
            .setBaker(toAmountFrac(GAS_REWARDS_BAKER))
            .setChainUpdate(toAmountFrac(GAS_REWARDS_CHAIN))
            .setAccountCreation(toAmountFrac(GAS_REWARDS_ACCOUNT_CREATION))
            .build();
    private static final int GAS_REWARDS_FIN_PROOF = 102;
    private static final GasRewards GRPC_GAS_REWARDS = GasRewards.newBuilder()
            .setBaker(toAmountFrac(GAS_REWARDS_BAKER))
            .setChainUpdate(toAmountFrac(GAS_REWARDS_CHAIN))
            .setAccountCreation(toAmountFrac(GAS_REWARDS_ACCOUNT_CREATION))
            .setFinalizationProof(toAmountFrac(GAS_REWARDS_FIN_PROOF))
            .build();
    private static final int ELECTION_DIFF = 103;
    private static final long EURO_PER_ENERGY_NUMERATOR = 1;
    private static final long EURO_PER_ENERGY_DENOMINATOR = 123;
    private static final long EFFECTIVE_TIME = 20202020;
    private static final int LEVEL_1_UPDATE_KEYS_THRESHOLD = 24;
    private static final byte[] LEVEL_1_UPDATE_KEY_1 = new byte[]{5, 5, 5};
    private static final byte[] LEVEL_1_UPDATE_KEY_2 = new byte[]{6, 6, 6};
    private static final List<UpdatePublicKey> GRPC_LEVEL_1_UPDATE_KEYS = Lists.newArrayList(
            toUpdatePublicKey(LEVEL_1_UPDATE_KEY_1),
            toUpdatePublicKey(LEVEL_1_UPDATE_KEY_2)
    );
    private static final HigherLevelKeys GRPC_LEVEL_1_KEYS = HigherLevelKeys.newBuilder()
            .setThreshold(UpdateKeysThreshold.newBuilder().setValue(LEVEL_1_UPDATE_KEYS_THRESHOLD).build())
            .addAllKeys(GRPC_LEVEL_1_UPDATE_KEYS)
            .build();
    private static final byte[] LEVEL_2_UPDATE_KEY_1 = new byte[]{7, 7, 7};
    private static final byte[] LEVEL_2_UPDATE_KEY_2 = new byte[]{8, 8, 8};
    private static final AuthorizationsV0 GRPC_LEVEL_2_KEYS_CPV0 = AuthorizationsV0.newBuilder()
            .addAllKeys(Lists.newArrayList(
                    toUpdatePublicKey(LEVEL_2_UPDATE_KEY_1),
                    toUpdatePublicKey(LEVEL_2_UPDATE_KEY_2)
            ))
            .setAddAnonymityRevoker(toAccessStructure(1))
            .setAddIdentityProvider(toAccessStructure(2))
            .setEmergency(toAccessStructure(3))
            .setParameterConsensus(toAccessStructure(4))
            .setParameterEuroPerEnergy(toAccessStructure(5))
            .setParameterFoundationAccount(toAccessStructure(6))
            .setParameterGasRewards(toAccessStructure(7))
            .setParameterMicroCCDPerEuro(toAccessStructure(8))
            .setParameterMintDistribution(toAccessStructure(9))
            .setParameterTransactionFeeDistribution(toAccessStructure(10))
            .setPoolParameters(toAccessStructure(11))
            .setProtocol(toAccessStructure(12))
            .build();
    private static final AuthorizationsV1 GRPC_LEVEL_2_KEYS_CPV1 = AuthorizationsV1.newBuilder()
            .setV0(GRPC_LEVEL_2_KEYS_CPV0)
            .setParameterCooldown(toAccessStructure(13))
            .setParameterTime(toAccessStructure(14))
            .build();
    private static final Level2KeysUpdates EXPE_LEVEL2_KEYS_CPV0 = Level2KeysUpdates.builder()
            .verificationKeys(Lists.newArrayList(
                    toVerificationKey(LEVEL_2_UPDATE_KEY_1),
                    toVerificationKey(LEVEL_2_UPDATE_KEY_2)
            ))
            .addAnonymityRevoker(toAuthorization(1))
            .addIdentityProvider(toAuthorization(2))
            .emergency(toAuthorization(3))
            .electionDifficulty(toAuthorization(4))
            .euroPerEnergy(toAuthorization(5))
            .foundationAccount(toAuthorization(6))
            .paramGASRewards(toAuthorization(7))
            .microGTUPerEuro(toAuthorization(8))
            .mintDistribution(toAuthorization(9))
            .transactionFeeDistribution(toAuthorization(10))
            .poolParameters(toAuthorization(11))
            .protocol(toAuthorization(12))
            .build();
    private static final Level2KeysUpdates EXPE_LEVEL2_KEYS_CPV1 = Level2KeysUpdates.builder()
            .verificationKeys(Lists.newArrayList(
                    toVerificationKey(LEVEL_2_UPDATE_KEY_1),
                    toVerificationKey(LEVEL_2_UPDATE_KEY_2)
            ))
            .addAnonymityRevoker(toAuthorization(1))
            .addIdentityProvider(toAuthorization(2))
            .emergency(toAuthorization(3))
            .electionDifficulty(toAuthorization(4))
            .euroPerEnergy(toAuthorization(5))
            .foundationAccount(toAuthorization(6))
            .paramGASRewards(toAuthorization(7))
            .microGTUPerEuro(toAuthorization(8))
            .mintDistribution(toAuthorization(9))
            .transactionFeeDistribution(toAuthorization(10))
            .poolParameters(toAuthorization(11))
            .protocol(toAuthorization(12))
            .cooldownParameters(toAuthorization(13))
            .timeParameters(toAuthorization(14))
            .build();
    private static final long TIMEOUT_DURATION = 90;
    private static final long TIMEOUT_INC_NUMERATOR = 11;
    private static final long TIMEOUT_INC_DENOMINATOR = 12;
    private static final long TIMEOUT_DEC_NUMERATOR = 13;
    private static final long TIMEOUT_DEC_DENOMINATOR = 14;
    private static final TimeoutParameters GRPC_TIMEOUT_PARAMS = TimeoutParameters.newBuilder()
            .setTimeoutBase(Duration.newBuilder().setValue(TIMEOUT_DURATION).build())
            .setTimeoutIncrease(toRatio(TIMEOUT_INC_NUMERATOR, TIMEOUT_INC_DENOMINATOR))
            .setTimeoutDecrease(toRatio(TIMEOUT_DEC_NUMERATOR, TIMEOUT_DEC_DENOMINATOR))
            .build();
    private static final int TIME_MINT_RATE_MANTISSA = 2;
    private static final int TIME_MINT_RATE_EXPONENT = 10;
    private static final long TIME_REWARD_PERIOD_LENGTH = 56;
    private static final TimeParametersCpv1 GRPC_TIME_PARAMS_CPV1 = TimeParametersCpv1.newBuilder()
            .setMintPerPayday(
                    MintRate.newBuilder()
                            .setMantissa(TIME_MINT_RATE_MANTISSA)
                            .setExponent(TIME_MINT_RATE_EXPONENT)
                            .build())
            .setRewardPeriodLength(RewardPeriodLength.newBuilder().setValue(
                            Epoch.newBuilder().setValue(TIME_REWARD_PERIOD_LENGTH).build())
                    .build())
            .build();
    private static final int TXN_FEE_BAKER = 5;
    private static final int TXN_FEES_GAS_ACCNT = 23;
    private static final TransactionFeeDistribution GRPC_TXN_FEES_DISTRIBUTION = TransactionFeeDistribution.newBuilder()
            .setBaker(toAmountFrac(TXN_FEE_BAKER))
            .setGasAccount(toAmountFrac(TXN_FEES_GAS_ACCNT))
            .build();
    private static final ExchangeRate GRPC_EURO_PER_ENERGY = ExchangeRate.newBuilder()
            .setValue(toRatio(EURO_PER_ENERGY_NUMERATOR, EURO_PER_ENERGY_DENOMINATOR))
            .build();
    private static final long MIN_BLOCK_TIME = 10;
    private static final Duration GRPC_MIN_BLOCK_TIME = Duration.newBuilder().setValue(MIN_BLOCK_TIME).build();
    private static final String PROTOCOL_MSG = "proto message";
    private static final byte[] PROTOCOL_SPEC_HASH = new byte[]{11, 11, 11};
    private static final String PROTOCOL_SPEC_URL = "proto.url";
    private static final byte[] PROTOCOL_SPEC_AUX_DATA = new byte[]{12, 12, 12};
    private static final ProtocolUpdate GRPC_PROTOCOL = ProtocolUpdate.newBuilder()
            .setMessage(PROTOCOL_MSG)
            .setSpecificationHash(Sha256Hash.newBuilder().setValue(ByteString.copyFrom(PROTOCOL_SPEC_HASH)).build())
            .setSpecificationUrl(PROTOCOL_SPEC_URL)
            .setSpecificationAuxiliaryData(ByteString.copyFrom(PROTOCOL_SPEC_AUX_DATA))
            .build();
    private static final AccountAddress GRPC_FOUNDATION_ACCNT = AccountAddress.newBuilder()
            .setValue(ByteString.copyFrom(FOUNDATION_ACCNT.getBytes()))
            .build();
    private static final ElectionDifficulty GRPC_ELECTION_DIFF = ElectionDifficulty.newBuilder()
            .setValue(toAmountFrac(ELECTION_DIFF)).build();
    private static final TransactionTime GRPC_EFFECTIVE_TIME = TransactionTime.newBuilder()
            .setValue(EFFECTIVE_TIME).build();
    private static final Energy GRPC_BLOCK_ENERGY_LIMIT = Energy.newBuilder().setValue(BLOCK_ENERGY_LIMIT).build();
    private static final int POOL_PARAMS_CPV1_CAPITAL_BOUND = 21;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MIN = 22;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MAX = 23;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_FIN_MIN = 24;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_FIN_MAX = 25;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_TXN_MIN = 26;
    private static final int POOL_PARAMS_CPV1_COMM_RANGE_TXN_MAX = 27;
    private static final long POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_NUME = 28;
    private static final long POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_DENO = 29;
    private static final long POOL_PARAMS_CPV1_MIN_EQUITY_CAPITAL = 30;
    private static final int POOL_PARAMS_CPV1_PASSIVE_BAKING_COMM = 31;
    private static final int POOL_PARAMS_CPV1_PASSIVE_FIN_COMM = 32;
    private static final int POOL_PARAMS_CPV1_PASSIVE_TXN_COMM = 33;
    private static final PoolParametersCpv1 GRPC_POOL_PARAMS_CPV1 = PoolParametersCpv1.newBuilder()
            .setCapitalBound(CapitalBound.newBuilder()
                    .setValue(toAmountFrac(POOL_PARAMS_CPV1_CAPITAL_BOUND))
                    .build())
            .setCommissionBounds(CommissionRanges.newBuilder()
                    .setBaking(toRangeAmountFraction(
                            POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MIN,
                            POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MAX))
                    .setFinalization(toRangeAmountFraction(
                            POOL_PARAMS_CPV1_COMM_RANGE_FIN_MIN,
                            POOL_PARAMS_CPV1_COMM_RANGE_FIN_MAX))
                    .setTransaction(toRangeAmountFraction(
                            POOL_PARAMS_CPV1_COMM_RANGE_TXN_MIN,
                            POOL_PARAMS_CPV1_COMM_RANGE_TXN_MAX))
                    .build())
            .setLeverageBound(LeverageFactor.newBuilder()
                    .setValue(toRatio(
                            POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_NUME,
                            POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_DENO))
                    .build())
            .setMinimumEquityCapital(Amount.newBuilder().setValue(POOL_PARAMS_CPV1_MIN_EQUITY_CAPITAL).build())
            .setPassiveBakingCommission(toAmountFrac(POOL_PARAMS_CPV1_PASSIVE_BAKING_COMM))
            .setPassiveFinalizationCommission(toAmountFrac(POOL_PARAMS_CPV1_PASSIVE_FIN_COMM))
            .setPassiveTransactionCommission(toAmountFrac(POOL_PARAMS_CPV1_PASSIVE_TXN_COMM))
            .build();
    private static final long POOL_PARAMS_CPV0_BAKER_THRESHOLD = 33;
    private static final BakerStakeThreshold GRPC_POOL_PARAMS_CPV0 = BakerStakeThreshold.newBuilder()
            .setBakerStakeThreshold(Amount.newBuilder().setValue(POOL_PARAMS_CPV0_BAKER_THRESHOLD).build())
            .build();
    private static final long MICRO_CCD_PER_EURO_NUME = 34;
    private static final long MICRO_CCD_PER_EURO_DENO = 35;
    private static final ExchangeRate GRPC_MICRO_CCD_PER_EURO = ExchangeRate.newBuilder()
            .setValue(toRatio(MICRO_CCD_PER_EURO_NUME, MICRO_CCD_PER_EURO_DENO))
            .build();
    private static final int MINT_DISTRIBUTION_BAKING_REWARD = 36;
    private static final int MINT_DISTRIBUTION_FIN_REWARD = 37;
    private static final int MINT_DISTRIBUTION_MINT_RATE_MANTISSA = 38;
    private static final int MINT_DISTRIBUTION_MINT_RATE_EXPONENT = 39;
    private static final MintDistributionCpv0 GRPC_MINT_DISTRIBUTION_CPV0 = MintDistributionCpv0.newBuilder()
            .setBakingReward(toAmountFrac(MINT_DISTRIBUTION_BAKING_REWARD))
            .setFinalizationReward(toAmountFrac(MINT_DISTRIBUTION_FIN_REWARD))
            .setMintPerSlot(MintRate.newBuilder()
                    .setMantissa(MINT_DISTRIBUTION_MINT_RATE_MANTISSA)
                    .setExponent(MINT_DISTRIBUTION_MINT_RATE_EXPONENT)
                    .build())
            .build();
    private static final MintDistributionCpv1 GRPC_MINT_DISTRIBUTION_CPV1 = MintDistributionCpv1.newBuilder()
            .setBakingReward(toAmountFrac(MINT_DISTRIBUTION_BAKING_REWARD))
            .setFinalizationReward(toAmountFrac(MINT_DISTRIBUTION_FIN_REWARD))
            .build();
    private static final List<PendingUpdate> GRPC_PENDING_UPDATES = Lists.newArrayList(
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setRootKeys(GRPC_ROOT_KEYS).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setLevel1Keys(GRPC_LEVEL_1_KEYS).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setLevel2KeysCpv0(GRPC_LEVEL_2_KEYS_CPV0).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setLevel2KeysCpv1(GRPC_LEVEL_2_KEYS_CPV1).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setProtocol(GRPC_PROTOCOL).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setElectionDifficulty(GRPC_ELECTION_DIFF).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setEuroPerEnergy(GRPC_EURO_PER_ENERGY).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setMicroCcdPerEuro(GRPC_MICRO_CCD_PER_EURO).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setFoundationAccount(GRPC_FOUNDATION_ACCNT).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setMintDistributionCpv0(GRPC_MINT_DISTRIBUTION_CPV0).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setMintDistributionCpv1(GRPC_MINT_DISTRIBUTION_CPV1).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setTransactionFeeDistribution(GRPC_TXN_FEES_DISTRIBUTION).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setGasRewards(GRPC_GAS_REWARDS).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setPoolParametersCpv0(GRPC_POOL_PARAMS_CPV0).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setPoolParametersCpv1(GRPC_POOL_PARAMS_CPV1).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setAddAnonymityRevoker(GRPC_AR_INFO).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setAddIdentityProvider(GRPC_IP_INFO).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setCooldownParameters(GRPC_COOLDOWN_PARAMS).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setTimeParameters(GRPC_TIME_PARAMS_CPV1).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setGasRewardsCpv2(GRPC_GAS_REWARDS_CPV2).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setTimeoutParameters(GRPC_TIMEOUT_PARAMS).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setMinBlockTime(GRPC_MIN_BLOCK_TIME).build(),
            PendingUpdate.newBuilder().setEffectiveTime(GRPC_EFFECTIVE_TIME).setBlockEnergyLimit(GRPC_BLOCK_ENERGY_LIMIT).build()
    );
    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {

                @Override
                public void getBlockPendingUpdates(
                        com.concordium.grpc.v2.BlockHashInput request,
                        StreamObserver<PendingUpdate> responseObserver) {
                    GRPC_PENDING_UPDATES.forEach(responseObserver::onNext);
                    responseObserver.onCompleted();
                }
            }
    ));
    private static final UInt64 EXPE_EFFECTIVE_TIME = UInt64.from(EFFECTIVE_TIME);
    private static final KeysUpdate EXPE_ROOT_KEYS = KeysUpdate.builder()
            .threshold(ROOT_KEYS_UPDATE_THRESHOLD)
            .verificationKeys(Lists.newArrayList(
                    toVerificationKey(ROOT_KEY_1)
            ))
            .build();
    private static final KeysUpdate EXPE_LEVEL1_KEYS = KeysUpdate.builder()
            .threshold(LEVEL_1_UPDATE_KEYS_THRESHOLD)
            .verificationKeys(Lists.newArrayList(
                    toVerificationKey(LEVEL_1_UPDATE_KEY_1),
                    toVerificationKey(LEVEL_1_UPDATE_KEY_2)
            ))
            .build();
    private static final com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate EXPE_PROTOCOL
            = com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate.builder()
            .message(PROTOCOL_MSG)
            .specificationAuxiliaryData(PROTOCOL_SPEC_AUX_DATA)
            .specificationHash(Hash.from(PROTOCOL_SPEC_HASH))
            .specificationURL(PROTOCOL_SPEC_URL)
            .build();
    private static final double HUNDRED_THOUSAND = 100_000D;
    private static final com.concordium.sdk.responses.chainparameters.GasRewards EXPE_GAS_REWARDS
            = com.concordium.sdk.responses.chainparameters.GasRewards.builder()
            .baker(toPPHT(GAS_REWARDS_BAKER))
            .chainUpdate(toPPHT(GAS_REWARDS_CHAIN))
            .accountCreation(toPPHT(GAS_REWARDS_ACCOUNT_CREATION))
            .finalizationProof(toPPHT(GAS_REWARDS_FIN_PROOF))
            .build();
    private static final com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution EXPE_TXN_FEE_DISTRIBUTION = com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution.builder()
            .allocatedForBaker(toPPHT(TXN_FEE_BAKER))
            .allocatedForGASAccount(toPPHT(TXN_FEES_GAS_ACCNT))
            .build();
    private static final PoolParameters EXPE_POOL_PARAMS_CPV1 = PoolParameters.builder()
            .capitalBound(toPPHT(POOL_PARAMS_CPV1_CAPITAL_BOUND))
            .bakingCommissionRange(Range.builder()
                    .min(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MIN))
                    .max(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_BAKING_MAX))
                    .build())
            .finalizationCommissionRange(Range.builder()
                    .min(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_FIN_MIN))
                    .max(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_FIN_MAX))
                    .build())
            .transactionCommissionRange(Range.builder()
                    .min(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_TXN_MIN))
                    .max(toPPHT(POOL_PARAMS_CPV1_COMM_RANGE_TXN_MAX))
                    .build())
            .leverageBound(Fraction.from(
                    POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_NUME,
                    POOL_PARAMS_CPV1_COMM_RANGE_LEVERAGE_DENO
            ))
            .minimumEquityCapital(CCDAmount.fromMicro(POOL_PARAMS_CPV1_MIN_EQUITY_CAPITAL))
            .passiveFinalizationCommission(toPPHT(POOL_PARAMS_CPV1_PASSIVE_FIN_COMM))
            .passiveBakingCommission(toPPHT(POOL_PARAMS_CPV1_PASSIVE_BAKING_COMM))
            .passiveFinalizationCommission(toPPHT(POOL_PARAMS_CPV1_PASSIVE_FIN_COMM))
            .passiveTransactionCommission(toPPHT(POOL_PARAMS_CPV1_PASSIVE_TXN_COMM))
            .build();
    private static final MintDistributionCpV0 EXPE_MINT_DISTRIBUTION_CPV0 = MintDistributionCpV0.builder()
            .bakingReward(toPPHT(MINT_DISTRIBUTION_BAKING_REWARD))
            .finalizationReward(toPPHT(MINT_DISTRIBUTION_FIN_REWARD))
            .mintPerSlot(MINT_DISTRIBUTION_MINT_RATE_MANTISSA * Math.pow(10, -1 * MINT_DISTRIBUTION_MINT_RATE_EXPONENT))
            .build();
    private static final MintDistributionCpV1 EXPE_MINT_DISTRIBUTION_CPV1 = MintDistributionCpV1.builder()
            .bakingReward(toPPHT(MINT_DISTRIBUTION_BAKING_REWARD))
            .finalizationReward(toPPHT(MINT_DISTRIBUTION_FIN_REWARD))
            .build();
    private static final AnonymityRevokerInfo EXPE_AR_INFO = AnonymityRevokerInfo.builder()
            .arIdentity(AR_IDENTITY)
            .description(com.concordium.sdk.responses.blocksummary.updates.queues.Description.builder()
                    .description(AR_DESC)
                    .name(AR_NAME)
                    .url(AR_URL)
                    .build())
            .arPublicKey(ElgamalPublicKey.from(AR_PUBLIC_KEY))
            .build();
    private static final IdentityProviderInfo EXPE_IP_INFO = IdentityProviderInfo.builder()
            .description(com.concordium.sdk.responses.blocksummary.updates.queues.Description.builder()
                    .name(IP_NAME)
                    .url(IP_URL)
                    .description(IP_DESC)
                    .build())
            .ipIdentity(IP_IDENTITY)
            .ipVerifyKey(PSPublicKey.from(IP_VERIFY_KEY))
            .ipCdiVerifyKey(ED25519PublicKey.from(IP_CDR_VERIFY_KEY))
            .build();
    private static final com.concordium.sdk.responses.chainparameters.CooldownParametersCpv1 EXPE_COOL_DOWN_PARAMS
            = com.concordium.sdk.responses.chainparameters.CooldownParametersCpv1.builder()
            .poolOwnerCooldown(POOL_OWNER_COOLDOWN)
            .delegatorCooldown(POOL_DELEGATOR_COOLDOWN)
            .build();
    private static final TimeParameters EXPE_TIME_PARAMS = TimeParameters.builder()
            .mintPerPayday(TIME_MINT_RATE_MANTISSA * Math.pow(10, -1 * TIME_MINT_RATE_EXPONENT))
            .rewardPeriodLength(TIME_REWARD_PERIOD_LENGTH)
            .build();
    private static final GasRewardsCpV2 EXPE_GAS_REWARDS_CPV2 = GasRewardsCpV2.builder()
            .baker(toPPHT(GAS_REWARDS_BAKER))
            .chainUpdate(toPPHT(GAS_REWARDS_CHAIN))
            .accountCreation(toPPHT(GAS_REWARDS_ACCOUNT_CREATION))
            .build();
    private static final com.concordium.sdk.responses.TimeoutParameters EXPE_TIMEOUT = com.concordium.sdk.responses.TimeoutParameters.builder()
            .timeoutBase(java.time.Duration.ofMillis(TIMEOUT_DURATION))
            .timeoutIncrease(Fraction.from(TIMEOUT_INC_NUMERATOR, TIMEOUT_INC_DENOMINATOR))
            .timeoutDecrease(Fraction.from(TIMEOUT_DEC_NUMERATOR, TIMEOUT_DEC_DENOMINATOR))
            .build();
    private static final List<PendingUpdateV2> EXPECTED
            = Lists.newArrayList(
            PendingUpdateV2.<KeysUpdate>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.RootKeys)
                    .update(EXPE_ROOT_KEYS)
                    .build(),
            PendingUpdateV2.<KeysUpdate>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.Level1Keys)
                    .update(EXPE_LEVEL1_KEYS)
                    .build(),
            PendingUpdateV2.<Level2KeysUpdates>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.Level2CpV0Keys)
                    .update(EXPE_LEVEL2_KEYS_CPV0)
                    .build(),
            PendingUpdateV2.<Level2KeysUpdates>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.Level2CpV1Keys)
                    .update(EXPE_LEVEL2_KEYS_CPV1)
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.responses.blocksummary.updates.ProtocolUpdate>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.Protocol)
                    .update(EXPE_PROTOCOL)
                    .build(),
            PendingUpdateV2.<Double>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.ElectionDifficulty)
                    .update(PartsPerHundredThousand.from(ELECTION_DIFF).asDouble())
                    .build(),
            PendingUpdateV2.<Fraction>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.EuroPerEnergy)
                    .update(Fraction.from(EURO_PER_ENERGY_NUMERATOR, EURO_PER_ENERGY_DENOMINATOR))
                    .build(),
            PendingUpdateV2.<Fraction>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.MicroCcdPerEuro)
                    .update(Fraction.from(MICRO_CCD_PER_EURO_NUME, MICRO_CCD_PER_EURO_DENO))
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.types.AccountAddress>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.FoundationAccount)
                    .update(FOUNDATION_ACCNT)
                    .build(),
            PendingUpdateV2.<MintDistributionCpV0>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.MintDistributionCpV0)
                    .update(EXPE_MINT_DISTRIBUTION_CPV0)
                    .build(),
            PendingUpdateV2.<MintDistributionCpV1>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.MintDistributionCpV1)
                    .update(EXPE_MINT_DISTRIBUTION_CPV1)
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.responses.chainparameters.TransactionFeeDistribution>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.TransactionFeeDistribution)
                    .update(EXPE_TXN_FEE_DISTRIBUTION)
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.responses.chainparameters.GasRewards>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.GasRewards)
                    .update(EXPE_GAS_REWARDS)
                    .build(),
            PendingUpdateV2.<CCDAmount>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.PoolParametersCpV0)
                    .update(CCDAmount.fromMicro(POOL_PARAMS_CPV0_BAKER_THRESHOLD))
                    .build(),
            PendingUpdateV2.<PoolParameters>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.PoolParametersCpV1)
                    .update(EXPE_POOL_PARAMS_CPV1)
                    .build(),
            PendingUpdateV2.<AnonymityRevokerInfo>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.AddAnonymityRevoker)
                    .update(EXPE_AR_INFO)
                    .build(),
            PendingUpdateV2.<IdentityProviderInfo>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.AddIdentityProvider)
                    .update(EXPE_IP_INFO)
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.responses.chainparameters.CooldownParametersCpv1>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.CoolDownParameters)
                    .update(EXPE_COOL_DOWN_PARAMS)
                    .build(),
            PendingUpdateV2.<TimeParameters>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.TimeParameters)
                    .update(EXPE_TIME_PARAMS)
                    .build(),
            PendingUpdateV2.<GasRewardsCpV2>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.GasRewardsCpV2)
                    .update(EXPE_GAS_REWARDS_CPV2)
                    .build(),
            PendingUpdateV2.<com.concordium.sdk.responses.TimeoutParameters>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.TimeoutParameters)
                    .update(EXPE_TIMEOUT)
                    .build(),
            PendingUpdateV2.<java.time.Duration>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.MinBlockTime)
                    .update(java.time.Duration.ofMillis(MIN_BLOCK_TIME))
                    .build(),
            PendingUpdateV2.<UInt64>builder()
                    .effectiveTime(EXPE_EFFECTIVE_TIME)
                    .type(PendingUpdateType.BlockEnergyLimit)
                    .update(UInt64.from(BLOCK_ENERGY_LIMIT))
                    .build()
    );
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    private static Authorization toAuthorization(int value) {
        return Authorization.builder()
                .authorizedKeys(Lists.newArrayList(value++, value++))
                .threshold((byte) value)
                .build();
    }

    private static double toPPHT(int value) {
        return PartsPerHundredThousand.from(value).asDouble();
    }

    @NonNull
    private static VerificationKey toVerificationKey(byte[] key) {
        return VerificationKey.builder().signingScheme(SigningScheme.ED25519).verifyKey(key).build();
    }

    @NonNull
    private static InclusiveRangeAmountFraction toRangeAmountFraction(int min, int max) {
        return InclusiveRangeAmountFraction.newBuilder()
                .setMin(toAmountFrac(min))
                .setMax(toAmountFrac(max))
                .build();
    }

    @NonNull
    private static Ratio toRatio(long grpcMicroCcdPerEuroNume, long grpcMicroCcdPerEuroDeno) {
        return Ratio.newBuilder()
                .setNumerator(grpcMicroCcdPerEuroNume)
                .setDenominator(grpcMicroCcdPerEuroDeno).build();
    }

    @NonNull
    private static AccessStructure toAccessStructure(int value) {
        return AccessStructure.newBuilder()
                .addAllAccessPublicKeys(Lists.newArrayList(
                        UpdateKeysIndex.newBuilder().setValue(value++).build(),
                        UpdateKeysIndex.newBuilder().setValue(value++).build())
                )
                .setAccessThreshold(UpdateKeysThreshold.newBuilder().setValue(value).build())
                .build();
    }

    @NonNull
    private static UpdatePublicKey toUpdatePublicKey(byte[] level1UpdateKey1) {
        return UpdatePublicKey.newBuilder().setValue(ByteString.copyFrom(level1UpdateKey1)).build();
    }

    @NonNull
    private static AmountFraction toAmountFrac(int gasRewardsChainCpv2) {
        return AmountFraction.newBuilder().setPartsPerHundredThousand(gasRewardsChainCpv2).build();
    }

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
    }

    @Test
    public void getBlockPendingUpdatesTest() {
        var res = client.getBlockPendingUpdates(BlockQuery.BEST);

        verify(serviceImpl).getBlockPendingUpdates(eq(
                        com.concordium.grpc.v2.BlockHashInput.newBuilder().setBest(Empty.getDefaultInstance()).build()),
                any(StreamObserver.class));
        var actual = Lists.newArrayList(res);

        for (int i = 0; i < EXPECTED.size(); i++) {
            assertEquals(EXPECTED.get(i), actual.get(i));
        }
    }
}
