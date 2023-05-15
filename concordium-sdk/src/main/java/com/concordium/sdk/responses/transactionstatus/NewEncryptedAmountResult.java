package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.NewEncryptedAmountEvent;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString
@Builder
public final class NewEncryptedAmountResult extends TransactionResultEvent {
    private final AccountAddress account;
    private final String newIndex;
    private final String encryptedAmount;


    @JsonCreator
    NewEncryptedAmountResult(@JsonProperty("account") AccountAddress account,
                             @JsonProperty("newIndex") String newIndex,
                             @JsonProperty("encryptedAmount") String encryptedAmount) {

        this.account = account;
        this.newIndex = newIndex;
        this.encryptedAmount = encryptedAmount;
    }

    public static NewEncryptedAmountResult parse(NewEncryptedAmountEvent added) {
        return NewEncryptedAmountResult.builder()
                .account(AccountAddress.from(added.getReceiver().getValue().toByteArray()))
                .newIndex(String.valueOf(added.getNewIndex()))
                .encryptedAmount(Hex.encodeHexString(added.getEncryptedAmount().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.NEW_ENCRYPTED_AMOUNT;
    }
}
