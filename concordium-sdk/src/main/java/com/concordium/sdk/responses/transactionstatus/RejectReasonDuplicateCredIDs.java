package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class RejectReasonDuplicateCredIDs extends RejectReasonContent {
    private final List<String> duplicates;

    @JsonCreator
    RejectReasonDuplicateCredIDs(@JsonProperty("duplicates") List<String> duplicates) {
        this.duplicates = duplicates;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_CRED_IDS;
    }
}
