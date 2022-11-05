package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


/**
 * A receive name (owned version). Expected format:
 * "<contract_name>.<method_name>".
 */

@Getter
@ToString
public final class ReceiveName {
    /**
     * The name of the contract.
     */
    private final String contractName;
    /**
     * The entrypoint name of the contract. The name of the function consists solely of ASCII
     * alphanumeric or punctuation characters.
     */
    private final String method;

    @JsonCreator
    ReceiveName(String contractName, String method) {
        this.contractName = contractName;
        this.method = method;
    }

    /**
     * This function takes a contract name and a method name and returns a ReceiveName object.
     *
     * @param contractName The name of the contract.
     * @param method       The name of the method to be called.
     * @return A new instance of the ReceiveName class.
     */
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
