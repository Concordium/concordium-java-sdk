package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt64;
import com.concordium.sdk.types.UInt16;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;
import java.util.Objects;

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

    @Override
    public PayloadType getType() {
        return PayloadType.REGISTER_DATA;
    }

    @Override
    byte[] getBytes() {
        val buffer = ByteBuffer.allocate(
                TransactionType.BYTES +
                        data.getLength());
        buffer.put(TransactionType.REGISTER_DATA.getValue());
        buffer.put(data.getBytes());
        return buffer.array();
    }
    

    public static RegisterData fromBytes(ByteBuffer source) {
        val data = Data.fromBytes(source);
        return new RegisterData(data);
    }

    @Override
    UInt64 getTransactionTypeCost() {
        return BASE_ENERGY_COST;
    }

    private final static UInt64 BASE_ENERGY_COST = UInt64.from(300);
}
