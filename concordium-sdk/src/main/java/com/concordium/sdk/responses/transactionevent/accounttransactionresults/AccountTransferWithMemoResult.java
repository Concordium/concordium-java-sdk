package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import com.concordium.sdk.transactions.Memo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountTransferWithMemoResult extends AccountTransferResult {

    private Memo memo;

    /**
     * Parses {@link AccountTransactionEffects.AccountTransfer} to {@link AccountTransferWithMemoResult}.
     * @param accountTransfer {@link AccountTransactionEffects.AccountTransfer} returned by the GRPC V2 API.
     * @return parsed {@link AccountTransferWithMemoResult}
     */
    public static AccountTransferWithMemoResult parse(AccountTransactionEffects.AccountTransfer accountTransfer) {
        return AccountTransferWithMemoResult.builder()
                .amount(CCDAmount.fromMicro(accountTransfer.getAmount().getValue()))
                .receiver(AccountAddress.parse(accountTransfer.getReceiver()))
                .memo(Memo.from(accountTransfer.getMemo().getValue().toByteArray()))
                .build();
    }
    @Override
    public TransactionType getResultType(){
        return TransactionType.TRANSFER_WITH_MEMO;
    }
}
