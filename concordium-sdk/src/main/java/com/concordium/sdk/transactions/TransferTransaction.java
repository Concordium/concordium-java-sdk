package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TransferTransaction extends AbstractTransaction {
    private final AccountAddress receiver;
    private final CCDAmount amount;

    @Builder
    public TransferTransaction(
            final AccountAddress sender,
            final AccountAddress receiver,
            final CCDAmount amount,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);
        if (Objects.isNull(receiver)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Receiver cannot be null"));
        }
        if (Objects.isNull(amount)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Amount cannot be null"));
        }
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public BlockItem getBlockItem() {
        return Transfer.createNew(getReceiver(), getAmount()).
                withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
