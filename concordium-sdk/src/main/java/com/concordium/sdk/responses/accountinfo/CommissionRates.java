package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Commission rates for a baker
 */
@ToString
@Getter
@EqualsAndHashCode
public class CommissionRates {
    /**
     * Fraction of transaction rewards charged by the pool owner.
     */
    private final double transactionCommission;
    /**
     * Fraction of finalization rewards charged by the pool owner.
     */
    private final double finalizationCommission;
    /**
     * Fraction of baking rewards charged by the pool owner.
     */
    private final double bakingCommission;

    @JsonCreator
    CommissionRates(@JsonProperty("transactionCommission") double transactionCommission,
                    @JsonProperty("finalizationCommission") double finalizationCommission,
                    @JsonProperty("bakingCommission") double bakingCommission) {
        this.transactionCommission = transactionCommission;
        this.finalizationCommission = finalizationCommission;
        this.bakingCommission = bakingCommission;
    }
}
