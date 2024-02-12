package com.concordium.sdk.responses.accountinfo;

import com.concordium.sdk.responses.transactionstatus.PartsPerHundredThousand;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Commission rates for a baker
 */
@Data
@Jacksonized
@Builder
@ToString
public class CommissionRates {
    /**
     * Fraction of transaction rewards charged by the pool owner.
     */
    private final PartsPerHundredThousand transactionCommission;
    /**
     * Fraction of finalization rewards charged by the pool owner.
     */
    private final PartsPerHundredThousand finalizationCommission;
    /**
     * Fraction of baking rewards charged by the pool owner.
     */
    private final PartsPerHundredThousand bakingCommission;

    public static CommissionRates from(com.concordium.grpc.v2.CommissionRates commissionRates) {
        return CommissionRates.builder()
                .transactionCommission(PartsPerHundredThousand.from(commissionRates.getTransaction().getPartsPerHundredThousand()))
                .finalizationCommission(PartsPerHundredThousand.from(commissionRates.getFinalization().getPartsPerHundredThousand()))
                .bakingCommission(PartsPerHundredThousand.from(commissionRates.getBaking().getPartsPerHundredThousand()))
                .build();
    }
}
