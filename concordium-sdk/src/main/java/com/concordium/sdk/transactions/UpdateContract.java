package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

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

    // A constructor.
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

    /**
     * This function returns the type of the payload, which is UPDATE.
     */
    @Override
    public PayloadType getType() {
        return PayloadType.UPDATE;
    }

    /**
     * This function returns the byte buffer array of the payload.
     */
    @Override
    byte[] getBytes() {
        val payload_bytes = payload.getBytes();
        val buffer = ByteBuffer.allocate(payload_bytes.length);
        buffer.put(payload_bytes);
        return buffer.array();
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }
}
