package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * A simple account to account transfer occurred.
 * This is the result of a successful Transfer transaction
 */
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class AccountTransferResult implements AccountTransactionResult {

    /**
     * Amount that was transferred.
     */
    private CCDAmount amount;

    /**
     * Receiver account.
     */
    private AccountAddress receiver;

    /**
     * Parses {@link AccountTransactionEffects.AccountTransfer} to {@link AccountTransferResult}.
     * @param accountTransfer {@link AccountTransactionEffects.AccountTransfer} returned by the GRPC V2 API.
     * @return parsed {@link AccountTransferResult}
     */
    public static AccountTransferResult parse(AccountTransactionEffects.AccountTransfer accountTransfer) {

        return AccountTransferResult.builder()
                .amount(CCDAmount.fromMicro(accountTransfer.getAmount().getValue()))
                .receiver(AccountAddress.parse(accountTransfer.getReceiver()))
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.TRANSFER;
    }
}
