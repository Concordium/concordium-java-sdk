package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import lombok.Getter;

@Getter
public class IncorrectParams extends SchemaParameter {

    private final int value;

    protected IncorrectParams(Schema schema, ReceiveName receiveName) {
        super(schema, receiveName);
        value = 123;
    }
}
