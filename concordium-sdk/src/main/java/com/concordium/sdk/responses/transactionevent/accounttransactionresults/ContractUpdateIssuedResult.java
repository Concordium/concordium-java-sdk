package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * A contract update transaction was issued and produced the given trace.
 * This is the result of an Update transaction.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ContractUpdateIssuedResult implements AccountTransactionResult {

    private List<ContractTraceElement> effects;

    /**
     * Parses {@link AccountTransactionEffects.ContractUpdateIssued} to {@link ContractUpdateIssuedResult}.
     * @param contractUpdateIssued {@link AccountTransactionEffects.ContractUpdateIssued} returned by the GRPC V2 API.
     * @return parsed {@link ContractUpdateIssuedResult}.
     */
    public static ContractUpdateIssuedResult parse(AccountTransactionEffects.ContractUpdateIssued contractUpdateIssued) {
        val effects = new ImmutableList.Builder<ContractTraceElement>();
        val traceElements = contractUpdateIssued.getEffectsList();
        traceElements.forEach(traceElement -> effects.add(ContractTraceElement.parse(traceElement)));

        return ContractUpdateIssuedResult.builder()
                .effects(effects.build())
                .build();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UPDATE;
    }
}
