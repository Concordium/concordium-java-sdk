package com.concordium.sdk.responses.accountinfo;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * Commission rates for a baker
 */
@Data
@Jacksonized
@Builder
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
}
