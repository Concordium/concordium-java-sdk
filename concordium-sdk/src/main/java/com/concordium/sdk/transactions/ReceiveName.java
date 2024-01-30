package com.concordium.sdk.transactions;

import com.concordium.sdk.types.UInt16;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
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

    /**
     * Create a {@link ReceiveName} from a {@link InitName}
     * @param initName the {@link InitName}
     * @param endpoint name of the receive endpoint
     * @return a {@link ReceiveName}
     */
    public static ReceiveName from(InitName initName, String endpoint) {
        return new ReceiveName(initName.getName().split("init_")[1], endpoint);
    }

    public static ReceiveName parse(final String value) {
        val parts = value.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("name should be in format <ContractName>.<MethodName>");
        }

        return from(parts[0], parts[1]);
    }

    public byte[] getBytes() {
        val receiveNameBuffer = (contractName + "." + method).getBytes();
        val buffer = ByteBuffer.allocate(UInt16.BYTES + receiveNameBuffer.length);
        buffer.put(UInt16.from(receiveNameBuffer.length).getBytes());
        buffer.put(receiveNameBuffer);
        return buffer.array();
    }
}
