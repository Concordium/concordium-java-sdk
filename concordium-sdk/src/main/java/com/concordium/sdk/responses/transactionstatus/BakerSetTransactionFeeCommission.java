package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    BakerSetTransactionFeeCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                     @JsonProperty("account") AccountAddress bakerAccount,
                                     @JsonProperty("transactionFeeCommission") long feeCommission) {
        super(bakerId, bakerAccount);
        this.feeCommission = feeCommission/100_000d;

    }

    public static BakerSetTransactionFeeCommission from(BakerEvent.BakerSetTransactionFeeCommission bakerSetTransactionFeeCommission, AccountAddress sender) {
        return BakerSetTransactionFeeCommission
                .builder()
                .feeCommission(bakerSetTransactionFeeCommission.getTransactionFeeCommission().getPartsPerHundredThousand()/100_000d)
                .bakerId(BakerId.from(bakerSetTransactionFeeCommission.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }
}
