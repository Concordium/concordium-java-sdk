package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The Baker updated its {@link OpenStatus}
 */
@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSetOpenStatus extends AbstractBakerResult {

    /**
     * The updated {@link OpenStatus} for the baker.
     */
    private final OpenStatus openStatus;

    public static BakerSetOpenStatus from(BakerEvent.BakerSetOpenStatus bakerSetOpenStatus, AccountAddress sender) {
        return BakerSetOpenStatus
                .builder()
                .openStatus(OpenStatus.from(bakerSetOpenStatus.getOpenStatus()))
                .bakerId(BakerId.from(bakerSetOpenStatus.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_OPEN_STATUS;
    }
}
