package com.concordium.sdk.responses.chainparameters;

import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Parameters for determining the distribution of transaction fees.
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class TransactionFeeDistribution {
    /**
     * The fraction allocated for the baker.
     */
    private final double allocatedForBaker;

    /**
     * The fraction allocated for the GAS account.
     */
    private final double allocatedForGASAccount;

    @JsonCreator
    public TransactionFeeDistribution(@JsonProperty("baker") double allocatedForBaker,
                                      @JsonProperty("gasAccount") double allocatedForGASAccount) {
        this.allocatedForBaker = allocatedForBaker;
        this.allocatedForGASAccount = allocatedForGASAccount;
    }

    public static TransactionFeeDistribution from(com.concordium.grpc.v2.TransactionFeeDistribution update) {
        return TransactionFeeDistribution
                .builder()
                .allocatedForBaker(PartsPerHundredThousand.from(update.getBaker().getPartsPerHundredThousand()).asDouble())
                .allocatedForGASAccount(PartsPerHundredThousand.from(update.getGasAccount().getPartsPerHundredThousand()).asDouble())
                .build();
    }
}
