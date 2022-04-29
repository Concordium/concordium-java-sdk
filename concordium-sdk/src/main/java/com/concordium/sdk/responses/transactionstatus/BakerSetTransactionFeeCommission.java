package com.concordium.sdk.responses.transactionstatus;

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
    BakerSetTransactionFeeCommission(@JsonProperty("bakerId") String bakerId,
                                     @JsonProperty("account") String bakerAccount,
                                     @JsonProperty("transactionFeeCommission") int feeCommission) {
        super(bakerId, bakerAccount);
        this.feeCommission = feeCommission;

    }
}
