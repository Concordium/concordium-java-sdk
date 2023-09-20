package com.concordium.sdk.examples.contractexample.wccd;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import lombok.Getter;

@Getter
public class SetPausedParams extends SchemaParameter {

    private final boolean paused;
    public SetPausedParams(Schema schema, ReceiveName receiveName, boolean paused) {
        super(schema, receiveName);
        this.paused = paused;
    }
}
