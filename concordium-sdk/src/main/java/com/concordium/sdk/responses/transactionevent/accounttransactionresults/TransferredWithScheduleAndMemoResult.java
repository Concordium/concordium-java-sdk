package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.NewRelease;
import com.concordium.sdk.responses.transactionstatus.TransferredWithScheduleResult;
import com.concordium.sdk.transactions.Memo;
import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.val;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class TransferredWithScheduleAndMemoResult extends TransferredWithScheduleResult {

    private Memo memo;

    /**
     * Parses {@link AccountTransactionEffects.TransferredWithSchedule} and {@link com.concordium.grpc.v2.AccountAddress} to {@link TransferredWithScheduleAndMemoResult}.
     * @param transferredWithSchedule {@link AccountTransactionEffects.TransferredWithSchedule} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link TransferredWithScheduleAndMemoResult}.
     */
    public static TransferredWithScheduleAndMemoResult parse(AccountTransactionEffects.TransferredWithSchedule transferredWithSchedule, AccountAddress sender) {
        val amount = new ImmutableList.Builder<NewRelease>();
        transferredWithSchedule.getAmountList().forEach(newRelease -> amount.add(NewRelease.parse(newRelease)));
        return TransferredWithScheduleAndMemoResult.builder()
                .amount(amount.build())
                .to(com.concordium.sdk.transactions.AccountAddress.parse(transferredWithSchedule.getReceiver()))
                .from(com.concordium.sdk.transactions.AccountAddress.parse(sender))
                .memo(Memo.from(transferredWithSchedule.getMemo().getValue().toByteArray()))
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE_AND_MEMO;
    }
}
