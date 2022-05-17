package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A credential id that was to be removed is not part of the account.
 */
@ToString
public class RejectReasonNonExistentCredIDs extends RejectReason {
    @Getter
    private final List<CredentialRegistrationId> ids;

    @JsonCreator
    RejectReasonNonExistentCredIDs(@JsonProperty("contents") List<String> ids) {
        this.ids = new ArrayList<>();
        for (String id : ids) {
            this.ids.add(CredentialRegistrationId.from(id));
        }
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
