package com.concordium.sdk.transactions;


import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Transfer from public to encrypted balance of the sender account.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class TransferToEncrypted extends Payload {

    /**
     * The amount to transfer from public to encrypted balance of the sender account.
     */
    private final CCDAmount amount;

    public TransferToEncrypted(CCDAmount amount) {
        this.amount = amount;
    }

    /**
     * This is a method that returns the type of the payload
     */
    @Override
    public PayloadType getType() {
        return PayloadType.TRANSFER_TO_ENCRYPTED;
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return TransactionTypeCost.TRANSFER_TO_ENCRYPTED.getValue();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_TO_ENCRYPTED;
    }

    @Override
    public byte[] getTransactionPayloadBytes() {
        return amount.getBytes();
    }

    static TransferToEncrypted createNew(CCDAmount amount) {
        return new TransferToEncrypted(amount);
    }
}
