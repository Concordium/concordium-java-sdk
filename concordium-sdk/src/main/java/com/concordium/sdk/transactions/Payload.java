package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.exceptions.ED25519Exception;
import com.concordium.sdk.exceptions.TransactionCreationException;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public abstract class Payload {
    TransactionHeader header;
    TransactionSignature signature;

    BlockItem toBlockItem() {
        return new AccountTransaction(signature, header, this);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Payload) {
            return Arrays.equals(this.getBytes(), ((Payload) obj).getBytes());
        }
        return false;
    }

    /**
     * Get the serialized payload.
     * The actual payload is prepended with a tag (a byte) which
     * indicates the {@link TransactionType}
     *
     * @return the serialized payload (including the type tag).
     */
    public final byte[] getBytes() {
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
     * Sets the signature and compute energy cost.
     * Note that this only computes and sets the energy cost if it hasn't already been set.
     * The energy cost is already set for smart contract transactions as the energy cost
     * cannot be computed before the transaction is executed.
     * Uses provided {@link TransactionSignature}.
     *
     * @param signer {@link TransactionSigner}
     * @return {@link Payload} with {@link Payload#signature} and {@link Payload#header} energy cost set.
     * @throws TransactionCreationException When there is a {@link ED25519Exception} while signing.
     */
    final Payload signWith(TransactionSigner signer) {
        if (Objects.isNull(this.header)) {
            throw TransactionCreationException.from(new IllegalStateException("TransactionHeader must be set before signing"));
        }
        // Compute and set the max energy cost if it can be computed,
        // otherwise rely on the energy cost set already.
        if (!(this instanceof PayloadUnknownCost)) {
            this.header.setMaxEnergyCost(
                    calculateEnergyCost(
                            signer.size(),
                            getBytes().length,
                            getTransactionTypeCost()
                    ));
        }
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

    /**
     * Calculate the total energy cost of a transaction.
     * This can only be used for non-smart contract transactions as
     * it is not possible to deduce the cost of such one before it has
     * been executed.
     * @param noOfSignatures number of signatures in the transaction
     * @param payloadSize size of the payload
     * @param transactionSpecificCost cost of the specific transaction type.
     * @return the computed cost.
     */
    private static UInt64 calculateEnergyCost(int noOfSignatures,
                                              int payloadSize,
                                              UInt64 transactionSpecificCost) {
        return UInt64.from((long)
                CONSTANT_A * noOfSignatures +
                CONSTANT_B * (TRANSACTION_HEADER_SIZE + payloadSize)
                + transactionSpecificCost.getValue());
    }

    public abstract TransactionType getTransactionType();

    /**
     * This must return the raw payload i.e., the
     * payload only. The tag will be prepended by {@link Payload#getBytes()}
     * @return the raw serialized payload.
     */
    protected abstract byte[] getRawPayloadBytes();

}
