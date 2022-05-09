package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * A module was deployed on chain.
 */
@Getter
@ToString
public final class ModuleDeployedResult extends TransactionResultEvent {

    /**
     * The reference to the module.
     */
    private final String reference;

    @JsonCreator
    ModuleDeployedResult(@JsonProperty("contents") String reference) {
        this.reference = reference;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.MODULE_DEPLOYED;
    }
}
