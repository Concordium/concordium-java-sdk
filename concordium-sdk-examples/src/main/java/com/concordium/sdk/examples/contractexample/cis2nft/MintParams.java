package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.AbstractAddress;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.List;

/**
 * Represents the parameter 'MintParams' in the <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft example</a>
 * Used with contract function 'mint' which mints a number of tokens to a given {@link AbstractAddress}.
 */
@Getter
public class MintParams extends SchemaParameter {

    /**
     * Owner of the newly minted tokens.
     */
    @JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)
    private final AbstractAddress owner;
    /**
     * A collection of tokens to mint.
     */
    private final List<TokenIdU32> tokens;

    public MintParams(Schema schema, ReceiveName receiveName, AbstractAddress owner, List<TokenIdU32> tokens) {
        super(schema, receiveName);
        this.owner = owner;
        this.tokens = tokens;
    }
}
