package com.concordium.sdk;

import com.concordium.grpc.v2.AccountInfo;
import com.concordium.grpc.v2.BakerPoolInfo;
import com.concordium.grpc.v2.CommissionRates;
import com.concordium.grpc.v2.Commitment;
import com.concordium.grpc.v2.CredentialPublicKeys;
import com.concordium.grpc.v2.Policy;
import com.concordium.grpc.v2.ReleaseSchedule;
import com.concordium.grpc.v2.*;
import com.concordium.sdk.crypto.bls.BLSPublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519SecretKey;
import com.concordium.sdk.crypto.elgamal.ElgamalPublicKey;
import com.concordium.sdk.crypto.vrf.VRFPublicKey;
import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.*;
import com.concordium.sdk.responses.accountinfo.credential.CredentialType;
import com.concordium.sdk.responses.accountinfo.credential.*;
import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.EncryptedAmountIndex;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.transactions.Index;
import com.concordium.sdk.types.Nonce;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.concordium.sdk.responses.accountinfo.credential.AttributeType.FIRST_NAME;
import static com.concordium.sdk.transactions.CredentialRegistrationId.fromBytes;
import static java.time.YearMonth.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientV2GetAccountInfoTest {

    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_1
            = com.concordium.sdk.types.AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf");
    private static final com.concordium.sdk.types.AccountAddress ACCOUNT_ADDRESS_2
            = com.concordium.sdk.types.AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
    private static final byte[] ENCRYPTED_AMOUNT = new byte[]{0, 0, 1};
    private static final long ACCOUNT_AMOUNT = 10000;
    private static final long ACCOUNT_INDEX = 1;
    private static final byte[] ENCRYPTION_KEY = new byte[]{0, 0, 2};
    private static final long RELEASE_AMOUNT = 10;
    private static final long RELEASE_TIME = 1;
    private static final byte[] RELEASE_TRANSACTION_HASH = new byte[]{0, 0, 3};
    private static final long SEQUENCE_NUMBER = 2;
    private static final long BAKER_ID = 1;
    private static final byte[] BAKER_AGGREGATION_KEY
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_ELECTION_VERIFY_KEY
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] BAKER_SIGNATURE_VERIFY_KEY
            = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final String BAKER_POOL_URL = "www.example-baker-pool.com";
    private static final int COMMISSION_BAKING_PPHT = 10;
    private static final int COMMISSION_FINALIZATION_PPHT = 11;
    private static final int COMMISSION_TRANSACTION_PPHT = 12;
    private static final boolean RESTAKE_EARNINGS = true;
    private static final long BAKER_REDUCE_STAKE_AMOUNT = 100;
    private static final long BAKER_REDUCE_STAKE_TIME = 2;
    private static final long STAKED_AMOUNT = 1000;
    private static final int ACCOUNT_THRESHOLD = 10;
    private static final int AR_THRESHOLD = 5;
    private static final byte[] COMMITMENT_CRED_COUNTER = new byte[]{0, 0, 9};
    private static final byte[] CREDENTIAL_REG_ID = com.concordium.sdk.transactions.CredentialRegistrationId
            .from("8e5c75fda3f791efd025e7e8fb0c26c3e111211e375fc9423b1ca308a696140e44c90dcabe75108b41db625152acae50")
            .getRegId();
    private static final int IDP_ID = 999;
    private static final int SIGNATURE_THRESHOLD = 8;
    private static final byte[] NORMAL_CRED_SIG_0 = ED25519PublicKey.from(ED25519SecretKey.createNew()).getBytes();
    private static final byte[] COMMITMENT_MAX_ACCOUNTS = new byte[]{10, 10, 10};
    private static final byte[] COMMITMENT_PRF = new byte[]{11, 11, 11};
    private static final byte[] COMMITMENT_ID_CRED_SHARING_COEFF = new byte[]{12, 12, 12};
    private static final byte[] COMMITMENT_ATTRIBUTE_0 = new byte[]{13, 13, 13};
    private static final YearMonth POLICY_CREATED_AT
            = YearMonth.newBuilder().setYear(2022).setMonth(10).build();
    private static final YearMonth POLICY_VALID_TO
            = YearMonth.newBuilder().setYear(2024).setMonth(10).build();
    private static final byte[] FIRST_NAME_VALUE
            = "policy-value-0".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ENC_ID_PUB_SHARE = new byte[]{90, 90, 90};

    private static Commitment toCommitment(byte[] commitmentCredCounter) {
        return Commitment.newBuilder()
                .setValue(ByteString.copyFrom(commitmentCredCounter))
                .build();
    }

    private static final long ENCRYPTED_AMOUNT_START_INDEX = 1;
    private static final byte[] ENCRYPTED_AMOUNT_INCOMING_AMOUNT_1 = new byte[]{0, 0, 7};
    private static final byte[] ENCRYPTED_AMOUNT_SELF_AMOUNT_1 = new byte[]{0, 0, 8};
    private static final long RELEASE_AMOUNT_TOTAL = RELEASE_AMOUNT * 10;
    private static final StakePendingChange PENDING_CHANGE_GRPC = StakePendingChange.newBuilder()
            .setReduce(StakePendingChange.Reduce.newBuilder()
                    .setNewStake(Amount.newBuilder().setValue(BAKER_REDUCE_STAKE_AMOUNT).build())
                    .setEffectiveTime(Timestamp.newBuilder().setValue(BAKER_REDUCE_STAKE_TIME).build())
                    .build())
            .build();

    private static final AccountInfo GRPC_RES_1 = NEW_GRPC_RES(ACCOUNT_ADDRESS_1, Optional.of(PENDING_CHANGE_GRPC));
    private static final AccountInfo GRPC_RES_3 = NEW_GRPC_RES(ACCOUNT_ADDRESS_2, Optional.empty());

    private static AccountInfo NEW_GRPC_RES(com.concordium.sdk.types.AccountAddress address, Optional<StakePendingChange> pendingChange) {
        val bakerInfoBuilder = AccountStakingInfo.Baker.newBuilder()
                .setStakedAmount(Amount.newBuilder().setValue(STAKED_AMOUNT).build())
                .setRestakeEarnings(RESTAKE_EARNINGS)
                .setBakerInfo(BakerInfo.newBuilder()
                        .setBakerId(BakerId.newBuilder().setValue(BAKER_ID).build())
                        .setAggregationKey(BakerAggregationVerifyKey.newBuilder()
                                .setValue(ByteString.copyFrom(BAKER_AGGREGATION_KEY))
                                .build())
                        .setElectionKey(BakerElectionVerifyKey.newBuilder()
                                .setValue(ByteString.copyFrom(BAKER_ELECTION_VERIFY_KEY))
                                .build())
                        .setSignatureKey(BakerSignatureVerifyKey.newBuilder()
                                .setValue(ByteString.copyFrom(BAKER_SIGNATURE_VERIFY_KEY))
                                .build()));
        pendingChange.ifPresent(bakerInfoBuilder::setPendingChange);
        return AccountInfo.newBuilder()
                .setAddress(com.concordium.grpc.v2.AccountAddress.newBuilder().setValue(ByteString.copyFrom(address.getBytes())).build())
                .setAmount(Amount.newBuilder().setValue(ACCOUNT_AMOUNT).build())
                .setEncryptedBalance(EncryptedBalance.newBuilder()
                        .setStartIndex(ENCRYPTED_AMOUNT_START_INDEX)
                        .setAggregatedAmount(from(ENCRYPTED_AMOUNT))
                        .setNumAggregated(1)
                        .addIncomingAmounts(from(ENCRYPTED_AMOUNT_INCOMING_AMOUNT_1))
                        .setSelfAmount(from(ENCRYPTED_AMOUNT_SELF_AMOUNT_1))
                        .build())
                .setIndex(AccountIndex.newBuilder().setValue(ACCOUNT_INDEX).build())
                .setThreshold(AccountThreshold.newBuilder()
                        .setValue(ACCOUNT_THRESHOLD)
                        .build())
                .setEncryptionKey(EncryptionKey.newBuilder()
                        .setValue(ByteString.copyFrom(ENCRYPTION_KEY))
                        .build())
                .setSchedule(ReleaseSchedule.newBuilder()
                        .addSchedules(Release.newBuilder()
                                .setAmount(Amount.newBuilder().setValue(RELEASE_AMOUNT).build())
                                .setTimestamp(Timestamp.newBuilder().setValue(RELEASE_TIME).build())
                                .addTransactions(TransactionHash.newBuilder()
                                        .setValue(ByteString.copyFrom(RELEASE_TRANSACTION_HASH))
                                        .build())
                                .build())
                        .setTotal(Amount.newBuilder().setValue(RELEASE_AMOUNT_TOTAL).build())
                        .build())
                .setSequenceNumber(SequenceNumber.newBuilder()
                        .setValue(SEQUENCE_NUMBER)
                        .build())
                .putAllCreds(ImmutableMap.of(0, AccountCredential.newBuilder()
                        .setNormal(NormalCredentialValues.newBuilder()
                                .setArThreshold(ArThreshold.newBuilder().setValue(AR_THRESHOLD).build())
                                .putAllArData(ImmutableMap.of(0, ChainArData.newBuilder()
                                        .setEncIdCredPubShare(ByteString.copyFrom(ENC_ID_PUB_SHARE))
                                        .build()))
                                .setCommitments(CredentialCommitments.newBuilder()
                                        .setCredCounter(toCommitment(COMMITMENT_CRED_COUNTER))
                                        .setMaxAccounts(toCommitment(COMMITMENT_MAX_ACCOUNTS))
                                        .setPrf(toCommitment(COMMITMENT_PRF))
                                        .addIdCredSecSharingCoeff(0, toCommitment(COMMITMENT_ID_CRED_SHARING_COEFF))
                                        .putAllAttributes(ImmutableMap.of(0, toCommitment(COMMITMENT_ATTRIBUTE_0)))
                                        .build())
                                .setCredId(CredentialRegistrationId.newBuilder()
                                        .setValue(ByteString.copyFrom(CREDENTIAL_REG_ID))
                                        .build())
                                .setIpId(IdentityProviderIdentity.newBuilder()
                                        .setValue(IDP_ID)
                                        .build())
                                .setKeys(CredentialPublicKeys.newBuilder()
                                        .setThreshold(SignatureThreshold.newBuilder().setValue(SIGNATURE_THRESHOLD).build())
                                        .putAllKeys(ImmutableMap.of(0, AccountVerifyKey.newBuilder()
                                                .setEd25519Key(ByteString.copyFrom(NORMAL_CRED_SIG_0))
                                                .build()))
                                        .build())
                                .setPolicy(Policy.newBuilder()
                                        .setCreatedAt(POLICY_CREATED_AT)
                                        .setValidTo(POLICY_VALID_TO)
                                        .putAllAttributes(ImmutableMap.of(
                                                FIRST_NAME.ordinal(),
                                                ByteString.copyFrom(FIRST_NAME_VALUE)))
                                        .build())
                                .build())
                        .build()))
                .setStake(AccountStakingInfo.newBuilder()
                        .setBaker(bakerInfoBuilder
                                .setPoolInfo(BakerPoolInfo.newBuilder()
                                        .setUrl(BAKER_POOL_URL)
                                        .setOpenStatus(OpenStatus.OPEN_STATUS_OPEN_FOR_ALL)
                                        .setCommissionRates(CommissionRates.newBuilder()
                                                .setBaking(toAmountFrac(COMMISSION_BAKING_PPHT))
                                                .setFinalization(toAmountFrac(COMMISSION_FINALIZATION_PPHT))
                                                .setTransaction(toAmountFrac(COMMISSION_TRANSACTION_PPHT))
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }

    private static final Optional<PendingChange> PENDING_CHANGE = Optional.of(ReduceStakeChange.builder()
            .newStake(CCDAmount.fromMicro(BAKER_REDUCE_STAKE_AMOUNT))
            .effectiveTime(com.concordium.sdk.types.Timestamp.newMillis(BAKER_REDUCE_STAKE_TIME))
            .build());

    private static final com.concordium.sdk.responses.accountinfo.AccountInfo ACCOUNT_INFO_RES_EXPECTED_1 = NEW_EXPECTED_GRPC_RES(ACCOUNT_ADDRESS_1, PENDING_CHANGE);

    private static final com.concordium.sdk.responses.accountinfo.AccountInfo ACCOUNT_INFO_RES_EXPECTED_2 = NEW_EXPECTED_GRPC_RES(ACCOUNT_ADDRESS_2, Optional.empty());

    private static com.concordium.sdk.responses.accountinfo.AccountInfo NEW_EXPECTED_GRPC_RES(com.concordium.sdk.types.AccountAddress address, Optional<PendingChange> pendingChange) {
        return com.concordium.sdk.responses.accountinfo.AccountInfo.builder().
                accountAddress(address).
                accountAmount(CCDAmount.fromMicro(ACCOUNT_AMOUNT)).
                accountEncryptedAmount(AccountEncryptedAmount.builder().
                        startIndex(EncryptedAmountIndex.from(ENCRYPTED_AMOUNT_START_INDEX)).
                        selfAmount(com.concordium.sdk.transactions.EncryptedAmount.from(ENCRYPTED_AMOUNT_SELF_AMOUNT_1)).
                        incomingAmounts(ImmutableList.of(com.concordium.sdk.transactions.EncryptedAmount.from(ENCRYPTED_AMOUNT_INCOMING_AMOUNT_1))).
                        numAggregated(Optional.of(1)).
                        aggregatedAmount(Optional.of(com.concordium.sdk.transactions.EncryptedAmount.from(ENCRYPTED_AMOUNT))).
                        build()).
                accountIndex(com.concordium.sdk.responses.AccountIndex.from(ACCOUNT_INDEX)).
                accountThreshold(ACCOUNT_THRESHOLD).
                accountEncryptionKey(ElgamalPublicKey.from(ENCRYPTION_KEY)).
                accountReleaseSchedule(com.concordium.sdk.responses.accountinfo.ReleaseSchedule.builder().
                        total(CCDAmount.fromMicro(RELEASE_AMOUNT_TOTAL)).
                        schedule(ImmutableList.of(ScheduledRelease.builder().
                                amount(CCDAmount.fromMicro(RELEASE_AMOUNT)).
                                timestamp(com.concordium.sdk.types.Timestamp.newMillis(RELEASE_TIME)).
                                transaction(Hash.from(RELEASE_TRANSACTION_HASH)).
                                build())).
                        build()).
                Nonce(Nonce.from(SEQUENCE_NUMBER)).
                accountCredential(Index.from(0), Credential.
                        builder().
                        type(CredentialType.NORMAL).
                        revocationThreshold(AR_THRESHOLD).
                        arDataItem(Index.from(0), ArData.
                                builder().
                                encIdCredPubShare(EncIdPubShare.from(ENC_ID_PUB_SHARE)).
                                build()).
                        commitments(Commitments.builder().
                                cmmCredCounter(com.concordium.sdk.responses.accountinfo.credential.Commitment.from(COMMITMENT_CRED_COUNTER)).
                                cmmIdCredSecSharingCoeffItem(com.concordium.sdk.responses.accountinfo.credential.Commitment.from(COMMITMENT_ID_CRED_SHARING_COEFF)).
                                cmmMaxAccounts(com.concordium.sdk.responses.accountinfo.credential.Commitment.from(COMMITMENT_MAX_ACCOUNTS)).
                                cmmPrf(com.concordium.sdk.responses.accountinfo.credential.Commitment.from(COMMITMENT_PRF)).
                                cmmAttribute(FIRST_NAME, com.concordium.sdk.responses.accountinfo.credential.Commitment.from(COMMITMENT_ATTRIBUTE_0)).
                                build()).
                        credId(fromBytes(CREDENTIAL_REG_ID)).
                        ipIdentity(IDP_ID).
                        credentialPublicKeys(com.concordium.sdk.responses.accountinfo.credential.CredentialPublicKeys.builder().
                                threshold(SIGNATURE_THRESHOLD).
                                key(Index.from(0), Key.
                                        builder().
                                        verifyKey(NORMAL_CRED_SIG_0).
                                        schemeId(VerificationScheme.Ed25519).
                                        build()).
                                build()).
                        policy(com.concordium.sdk.responses.accountinfo.credential.Policy.builder().
                                createdAt(of(POLICY_CREATED_AT.getYear(), POLICY_CREATED_AT.
                                        getMonth())).
                                validTo(of(POLICY_VALID_TO.getYear(), POLICY_VALID_TO.
                                        getMonth())).
                                revealedAttributes(ImmutableMap.of(FIRST_NAME, new String(FIRST_NAME_VALUE, StandardCharsets.UTF_8))).
                                build()).build()).
                bakerInfo(Baker.builder().
                        stakedAmount(CCDAmount.fromMicro(STAKED_AMOUNT)).
                        restakeEarnings(RESTAKE_EARNINGS).
                        pendingChange(pendingChange).
                        bakerInfo(com.concordium.sdk.responses.bakersrewardperiod.BakerInfo.builder().
                                bakerId(com.concordium.sdk.responses.BakerId.from(BAKER_ID)).
                                bakerAggregationVerifyKey(BLSPublicKey.from(BAKER_AGGREGATION_KEY)).
                                bakerElectionVerifyKey(VRFPublicKey.from(BAKER_ELECTION_VERIFY_KEY)).
                                bakerSignatureVerifyKey(ED25519PublicKey.from(BAKER_SIGNATURE_VERIFY_KEY)).
                                build()).
                        bakerPoolInfo(com.concordium.sdk.responses.accountinfo.BakerPoolInfo.builder().
                                metadataUrl(BAKER_POOL_URL).
                                openStatus(com.concordium.sdk.responses.transactionstatus.OpenStatus.OPEN_FOR_ALL).
                                commissionRates(com.concordium.sdk.responses.accountinfo.CommissionRates.builder().
                                        bakingCommission(PartsPerHundredThousand.from(COMMISSION_BAKING_PPHT)).
                                        finalizationCommission(PartsPerHundredThousand.from(COMMISSION_FINALIZATION_PPHT)).
                                        transactionCommission(PartsPerHundredThousand.from(COMMISSION_TRANSACTION_PPHT)).
                                        build()).
                                build()).
                        build()).
                build();

    }

    private static AmountFraction toAmountFrac(int commissionPartsPerHundredThousand) {
        return AmountFraction.newBuilder().setPartsPerHundredThousand(commissionPartsPerHundredThousand).build();
    }

    private static EncryptedAmount from(byte[] bytes) {
        return EncryptedAmount.newBuilder()
                .setValue(ByteString.copyFrom(bytes))
                .build();
    }

    private static final AccountInfo GRPC_RES_2 = AccountInfo.newBuilder().build();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getAccountInfo(AccountInfoRequest request, StreamObserver<AccountInfo> responseObserver) {
                    if (request.getAccountIdentifier().getAddress().equals(GRPC_RES_1.getAddress()))
                        responseObserver.onNext(GRPC_RES_1);
                    if (request.getAccountIdentifier().getAddress().equals(GRPC_RES_2.getAddress()))
                        responseObserver.onNext(GRPC_RES_2);
                    if (request.getAccountIdentifier().getAddress().equals(GRPC_RES_3.getAddress()))
                        responseObserver.onNext(GRPC_RES_3);

                    responseObserver.onCompleted();
                }
            }
    ));

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ClientV2 client;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new ClientV2(10000, channel, Optional.empty());
        Mockito.reset(serviceImpl);
    }

    @Test
    public void getAccountInfoTest() {
        var res = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(ACCOUNT_ADDRESS_1));

        verify(serviceImpl).getAccountInfo(any(AccountInfoRequest.class), any(StreamObserver.class));
        assertEquals(ACCOUNT_INFO_RES_EXPECTED_1, res);
    }

    @Test
    public void getAccountInfoNoPendingChangeTest() {
        var res = client.getAccountInfo(BlockQuery.BEST, AccountQuery.from(ACCOUNT_ADDRESS_2));

        verify(serviceImpl).getAccountInfo(any(AccountInfoRequest.class), any(StreamObserver.class));
        assertEquals(ACCOUNT_INFO_RES_EXPECTED_2, res);
    }

    @Test
    public void mapAccountStakeDelegationTest() {
        var expected = AccountDelegation.builder()
                .target(com.concordium.sdk.responses.transactionstatus.DelegationTarget.builder()
                        .bakerId(com.concordium.sdk.responses.BakerId.from(BAKER_ID))
                        .type(com.concordium.sdk.responses.transactionstatus.DelegationTarget.DelegationType.BAKER)
                        .build())
                .stakedAmount(CCDAmount.fromMicro(STAKED_AMOUNT))
                .restakeEarnings(RESTAKE_EARNINGS)
                .pendingChange(PENDING_CHANGE)
                .build();

        var res = ClientV2MapperExtensions.to(AccountStakingInfo.Delegator.newBuilder()
                .setStakedAmount(Amount.newBuilder().setValue(STAKED_AMOUNT).build())
                .setRestakeEarnings(RESTAKE_EARNINGS)
                .setPendingChange(PENDING_CHANGE_GRPC)
                .setTarget(DelegationTarget.newBuilder()
                        .setBaker(BakerId.newBuilder().setValue(BAKER_ID).build())
                        .build())
                .build());

        assertEquals(expected, res);
    }

    @Test
    public void mapPendingRemoveStakeChangeTest() {
        var expected = Optional.of(RemoveStakeChange.builder()
                .effectiveTime(com.concordium.sdk.types.Timestamp.newMillis(BAKER_REDUCE_STAKE_TIME))
                .build());

        var res = ClientV2MapperExtensions.to(StakePendingChange.newBuilder()
                .setRemove(Timestamp.newBuilder().setValue(BAKER_REDUCE_STAKE_TIME).build())
                .build());

        assertEquals(expected, res);
    }

    @Test
    public void mapAccountIdentifierIndexTest() {
        var expected = AccountIdentifierInput.newBuilder()
                .setAccountIndex(AccountIndex.newBuilder().setValue(ACCOUNT_INDEX).build())
                .build();

        var res
                = ClientV2MapperExtensions.to(AccountQuery.from(
                com.concordium.sdk.responses.AccountIndex.from(ACCOUNT_INDEX)));

        assertEquals(expected, res);
    }

    @Test
    public void mapAccountIdentifierCredIdTest() {
        var expected = AccountIdentifierInput.newBuilder()
                .setCredId(CredentialRegistrationId.newBuilder()
                        .setValue(ByteString.copyFrom(CREDENTIAL_REG_ID))
                        .build())
                .build();

        var res
                = ClientV2MapperExtensions.to(AccountQuery.from(
                com.concordium.sdk.transactions.CredentialRegistrationId.fromBytes(CREDENTIAL_REG_ID)));

        assertEquals(expected, res);
    }

    @Test
    public void mapDelegationPassiveTest() {
        var expected = com.concordium.sdk.responses.transactionstatus.DelegationTarget
                .newPassiveDelegationTarget();
        var res = ClientV2MapperExtensions.to(DelegationTarget.newBuilder()
                .setPassive(Empty.newBuilder().build())
                .build());

        assertEquals(expected, res);
    }

    @Test
    public void mapOpenStatusTest() {
        assertEquals(
                com.concordium.sdk.responses.transactionstatus.OpenStatus.OPEN_FOR_ALL,
                ClientV2MapperExtensions.to(OpenStatus.OPEN_STATUS_OPEN_FOR_ALL));

        assertEquals(
                com.concordium.sdk.responses.transactionstatus.OpenStatus.CLOSED_FOR_ALL,
                ClientV2MapperExtensions.to(OpenStatus.OPEN_STATUS_CLOSED_FOR_ALL));

        assertEquals(
                com.concordium.sdk.responses.transactionstatus.OpenStatus.CLOSED_FOR_NEW,
                ClientV2MapperExtensions.to(OpenStatus.OPEN_STATUS_CLOSED_FOR_NEW));
    }

    @Test
    public void mapAccountCredentialsInitialTest() {
        var expected = Credential.builder()
                .version(0)
                .type(CredentialType.INITIAL)
                .credId(fromBytes(CREDENTIAL_REG_ID))
                .ipIdentity(IDP_ID)
                .credentialPublicKeys(com.concordium.sdk.responses.accountinfo.credential.CredentialPublicKeys.builder()
                        .threshold(SIGNATURE_THRESHOLD)
                        .key(Index.from(0), Key.builder()
                                .verifyKey(NORMAL_CRED_SIG_0)
                                .schemeId(VerificationScheme.Ed25519)
                                .build())
                        .build())
                .policy(com.concordium.sdk.responses.accountinfo.credential.Policy.builder()
                        .createdAt(of(POLICY_CREATED_AT.getYear(), POLICY_CREATED_AT.getMonth()))
                        .validTo(of(POLICY_VALID_TO.getYear(), POLICY_VALID_TO.getMonth()))
                        .revealedAttributes(ImmutableMap.of(FIRST_NAME, new String(FIRST_NAME_VALUE, StandardCharsets.UTF_8)))
                        .build())
                .build();

        var res = ClientV2MapperExtensions.to(InitialCredentialValues.newBuilder()
                .setCredId(CredentialRegistrationId.newBuilder()
                        .setValue(ByteString.copyFrom(CREDENTIAL_REG_ID))
                        .build())
                .setIpId(IdentityProviderIdentity.newBuilder()
                        .setValue(IDP_ID)
                        .build())
                .setKeys(CredentialPublicKeys.newBuilder()
                        .setThreshold(SignatureThreshold.newBuilder().setValue(SIGNATURE_THRESHOLD).build())
                        .putAllKeys(ImmutableMap.of(0, AccountVerifyKey.newBuilder()
                                .setEd25519Key(ByteString.copyFrom(NORMAL_CRED_SIG_0))
                                .build()))
                        .build())
                .setPolicy(Policy.newBuilder()
                        .setCreatedAt(POLICY_CREATED_AT)
                        .setValidTo(POLICY_VALID_TO)
                        .putAllAttributes(ImmutableMap.of(
                                FIRST_NAME.ordinal(),
                                ByteString.copyFrom(FIRST_NAME_VALUE)))
                        .build())
                .build());

        assertEquals(expected, res);
    }
}
