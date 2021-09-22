package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

public class RejectReasonNonExistentCredIDs extends RejectReasonContent {
    @Getter
    private final List<String> ids;

    @JsonCreator
    RejectReasonNonExistentCredIDs(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
