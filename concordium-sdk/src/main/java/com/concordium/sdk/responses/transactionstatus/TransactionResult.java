package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public final class TransactionResult {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tag")
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
            @JsonSubTypes.Type(value = AmountAddedByDecryptionResult.class, name = "AmountAddedByDecryption"),
            @JsonSubTypes.Type(value = EncryptedSelfAmountAddedResult.class, name = "EncryptedSelfAmountAdded"),
            @JsonSubTypes.Type(value = UpdateEnqueuedResult.class, name = "UpdateEnqueued"),
            @JsonSubTypes.Type(value = TransferredWithScheduleResult.class, name = "TransferredWithSchedule"),
            @JsonSubTypes.Type(value = CredentialsUpdatedResult.class, name = "CredentialsUpdated"),
            @JsonSubTypes.Type(value = DataRegisteredResult.class, name = "DataRegistered"),
            @JsonSubTypes.Type(value = TransferredResult.class, name = "Transferred"),
            @JsonSubTypes.Type(value = TransferMemoResult.class, name = "TransferMemo"),
            @JsonSubTypes.Type(value = EncryptedAmountsRemovedResult.class, name = "EncryptedAmountsRemoved"),
            @JsonSubTypes.Type(value = ContractUpdated.class, name = "Updated")
    })
    private final List<TransactionResultEvent> events;
    private final Outcome outcome;
    private final RejectReason rejectReason;

    @JsonCreator
    TransactionResult(@JsonProperty("events") List<TransactionResultEvent> events,
                      @JsonProperty("outcome") Outcome outcome,
                      @JsonProperty("rejectReason") RejectReason rejectReason) {
        this.events = events;
        this.outcome = outcome;
        this.rejectReason = rejectReason;
    }
}
