package com.concordium.sdk.responses.transactionstatus;

import lombok.Getter;

public class RejectContentString extends RejectContent {
    @Getter
    private final String value;

    RejectContentString(String value) {
        this.value = value;
    }
}
