package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.NonNull;

@Getter
abstract class AbstractAccountTransaction<T extends Payload> implements Transaction {

    /**
     * A signer object that is used to sign the transaction.
     */
    private final TransactionSigner signer;
    private final T payload;
    private final BlockItem blockItem;
    private final TransactionHeader header;
    private final TransactionSignature signature;

    public AbstractAccountTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final T payload) {
        this.signer = signer;
        this.blockItem = payload
                .withHeader(TransactionHeader.builder()
                        .sender(sender)
                        .accountNonce(nonce.getNonce())
                        .expiry(expiry.getValue())
                        .build())
                .signWith(getSigner())
                .toBlockItem();
        this.header = payload.header;
        this.signature = payload.signature;
        this.payload = payload;
    }

    public AccountAddress getSender() {
        return this.header.getSender();
    }

    public AccountNonce getNonce() {
        return AccountNonce.from(this.header.getAccountNonce());
    }

    /**
     * Indicates when the transaction should expire.
     */
    public Expiry getExpiry() {
        return Expiry.from(this.header.getExpiry().getValue());
    }

    @Override
    public byte[] getBytes() {
        return getBlockItem().getVersionedBytes();
    }

    @Override
    public Hash getHash() {
        return getBlockItem().getHash();
    }
}
