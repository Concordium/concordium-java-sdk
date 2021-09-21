package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;

public class RejectContentAccount extends RejectContent {
    @Getter
    private final AbstractAccount account;

    public RejectContentAccount(AbstractAccount account) {
        this.account = account;
    }
}
