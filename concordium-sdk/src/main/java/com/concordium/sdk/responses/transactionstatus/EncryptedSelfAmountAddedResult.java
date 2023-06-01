package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.EncryptedSelfAmountAddedEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

/**
 * An account encrypted an amount from public to encrypted balance.
 */
@Getter
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public final class EncryptedSelfAmountAddedResult implements TransactionResultEvent, AccountTransactionResult {

    /**
     * The amount that was transferred from public to encrypted balance.
     */
    private final CCDAmount amount;

    /**
     * The affected account.
     */
    private final AccountAddress account;

    /**
     * The new self encrypted amount of the account.
     */
    private final String newAmount;

    @JsonCreator
    EncryptedSelfAmountAddedResult(@JsonProperty("amount") String amount,
                                   @JsonProperty("account") AccountAddress account,
                                   @JsonProperty("newAmount") String newAmount) {
        this.amount = CCDAmount.fromMicro(amount);
        this.account = account;
        this.newAmount = newAmount;
    }

    /**
     * Parses {@link EncryptedSelfAmountAddedEvent} to {@link EncryptedSelfAmountAddedResult}.
     * @param transferredToEncrypted {@link EncryptedSelfAmountAddedEvent} returned by the GRPC V2 API.
     * @return parsed {@link EncryptedSelfAmountAddedEvent}.
     */
    public static EncryptedSelfAmountAddedResult parse(EncryptedSelfAmountAddedEvent transferredToEncrypted) {
        return EncryptedSelfAmountAddedResult.builder()
                .amount(CCDAmount.fromMicro(transferredToEncrypted.getAmount().getValue()))
                .account(AccountAddress.parse(transferredToEncrypted.getAccount()))
                .newAmount(Hex.encodeHexString(transferredToEncrypted.getNewAmount().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.ENCRYPTED_SELF_AMOUNT_ADDED;
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.TRANSFER_TO_ENCRYPTED;
    }
}
