package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class RejectReasonDuplicateCredIDs extends RejectReason {
    private final List<String> duplicates;

    @JsonCreator
    RejectReasonDuplicateCredIDs(@JsonProperty("contents") List<String> duplicates) {
        this.duplicates = duplicates;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_CRED_IDS;
    }
}
