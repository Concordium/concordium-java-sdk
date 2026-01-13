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
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class TransactionHeader {
    /**
     * The address of the account that is the source of the transaction.
     */
    @NonNull
    private final AccountAddress sender;

    /**
     * The sequence number of the transaction, sender (source) account nonce.
     * Transactions executed on an account must have sequential sequence numbers, starting from 1.
     */
    @NonNull
    private final Nonce nonce;

    /**
     * The amount of energy allocated for executing this transaction.
     * This is the maximum amount of energy that can be spent on executing this transaction.
     *
     * @see TransactionHeader#calculateMaxEnergyCost(int, int, UInt64)
     */
    @NonNull
    private final UInt64 maxEnergyCost;

    /**
     * The size of the transaction payload in bytes.
     */
    @NonNull
    private final UInt32 payloadSize;

    /**
     * A Unix timestamp indicating when the transaction should expire.
     * A transaction cannot be included in a block with a timestamp
     * later than the transaction’s expiry time.
     */
    @NonNull
    private final Expiry expiry;

    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(BYTES);
        buffer.put(sender.getBytes());
        buffer.put(nonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getValue().getBytes());
        return buffer.array();
    }

    public static TransactionHeader fromBytes(ByteBuffer source) {
        return TransactionHeader
                .builder()
                .sender(AccountAddress.fromBytes(source))
                .nonce(Nonce.fromBytes(source))
                .maxEnergyCost(UInt64.fromBytes(source))
                .payloadSize(UInt32.fromBytes(source))
                .expiry(Expiry.fromBytes(source))
                .build();
    }

    private final static int CONSTANT_A = 100;
    private final static int CONSTANT_B = 1;

    /**
     * Calculate the maximum amount of energy that can be spent on executing a transaction.
     *
     * @param noOfSignatures          number of signatures in the transaction ({@link TransactionSigner#size()})
     * @param payloadSize             size of the transaction payload in bytes
     * @param transactionSpecificCost cost of the specific transaction (payload) type
     * @return the energy cost for the transaction, to be set in the transaction header.
     * @see TransactionTypeCost
     */
    public static UInt64 calculateMaxEnergyCost(int noOfSignatures,
                                                int payloadSize,
                                                UInt64 transactionSpecificCost) {
        return UInt64.from((long)
                CONSTANT_A * noOfSignatures +
                CONSTANT_B * (TransactionHeader.BYTES + payloadSize)
                + transactionSpecificCost.getValue());
    }

    public static int BYTES = AccountAddress.BYTES + Nonce.BYTES + UInt64.BYTES + UInt32.BYTES + Expiry.BYTES;
}
