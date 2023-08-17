package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
import lombok.Getter;

@Getter
public class IncorrectParams extends SchemaParameter {

    private final int value;

    protected IncorrectParams(Schema schema, ReceiveName receiveName) {
        super(schema, receiveName);
        value = 123;
    }
}
