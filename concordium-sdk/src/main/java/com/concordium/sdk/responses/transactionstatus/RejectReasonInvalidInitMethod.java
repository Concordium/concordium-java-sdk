package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class RejectReasonInvalidInitMethod extends RejectReason {
    private final String moduleRef;
    private final String initName;

    @JsonCreator
    RejectReasonInvalidInitMethod(@JsonProperty("contents") List<String> contents) {
        if (contents.size() != 2) {
            throw new IllegalArgumentException("Unable to parse RejectReasonInvalidInitMethod. Unexpected contents size.");
        }
        this.initName = contents.get(0);
        this.moduleRef = contents.get(1);

    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.INVALID_INIT_METHOD;
    }
}
