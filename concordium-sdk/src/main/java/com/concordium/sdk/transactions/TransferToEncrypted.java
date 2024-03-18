package com.concordium.sdk.transactions;

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

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_TO_ENCRYPTED;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return amount.getBytes();
    }

    static TransferToEncrypted createNew(CCDAmount amount) {
        return new TransferToEncrypted(amount);
    }
}
