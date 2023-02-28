package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Construct a transaction to update a smart contract instance.
 */
@Getter
public class UpdateContractTransaction extends AbstractTransaction {
    /**
     * The payload for updating a smart contract.
     */
    private final UpdateContractPayload payload;

    /**
     * Maximum energy **allowed** for the transaction to use.
     */
    private final UInt64 maxEnergyCost;

    @Builder
    public UpdateContractTransaction(
            final UpdateContractPayload payload,
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

    /**
     * This function returns the block item associated with this block.
     */
    @Override
    public BlockItem getBlockItem() {
        return UpdateContract.createNew(getPayload(), getMaxEnergyCost())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
