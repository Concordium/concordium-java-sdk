package com.concordium.sdk.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString
public final class AccountTransaction extends AbstractTransaction {

    /**
     * The type of the {@link Payload} associated with this {@link AccountTransaction}.
     */
    private final Payload.PayloadType type;

    AccountTransaction(TransactionSignature signature, TransactionHeader header, Payload payload) {
        super(BlockItemType.ACCOUNT_TRANSACTION, header, signature, payload);
        this.type = payload.getType();
    }

    public static AccountTransaction fromBytes(ByteBuffer source) {
        val signature = TransactionSignature.fromBytes(source);
        val header = TransactionHeader.fromBytes(source);
        byte tag = source.get();
        Payload payload;
        switch (tag) {
            case 3:
                payload = Transfer.fromBytes(source);
                break;
            case 21:
                payload = RegisterData.fromBytes(source);
                break;
            case 22:
                payload = TransferWithMemo.fromBytes(source);
                break;
            default:
                throw new UnsupportedOperationException("Only transfers and transfers with memo are currently supported.");
        }
        return new AccountTransaction(signature, header, payload);
    }
}
