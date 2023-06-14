package com.concordium.sdk.responses.transactionevent;

import com.concordium.grpc.v2.BlockItemSummary;
import com.concordium.sdk.responses.transactionstatus.TransactionType;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.Energy;
import com.concordium.sdk.types.UInt64;
import lombok.*;

/**
 * Summary of the outcome of a block item.
 */
@Builder
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
public class BlockTransactionEvent {

    /**
     * Index of the transaction in the block where it is included.
     */
    @Getter
    private final UInt64 index;

    /**
     * The amount of energy the transaction cost.
     */
    @Getter
    private final Energy energyCost;

    /**
     * Hash of the transaction.
     */
    @Getter
    private final Hash hash;

    /**
     * Type of the transaction.
     */
    @Getter
    private final TransactionType type;

    /**
     * Details about an account transaction.
     */
    private final AccountTransactionDetails accountTransactionDetails;

    /**
     * Details about an account creation.
     */
    private final AccountCreationDetails accountCreationDetails;

    /**
     * Details about a chain update.
     */
    private final UpdateDetails updateDetails;

    /**
     * Gets the {@link AccountTransactionDetails} of an {@link BlockTransactionEvent}.
     * {@link TransactionType} of the {@link BlockTransactionEvent} must be {@link TransactionType#ACCOUNT_TRANSACTION}
     *
     * @return {@link AccountTransactionDetails} of the {@link BlockTransactionEvent}
     */
    public AccountTransactionDetails getAccountTransactionDetails() {
        if (this.type != TransactionType.ACCOUNT_TRANSACTION) {
            throw new IllegalStateException("AccountTransactionDetails only present for Transaction event of type: " + TransactionType.ACCOUNT_TRANSACTION + " not: " + this.type);
        }
        return accountTransactionDetails;
    }

    /**
     * Gets the {@link AccountCreationDetails} of an {@link BlockTransactionEvent}.
     * {@link TransactionType} of the {@link BlockTransactionEvent} must be {@link TransactionType#CREDENTIAL_DEPLOYMENT_TRANSACTION}
     *
     * @return {@link AccountCreationDetails} of the {@link BlockTransactionEvent}
     */
    public AccountCreationDetails getAccountCreationDetails() {
        if (this.type != TransactionType.CREDENTIAL_DEPLOYMENT_TRANSACTION) {
            throw new IllegalStateException("AccountCreationDetails only present for Transaction event of type: " + TransactionType.CREDENTIAL_DEPLOYMENT_TRANSACTION + " not: " + this.type);
        }
        return accountCreationDetails;
    }

    /**
     * Gets the {@link UpdateDetails} of an {@link BlockTransactionEvent}.
     * {@link TransactionType} of the {@link BlockTransactionEvent} must be {@link TransactionType#UPDATE_TRANSACTION}
     *
     * @return {@link UpdateDetails} of the {@link BlockTransactionEvent}
     */
    public UpdateDetails getUpdateDetails() {
        if (this.type != TransactionType.UPDATE_TRANSACTION) {
            throw new IllegalStateException("UpdateDetails only present for Transaction event of type: " + TransactionType.UPDATE_TRANSACTION + " not: " + this.type);
        }
        return updateDetails;
    }

    /**
     * Parses {@link BlockItemSummary} to {@link BlockTransactionEvent}.
     * @param blockItemSummary {@link BlockItemSummary} returned by the GRPC V2 API.
     * @return parsed {@link BlockTransactionEvent}.
     */
    public static BlockTransactionEvent parse(BlockItemSummary blockItemSummary) {
        var builder = BlockTransactionEvent.builder()
                .index(UInt64.from(blockItemSummary.getIndex().getValue()))
                .energyCost(Energy.parse(blockItemSummary.getEnergyCost()))
                .hash(Hash.from(blockItemSummary.getHash().getValue().toByteArray()));

        switch (blockItemSummary.getDetailsCase()) {
            case ACCOUNT_TRANSACTION:
                builder.accountTransactionDetails(AccountTransactionDetails.parse(blockItemSummary.getAccountTransaction()))
                        .type(TransactionType.ACCOUNT_TRANSACTION);
                break;
            case ACCOUNT_CREATION:
                builder.accountCreationDetails(AccountCreationDetails.parse(blockItemSummary.getAccountCreation()))
                        .type(TransactionType.CREDENTIAL_DEPLOYMENT_TRANSACTION);
                break;
            case UPDATE:
                builder.updateDetails(UpdateDetails.parse(blockItemSummary.getUpdate()))
                        .type(TransactionType.UPDATE_TRANSACTION);
                break;
        }

        return builder.build();
    }

}
