package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} representing '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1327">SupportsQueryParams</a> used in different smart contracts
 */
public class SupportsQueryParams extends ListParam {
    public SupportsQueryParams(Schema cis2nftSchema, ReceiveName supportsReceiveName, List<String> identifiers) {
        super(cis2nftSchema, supportsReceiveName, identifiers);
    }
}
