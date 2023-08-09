package com.concordium.sdk.requests.smartcontracts;

import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

import java.util.Objects;

/**
 * Request for InvokeInstance
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class InvokeInstanceRequest {

    /**
     * Block to invoke the contract. The invocation will be at the end of the given block.
     */
    private BlockQuery blockHash;

    /**
     * Invoker of the contract.
     * If this is not supplied then the contract will be invoked by an account with address 0,
     * no credentials and sufficient amount of CCD to cover the transfer amount.
     * If given, the relevant address (either account or contract) must exist in the blockstate.
     */
    private AbstractAddress invoker;

    /**
     * Address of the contract instance to invoke.
     */
    private ContractAddress instance;

    /**
     * Amount to invoke the smart contract instance with.
     */
    private CCDAmount amount;

    /**
     * The entrypoint of the smart contract instance to invoke.
     */
    private ReceiveName entrypoint;

    /**
     * The parameter bytes to include in the invocation of the entrypoint.
     */
    private Parameter parameter;

    /**
     * The amount of energy to allow for execution.
     * This cannot exceed `100_000_000_000`, but in practice it should be much less.
     * The maximum block energy is typically in the range of a few million.
     */
    private Energy energy;

    public static InvokeInstanceRequest from(BlockQuery blockHash,
                                        AbstractAddress invoker,
                                        ContractAddress instance,
                                        CCDAmount amount,
                                        ReceiveName entrypoint,
                                        Parameter parameter,
                                        Energy energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .invoker(invoker)
                .instance(instance)
                .amount(amount)
                .entrypoint(entrypoint)
                .parameter(parameter)
                .energy(energy).build();
    }
    public static InvokeInstanceRequest from(BlockQuery blockHash,
                                             ContractAddress instance,
                                             CCDAmount amount,
                                             ReceiveName entrypoint,
                                             Parameter parameter,
                                             Energy energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .instance(instance)
                .amount(amount)
                .entrypoint(entrypoint)
                .parameter(parameter)
                .energy(energy).build();
    }

    public static InvokeInstanceRequest from(BlockQuery blockHash,
                                             AbstractAddress invoker,
                                             ContractAddress instance,
                                             CCDAmount amount,
                                             SchemaReceiveParameter schemaReceiveParameter,
                                             Energy energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .invoker(invoker)
                .instance(instance)
                .amount(amount)
                .entrypoint(schemaReceiveParameter.getReceiveName())
                .parameter(Parameter.from(schemaReceiveParameter))
                .energy(energy).build();
    }

    public static InvokeInstanceRequest from(BlockQuery blockHash,
                                             ContractAddress instance,
                                             CCDAmount amount,
                                             SchemaReceiveParameter schemaReceiveParameter,
                                             Energy energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .instance(instance)
                .amount(amount)
                .entrypoint(schemaReceiveParameter.getReceiveName())
                .parameter(Parameter.from(schemaReceiveParameter))
                .energy(energy).build();
    }
    public boolean hasInvoker() {
        return !Objects.isNull(this.invoker);
    }
}