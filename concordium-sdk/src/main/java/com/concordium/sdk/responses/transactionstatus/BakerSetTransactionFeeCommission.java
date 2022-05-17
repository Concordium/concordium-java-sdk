package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BakerSetTransactionFeeCommission extends AbstractBakerResult {
    // parts per 100000
    private final int feeCommission;

    @JsonCreator
    BakerSetTransactionFeeCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                     @JsonProperty("account") AccountAddress bakerAccount,
                                     @JsonProperty("transactionFeeCommission") int feeCommission) {
        super(bakerId, bakerAccount);
        this.feeCommission = feeCommission;

    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }
}
