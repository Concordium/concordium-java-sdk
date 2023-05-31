package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.*;

/**
 * Details about an account transaction.
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class AccountTransactionDetails {

    /**
     * The cost of the transaction. Paid by the sender.
     */
    private CCDAmount amount;

    /**
     * The sender of the transaction.
     */
    private AccountAddress sender;

    /**
     * The result of the transaction.
     */
    private AccountTransactionResult result;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountTransactionDetails} to {@link AccountCreationDetails}.
     * @param accountTransaction {@link AccountTransactionDetails} returned by the GRPC V2 API.
     * @return parsed {@link AccountCreationDetails}.
     */
    public static AccountTransactionDetails parse(com.concordium.grpc.v2.AccountTransactionDetails accountTransaction) {
        val sender =  accountTransaction.getSender();
        return AccountTransactionDetails.builder()
                .amount(CCDAmount.fromMicro(accountTransaction.getCost().getValue()))
                .sender(AccountAddress.parse(sender))
                .result(AccountTransactionResult.parse(accountTransaction.getEffects(), sender))
                .build();
    }
}
