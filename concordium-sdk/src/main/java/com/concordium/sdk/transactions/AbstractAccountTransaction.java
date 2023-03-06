package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAccountTransaction extends BlockItem {

    private final TransactionHeader header;
    private final TransactionSignature signature;
    private final TransactionType transactionType;
    private final byte[] payloadBytes;

    AbstractAccountTransaction(
            @NonNull final AccountAddress sender,
            @NonNull final AccountNonce nonce,
            @NonNull final Expiry expiry,
            @NonNull final TransactionSigner signer,
            @NonNull final Payload payload) {
        this(payload
                .withHeader(TransactionHeader.builder()
                        .sender(sender)
                        .accountNonce(nonce.getNonce())
                        .expiry(expiry.getValue())
                        .build())
                .signWith(signer));
    }

    public AbstractAccountTransaction(
            @NonNull final TransactionHeader header,
            @NonNull final TransactionSignature signature,
            @NonNull final Payload payload) {
        this(header, signature, payload.getTransactionType(), payload.getTransactionPayloadBytes());
    }

    AbstractAccountTransaction(Payload payload) {
        this(payload.header, payload.signature, payload.getTransactionType(), payload.getTransactionPayloadBytes());
    }

    public AbstractAccountTransaction(
            @NonNull final TransactionHeader header,
            @NonNull final TransactionSignature signature,
            @NonNull final TransactionType transactionType,
            @NonNull final byte[] payloadBytes) {
        super(BlockItemType.ACCOUNT_TRANSACTION);
        this.header = header;
        this.signature = signature;
        this.payloadBytes = payloadBytes;
        this.transactionType = transactionType;
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

    final byte[] getBlockItemBytes() {
        val signatureBytes = signature.getBytes();
        val headerBytes = header.getBytes();
        val payloadBytes = getBlockItemBodyBytes();
        val buffer = ByteBuffer.allocate(signatureBytes.length + headerBytes.length + payloadBytes.length);
        buffer.put(signatureBytes);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);

        return buffer.array();
    }

    final byte[] getBlockItemBodyBytes() {
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + payloadBytes.length);
        buffer.put(getTransactionType().getValue());
        buffer.put(payloadBytes);

        return buffer.array();
    }
}
