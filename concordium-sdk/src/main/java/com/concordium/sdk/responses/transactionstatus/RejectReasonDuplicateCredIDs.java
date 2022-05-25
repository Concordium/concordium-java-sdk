package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Some of the credential IDs already exist or are duplicated in the transaction.
 */
@Getter
@ToString
public class RejectReasonDuplicateCredIDs extends RejectReason {
    private List<CredentialRegistrationId> duplicates;

    @JsonCreator
    RejectReasonDuplicateCredIDs(@JsonProperty("contents") List<String> duplicates) {
        if(!Objects.isNull(duplicates)) {
            this.duplicates = new ArrayList<>();
            for (String duplicate : duplicates) {
                this.duplicates.add(CredentialRegistrationId.from(duplicate));
            }
        }
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_CRED_IDS;
    }
}
