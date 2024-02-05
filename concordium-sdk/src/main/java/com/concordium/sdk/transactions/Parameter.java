package com.concordium.sdk.transactions;

import com.concordium.sdk.requests.smartcontracts.InvokeInstanceRequest;
import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.UInt16;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.nio.ByteBuffer;


/**
 * The parameters are used for calling a smart contract instance.
 * i.e. calling a "init" or "receive" function exposed in the smart contract with the parameters.
 * This object retains the raw parameters that are sent to the contract.
 * For protocol versions below {@link ProtocolVersion#V5} the size is limited to 1kb.
 * From protocol version {@link ProtocolVersion#V5} and onwards the size is limited to be 64kb.
 */
@ToString
@EqualsAndHashCode
public final class Parameter {
    public static final int MAX_SIZE = 65535;
    public static final Parameter EMPTY = Parameter.from(new byte[0]);
    private final byte[] bytes;

    Parameter(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * This function takes a byte array and returns a Parameter object.
     */
    public static Parameter from(byte[] parameter) {
        if (parameter.length > MAX_SIZE) {throw new IllegalArgumentException("Parameter must not exceed " + MAX_SIZE + " bytes, argument size was: " + parameter.length);}
        return new Parameter(parameter);
    }

    /**
     * Get the serialized parameter, namely the length
     * of the parameter (encoded via 2 bytes, big endian) and concatenated with the
     * actual parameter bytes.
     * @return the serialized parameter
     */
    public byte[] getBytes() {
        val paramBuffer = this.bytes;
        val buffer = ByteBuffer.allocate(UInt16.BYTES + paramBuffer.length);
        buffer.put(UInt16.from(paramBuffer.length).getBytes());
        buffer.put(paramBuffer);
        return buffer.array();
    }

    /**
     * Get the parameter bytes for contract invocation i.e. off-chain operation.
     * This differs from {@link Parameter#getBytes()} as this does not prepend the
     * returned byte array with the length of the parameters.
     *
     * This function should be only be used for {@link com.concordium.sdk.ClientV2#invokeInstance(InvokeInstanceRequest)} calls.
     * @return the parameters
     */
    public byte[] getBytesForContractInvocation() {
        return this.bytes;
    }

    /**
     * Constructs a {@link Parameter} from the provided {@link SchemaParameter}.
     * Provided {@link SchemaParameter} must be initialized using {@link SchemaParameter#initialize()} beforehand.
     * @param param {@link SchemaParameter} to convert.
     * @return converted {@link Parameter}.
     */
    public static Parameter from(SchemaParameter param)  {return from(param.toBytes());}
}
