package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class RejectReasonNonExistentCredIDs extends RejectReason {
    @Getter
    private List<CredentialRegistrationId> ids;

    @JsonCreator
    RejectReasonNonExistentCredIDs(@JsonProperty("contents") List<String> ids) {
        if (!Objects.isNull(ids)) {
            this.ids = new ArrayList<>();
            for (String id : ids) {
                this.ids.add(CredentialRegistrationId.from(id));
            }
        }
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.NON_EXISTENT_CREDENTIAL_ID;
    }
}
