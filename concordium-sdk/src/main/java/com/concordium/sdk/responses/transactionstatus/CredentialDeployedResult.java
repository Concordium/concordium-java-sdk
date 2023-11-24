package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public final class CredentialDeployedResult implements TransactionResultEvent {
    private final CredentialRegistrationId regId;
    private final AccountAddress account;

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.CREDENTIAL_DEPLOYED;
    }
}
