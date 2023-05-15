package com.concordium.sdk.responses.transactionevent;

import com.concordium.sdk.responses.transactionstatus.TransactionResult;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
    private TransactionResult result;

    /**
     * Parses {@link com.concordium.grpc.v2.AccountTransactionDetails} to {@link AccountCreationDetails}.
     * @param accountTransaction {@link AccountTransactionDetails} returned by the GRPC V2 API.
     * @return parsed {@link AccountCreationDetails}.
     */
    public static AccountTransactionDetails parse(com.concordium.grpc.v2.AccountTransactionDetails accountTransaction) {
        return AccountTransactionDetails.builder()
                .amount(CCDAmount.fromMicro(accountTransaction.getCost().getValue()))
                .sender(AccountAddress.from(accountTransaction.getSender().getValue().toByteArray()))
                .result(TransactionResult.parse(accountTransaction.getEffects()))
                .build();
    }
}
