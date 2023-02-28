package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Construct a transaction to transfer from public to encrypted balance of the sender account.
 */
@Getter
public class TransferToEncryptedTransaction extends AbstractTransaction {
    /**
     * The amount to transfer from public to encrypted balance of the sender account.
     */
    private final CCDAmount amount;

    @Builder
    public TransferToEncryptedTransaction(
            final CCDAmount amount,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(amount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
        }
        this.amount = amount;
    }

    @Override
    public BlockItem getBlockItem() {
        return TransferToEncrypted.createNew(getAmount()).
                withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
