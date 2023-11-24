package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Some of the credential IDs already exist or are duplicated in the transaction.
 */
@Getter
@ToString
@Builder
public class RejectReasonDuplicateCredIDs extends RejectReason {
    private List<CredentialRegistrationId> duplicates;

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.DUPLICATE_CRED_IDS;
    }
}
