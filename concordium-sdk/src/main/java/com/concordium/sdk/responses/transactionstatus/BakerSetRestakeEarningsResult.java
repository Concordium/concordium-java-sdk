package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The baker's setting for restaking earnings was updated.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public final class BakerSetRestakeEarningsResult extends AbstractBakerResult {

    /**
     * Whether the baker will automatically add earnings to their stake or not.
     */
    private final boolean restakeEarnings;

    @JsonCreator
    BakerSetRestakeEarningsResult(@JsonProperty("bakerId") AccountIndex bakerId,
                                  @JsonProperty("account") AccountAddress account,
                                  @JsonProperty("restakeEarnings") boolean restakeEarnings) {
        super(bakerId, account);
        this.restakeEarnings = restakeEarnings;
    }

    /**
     * Parses {@link BakerEvent.BakerRestakeEarningsUpdated} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetRestakeEarningsResult}.
     * @param bakerRestakeEarningsUpdated {@link BakerEvent.BakerRestakeEarningsUpdated} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetRestakeEarningsResult}
     */
    public static BakerSetRestakeEarningsResult parse(BakerEvent.BakerRestakeEarningsUpdated bakerRestakeEarningsUpdated, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetRestakeEarningsResult.builder()
                .bakerId(AccountIndex.from(bakerRestakeEarningsUpdated.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .restakeEarnings(bakerRestakeEarningsUpdated.getRestakeEarnings())
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_RESTAKE_EARNINGS;
    }
}
