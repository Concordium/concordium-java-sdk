package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public final class BakerSetRestakeEarningsResult extends AbstractBakerResult {
    private final boolean restakeEarnings;

    @JsonCreator
    BakerSetRestakeEarningsResult(@JsonProperty("bakerId") AccountIndex bakerId,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("restakeEarnings") boolean restakeEarnings) {
        super(bakerId, account);
        this.restakeEarnings = restakeEarnings;
    }

    public static BakerSetRestakeEarningsResult from(BakerEvent.BakerRestakeEarningsUpdated restake, AccountAddress account) {
        return BakerSetRestakeEarningsResult
                .builder()
                .bakerId(BakerId.from(restake.getBakerId()))
                .account(account)
                .restakeEarnings(restake.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS;
    }
}
