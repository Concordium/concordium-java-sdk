package com.concordium.sdk.examples.smartcontracts.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.transactions.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter 'MintParams' in the <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft example</a>
 */
@Getter
public class MintParams extends SchemaParameter {
    private final AbstractAddress owner;

    private final List<String> tokens;

    public MintParams(Schema schema, ReceiveName receiveName, AbstractAddress owner, List<String> tokens) {
        super(schema, receiveName);
        this.owner = owner;
        this.tokens = tokens;
    }
}
