package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.parameters.ListParam;

import java.util.List;

/**
 * Wrapper class for {@link ListParam} enforcing the correct {@link Transfer} id for cis2-wccd contract.
 */
public class WCCDTransferParam extends ListParam {
    public WCCDTransferParam(Schema schema, ReceiveName receiveName, List<WCCDTransfer> transfers) {
        super(schema, receiveName, transfers);
    }
}
