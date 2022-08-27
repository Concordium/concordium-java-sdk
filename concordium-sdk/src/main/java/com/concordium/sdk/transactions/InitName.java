package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


@Getter
@ToString
public final class InitName {
    private final String name;

    @JsonCreator
    InitName(String name) {
        this.name = name;
    }

    public static InitName from(String name) {
        return new InitName(name);
    }

    public byte[] getBytes() {
        val initNameBuffer = name.getBytes();
        val buffer = ByteBuffer.allocate(UInt16.BYTES + initNameBuffer.length);
        buffer.put(UInt16.from(initNameBuffer.length).getBytes());
        buffer.put(initNameBuffer);
        return buffer.array();
    }
}
