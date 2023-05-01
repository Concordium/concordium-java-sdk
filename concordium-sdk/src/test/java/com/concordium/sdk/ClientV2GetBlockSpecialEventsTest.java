package com.concordium.sdk;

import com.concordium.grpc.v2.BlockSpecialEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.blocksummary.specialoutcomes.*;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

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
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_ONE = null;
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_TWO = null;
    private static final BlockSpecialEvent.AccountAmounts.Entry GRPC_REWARD_THREE = null;

    /** TODO
    private static final BlockSpecialEvent.AccountAmounts GRPC_REWARDS_LIST = BlockSpecialEvent.AccountAmounts.newBuilder()
            .addEntries(GRPC_REWARD_ONE)
            .addEntries(GRPC_REWARD_TWO)
            .addEntries(GRPC_REWARD_THREE)
            .build();
    */

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
    private static final BlockSpecialEvent GRPC_BAKING_REWARDS = null;
    private static final BlockSpecialEvent GRPC_MINT = null;
    private static final BlockSpecialEvent GRPC_FINALIZATION_REWARDS = null;
    private static final BlockSpecialEvent GRPC_BLOCK_REWARD = null;
    private static final BlockSpecialEvent GRPC_PAYDAY_FOUNDATION_REWARD = null;
    private static final BlockSpecialEvent GRPC_PAYDAY_ACCOUNT_REWARD = null;
    private static final BlockSpecialEvent GRPC_BLOCK_ACCRUE_REWARD = null;
    private static final BlockSpecialEvent GRPC_PAYDAY_POOL_REWARD = null;

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
    /** TODO
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
    */

    @Test
    public void foo() {
        System.out.println(CLIENT_EXPECTED_RESULT);
    }
}
