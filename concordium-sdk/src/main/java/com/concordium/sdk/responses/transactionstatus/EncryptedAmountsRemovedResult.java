package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedAmountRemovedEvent;
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
public final class EncryptedAmountsRemovedResult extends TransactionResultEvent {
    private final int upToIndex;
    private final AccountAddress account;
    private final String inputAmount;
    private final String newAmount;

    @JsonCreator
    EncryptedAmountsRemovedResult(@JsonProperty("upToIndex") int upToIndex,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("inputAmount") String inputAmount,
                                  @JsonProperty("newAmount") String newAmount) {
        this.upToIndex = upToIndex;
        this.account = account;
        this.inputAmount = inputAmount;
        this.newAmount = newAmount;
    }

    public static EncryptedAmountsRemovedResult parse(EncryptedAmountRemovedEvent removed) {
        return EncryptedAmountsRemovedResult.builder()
                .upToIndex((int) removed.getUpToIndex())
                .account(AccountAddress.from(removed.getAccount().getValue().toByteArray()))
                .inputAmount(Hex.encodeHexString(removed.getInputAmount().getValue().toByteArray()))
                .newAmount(Hex.encodeHexString(removed.getNewAmount().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ENCRYPTED_AMOUNTS_REMOVED;
    }
}
