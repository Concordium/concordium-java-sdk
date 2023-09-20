package com.concordium.sdk.examples.contractexample.wccd;


import com.concordium.sdk.examples.contractexample.cis2nft.Receiver;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.UInt8;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.List;

@Getter
public class UnwrapParams extends SchemaParameter {

    private final String amount;
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private final AbstractAddress owner;
    private final Receiver receiver;
    private final List<UInt8> data;



    public UnwrapParams(Schema schema, ReceiveName receiveName, String amount, AbstractAddress owner, Receiver receiver, List<UInt8> data) {
        super(schema, receiveName);
        this.amount = amount;
        this.owner = owner;
        this.receiver = receiver;
        this.data = data;
    }
}
