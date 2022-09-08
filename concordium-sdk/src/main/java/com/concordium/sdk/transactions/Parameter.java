package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;

@Getter
@ToString
public final class Parameter {
    private final byte[] bytes;

    @JsonCreator
    Parameter(byte[] bytes) {
        this.bytes = bytes;
    }

    public static Parameter from(byte[] parameter) {
        return new Parameter(parameter);
    }

    public int getSize() {
        return bytes.length;
    }

    public byte[] getBytes() {
        val param_buffer = this.bytes;
        val buffer = ByteBuffer.allocate(UInt16.BYTES + param_buffer.length);
        buffer.put(UInt16.from(param_buffer.length).getBytes());
        buffer.put(param_buffer);
        return buffer.array();
    }
}
