package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * A module was deployed on chain.
 */
@Getter
@ToString
public final class ModuleDeployedResult extends TransactionResultEvent {

    /**
     * The reference to the module.
     */
    private final byte[] reference;

    @SneakyThrows
    @JsonCreator
    ModuleDeployedResult(@JsonProperty("contents") String reference) {
        this.reference = Hex.decodeHex(reference);
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.MODULE_DEPLOYED;
    }
}
