package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted when a contract has been successfully upgraded.
 * The event contains the {@link ContractAddress} that was upgraded, a {@link ModuleRef}
 * pointing to the old 'Module and a {@link ModuleRef} pointing to the new 'Module' which
 * will be used for future executions of the contract.
 *
 * Native contract upgrading was added as part of P5 {@link com.concordium.sdk.responses.ProtocolVersion#V5}.
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class UpgradedResult extends TransactionResultEvent {

    /**
     * The contract that was upgraded.
     */
    private final ContractAddress contractAddress;

    /**
     * A reference to the old module.
     */
    private final ModuleRef from;

    /**
     * A reference to the new module.
     */
    private final ModuleRef to;

    @JsonCreator
    public UpgradedResult(
            @JsonProperty("address") ContractAddress contractAddress,
            @JsonProperty("from") ModuleRef from,
            @JsonProperty("to") ModuleRef to) {
        this.contractAddress = contractAddress;
        this.from = from;
        this.to = to;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPGRADED;
    }
}
