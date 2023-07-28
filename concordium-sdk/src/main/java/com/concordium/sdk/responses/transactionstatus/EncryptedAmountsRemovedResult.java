package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedAmountRemovedEvent;
import com.concordium.sdk.transactions.EncryptedAmount;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public final class EncryptedAmountsRemovedResult implements TransactionResultEvent {
    private final long upToIndex;
    private final AccountAddress account;
    private final EncryptedAmount inputAmount;
    private final EncryptedAmount newAmount;

    @JsonCreator
    EncryptedAmountsRemovedResult(@JsonProperty("upToIndex") long upToIndex,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("inputAmount") String inputAmount,
                                  @JsonProperty("newAmount") String newAmount) {
        this.upToIndex = upToIndex;
        this.account = account;
        this.inputAmount = EncryptedAmount.from(inputAmount);
        this.newAmount = EncryptedAmount.from(newAmount);
    }

    public static EncryptedAmountsRemovedResult from(EncryptedAmountRemovedEvent removed) {
        return EncryptedAmountsRemovedResult
                .builder()
                .account(AccountAddress.from(removed.getAccount()))
                .inputAmount(EncryptedAmount.from(removed.getInputAmount()))
                .newAmount(EncryptedAmount.from(removed.getNewAmount()))
                .upToIndex(removed.getUpToIndex())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ENCRYPTED_AMOUNTS_REMOVED;
    }
}
