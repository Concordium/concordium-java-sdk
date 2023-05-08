package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.TransactionFeeDistribution;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TransactionFeeDistributionUpdatePayload implements UpdatePayload {

    /**
     * The fraction allocated to the baker
     */
    private Fraction baker;

    /**
     * The fraction allocated to the GAS account;
     */
    private Fraction gasAccount;

    /**
     * parses {@link TransactionFeeDistribution} to {@link TransactionFeeDistributionUpdatePayload}
     * @param transactionFeeDistribution {@link TransactionFeeDistribution} returned by the GRPC V2 API
     * @return parsed {@link TransactionFeeDistributionUpdatePayload}
     */
    public static TransactionFeeDistributionUpdatePayload parse(TransactionFeeDistribution transactionFeeDistribution) {
        return TransactionFeeDistributionUpdatePayload.builder()
                .baker(Fraction.from(transactionFeeDistribution.getBaker()))
                .gasAccount(Fraction.from(transactionFeeDistribution.getGasAccount()))
                .build();
    }
    @Override
    public UpdateType getType() {
        return UpdateType.TRANSACTION_FEE_DISTRIBUTION_UPDATE;
    }
}
