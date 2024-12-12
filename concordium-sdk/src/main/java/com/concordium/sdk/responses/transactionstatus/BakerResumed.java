package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A baker has been resumed.
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerResumed extends AbstractBakerResult {

    private final BakerId bakerId;

    public static BakerResumed from(BakerEvent.BakerResumed bakerResumed, AccountAddress sender) {
        return BakerResumed
                .builder()
                .bakerId(BakerId.from(bakerResumed.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_RESUMED;
    }
}
