package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public final class BakerRemovedResult extends AbstractBakerResult {
    public static BakerRemovedResult from(com.concordium.grpc.v2.BakerId bakerRemoved, AccountAddress accountAddress) {
        return BakerRemovedResult
                .builder()
                .bakerId(BakerId.from(bakerRemoved))
                .account(accountAddress)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_REMOVED;
    }
}
