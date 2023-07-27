package com.concordium.sdk.responses.chainparameters;

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

    public TransactionFeeDistribution(double allocatedForBaker, double allocatedForGASAccount) {
        this.allocatedForBaker = allocatedForBaker;
        this.allocatedForGASAccount = allocatedForGASAccount;
    }

    public static TransactionFeeDistribution from(com.concordium.grpc.v2.TransactionFeeDistribution update) {
        return TransactionFeeDistribution
                .builder()
                .allocatedForBaker(update.getBaker().getPartsPerHundredThousand()/100_000d)
                .allocatedForGASAccount(update.getGasAccount().getPartsPerHundredThousand()/100_000d)
                .build();
    }
}
