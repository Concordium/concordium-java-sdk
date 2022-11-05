package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

/**
 * Initialize a new smart contract instance.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class InitContract extends Payload {

    private final static TransactionType TYPE = TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE;

    /**
     * Payload to initialize a smart contract.
     */
    private final InitContractPayload payload;

    private final UInt64 maxEnergyCost;

    private InitContract(InitContractPayload payload, UInt64 maxEnergyCost) {
        this.payload = payload;
        this.maxEnergyCost = maxEnergyCost;
    }

    static InitContract createNew(InitContractPayload payload, UInt64 maxEnergyCost) {
        return new InitContract(payload, maxEnergyCost);
    }

    /**
     * This function returns the type of the payload.
     */
    @Override
    public PayloadType getType() {
        return PayloadType.INIT_CONTRACT;
    }

    /**
     * Get the bytes of the payload and put them in a buffer.
     *
     * @return The byte array of the payload.
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
