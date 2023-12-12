package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import lombok.Getter;

/**
 * Parameters not matching the provided {@link Schema}. Used in {@link SerializeParameterTest} to ensure serialization of incorrect parameters throws an exception.
 */
@Getter
public class IncorrectParams extends SchemaParameter {

    /**
     * Has no special meaning, just some value that does not adhere to the {@link Schema} provided in the test.
     */
    private final int value;

    protected IncorrectParams(Schema schema, ReceiveName receiveName) {
        super(schema, receiveName);
        value = 123;
    }
}
