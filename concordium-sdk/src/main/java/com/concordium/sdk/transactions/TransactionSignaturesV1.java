package com.concordium.sdk.transactions;

import lombok.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Optional;

import static com.google.common.primitives.Bytes.concat;

/**
 * The signatures for the extended transaction format.
 * Contain not only the sender, but also the sponsor signature.
 * Used for {@link AccountTransactionV1}.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TransactionSignaturesV1 {
    /**
     * The signature on the transaction by the source account.
     */
    private final TransactionSignature senderSignature;

    /**
     * The optional signature on the transaction by the sponsor account.
     */
    @Nullable
    private final TransactionSignature sponsorSignature;

    @SuppressWarnings("unused")
    public Optional<TransactionSignature> getSponsorSignature() {
        return Optional.ofNullable(sponsorSignature);
    }

    public byte[] getBytes() {
        return concat(
                senderSignature.getBytes(),
                // The non-presence of the sponsor signature is represented by a single 0 byte.
                (sponsorSignature != null) ? sponsorSignature.getBytes() : new byte[]{0}
        );
    }

    public static TransactionSignaturesV1 fromBytes(ByteBuffer source) {
        val senderSignature = TransactionSignature.fromBytes(source);
        val sponsorSignature = TransactionSignature.fromBytes(source);

        return new TransactionSignaturesV1(
                senderSignature,
                // The non-presence of the sponsor signature is represented by a single 0 byte.
                // When read as TransactionSignature, it results in empty signature map.
                (sponsorSignature.getSignatures().isEmpty()) ? null : sponsorSignature
        );
    }
}
