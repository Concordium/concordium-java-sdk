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


    private InitContract(InitContractPayload payload) {
        this.payload = payload;
    }

    static InitContract createNew(InitContractPayload payload) {
        return new InitContract(payload);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.INITIALIZE_SMART_CONTRACT_INSTANCE;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }
}
