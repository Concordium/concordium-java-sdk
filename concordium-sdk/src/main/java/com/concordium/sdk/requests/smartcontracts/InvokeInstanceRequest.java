package com.concordium.sdk.requests.smartcontracts;

import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.transactions.*;
import com.concordium.sdk.transactions.smartcontracts.ParameterType;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.*;

import java.util.Objects;
import java.util.Optional;

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
     * If this is not set, then the node will determine a sufficient amount of energy allowed for the
     * transaction execution.
     */
    private Optional<Energy> energy;

    /**
     * Creates a {@link InvokeInstanceRequest} from the given parameters.
     *
     * @param blockHash  Block to invoke the contract. The invocation will be at the end of the given block.
     * @param invoker    Invoker of the contract, must exist in the blockstate.
     *                   If this is not supplied then the contract will be invoked by an account with address 0,
     *                   no credentials and sufficient amount of CCD to cover the transfer amount.
     * @param instance   Address of the contract instance to invoke.
     * @param entrypoint The {@link ReceiveName} of the smart contract instance to invoke.
     * @param parameter  The parameter bytes to include in the invocation of the entrypoint.
     * @param energy     The amount of energy to allow for execution. If this is not set, then the node
     *                   will decide a sufficient amount of energy allowed for transaction execution.
     */
    public static InvokeInstanceRequest from(@NonNull BlockQuery blockHash,
                                             @NonNull AbstractAddress invoker,
                                             @NonNull ContractAddress instance,
                                             @NonNull ReceiveName entrypoint,
                                             @NonNull Parameter parameter,
                                             @NonNull Optional<Energy> energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .invoker(invoker)
                .instance(instance)
                .entrypoint(entrypoint)
                .parameter(parameter)
                .energy(energy).build();
    }

    /**
     * Creates a {@link InvokeInstanceRequest} from the given parameters.
     * The contract will be invoked by an account with address 0, no credentials and sufficient CCD to cover the transfer amount.
     *
     * @param blockHash  Block to invoke the contract. The invocation will be at the end of the given block.
     * @param instance   Address of the contract instance to invoke.
     * @param entrypoint The {@link ReceiveName} of the smart contract instance to invoke.
     * @param parameter  The parameter bytes to include in the invocation of the entrypoint.
     * @param energy     The amount of energy to allow for execution. If this is not set, then the node
     *                   will decide a sufficient amount of energy allowed for transaction execution.
     */
    public static InvokeInstanceRequest from(@NonNull BlockQuery blockHash,
                                             @NonNull ContractAddress instance,
                                             @NonNull ReceiveName entrypoint,
                                             @NonNull Parameter parameter,
                                             @NonNull Optional<Energy> energy) {
        return InvokeInstanceRequest.builder()
                .blockHash(blockHash)
                .instance(instance)
                .entrypoint(entrypoint)
                .parameter(parameter)
                .energy(energy).build();
    }

    /**
     * Creates a {@link InvokeInstanceRequest} from the given parameters.
     *
     * @param blockHash       Block to invoke the contract. The invocation will be at the end of the given block.
     * @param invoker         Invoker of the contract, must exist in the blockstate.
     *                        If this is not supplied then the contract will be invoked by an account with address 0,
     *                        no credentials and sufficient amount of CCD to cover the transfer amount.
     * @param instance        Address of the contract instance to invoke.
     * @param schemaParameter {@link SchemaParameter} message to invoke the contract with. Must be initialized with {@link SchemaParameter#initialize()} beforehand.
     * @param energy          The amount of energy to allow for execution. If this is not set, then the node
     *                        will decide a sufficient amount of energy allowed for transaction execution.
     */
    public static InvokeInstanceRequest from(@NonNull BlockQuery blockHash,
                                             @NonNull AbstractAddress invoker,
                                             @NonNull ContractAddress instance,
                                             @NonNull SchemaParameter schemaParameter,
                                             @NonNull Optional<Energy> energy) {
        if (!(schemaParameter.getType() == ParameterType.RECEIVE)) {
            throw new IllegalArgumentException("Cannot initialize smart contract with InvokeInstance. SchemaParameter for InvokeInstanceRequest must be initialized with a ReceiveName");
        }
        return from(blockHash, invoker, instance, schemaParameter.getReceiveName(), Parameter.from(schemaParameter), energy);
    }

    /**
     * Creates a {@link InvokeInstanceRequest} from the given parameters.
     * The contract will be invoked by an account with address 0, no credentials and sufficient CCD to cover the transfer amount.
     *
     * @param blockHash       Block to invoke the contract. The invocation will be at the end of the given block.
     * @param instance        Address of the contract instance to invoke.
     * @param schemaParameter {@link SchemaParameter} message to invoke the contract with. Must be initialized with {@link SchemaParameter#initialize()} beforehand.
     * @param energy          The amount of energy to allow for execution. If this is not set, then the node
     *                        will decide a sufficient amount of energy allowed for transaction execution.
     */
    public static InvokeInstanceRequest from(@NonNull BlockQuery blockHash,
                                             @NonNull ContractAddress instance,
                                             @NonNull SchemaParameter schemaParameter,
                                             @NonNull Optional<Energy> energy) {
        if (!(schemaParameter.getType() == ParameterType.RECEIVE)) {
            throw new IllegalArgumentException("Cannot initialize smart contract with InvokeInstance. SchemaParameter for InvokeInstanceRequest must be initialized with a ReceiveName");
        }
        return from(blockHash, instance, schemaParameter.getReceiveName(), Parameter.from(schemaParameter), energy);
    }

    public boolean hasInvoker() {
        return !Objects.isNull(this.invoker);
    }
}