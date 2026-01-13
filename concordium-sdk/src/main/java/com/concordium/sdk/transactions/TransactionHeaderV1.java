package com.concordium.sdk.transactions;

import com.concordium.sdk.types.*;
import lombok.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * The transaction header used for the extended transaction format,
 * used for {@link AccountTransactionV1}.
 */
@RequiredArgsConstructor
@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
public class TransactionHeaderV1 {
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
     * @see TransactionHeaderV1#calculateMaxEnergyCost(int, int, int, UInt64)
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

    /**
     * The optional address of an account that sponsors the transaction.
     */
    @Nullable
    private final AccountAddress sponsor;

    @SuppressWarnings("unused")
    public Optional<AccountAddress> getSponsor() {
        return Optional.ofNullable(sponsor);
    }

    public byte[] getBytes() {
        val buffer = ByteBuffer.allocate(
                (sponsor != null)
                        ? BYTES_WITH_SPONSOR
                        : BYTES_WITHOUT_SPONSOR
        );
        int bitmap = 0;
        if (sponsor != null) {
            bitmap = bitmap | 1;
        }
        buffer.put(UInt16.from(bitmap).getBytes());
        buffer.put(sender.getBytes());
        buffer.put(nonce.getBytes());
        buffer.put(maxEnergyCost.getBytes());
        buffer.put(payloadSize.getBytes());
        buffer.put(expiry.getValue().getBytes());
        if (sponsor != null) {
            buffer.put(sponsor.getBytes());
        }
        return buffer.array();
    }

    public static TransactionHeaderV1 fromBytes(ByteBuffer source) {
        int bitmap = UInt16.fromBytes(source).getValue();
        boolean hasSponsor = (bitmap & 1) != 0;

        val sender = AccountAddress.fromBytes(source);
        val nonce = Nonce.fromBytes(source);
        val maxEnergyCost = UInt64.fromBytes(source);
        val payloadSize = UInt32.fromBytes(source);
        val expiry = Expiry.fromBytes(source);
        val sponsor = (hasSponsor) ? AccountAddress.fromBytes(source) : null;

        return new TransactionHeaderV1(
                sender,
                nonce,
                maxEnergyCost,
                payloadSize,
                expiry,
                sponsor
        );
    }

    /**
     * Calculate the maximum amount of energy that can be spent on executing a transaction.
     *
     * @param noOfSenderSignatures    number of signatures by the sender in the transaction
     *                                ({@link TransactionSigner#size()})
     * @param noOfSponsorSignatures   number of signatures by the sponsor in the transaction
     *                                ({@link TransactionSigner#size()})
     * @param payloadSize             size of the transaction payload in bytes
     * @param transactionSpecificCost cost of the specific transaction (payload) type
     * @return the energy cost for the transaction, to be set in the transaction header.
     * @see TransactionTypeCost
     */
    public static UInt64 calculateMaxEnergyCost(int noOfSenderSignatures,
                                                int noOfSponsorSignatures,
                                                int payloadSize,
                                                UInt64 transactionSpecificCost) {
        val headerSize = (noOfSponsorSignatures > 0)
                ? BYTES_WITH_SPONSOR
                : BYTES_WITHOUT_SPONSOR;
        return UInt64.from((long)
                TransactionHeader.COST_CONSTANT_A * (noOfSponsorSignatures + noOfSenderSignatures) +
                TransactionHeader.COST_CONSTANT_B * (headerSize + payloadSize)
                + transactionSpecificCost.getValue());
    }

    public static int BYTES_WITHOUT_SPONSOR = 2 + TransactionHeader.BYTES; // Bitmap + header.
    public static int BYTES_WITH_SPONSOR = BYTES_WITHOUT_SPONSOR + AccountAddress.BYTES;
}
