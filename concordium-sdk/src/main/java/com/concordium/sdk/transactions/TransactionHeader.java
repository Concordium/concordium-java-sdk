package com.concordium.sdk.transactions;

import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.*;

import java.nio.ByteBuffer;

/**
 * Header for an {@link AccountTransaction}.
 */
@Getter
@EqualsAndHashCode
@ToString
public class TransactionHeader {
    private final AccountAddress sender;
    private final Nonce nonce;
    private final UInt64 expiry;

    @Setter
    private UInt64 maxEnergyCost;
    @Setter
    private UInt32 payloadSize;

    /**
     * Create a {@link TransactionHeader}
     *
     * @param sender       the sender {@link AccountAddress}
     * @param Nonce the nonce.
     *                     Note. this should be the next available account nonce.
     *                     This can e.g. be retrieved via {@link com.concordium.sdk.responses.accountinfo.AccountInfo}
     * @param expiry       A Unix timestamp indicating when the transaction should expire.
     */
    @Builder
    TransactionHeader(@NonNull AccountAddress sender, @NonNull Nonce Nonce, @NonNull UInt64 expiry) {
        this.sender = sender;
        this.nonce = Nonce;
        this.expiry = expiry;
        this.maxEnergyCost = UInt64.from(0); // dummy value will be overwritten when calculating the cost of the transaction.
    }

    /**
     * Builder for constructing headers where the allowed maximum energy to spend is
     * set directly in the header as opposed to being calculated.
     * This is the case for transactions that operate on smart contracts.
     * @param sender the sender of the transaction
     * @param Nonce the nonce of the sender account
     * @param expiry the expiry of the transaction
     * @param maxEnergyCost the maximum allowed energy to spend on this transaction.
     */
    @Builder(builderMethodName = "explicitMaxEnergyBuilder", builderClassName = "TransactionHeaderExplicitMaxEnergyBuilder")
    TransactionHeader(@NonNull AccountAddress sender, @NonNull Nonce Nonce, @NonNull UInt64 expiry, @NonNull UInt64 maxEnergyCost) {
        this.sender = sender;
        this.nonce = Nonce;
        this.expiry = expiry;
        this.maxEnergyCost = maxEnergyCost;
    }

    private TransactionHeader(@NonNull final AccountAddress sender,
                              @NonNull final Nonce Nonce,
                              @NonNull final UInt64 expiry,
                              @NonNull final UInt64 maxEnergyCost,
                              @NonNull final UInt32 payloadSize) {
        this(sender, Nonce, expiry);
        this.maxEnergyCost = maxEnergyCost;
        this.payloadSize = payloadSize;
    }

    /**
     * Creates a new Account {@link TransactionHeader}.
     *
     * @param sender        Sender ({@link AccountAddress}) of this Transaction.
     * @param Nonce  Account {@link com.concordium.sdk.types.Nonce} Of the Sender Account.
     * @param expiry        {@link Expiry} of this transaction.
     * @param maxEnergyCost Energy allowed for this transaction.
     * @param payloadSize   Byte size of the payload for this transaction.
     * @return Instantiated {@link TransactionHeader}.
     */
    @Builder(
            toBuilder = true,
            builderClassName = "TransactionHeaderImmutableBuilder",
            builderMethodName = "builderImmutable")
    public static TransactionHeader from(final AccountAddress sender,
                                         final Nonce nonce,
                                         final UInt64 expiry,
                                         final UInt64 maxEnergyCost,
                                         final UInt32 payloadSize) {
        return new TransactionHeader(sender, nonce, expiry, maxEnergyCost, payloadSize);
    }

    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(AccountAddress.BYTES + UInt64.BYTES + UInt64.BYTES + UInt32.BYTES + UInt64.BYTES);
        buffer.put(sender.getBytes());
        buffer.put(nonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getBytes());
        return buffer.array();
    }

    public static TransactionHeader fromBytes(ByteBuffer source) {
        AccountAddress sender = AccountAddress.fromBytes(source);
        val nonce = Nonce.fromBytes(source);
        UInt64 maxEnergyCost = UInt64.fromBytes(source);
        UInt32 payloadSize = UInt32.fromBytes(source);
        UInt64 expiry = UInt64.fromBytes(source);
        TransactionHeader header = new TransactionHeader(sender, nonce, expiry);
        header.setMaxEnergyCost(maxEnergyCost);
        header.setPayloadSize(payloadSize);
        return header;
    }
}
