package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.BlockItemSummaryInBlock;
import com.concordium.sdk.requests.smartcontracts.Energy;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Summary of a transaction executed on the chain.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Summary {
    /**
     * The offset where the transaction was executed.
     */
    private final UInt64 transactionIndex;

    /**
     * The cost of the transaction.
     */
    private final Energy energyCost;

    /**
     * The transaction hash
     */
    private final Hash transactionHash;

    /**
     * The details of the block item that was processed in the block.
     */
    private final Details details;

    public static Summary from(BlockItemSummaryInBlock summary) {
        val outcome = summary.getOutcome();
        val builder = Summary
                .builder()
                .transactionIndex(UInt64.from(outcome.getIndex().getValue()))
                .energyCost(Energy.from(outcome.getEnergyCost()))
                .transactionHash(Hash.from(outcome.getHash()));

        switch (outcome.getDetailsCase()) {
            case ACCOUNT_TRANSACTION:
                builder.details(Details.newAccountTransaction(outcome.getAccountTransaction()));
                break;
            case ACCOUNT_CREATION:
                builder.details(Details.newAcountCreation(outcome.getAccountCreation()));
                break;
            case UPDATE:
                builder.details(Details.newChainUpdate(outcome.getUpdate()));
                break;
            case TOKEN_CREATION:
                builder.details(Details.newTokenCreation(outcome.getTokenCreation()));
            case DETAILS_NOT_SET:
                throw new IllegalArgumentException("Details type is not set.");
        }
        return builder.build();
    }
}
