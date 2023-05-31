package com.concordium.sdk.responses.transactionstatus;


import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class AmountAddedByDecryptionResult implements TransactionResultEvent {

    /**
     * The amount transferred from encrypted to public balance.
     */
    private final CCDAmount amount;

    /**
     * The affected account.
     */
    private final AccountAddress account;

    @JsonCreator
    AmountAddedByDecryptionResult(@JsonProperty("amount") String amount,
                                  @JsonProperty("account") AccountAddress account) {
        this.amount = CCDAmount.fromMicro(amount);
        this.account = account;
    }

    /**
     * Parses {@link AccountTransactionEffects.TransferredToPublic} to {@link AmountAddedByDecryptionResult}.
     * @param transferredToPublic {@link AccountTransactionEffects.TransferredToPublic} returned by the GRPC V2 API.
     * @return parsed {@link AmountAddedByDecryptionResult}.
     */
    public static AmountAddedByDecryptionResult parse(AccountTransactionEffects.TransferredToPublic transferredToPublic) {
        val removed = transferredToPublic.getRemoved();
        return AmountAddedByDecryptionResult.builder()
                .amount(CCDAmount.fromMicro(transferredToPublic.getAmount().getValue()))
                .account(AccountAddress.parse(removed.getAccount()))
                .build();

    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.AMOUNT_ADDED_BY_DECRYPTION;
    }
}
