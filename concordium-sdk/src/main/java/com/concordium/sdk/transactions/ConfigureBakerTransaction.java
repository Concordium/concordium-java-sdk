package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * A class that represents a ConfigureBakerTransaction.
 * This transaction is used to register the account as a baker.
 */
@Getter
public class ConfigureBakerTransaction extends AbstractTransaction {
    /**
     * Whether to add earnings to the stake automatically or not.
     */
    private final ConfigureBakerPayload payload;

    /**
     * A constructor of {@link ConfigureBakerTransaction} class.
     */
    @Builder
    public ConfigureBakerTransaction(
            final ConfigureBakerPayload payload,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(payload)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Payload cannot be null"));
        }

        this.payload = payload;
    }

    @Override
    public BlockItem getBlockItem() {
        return ConfigureBaker.createNew(getPayload())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
