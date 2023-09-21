package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
public final class RegisterData extends Payload {

    private final Data data;

    private RegisterData(Data data) {
        this.data = data;
    }

    static RegisterData createNew(Data data) {
        return new RegisterData(data);
    }

    public static RegisterData fromBytes(ByteBuffer source) {
        val data = Data.fromBytes(source);
        return new RegisterData(data);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.REGISTER_DATA;
    }

    @Override
    protected byte[] getRawPayloadBytes() {
        return data.getBytes();
    }

}
