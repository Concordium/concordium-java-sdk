package com.concordium.sdk.smartcontract;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter 'MintParams' in the <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft example</a>
 * Used for testing serialization of {@link com.concordium.sdk.types.AbstractAddress}
 */
@Getter
public class MintParams extends SchemaParameter {

    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private final AbstractAddress owner;
    private final List<String> tokens;

    public MintParams(Schema schema, ReceiveName receiveName, AbstractAddress owner, List<String> tokens) {
        super(schema, receiveName);
        this.owner = owner;
        this.tokens = tokens;
    }
}
