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
    private final String contract_name;
    private final String method;

    @JsonCreator
    ReceiveName(String contract_name, String method) {
        this.contract_name = contract_name;
        this.method = method;
    }

    public static ReceiveName from(String contract_name, String method) {
        return new ReceiveName(contract_name, method);
    }

    public byte[] getBytes() {
        val receiveNameBuffer = (contract_name + "." + method).getBytes();
        val buffer = ByteBuffer.allocate(UInt16.BYTES + receiveNameBuffer.length);
        buffer.put(UInt16.from(receiveNameBuffer.length).getBytes());
        buffer.put(receiveNameBuffer);
        return buffer.array();
    }
}
