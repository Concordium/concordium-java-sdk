package com.concordium.sdk.responsetypes.transactionstatus;

import com.concordium.sdk.responsetypes.transactionstatus.results.*;
import com.concordium.sdk.responsetypes.transactionstatus.results.baker.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TransactionResult {
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property = "tag")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CredentialDeployedResult.class, name = "CredentialDeployed"),
            @JsonSubTypes.Type(value = ModuleCreatedResult.class, name = "ModuleDeployed"),
            @JsonSubTypes.Type(value = ContractInitializedResult.class, name = "ContractInitialized"),
            @JsonSubTypes.Type(value = AccountCreatedResult.class, name = "AccountCreated"),
            @JsonSubTypes.Type(value = BakerAddedResult.class, name = "BakerAdded"),
            @JsonSubTypes.Type(value = BakerRemovedResult.class, name = "BakerRemoved"),
            @JsonSubTypes.Type(value = BakerStakeIncreasedResult.class, name = "BakerStakeIncreased"),
            @JsonSubTypes.Type(value = BakerStakeDecreasedResult.class, name = "BakerStakeDecreased"),
            @JsonSubTypes.Type(value = BakerSetRestakeEarningsResult.class, name = "BakerSetRestakeEarnings"),
            @JsonSubTypes.Type(value = BakerKeysUpdatedResult.class, name = "BakerKeysUpdated"),
            @JsonSubTypes.Type(value = CredentialKeysUpdatedResult.class, name = "CredentialKeysUpdated"),
            @JsonSubTypes.Type(value = NewEncryptedAmountResult.class, name = "NewEncryptedAmount"),
            @JsonSubTypes.Type(value = EncryptedAmountRemovedResult.class, name = "EncryptedAmountRemoved"),
            @JsonSubTypes.Type(value = AmountAddedByDecryptionResult.class, name = "AmountAddedByDecryption"),
            @JsonSubTypes.Type(value = EncryptedSelfAmountAddedResult.class, name = "EncryptedSelfAmountAdded"),
            @JsonSubTypes.Type(value = UpdateEnqueuedResult.class, name = "UpdateEnqueued"),
            @JsonSubTypes.Type(value = TransferredWithScheduleResult.class, name = "TransferredWithSchedule"),
            @JsonSubTypes.Type(value = CredentialsUpdatedResult.class, name = "CredentialsUpdated"),
            @JsonSubTypes.Type(value = DataRegisteredResult.class, name = "DataRegistered"),
            @JsonSubTypes.Type(value = TransferredResult.class, name = "Transferred"),
            @JsonSubTypes.Type(value = TransferMemoResult.class, name = "TransferMemo"),
            @JsonSubTypes.Type(value = EncryptedAmountsRemovedResult.class, name = "EncryptedAmountsRemoved"),
    })
    private List<TransactionResultEvent> events;
    private Outcome outcome;
    private RejectReason rejectReason;
}
