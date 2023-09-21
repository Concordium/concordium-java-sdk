package com.concordium.sdk.transactions;


import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public final class EncryptedTransferWithMemo extends Payload {
    private final EncryptedTransferWithMemoPayload payload;

    public EncryptedTransferWithMemo(EncryptedAmountTransferData data, AccountAddress receiver, Memo memo) {
        this.payload = EncryptedTransferWithMemoPayload.from(data, receiver, memo);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.ENCRYPTED_TRANSFER_WITH_MEMO;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return payload.getBytes();
    }

    static EncryptedTransferWithMemo createNew(EncryptedAmountTransferData data, AccountAddress receiver, Memo memo) {
        return new EncryptedTransferWithMemo(data, receiver, memo);
    }
}
