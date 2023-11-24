package com.concordium.sdk.responses.transactionstatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A module was deployed on chain.
 */
@Getter
@ToString
@Builder
public final class ModuleDeployedResult implements TransactionResultEvent {

    /**
     * The reference to the module.
     */
    private final byte[] reference;

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.MODULE_DEPLOYED;
    }
}
