package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Account tried to transfer an encrypted amount to itself, that's not allowed.
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class RejectReasonEncryptedAmountSelfTransfer extends RejectReason {
    private final AccountAddress address;

    @JsonCreator
    RejectReasonEncryptedAmountSelfTransfer(@JsonProperty("contents") AccountAddress address) {
        this.address = address;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.AccountAddress} to {@link RejectReasonEncryptedAmountSelfTransfer}.
     * @param encryptedAmountSelfTransfer {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link RejectReasonEncryptedAmountSelfTransfer}.
     */
    public static RejectReasonEncryptedAmountSelfTransfer parse(com.concordium.grpc.v2.AccountAddress encryptedAmountSelfTransfer) {
        return new RejectReasonEncryptedAmountSelfTransfer(AccountAddress.parse(encryptedAmountSelfTransfer));
    }

    @Override
    public RejectReasonType getType() {
        return RejectReasonType.ENCRYPTED_AMOUNT_SELF_TRANSFER;
    }
}
