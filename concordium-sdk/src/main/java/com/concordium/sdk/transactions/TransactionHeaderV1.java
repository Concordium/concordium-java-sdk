package com.concordium.sdk.transactions;

import com.concordium.sdk.types.*;
import lombok.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Optional;

import static com.google.common.primitives.Bytes.concat;

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
     * The optional address of an account that sponsors the transaction.
     */
    @Nullable
    private final AccountAddress sponsor;

    /**
     * The sequence number of the transaction, sender (source) account nonce.
     * Transactions executed on an account must have sequential sequence numbers, starting from 1.
     */
    @NonNull
    private final Nonce nonce;

    /**
     * The amount of energy allocated for executing this transaction.
     * This is the maximum amount of energy that can be spent on executing this transaction.
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

    public Optional<AccountAddress> getSponsor() {
        return Optional.ofNullable(sponsor);
    }

    public byte[] getBytes() {
        int bitmap = 0;
        if (sponsor != null) {
            bitmap = bitmap | 1;
        }

        return concat(
                UInt16.from(bitmap).getBytes(),
                sender.getBytes(),
                nonce.getBytes(),
                maxEnergyCost.getBytes(),
                payloadSize.getBytes(),
                expiry.getValue().getBytes(),
                (sponsor != null) ? sponsor.getBytes() : new byte[]{}
        );
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
                sponsor,
                nonce,
                maxEnergyCost,
                payloadSize,
                expiry
        );
    }
}
