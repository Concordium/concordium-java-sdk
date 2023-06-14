package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.AccountTransactionResult;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.TransactionType;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * A transfer with schedule was performed.
 * This is the result of a successful TransferWithSchedule transaction.
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public  class TransferredWithScheduleResult extends TransactionResultEvent implements AccountTransactionResult {

    /**
     * The list of new releases. Ordered by increasing timestamp.
     */
    private final List<NewRelease> amount;

    /**
     * Receiver account.
     */
    private final AccountAddress to;

    /**
     * Sender account.
     */
    private final AccountAddress from;


    @Builder
    @JsonCreator
    public TransferredWithScheduleResult(@JsonProperty("amount") List<NewRelease> amount,
                                  @JsonProperty("to") AccountAddress to,
                                  @JsonProperty("from") AccountAddress from) {
        this.amount = amount;
        this.to = to;
        this.from = from;
    }


    /**
     * Parses {@link AccountTransactionEffects.TransferredWithSchedule} and {@link com.concordium.grpc.v2.AccountAddress} to {@link TransferredWithScheduleResult}.
     * @param transferredWithSchedule {@link AccountTransactionEffects.TransferredWithSchedule} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link TransferredWithScheduleResult}.
     */
    public static TransferredWithScheduleResult parse(AccountTransactionEffects.TransferredWithSchedule transferredWithSchedule, com.concordium.grpc.v2.AccountAddress sender) {
        val amount = new ImmutableList.Builder<NewRelease>();
        transferredWithSchedule.getAmountList().forEach(newRelease -> amount.add(NewRelease.parse(newRelease)));
        return TransferredWithScheduleResult.builder()
                .amount(amount.build())
                .to(AccountAddress.parse(transferredWithSchedule.getReceiver()))
                .from(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER_WITH_SCHEDULE;
    }
}
