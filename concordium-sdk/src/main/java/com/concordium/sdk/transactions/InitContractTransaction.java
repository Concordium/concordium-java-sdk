package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;


/**
 * Construct a transaction to initialise a smart contract.
 */
@Getter
public class InitContractTransaction extends AbstractTransaction {

    /**
     * Payload to initialize a smart contract.
     */
    private final InitContractPayload payload;

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
    private final UInt64 maxEnergyCost;

    /**
     * A constructor of {@link InitContractTransaction} class.
     */
    @Builder
    public InitContractTransaction(
            final InitContractPayload payload,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer,
            final UInt64 maxEnergyCost) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(payload)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
        }
        this.payload = payload;
        this.maxEnergyCost = maxEnergyCost;
    }

    @Override
    public BlockItem getBlockItem() {
        return InitContract.createNew(getPayload(), getMaxEnergyCost())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
