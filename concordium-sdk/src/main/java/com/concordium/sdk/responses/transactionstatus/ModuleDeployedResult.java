package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ModuleRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

/**
 * A module was deployed on chain.
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public final class ModuleDeployedResult implements TransactionResultEvent {

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
