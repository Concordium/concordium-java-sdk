package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


/**
 * The parameters are used for updating the smart contract instance.
 * i.e. calling a recieve function exposed in the smart contract with the parameters.
 * Buffer of the parameters message.
 * In the current supported protocols the size is limited to be 1kb.
 */

@Getter
@ToString
public final class Parameter {
    public static final Parameter EMPTY = Parameter.from(new byte[0]);
    private final byte[] bytes;

    @JsonCreator
    Parameter(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * This function takes a byte array and returns a Parameter object.
     */
    public static Parameter from(byte[] parameter) {
        return new Parameter(parameter);
    }

    /**
     * @return buffer bytes of {@link Parameter}.
     */
    public byte[] getBytes() {
        val paramBuffer = this.bytes;
        val buffer = ByteBuffer.allocate(UInt16.BYTES + paramBuffer.length);
        buffer.put(UInt16.from(paramBuffer.length).getBytes());
        buffer.put(paramBuffer);
        return buffer.array();
    }
}
