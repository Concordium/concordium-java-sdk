package com.concordium.sdk.transactions;

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
 * The parameters are used for updating the smart contract instance.
 * i.e. calling a "receive" function exposed in the smart contract with the parameters.
 * Buffer of the parameters message.
 * For protocol versions below {@link ProtocolVersion#V5} the size is limited to 1kb.
 * From protocol version {@link ProtocolVersion#V5} and onwards the size is limited to be 64kb.
 */

@Getter
@ToString
@EqualsAndHashCode
public final class Parameter {
    public static final int MAX_SIZE = 65535;
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
        if (parameter.length > MAX_SIZE) {throw new IllegalArgumentException("Parameter must not exceed " + MAX_SIZE + " bytes, argument size was: " + parameter.length);}
        return new Parameter(parameter);
    }

    /**
     * Constructs a {@link Parameter} from the provided {@link SchemaParameter}.
     * Provided {@link SchemaParameter} must be initialized using {@link SchemaParameter#initialize()} beforehand.
     * @param param {@link SchemaParameter} to convert.
     * @return converted {@link Parameter}.
     */
    public static Parameter from(SchemaParameter param)  {return from(param.toBytes());}
}
