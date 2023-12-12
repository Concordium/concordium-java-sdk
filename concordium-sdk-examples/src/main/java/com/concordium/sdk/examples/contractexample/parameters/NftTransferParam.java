package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} enforcing the correct {@link Cis2Transfer} id for cis2-nft contract.
 */
public class NftTransferParam extends ListParam {
    public NftTransferParam(Schema cis2nftSchema, ReceiveName nftTransferReceiveName, List<NftTransfer> transfers) {
        super(cis2nftSchema, nftTransferReceiveName, transfers);
    }
}
