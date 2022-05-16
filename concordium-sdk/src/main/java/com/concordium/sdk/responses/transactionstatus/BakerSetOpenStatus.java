package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * The Baker updated its {@link OpenStatus}
 */
@ToString
@Getter
public class BakerSetOpenStatus extends AbstractBakerResult {

    /**
     * The updated {@link OpenStatus} for the baker.
     */
    private final OpenStatus openStatus;

    @JsonCreator
    BakerSetOpenStatus(@JsonProperty("bakerId") String bakerId,
                       @JsonProperty("account") AccountAddress bakerAccount,
                       @JsonProperty("openStatus") OpenStatus openStatus) {
        super(bakerId, bakerAccount);
        this.openStatus = openStatus;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_OPEN_STATUS;
    }
}
