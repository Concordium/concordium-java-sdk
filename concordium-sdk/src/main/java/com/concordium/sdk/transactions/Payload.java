package com.concordium.sdk.transactions;

import com.concordium.sdk.crypto.SHA256;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;

import lombok.val;

import java.nio.ByteBuffer;

abstract class Payload {
    TransactionHeader header;
    TransactionSignature signature;

    public BlockItem toBlockItem() {
        return BlockItem.from(new AccountTransaction(signature, header, this));
    }

    abstract byte[] getBytes();

    abstract UInt64 getTransactionTypeCost();

    final AccountTransaction toAccountTransaction() {
        return new AccountTransaction(signature, header, this);
    }

    final Payload withHeader(TransactionHeader header) {
        header.setPayloadSize(UInt32.from(getBytes().length));
        this.header = header;
        return this;
    }

    final Payload withSigner(TransactionSigner signer) {
        this.header.setMaxEnergyCost(
                calculateEnergyCost(
                        signer.size(),
                        getBytes().length,
                        getTransactionTypeCost()
                ));
        this.signature = signer.sign(getSignData());
        return this;
    }

    byte[] getSignData() {
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
}
