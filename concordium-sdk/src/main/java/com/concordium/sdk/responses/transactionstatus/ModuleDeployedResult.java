package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ModuleRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

/**
 * A module was deployed on chain.
 */
@Getter
@ToString
@Builder
public final class ModuleDeployedResult extends TransactionResultEvent {

    /**
     * The reference to the module.
     */
    private final byte[] reference;

    @SneakyThrows
    @JsonCreator
    public ModuleDeployedResult(@JsonProperty("contents") String reference) {
        this.reference = Hex.decodeHex(reference);
    }

    public static ModuleDeployedResult parse(ModuleRef moduleDeployed) {
        return ModuleDeployedResult.builder().reference(moduleDeployed.getValue().toByteArray()).build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.MODULE_DEPLOYED;
    }
}
