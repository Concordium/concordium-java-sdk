package com.concordium.sdk.transactions.schema;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A Single contract in {@link ModuleSchema}
 *
 * @param <TFunction> Type of Functions this Contract Has. Should implement {@link ContractFunction}
 */
@Getter
@RequiredArgsConstructor
public abstract class Contract<TFunction extends ContractFunction> {
    /**
     * Contract Initialize function. This will be {@link Optional#empty()} if the Init function has no Parameters.
     */
    private final Optional<TFunction> init;

    /**
     * A {@link Map} of Receive functions in {@link Contract}.
     */
    private final Map<String, TFunction> receive;

    /**
     * Gets a Single Receive Function from all the {@link Contract#getReceive()} functions.
     *
     * @param functionName Name of the Receive function.
     * @return Receive {@link ContractFunction}
     */
    public TFunction getReceiveFunction(String functionName) {
        val func = receive.get(functionName);

        if (Objects.isNull(func)) {
            throw new RuntimeException("Receive function: " + functionName + " not present");
        }

        return func;
    }
}
