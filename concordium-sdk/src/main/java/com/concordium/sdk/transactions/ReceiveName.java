package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


@Getter
@ToString
public final class ReceiveName {
    private final String contractName;
    private final String method;

    @JsonCreator
    ReceiveName(String contractName, String method) {
        this.contractName = contractName;
        this.method = method;
    }

    public static ReceiveName from(String contractName, String method) {
        return new ReceiveName(contractName, method);
    }

    public byte[] getBytes() {
        val receiveNameBuffer = (contractName + "." + method).getBytes();
        val buffer = ByteBuffer.allocate(UInt16.BYTES + receiveNameBuffer.length);
        buffer.put(UInt16.from(receiveNameBuffer.length).getBytes());
        buffer.put(receiveNameBuffer);
        return buffer.array();
    }
}
