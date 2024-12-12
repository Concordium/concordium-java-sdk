package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A baker has been suspended.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSuspended extends AbstractBakerResult {

    private final BakerId bakerId;

    public static BakerSuspended from(BakerEvent.BakerSuspended bakerSuspended, AccountAddress sender) {
        return BakerSuspended
                .builder()
                .bakerId(BakerId.from(bakerSuspended.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SUSPENDED;
    }
}
