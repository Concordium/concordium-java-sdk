package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Account is not a delegation account.
 */
@ToString
@Getter
public class RejectReasonNotADelegator extends RejectReason {
    private final AccountAddress accountAddress;

    @JsonCreator
    RejectReasonNotADelegator(@JsonProperty("contents") String accountAddress) {
        this.accountAddress = AccountAddress.from(accountAddress);
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NOT_A_DELEGATOR;
    }
}
