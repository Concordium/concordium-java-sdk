package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.GTUAmount;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// These types must correspond the 'RejectReason' types found here
// https://github.com/Concordium/concordium-base/blob/f0e1275dfde8502e3aabdb153b3246c18bee59f9/haskell-src/Concordium/Types/Execution.hs#L737
// This is not simply an enum because sometimes the contents holds extra information than simply just the `tag`.

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RejectReason {
    //private final RejectReasonType tag;
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "tag", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RejectReasonAmountTooLarge.class, name = "AmountTooLarge"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateAggregationKey.class, name = "DuplicateAggregationKey"),
            @JsonSubTypes.Type(value = RejectReasonBakerInCooldown.class, name = "BakerInCooldown"),
            @JsonSubTypes.Type(value = RejectReasonSerializationFailure.class, name = "SerializationFailure"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountReference.class, name = "InvalidAccountReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidEncryptedAmountTransferProof.class, name = "InvalidEncryptedAmountTransferProof"),
            @JsonSubTypes.Type(value = RejectReasonStakeUnderMinimumThresholdForBaking.class, name = "StakeUnderMinimumThresholdForBaking"),
            @JsonSubTypes.Type(value = RejectReasonModuleNotWF.class, name = "ModuleNotWF"),
            @JsonSubTypes.Type(value = RejectReasonRejectedReceive.class, name = "RejectedReceive"),
            @JsonSubTypes.Type(value = RejectReasonInsufficientBalanceForBakerStake.class, name = "InsufficientBalanceForBakerStake"),
            @JsonSubTypes.Type(value = RejectReasonModuleHashAlreadyExists.class, name = "ModuleHashAlreadyExists"),
            @JsonSubTypes.Type(value = RejectReasonOutOfEnergy.class, name = "OutOfEnergy"),
            @JsonSubTypes.Type(value = RejectReasonInvalidReceiveMethod.class, name = "InvalidReceiveMethod"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToReceiveEncrypted.class, name = "NotAllowedToReceiveEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonInvalidInitMethod.class, name = "InvalidInitMethod"),
            @JsonSubTypes.Type(value = RejectReasonRejectedInit.class, name = "RejectedInit"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentials.class, name = "InvalidCredentials"),
            @JsonSubTypes.Type(value = RejectReasonInvalidTransferToPublicProof.class, name = "InvalidTransferToPublicProof"),
            @JsonSubTypes.Type(value = RejectReasonZeroScheduledAmount.class, name = "ZeroScheduledAmount"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedMultipleCredentials.class, name = "NotAllowedMultipleCredentials"),
            @JsonSubTypes.Type(value = RejectReasonNotAllowedToHandleEncrypted.class, name = "NotAllowedToHandleEncrypted"),
            @JsonSubTypes.Type(value = RejectReasonDuplicateCredIDs.class, name = "DuplicateCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonFirstScheduledReleaseExpired.class, name = "FirstScheduledReleaseExpired"),
            @JsonSubTypes.Type(value = RejectReasonEncryptedAmountSelfTransfer.class, name = "EncryptedAmountSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonInvalidModuleReference.class, name = "InvalidModuleReference"),
            @JsonSubTypes.Type(value = RejectReasonInvalidContractAddress.class, name = "InvalidContractAddress"),
            @JsonSubTypes.Type(value = RejectReasonRuntimeFailure.class, name = "RuntimeFailure"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentRewardAccount.class, name = "NonExistentRewardAccount"),
            @JsonSubTypes.Type(value = RejectReasonInvalidProof.class, name = "InvalidProof"),
            @JsonSubTypes.Type(value = RejectReasonAlreadyABaker.class, name = "AlreadyABaker"),
            @JsonSubTypes.Type(value = RejectReasonNotABaker.class, name = "NotABaker"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredentialID.class, name = "NonExistentCredentialID"),
            @JsonSubTypes.Type(value = RejectReasonKeyIndexAlreadyInUse.class, name = "KeyIndexAlreadyInUse"),
            @JsonSubTypes.Type(value = RejectReasonInvalidAccountThreshold.class, name = "InvalidAccountThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidCredentialKeySignThreshold.class, name = "InvalidCredentialKeySignThreshold"),
            @JsonSubTypes.Type(value = RejectReasonInvalidIndexOnEncryptedTransfer.class, name = "InvalidIndexOnEncryptedTransfer"),
            @JsonSubTypes.Type(value = RejectReasonNonIncreasingSchedule.class, name = "NonIncreasingSchedule"),
            @JsonSubTypes.Type(value = RejectReasonScheduledSelfTransfer.class, name = "ScheduledSelfTransfer"),
            @JsonSubTypes.Type(value = RejectReasonNonExistentCredIDs.class, name = "NonExistentCredIDs"),
            @JsonSubTypes.Type(value = RejectReasonRemoveFirstCredential.class, name = "RemoveFirstCredential"),
            @JsonSubTypes.Type(value = RejectReasonCredentialHolderDidNotSign.class, name = "CredentialHolderDidNotSign")
    })
    @JsonDeserialize(using = CustomDeserializer.class)
    private final RejectReasonContent contents;

    @JsonCreator
    RejectReason(
            //@JsonProperty("tag") RejectReasonType tag,
            @JsonProperty("contents") RejectReasonContent contents) {
        //this.tag = tag;
        this.contents = contents;

    }

    static class CustomDeserializer extends StdDeserializer<RejectReasonContent> {
        protected CustomDeserializer() {
            super(CustomDeserializer.class);
        }

        @Override
        public RejectReasonContent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            val result = new RejectReasonAmountTooLargeEntry();
            if (p.currentToken() == JsonToken.START_ARRAY) {
                val list = p.readValueAs(List.class);
                for (Object item : list) {
                    if (item instanceof String) {
                        result.add(new RejectReasonAmountTooLargeEntryAmount(GTUAmount.fromMicro((String) item)));
                    } if (item instanceof Map) {
                        val account = AbstractAccount.parseAccount((Map<String, Object>) item);
                        result.add(new RejectReasonAmountTooLargeEntryAccount(account));
                    } else {
                        throw new JsonMappingException(p, "Unable to parse RejectReasonContent");
                    }
                }
            }
            return result;
        }
    }
}
