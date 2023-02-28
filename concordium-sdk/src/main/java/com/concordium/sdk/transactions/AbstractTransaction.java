package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.Getter;

import java.util.Objects;

@Getter
abstract class AbstractTransaction implements Transaction {

    /**
     * Account Address of the sender.
     */
    private final AccountAddress sender;
    /**
     * The senders account next available nonce.
     */
    private final AccountNonce nonce;
    /**
     * Indicates when the transaction should expire.
     */
    private final Expiry expiry;
    /**
     * A signer object that is used to sign the transaction.
     */
    private final TransactionSigner signer;

    public AbstractTransaction(
            final AccountAddress sender,
            final AccountNonce nonce,
            final Expiry expiry,
            final TransactionSigner signer) {
        this.sender = sender;
        this.nonce = nonce;
        this.expiry = expiry;
        this.signer = signer;

        if (Objects.isNull(sender)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Sender cannot be null"));
        }
        if (Objects.isNull(nonce)) {
            throw TransactionCreationException.from(new IllegalArgumentException("AccountNonce cannot be null"));
        }
        if (Objects.isNull(expiry)) {
            throw TransactionCreationException.from(new IllegalArgumentException("Expiry cannot be null"));
        }
        if (Objects.isNull(signer) || signer.isEmpty()) {
            throw TransactionCreationException.from(new IllegalArgumentException("Signer cannot be null or empty"));
        }
    }

    abstract BlockItem getBlockItem();

    @Override
    public byte[] getBytes() {
        return getBlockItem().getVersionedBytes();
    }

    @Override
    public Hash getHash() {
        return getBlockItem().getHash();
    }

    @Override
    public int getNetworkId() {
        return DEFAULT_NETWORK_ID;
    }

    TransactionHeader getHeader() {
        return TransactionHeader.builder()
                .sender(getSender())
                .accountNonce(getNonce().getNonce())
                .expiry(getExpiry().getValue())
                .build();
    }
}
