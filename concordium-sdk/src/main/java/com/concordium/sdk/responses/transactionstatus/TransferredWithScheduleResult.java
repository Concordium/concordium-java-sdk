package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.util.List;

@Getter
@ToString
@Builder
public final class TransferredWithScheduleResult implements TransactionResultEvent {
    private final List<NewRelease> amount;
    private final AccountAddress to;
    private final AccountAddress from;


    @JsonCreator
    TransferredWithScheduleResult(@JsonProperty("amount") List<NewRelease> amount,
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
}
