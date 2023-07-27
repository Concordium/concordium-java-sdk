package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.val;

/**
 * A base for {@link BakerStakeIncreasedResult} and {@link BakerStakeDecreasedResult}
 */
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public abstract class BakerStakeUpdated extends AbstractBakerResult {

    BakerStakeUpdated(AccountIndex bakerId, AccountAddress account) {
        super(bakerId, account);
    }

    public static BakerStakeUpdated from(AccountTransactionEffects.BakerStakeUpdated bakerStakeUpdated, AccountAddress account) {
        val update = bakerStakeUpdated.getUpdate();
        if (update.getIncreased()) {
            return BakerStakeIncreasedResult
                    .builder()
                    .newStake(CCDAmount.from(update.getNewStake()))
                    .bakerId(BakerId.from(update.getBakerId()))
                    .account(account)
                    .build();
        }else {
            return BakerStakeDecreasedResult
                    .builder()
                    .newStake(CCDAmount.from(update.getNewStake()))
                    .bakerId(BakerId.from(update.getBakerId()))
                    .account(account)
                    .build();
        }
    }
}
