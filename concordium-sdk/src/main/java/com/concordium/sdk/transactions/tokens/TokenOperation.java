package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.transactions.TokenUpdate;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A protocol-level token (PLT) operation used in {@link TokenUpdate}.
 * <p>
 * To deserialize an operation from CBOR, always use <code>TokenOperation.class</code>
 * even if you know the exact type.
 *
 * @see TransferTokenOperation TransferTokenOperation
 * as an example of a CBOR-serializable token operation
 */
@JsonTypeInfo(
        // For CBOR serialization, wrap the operation with a single field object
        // where the field name is the operation type.
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        // Use subtype name as the operation type.
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        // All the operations must be defined here.
        // If you use @JsonTypeName in the operation class instead,
        // deserializer won't work.
        @JsonSubTypes.Type(
                value = TransferTokenOperation.class,
                name = TransferTokenOperation.TYPE
        )
})
public interface TokenOperation {

    /**
     * @return operation type name, e.g. "transfer", "mint", etc.
     */
    @JsonIgnore
    String getType();

    /**
     * @return the base energy cost of this operation.
     */
    @JsonIgnore
    UInt64 getBaseCost();
}
