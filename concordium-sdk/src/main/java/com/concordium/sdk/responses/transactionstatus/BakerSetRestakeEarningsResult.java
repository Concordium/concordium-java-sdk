package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public final class BakerSetRestakeEarningsResult extends AbstractBakerResult {

    private final BakerId bakerId;
    private final boolean restakeEarnings;

    public static BakerSetRestakeEarningsResult from(BakerEvent.BakerRestakeEarningsUpdated restake, AccountAddress account) {
        return BakerSetRestakeEarningsResult
                .builder()
                .bakerId(BakerId.from(restake.getBakerId()))
                .account(account)
                .restakeEarnings(restake.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS;
    }
}
