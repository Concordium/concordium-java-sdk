package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ModuleRef;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
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
@EqualsAndHashCode(callSuper = true)
@Builder
public final class ModuleDeployedResult extends TransactionResultEvent implements AccountTransactionResult {

    /**
     * The reference to the module.
     */
    private final byte[] reference;

    @SneakyThrows
    @JsonCreator
    public ModuleDeployedResult(@JsonProperty("contents") String reference) {
        this.reference = Hex.decodeHex(reference);
    }

    /**
     * Parses {@link ModuleRef} to {@link ModuleDeployedResult}.
     * @param moduleDeployed {@link ModuleRef} returned by the GRPC V2 API.
     * @return parsed {@link ModuleDeployedResult}.
     */
    public static ModuleDeployedResult parse(ModuleRef moduleDeployed) {
        return ModuleDeployedResult.builder().reference(moduleDeployed.getValue().toByteArray()).build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.MODULE_DEPLOYED;
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.DEPLOY_MODULE;
    }
}
