package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.exceptions.TransactionCreationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.nio.ByteBuffer;

import static com.google.common.primitives.Bytes.concat;

/**
 * An extended transaction format originating from a particular account.
 * Such a transaction can be sponsored, in which case its cost is not paid
 * by the sender.
 *
 * @see AccountTransactionV1#from(PartiallySignedSponsoredTransaction, TransactionSignature) Create from a partially signed sponsored transaction
 * @see PartiallySignedSponsoredTransaction#buildSignedBySender() Build a partially signed sponsored transaction on the sender side
 * @see PartiallySignedSponsoredTransaction#buildSignedBySponsor() Build a partially signed sponsored transaction on the sponsor side
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountTransactionV1 extends BlockItem {

    /**
     * The signatures on the transaction by the source account and optionally a sponsor account.
     */
    private final TransactionSignaturesV1 signatures;

    /**
     * Transaction header data.
     */
    private final TransactionHeaderV1 header;

    /**
     * Transaction payload, defines what this transaction does.
     */
    private final Payload payload;

    /**
     * @param signatures The signatures on the transaction by the source account and optionally a sponsor account.
     * @param header     Transaction header data.
     * @param payload    Transaction payload, defines what this transaction does.
     */
    public AccountTransactionV1(@NonNull TransactionSignaturesV1 signatures,
                                @NonNull TransactionHeaderV1 header,
                                @NonNull Payload payload) {
        super(BlockItemType.ACCOUNT_TRANSACTION_V1);
        this.signatures = signatures;
        this.header = header;
        this.payload = payload;
    }

    @Override
    final byte[] getBlockItemBytes() {
        return concat(
                signatures.getBytes(),
                header.getBytes(),
                payload.getBytes()
        );
    }

    /**
     * @param source a buffer to read transaction bytes from, without {@link BlockItemType} byte.
     * @return deserialized {@link AccountTransactionV1}
     * @throws UnsupportedOperationException if payload can't be read.
     *                                       Not all transaction (payload) types can be read.
     *                                       For unsupported types, get the payload size
     *                                       from the manually read {@link TransactionHeaderV1}
     *                                       and then proceed with {@link RawPayload}.
     */
    public static AccountTransactionV1 fromBytes(ByteBuffer source) {
        return new AccountTransactionV1(
                TransactionSignaturesV1.fromBytes(source),
                TransactionHeaderV1.fromBytes(source),
                Payload.fromBytes(source)
        );
    }

    /**
     * @param partiallySigned    sponsored transaction signed by either sender or sponsor
     * @param remainingSignature the signature missing from the partially signed transaction
     * @return fully signed {@link AccountTransactionV1}
     * @throws TransactionCreationException when something goes wrong
     */
    public static AccountTransactionV1 from(@NonNull PartiallySignedSponsoredTransaction partiallySigned,
                                            @NonNull TransactionSignature remainingSignature) {
        try {
            return new AccountTransactionV1(
                    new TransactionSignaturesV1(
                            partiallySigned.getSenderSignature().orElse(remainingSignature),
                            partiallySigned.getSponsorSignature().orElse(remainingSignature)
                    ),
                    partiallySigned.getHeader(),
                    partiallySigned.getPayload()
            );
        } catch (Exception e) {
            throw TransactionCreationException.from(e);
        }
    }

    /**
     * @param partiallySigned sponsored transaction signed by either sender or sponsor
     * @param remainingSigner signer whose signature is missing from the partially signed transaction
     * @return fully signed {@link AccountTransactionV1}
     * @throws TransactionCreationException when something goes wrong
     */
    public static AccountTransactionV1 from(@NonNull PartiallySignedSponsoredTransaction partiallySigned,
                                            @NonNull TransactionSigner remainingSigner) {
        return from(
                partiallySigned,
                remainingSigner.sign(getDataToSign(partiallySigned.getHeader(), partiallySigned.getPayload()))
        );
    }

    public static byte[] getDataToSign(TransactionHeaderV1 header,
                                       Payload payload) {
        return SHA256.hash(
                concat(
                        // 32 byte prefix defining the transaction version.
                        new byte[]{
                                0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 1,
                        },
                        header.getBytes(),
                        payload.getBytes()
                )
        );
    }
}
