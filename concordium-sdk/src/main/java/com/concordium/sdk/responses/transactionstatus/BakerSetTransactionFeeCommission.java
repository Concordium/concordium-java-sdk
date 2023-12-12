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
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSetTransactionFeeCommission extends AbstractBakerResult {
    private final double feeCommission;

    public static BakerSetTransactionFeeCommission from(BakerEvent.BakerSetTransactionFeeCommission bakerSetTransactionFeeCommission, AccountAddress sender) {
        return BakerSetTransactionFeeCommission
                .builder()
                .feeCommission(PartsPerHundredThousand.from(bakerSetTransactionFeeCommission.getTransactionFeeCommission().getPartsPerHundredThousand()).asDouble())
                .bakerId(BakerId.from(bakerSetTransactionFeeCommission.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }
}
