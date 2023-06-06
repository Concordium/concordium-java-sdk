package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedAmountRemovedEvent;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

/**
 * An encrypted amount was consumed from the account.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public final class EncryptedAmountsRemovedResult extends TransactionResultEvent {

    /**
     * The index indicating which amounts were used.
     */
    private final UInt64 upToIndex;

    /**
     * The affected account.
     */
    private final AccountAddress account;

    /**
     * The encrypted amount that was removed.
     */
    private final String inputAmount;

    /**
     * The new self encrypted amount on the affected account.
     */
    private final String newAmount;

    @JsonCreator
    EncryptedAmountsRemovedResult(@JsonProperty("upToIndex") long upToIndex,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("inputAmount") String inputAmount,
                                  @JsonProperty("newAmount") String newAmount) {
        this.upToIndex = UInt64.from(upToIndex);
        this.account = account;
        this.inputAmount = inputAmount;
        this.newAmount = newAmount;
    }

    /**
     * Parses {@link EncryptedAmountRemovedEvent} to {@link EncryptedAmountsRemovedResult}.
     * @param removed {@link EncryptedAmountRemovedEvent} returned by the GRPC V2 API.
     * @return parsed {@link EncryptedAmountsRemovedResult}
     */
    public static EncryptedAmountsRemovedResult parse(EncryptedAmountRemovedEvent removed) {
        return EncryptedAmountsRemovedResult.builder()
                .upToIndex(UInt64.from(removed.getUpToIndex()))
                .account(AccountAddress.parse(removed.getAccount()))
                .inputAmount(Hex.encodeHexString(removed.getInputAmount().getValue().toByteArray()))
                .newAmount(Hex.encodeHexString(removed.getNewAmount().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ENCRYPTED_AMOUNTS_REMOVED;
    }
}
