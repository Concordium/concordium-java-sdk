package com.concordium.sdk.transactions;

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

    public RegisterData(Data data) {
        super(TransactionType.REGISTER_DATA);
        this.data = data;
    }

    public static RegisterData fromBytes(ByteBuffer source) {
        val data = Data.fromBytes(source);
        return new RegisterData(data);
    }

    @Override
    protected byte[] getPayloadBytes() {
        return data.getBytes();
    }
}
