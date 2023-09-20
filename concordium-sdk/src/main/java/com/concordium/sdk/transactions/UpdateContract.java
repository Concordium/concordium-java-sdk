package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Update a new smart contract instance.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class UpdateContract extends Payload {

    /**
     * The payload for updating a smart contract.
     */
    private final UpdateContractPayload payload;

    private final UInt64 maxEnergyCost;

    public UpdateContract(UpdateContractPayload payload, UInt64 maxEnergyCost) {
        this.payload = payload;
        this.maxEnergyCost = maxEnergyCost;
    }

    /**
     * Create a new UpdateContract object with the given payload and maximum energy cost.
     *
     * @param payload       The payload of the transaction.
     * @param maxEnergyCost The maximum amount of energy that can be consumed by the transaction.
     * @return A new instance of the UpdateContract class.
     */
    static UpdateContract createNew(UpdateContractPayload payload, UInt64 maxEnergyCost) {
        return new UpdateContract(payload, maxEnergyCost);
    }

    @Override
    protected UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UPDATE_SMART_CONTRACT_INSTANCE;
    }

    @Override
    public byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }
}
