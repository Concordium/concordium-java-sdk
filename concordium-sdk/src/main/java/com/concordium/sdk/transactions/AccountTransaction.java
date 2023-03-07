package com.concordium.sdk.transactions;

import lombok.*;

import java.nio.ByteBuffer;

@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
public final class AccountTransaction extends AbstractAccountTransaction {

    AccountTransaction(TransactionSignature signature, TransactionHeader header, Payload payload) {
        super(header, signature, payload);
    }

    @Builder(builderMethodName = "builderBlockItem")
    public AccountTransaction(final @NonNull TransactionHeader header,
                              final @NonNull TransactionSignature signature,
                              @NonNull final byte[] payloadBytes) {
        super(header, signature, TransactionType.UNKNOWN_ACCOUNT, payloadBytes);
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
