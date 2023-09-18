package com.concordium.sdk;

import com.concordium.grpc.v2.*;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.*;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * The test asserts that {@link ClientV2#getBlockSpecialEvents(BlockQuery)}
 * correctly converts the {@link Iterator} of {@link BlockSpecialEvent} returned by the server to {@link ImmutableList} of {@link SpecialOutcome}.
 */
public class ClientV2GetBlockSpecialEventsTest {

    // Amounts and addresses for creating Special Events / Outcomes
    private static final byte[] ADDRESS_ONE = AccountAddress.from("37UHs4b9VH3F366cdmrA4poBURzzARJLWxdXZ18zoa9pnfhhDf").getBytes();
    private static final byte[] ADDRESS_TWO = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e").getBytes();
    private static final byte[] ADDRESS_THREE = AccountAddress.from("2woqsM8vniCpyd92LKESejT8wY3kvAZivgumra7Er424ThVrRF").getBytes();
    private static final Long AMOUNT_ONE = 10L;
    private static final Long AMOUNT_TWO = 20L;
    private static final Long AMOUNT_THREE = 30L;
    private static final Long AMOUNT_FOUR = 40L;
    private static final Long AMOUNT_FIVE = 50L;
    private static final Long AMOUNT_SIX = 60L;

    private static final Long BAKER_ID = 70L;

    // Lists of rewards for BAKING_REWARDS and FINALIZATION_REWARDS
    private static final Reward CLIENT_REWARD_ONE = Reward.builder()
            .address(AccountAddress.from(ADDRESS_ONE))
            .amount(CCDAmount.fromMicro(AMOUNT_ONE))
            .build();
    private static final Reward CLIENT_REWARD_TWO = Reward.builder()
            .address(AccountAddress.from(ADDRESS_TWO))
            .amount(CCDAmount.fromMicro(AMOUNT_TWO))
            .build();
    private static final Reward CLIENT_REWARD_THREE = Reward.builder()
            .address(AccountAddress.from(ADDRESS_THREE))
            .amount(CCDAmount.fromMicro(AMOUNT_THREE))
            .build();

    private static final List<Reward> CLIENT_REWARDS_LIST = new ImmutableList.Builder<Reward>()
            .add(CLIENT_REWARD_ONE)
            .add(CLIENT_REWARD_TWO)
            .add(CLIENT_REWARD_THREE)
            .build();
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_ONE = BlockSpecialEvent.AccountAmounts.Entry.newBuilder()
            .setAccount(grpcAddressFrom(ADDRESS_ONE))
            .setAmount(Amount.newBuilder().setValue(AMOUNT_ONE))
            .build();
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_TWO = BlockSpecialEvent.AccountAmounts.Entry.newBuilder()
            .setAccount(grpcAddressFrom(ADDRESS_TWO))
            .setAmount(Amount.newBuilder().setValue(AMOUNT_TWO))
            .build();
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_THREE = BlockSpecialEvent.AccountAmounts.Entry.newBuilder()
            .setAccount(grpcAddressFrom(ADDRESS_THREE))
            .setAmount(Amount.newBuilder().setValue(AMOUNT_THREE))
            .build();

    private static final BlockSpecialEvent.AccountAmounts GRPC_REWARDS_LIST = BlockSpecialEvent.AccountAmounts.newBuilder()
            .addEntries(GRPC_REWARD_ONE)
            .addEntries(GRPC_REWARD_TWO)
            .addEntries(GRPC_REWARD_THREE)
            .build();

    // Client special events
    private static final SpecialOutcome CLIENT_BAKING_REWARDS = BakingRewards.builder()
            .remainder(CCDAmount.fromMicro(AMOUNT_ONE))
            .bakerRewards(CLIENT_REWARDS_LIST)
            .build();
    private static final SpecialOutcome CLIENT_MINT = Mint.builder()
            .mintBakingReward(CCDAmount.fromMicro(AMOUNT_ONE))
            .mintFinalizationReward(CCDAmount.fromMicro(AMOUNT_TWO))
            .mintPlatformDevelopmentCharge(CCDAmount.fromMicro(AMOUNT_THREE))
            .foundationAccount(AccountAddress.from(ADDRESS_ONE))
            .build();
    private static final SpecialOutcome CLIENT_FINALIZATION_REWARDS = FinalizationRewards.builder()
            .rewards(CLIENT_REWARDS_LIST)
            .remainder(CCDAmount.fromMicro(AMOUNT_ONE))
            .build();
    private static final SpecialOutcome CLIENT_BLOCK_REWARD = BlockReward.builder()
            .transactionFees(CCDAmount.fromMicro(AMOUNT_ONE))
            .oldGASAccount(CCDAmount.fromMicro(AMOUNT_TWO))
            .newGASAccount(CCDAmount.fromMicro(AMOUNT_THREE))
            .bakerReward(CCDAmount.fromMicro(AMOUNT_FOUR))
            .foundationCharge(CCDAmount.fromMicro(AMOUNT_FIVE))
            .baker(AccountAddress.from(ADDRESS_ONE))
            .foundationAccount(AccountAddress.from(ADDRESS_TWO))
            .build();
    private static final SpecialOutcome CLIENT_PAYDAY_FOUNDATION_REWARD = PaydayFoundationReward.builder()
            .foundationAccount(AccountAddress.from(ADDRESS_ONE))
            .developmentCharge(CCDAmount.fromMicro(AMOUNT_ONE))
            .build();
    private static final SpecialOutcome CLIENT_PAYDAY_ACCOUNT_REWARD = PaydayAccountReward.builder()
            .account(AccountAddress.from(ADDRESS_ONE))
            .transactionFees(CCDAmount.fromMicro(AMOUNT_ONE))
            .bakerReward(CCDAmount.fromMicro(AMOUNT_TWO))
            .finalizationReward(CCDAmount.fromMicro(AMOUNT_THREE))
            .build();
    private static final SpecialOutcome CLIENT_BLOCK_ACCRUE_REWARD = BlockAccrueReward.builder()
            .transactionFees(CCDAmount.fromMicro(AMOUNT_ONE))
            .oldGASAccount(CCDAmount.fromMicro(AMOUNT_TWO))
            .newGASAccount(CCDAmount.fromMicro(AMOUNT_THREE))
            .bakerReward(CCDAmount.fromMicro(AMOUNT_FOUR))
            .passiveReward(CCDAmount.fromMicro(AMOUNT_FIVE))
            .foundationCharge(CCDAmount.fromMicro(AMOUNT_SIX))
            .bakerId(AccountIndex.from(BAKER_ID))
            .build();
    private static final SpecialOutcome CLIENT_PAYDAY_POOL_REWARD = PaydayPoolReward.builder()
            .poolOwner(BAKER_ID)
            .transactionFees(CCDAmount.fromMicro(AMOUNT_ONE))
            .bakerReward(CCDAmount.fromMicro(AMOUNT_TWO))
            .finalizationReward(CCDAmount.fromMicro(AMOUNT_THREE))
            .build();

    // GRPC Special events
    private static final BlockSpecialEvent GRPC_BAKING_REWARDS = BlockSpecialEvent.newBuilder()
            .setBakingRewards(BlockSpecialEvent.BakingRewards.newBuilder()
                    .setBakerRewards(GRPC_REWARDS_LIST)
                    .setRemainder(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_MINT = BlockSpecialEvent.newBuilder()
            .setMint(BlockSpecialEvent.Mint.newBuilder()
                    .setMintBakingReward(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .setMintFinalizationReward(Amount.newBuilder().setValue(AMOUNT_TWO).build())
                    .setMintPlatformDevelopmentCharge(Amount.newBuilder().setValue(AMOUNT_THREE).build())
                    .setFoundationAccount(grpcAddressFrom(ADDRESS_ONE))
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_FINALIZATION_REWARDS = BlockSpecialEvent.newBuilder()
            .setFinalizationRewards(BlockSpecialEvent.FinalizationRewards.newBuilder()
                    .setFinalizationRewards(GRPC_REWARDS_LIST)
                    .setRemainder(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_BLOCK_REWARD = BlockSpecialEvent.newBuilder()
            .setBlockReward(BlockSpecialEvent.BlockReward.newBuilder()
                    .setTransactionFees(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .setOldGasAccount(Amount.newBuilder().setValue(AMOUNT_TWO).build())
                    .setNewGasAccount(Amount.newBuilder().setValue(AMOUNT_THREE).build())
                    .setBakerReward(Amount.newBuilder().setValue(AMOUNT_FOUR).build())
                    .setFoundationCharge(Amount.newBuilder().setValue(AMOUNT_FIVE).build())
                    .setBaker(grpcAddressFrom(ADDRESS_ONE))
                    .setFoundationAccount(grpcAddressFrom(ADDRESS_TWO))
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_PAYDAY_FOUNDATION_REWARD = BlockSpecialEvent.newBuilder()
            .setPaydayFoundationReward(BlockSpecialEvent.PaydayFoundationReward.newBuilder()
                    .setFoundationAccount(grpcAddressFrom(ADDRESS_ONE))
                    .setDevelopmentCharge(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_PAYDAY_ACCOUNT_REWARD = BlockSpecialEvent.newBuilder()
            .setPaydayAccountReward(BlockSpecialEvent.PaydayAccountReward.newBuilder()
                    .setAccount(grpcAddressFrom(ADDRESS_ONE))
                    .setTransactionFees(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .setBakerReward(Amount.newBuilder().setValue(AMOUNT_TWO).build())
                    .setFinalizationReward(Amount.newBuilder().setValue(AMOUNT_THREE).build())
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_BLOCK_ACCRUE_REWARD = BlockSpecialEvent.newBuilder()
            .setBlockAccrueReward(BlockSpecialEvent.BlockAccrueReward.newBuilder()
                    .setTransactionFees(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .setOldGasAccount(Amount.newBuilder().setValue(AMOUNT_TWO).build())
                    .setNewGasAccount(Amount.newBuilder().setValue(AMOUNT_THREE).build())
                    .setBakerReward(Amount.newBuilder().setValue(AMOUNT_FOUR).build())
                    .setPassiveReward(Amount.newBuilder().setValue(AMOUNT_FIVE).build())
                    .setFoundationCharge(Amount.newBuilder().setValue(AMOUNT_SIX).build())
                    .setBaker(BakerId.newBuilder().setValue(BAKER_ID))
                    .build())
            .build();
    private static final BlockSpecialEvent GRPC_PAYDAY_POOL_REWARD = BlockSpecialEvent.newBuilder()
            .setPaydayPoolReward(BlockSpecialEvent.PaydayPoolReward.newBuilder()
                    .setPoolOwner(BakerId.newBuilder().setValue(BAKER_ID).build())
                    .setTransactionFees(Amount.newBuilder().setValue(AMOUNT_ONE).build())
                    .setBakerReward(Amount.newBuilder().setValue(AMOUNT_TWO).build())
                    .setFinalizationReward(Amount.newBuilder().setValue(AMOUNT_THREE).build())
                    .build())
            .build();

    // GRPC return value and expected clientside result
    private static final ImmutableList<SpecialOutcome> CLIENT_EXPECTED_RESULT = new ImmutableList.Builder<SpecialOutcome>()
            .add(CLIENT_BAKING_REWARDS)
            .add(CLIENT_MINT)
            .add(CLIENT_FINALIZATION_REWARDS)
            .add(CLIENT_BLOCK_REWARD)
            .add(CLIENT_PAYDAY_FOUNDATION_REWARD)
            .add(CLIENT_PAYDAY_ACCOUNT_REWARD)
            .add(CLIENT_BLOCK_ACCRUE_REWARD)
            .add(CLIENT_PAYDAY_POOL_REWARD)
            .build();

    private static final Iterator<BlockSpecialEvent> GRPC_RESULT = new ImmutableList.Builder<BlockSpecialEvent>()
            .add(GRPC_BAKING_REWARDS)
            .add(GRPC_MINT)
            .add(GRPC_FINALIZATION_REWARDS)
            .add(GRPC_BLOCK_REWARD)
            .add(GRPC_PAYDAY_FOUNDATION_REWARD)
            .add(GRPC_PAYDAY_ACCOUNT_REWARD)
            .add(GRPC_BLOCK_ACCRUE_REWARD)
            .add(GRPC_PAYDAY_POOL_REWARD)
            .build().iterator();

    private static final QueriesGrpc.QueriesImplBase serviceImpl = mock(QueriesGrpc.QueriesImplBase.class, delegatesTo(
            new QueriesGrpc.QueriesImplBase() {
                @Override
                public void getBlockSpecialEvents(BlockHashInput request, StreamObserver<BlockSpecialEvent> responseObserver) {
                    GRPC_RESULT.forEachRemaining(responseObserver::onNext);
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
    }

    @Test
    public void test() {
        val res = client.getBlockSpecialEvents(BlockQuery.BEST);

        verify(serviceImpl).getBlockSpecialEvents(any(BlockHashInput.class), any(StreamObserver.class));
        assertEquals(CLIENT_EXPECTED_RESULT, res);
    }


    /**
     * Helper method for creating {@link com.concordium.grpc.v2.AccountAddress} from byte array to improve readability
     */
    private static com.concordium.grpc.v2.AccountAddress grpcAddressFrom(byte[] bytes) {
        return com.concordium.grpc.v2.AccountAddress.newBuilder().setValue(ByteString.copyFrom(bytes)).build();
    }
}
