package com.concordium.sdk.transactions;


import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class EncryptedTransferTransaction extends AbstractTransaction {

    /**
     *  Data that will go onto an encrypted amount transfer.
     */
    private final EncryptedAmountTransferData data;
    /**
     * The account address to which the transfer will be sent.
     */
    private final AccountAddress receiver;

    /**
     * A constructor of {@link EncryptedTransferTransaction} class.
     */
    @Builder
    public EncryptedTransferTransaction(
            final EncryptedAmountTransferData data,
            final AccountAddress receiver,
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        super(sender, nonce, expiry, signer);

        if (Objects.isNull(receiver)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Receiver address cannot be null"));
        }

        if (Objects.isNull(data)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Data cannot be null"));
        }
        this.receiver = receiver;
        this.data = data;
    }

    @Override
    public BlockItem getBlockItem() {
        return EncryptedTransfer.createNew(getData(), getReceiver())
                .withHeader(getHeader())
                .signWith(getSigner())
                .toBlockItem();
    }
}
