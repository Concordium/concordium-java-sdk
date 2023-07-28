package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class TransferredWithScheduleResult implements TransactionResultEvent {
    private final List<TransferRelease> releases;
    private final AccountAddress to;
    private final AccountAddress from;

    @JsonCreator
    TransferredWithScheduleResult(@JsonProperty("amount") List<TransferRelease> releases, // list of tuples
                                  @JsonProperty("to") AccountAddress to,
                                  @JsonProperty("from") AccountAddress from) {
        this.releases = releases;
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
