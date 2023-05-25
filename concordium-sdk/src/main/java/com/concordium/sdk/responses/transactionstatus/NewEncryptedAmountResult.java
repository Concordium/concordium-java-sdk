package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.NewEncryptedAmountEvent;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString
@AllArgsConstructor
@Builder
// Event generated when an account receives a new encrypted amount.
public final class NewEncryptedAmountResult implements TransactionResultEvent {
    private final AccountAddress account;
    private final UInt64 newIndex;
    private final String encryptedAmount;


    @JsonCreator
    NewEncryptedAmountResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newIndex") long newIndex,
                             @JsonProperty("encryptedAmount") String encryptedAmount) {

        this.account = account;
        this.newIndex = UInt64.from(newIndex);
        this.encryptedAmount = encryptedAmount;
    }

    public static NewEncryptedAmountResult parse(NewEncryptedAmountEvent added) {
        return NewEncryptedAmountResult.builder()
                .account(AccountAddress.parse(added.getReceiver()))
                .newIndex(UInt64.from(added.getNewIndex()))
                .encryptedAmount(Hex.encodeHexString(added.getEncryptedAmount().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.NEW_ENCRYPTED_AMOUNT;
    }
}
