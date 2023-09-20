package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Objects;

@EqualsAndHashCode
public abstract class Payload {
    TransactionHeader header;
    TransactionSignature signature;

    BlockItem toBlockItem() {
        return new AccountTransaction(signature, header, this);
    }


    /**
     * Get the bytes representation of the payload
     *
     * @return byte[]
     */
    final byte[] getBytes() {
        val payloadBytes = getRawPayloadBytes();
        val buffer = ByteBuffer.allocate(TransactionType.BYTES + payloadBytes.length);
        buffer.put(getTransactionType().getValue());
        buffer.put(payloadBytes);

        return buffer.array();
    }

    protected abstract UInt64 getTransactionTypeCost();

    final AccountTransaction toAccountTransaction() {
        return new AccountTransaction(signature, header, this);
    }

    final Payload withHeader(TransactionHeader header) {
        header.setPayloadSize(UInt32.from(getBytes().length));
        this.header = header;
        return this;
    }

    /**
     * Sets the signature and energy cost. Uses provided {@link TransactionSignature}.
     *
     * @param signer {@link TransactionSigner}
     * @return {@link Payload} with {@link Payload#signature} and {@link Payload#header} energy cost set.
     * @throws TransactionCreationException When there is a {@link ED25519Exception} while signing.
     */
    final Payload signWith(TransactionSigner signer) {
        if (Objects.isNull(this.header)) {
            throw TransactionCreationException.from(new IllegalStateException("TransactionHeader must be set before signing"));
        }
        this.header.setMaxEnergyCost(
                calculateEnergyCost(
                        signer.size(),
                        getBytes().length,
                        getTransactionTypeCost()
                ));
        try {
            this.signature = signer.sign(getDataToSign());
        } catch (ED25519Exception e) {
            throw TransactionCreationException.from(e);
        }
        return this;
    }

    byte[] getDataToSign() {
        val headerBytes = header.getBytes();
        val payloadBytes = getBytes();
        val buffer = ByteBuffer.allocate(headerBytes.length + payloadBytes.length);
        buffer.put(headerBytes);
        buffer.put(payloadBytes);
        return SHA256.hash(buffer.array());
    }

    private final static int CONSTANT_A = 100;
    private final static int CONSTANT_B = 1;

    private final static int ADDRESS_SIZE = 32;
    private final static int NONCE_SIZE = 8;
    private final static int ENERGY_SIZE = 8;
    private final static int PAYLOAD_SIZE = 4;
    private final static int EXPIRY_SIZE = 8;
    private final static int TRANSACTION_HEADER_SIZE = ADDRESS_SIZE + NONCE_SIZE + ENERGY_SIZE + PAYLOAD_SIZE + EXPIRY_SIZE;

    private static UInt64 calculateEnergyCost(int noOfSignatures,
                                              int payloadSize,
                                              UInt64 transactionSpecificCost) {
        return UInt64.from((long)
                CONSTANT_A * noOfSignatures +
                CONSTANT_B * (TRANSACTION_HEADER_SIZE + payloadSize)
                + transactionSpecificCost.getValue());
    }

    public abstract TransactionType getTransactionType();

    public abstract byte[] getRawPayloadBytes();

}
