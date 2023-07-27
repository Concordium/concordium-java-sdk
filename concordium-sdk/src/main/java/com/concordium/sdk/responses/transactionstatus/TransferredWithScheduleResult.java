package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public final class TransferredWithScheduleResult extends TransactionResultEvent {
    private final List<TransferRelease> releases;
    private final AccountAddress to;
    private final AccountAddress from;

    @JsonCreator
    TransferredWithScheduleResult(@JsonProperty("amount") List<Map<Long, String>> amount, // list of tuples
                                  @JsonProperty("to") AccountAddress to,
                                  @JsonProperty("from") AccountAddress from) {
        val amounts = new ArrayList<TransferRelease>();
        for (Map<Long, String> release : amount) {
            amounts.add(TransferRelease.from(release));
        }
        this.releases = amounts;
        this.to = to;
        this.from = from;
    }

    public static TransferredWithScheduleResult from(AccountTransactionEffects.TransferredWithSchedule transferredWithSchedule, AccountAddress sender) {
        val releases = transferredWithSchedule.getAmountList().stream().map(TransferRelease::from).collect(Collectors.toList());
        return TransferredWithScheduleResult
                .builder()
                .from(sender)
                .to(AccountAddress.from(transferredWithSchedule.getReceiver()))
                .releases(releases)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.TRANSFERRED_WITH_SCHEDULE;
    }

}
