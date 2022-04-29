package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * A credential id that was to be removed is not part of the account.
 */
@ToString
public class RejectReasonNonExistentCredIDs extends RejectReason {
    @Getter
    private final List<String> ids;

    @JsonCreator
    RejectReasonNonExistentCredIDs(@JsonProperty("contents") List<String> ids) {
        this.ids = ids;
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
