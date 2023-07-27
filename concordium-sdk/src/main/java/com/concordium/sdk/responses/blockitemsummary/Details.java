package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.UpdateDetails;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

/**
 * Details of a block item executed on the chain.
 */
@ToString
@Getter
@Builder
@EqualsAndHashCode
public class Details {
    /**
     * Type of the block item.
     */
    private final Type type;

    /**
     * Account transaction details.
     * This is only present if the type is {@link Type#ACCOUNT_TRANSACTION}
     */
    private final AccountTransactionDetails accountTransactionDetails;

    /**
     * Account transaction details.
     * This is only present if the type is {@link Type#ACCOUNT_CREATION}
     */
    private final AccountCreationDetails accountCreationDetails;

    /**
     * Account transaction details.
     * This is only present if the type is {@link Type#CHAIN_UPDATE}
     */
    private final ChainUpdateDetails chainUpdateDetails;

    public static Details newAccountTransaction(com.concordium.grpc.v2.AccountTransactionDetails accountTransaction) {
        return Details.builder().accountTransactionDetails(AccountTransactionDetails.from(accountTransaction)).build();
    }

    public static Details newAcountCreation(com.concordium.grpc.v2.AccountCreationDetails accountCreation) {
        return Details.builder().accountCreationDetails(AccountCreationDetails.from(accountCreation)).build();
    }

    public static Details newChainUpdate(UpdateDetails update) {
        return Details.builder().chainUpdateDetails(ChainUpdateDetails.from(update)).build();
    }

    /**
     * Get the account transaction details.
     * This is only present if the type is {@link Type#ACCOUNT_TRANSACTION}
     * @return the details
     */
    public Optional<AccountTransactionDetails> getAccountTransactionDetails() {
        return Optional.of(accountTransactionDetails);
    }

    /**
     * Get the account transaction details.
     * This is only present if the type is {@link Type#ACCOUNT_CREATION}
     * @return the details
     */
    public Optional<AccountCreationDetails> getAccountCreationDetails() {
        return Optional.of(accountCreationDetails);
    }

    /**
     * Get the account transaction details.
     * This is only present if the type is {@link Type#CHAIN_UPDATE}
     * @return the details
     */
    public Optional<ChainUpdateDetails> getChainUpdateDetails() {
        return Optional.of(chainUpdateDetails);
    }

}
