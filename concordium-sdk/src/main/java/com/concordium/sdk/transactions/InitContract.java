package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Initialize a new smart contract instance.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class InitContract extends Payload {

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

    @Override
    protected UInt64 getTransactionTypeCost() {
        return this.maxEnergyCost;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE;
    }

    @Override
    public byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }
}
