package com.concordium.sdk.examples.smartcontracts.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
import com.concordium.sdk.types.ContractAddress;
import lombok.Getter;

import java.util.List;

/**
 * TODO
 */
@Getter
public class SetImplementorsParams extends SchemaParameter {

    private final String id;
    private final List<ContractAddress> implementors;

    public SetImplementorsParams(Schema schema, ReceiveName receiveName, String id, List<ContractAddress> implementors) {
        super(schema, receiveName);
        this.id = id;
        this.implementors = implementors;
    }
}
