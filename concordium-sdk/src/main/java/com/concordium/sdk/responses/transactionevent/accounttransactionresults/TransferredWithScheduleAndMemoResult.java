package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.NewRelease;
import com.concordium.sdk.responses.transactionstatus.TransferredWithScheduleResult;
import com.concordium.sdk.transactions.Memo;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class TransferredWithScheduleAndMemoResult extends TransferredWithScheduleResult {

    private Memo memo;

    TransferredWithScheduleAndMemoResult(List<NewRelease> amount,
                                         com.concordium.sdk.types.AccountAddress to,
                                         com.concordium.sdk.types.AccountAddress from,
                                         Memo memo) {
        super(amount, to, from);
        this.memo = memo;

    }

    /**
     * Parses {@link AccountTransactionEffects.TransferredWithSchedule} and {@link AccountAddress} to {@link TransferredWithScheduleAndMemoResult}.
     *
     * @param transferredWithSchedule {@link AccountTransactionEffects.TransferredWithSchedule} returned by the GRPC V2 API.
     * @param sender                  {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link TransferredWithScheduleAndMemoResult}.
     */
    public static TransferredWithScheduleAndMemoResult parse(AccountTransactionEffects.TransferredWithSchedule transferredWithSchedule, AccountAddress sender) {
        val amount = new ImmutableList.Builder<NewRelease>();
        val to = com.concordium.sdk.types.AccountAddress.parse(transferredWithSchedule.getReceiver());
        val from = com.concordium.sdk.types.AccountAddress.parse(sender);
        val memo = Memo.from(transferredWithSchedule.getMemo().getValue().toByteArray());
        transferredWithSchedule.getAmountList().forEach(newRelease -> amount.add(NewRelease.parse(newRelease)));
        return new TransferredWithScheduleAndMemoResult(amount.build(), to, from, memo);
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE_AND_MEMO;
    }
}
