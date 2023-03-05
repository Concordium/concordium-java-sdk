package com.concordium.sdk.transactions;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
abstract class AbstractTransaction implements Transaction {

    private final BlockItem blockItem;
    private final TransactionHeader header;
    private final TransactionSignature signature;
    private final TransactionType transactionType;
    private final BlockItemType blockItemType;
    private final byte[] payloadBytes;

    public AbstractTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final Payload payload) {
        val blockItem = payload
                .withHeader(TransactionHeader.builder()
                        .sender(sender)
                        .accountNonce(nonce.getNonce())
                        .expiry(expiry.getValue())
                        .build())
                .signWith(signer)
                .toBlockItem();

        this.blockItem = blockItem;
        this.blockItemType = blockItem.getType();
        this.header = payload.header;
        this.signature = payload.signature;
        this.payloadBytes = payload.getTransactionPayloadBytes();
        this.transactionType = payload.getTransactionType();
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

    public byte[] getBytes() {
        val blockItemBytes = getBlockItemBytes();
        val buffer = ByteBuffer.allocate(BlockItemType.BYTES + blockItemBytes.length);
        buffer.put(blockItemType.getByte());
        buffer.put(blockItemBytes);

        return buffer.array();
    }

    private byte[] getBlockItemBytes() {
        val signatureBytes = signature.getBytes();
        val headerBytes = header.getBytes();
        val payloadBytes = getBlockItemBodyBytes();
        val buffer = ByteBuffer.allocate(signatureBytes.length + headerBytes.length + payloadBytes.length);
        buffer.put(signatureBytes);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);

        return buffer.array();
    }

    private byte[] getBlockItemBodyBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + payloadBytes.length);
        buffer.put(getTransactionType().getValue());
        buffer.put(payloadBytes);

        return buffer.array();
    }
}
