package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElement;
import com.concordium.sdk.responses.smartcontracts.ContractTraceElementType;
import com.concordium.sdk.types.ContractAddress;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted when a contract has been successfully upgraded.
 * The event contains the {@link ContractAddress} that was upgraded, a {@link ModuleRef}
 * pointing to the old 'Module and a {@link ModuleRef} pointing to the new 'Module' which
 * will be used for future executions of the contract.
 * <p>
 * Native contract upgrading was added as part of P5 {@link com.concordium.sdk.responses.ProtocolVersion#V5}.
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
public final class UpgradedResult implements TransactionResultEvent, ContractTraceElement {

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

    public static UpgradedResult from(com.concordium.grpc.v2.ContractTraceElement.Upgraded upgraded) {
        return UpgradedResult
                .builder()
                .contractAddress(ContractAddress.from(upgraded.getAddress()))
                .from(ModuleRef.from(upgraded.getFrom()))
                .to(ModuleRef.from(upgraded.getTo()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPGRADED;
    }

    @Override
    public ContractTraceElementType getTraceType() {
        return ContractTraceElementType.UPGRADED;
    }
}
