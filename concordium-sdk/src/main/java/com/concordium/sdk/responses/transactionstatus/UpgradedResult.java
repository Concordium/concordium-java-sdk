package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.ContractTraceElement;
import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event emitted when a contract has been successfully upgraded.
 * The event contains the {@link ContractAddress} that was upgraded, a {@link ModuleRef}
 * pointing to the old 'Module and a {@link ModuleRef} pointing to the new 'Module' which
 * will be used for future executions of the contract.
 * Native contract upgrading was added as part of P5 {@link com.concordium.sdk.responses.ProtocolVersion#V5}.
 */
@ToString
@Getter
@EqualsAndHashCode
@Builder
public final class UpgradedResult implements TransactionResultEvent {

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

    /**
     * Parses {@link com.concordium.grpc.v2.ContractTraceElement.Upgraded} to {@link UpgradedResult}.
     * @param upgraded {@link com.concordium.grpc.v2.ContractTraceElement.Upgraded} returned by the GRPC V2 API.
     * @return parsed {@link UpgradedResult}.
     */
    public static UpgradedResult parse(ContractTraceElement.Upgraded upgraded) {
        return UpgradedResult.builder()
                .contractAddress(ContractAddress.parse(upgraded.getAddress()))
                .from(ModuleRef.from(upgraded.getFrom().getValue().toByteArray()))
                .to(ModuleRef.from(upgraded.getTo().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.UPGRADED;
    }
}
