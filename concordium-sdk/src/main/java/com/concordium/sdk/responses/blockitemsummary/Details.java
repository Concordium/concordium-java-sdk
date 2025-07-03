package com.concordium.sdk.responses.blockitemsummary;

import com.concordium.grpc.v2.UpdateDetails;
import com.concordium.grpc.v2.plt.TokenCreationDetails;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    /**
     * Protocol-level token (PLT) creation details.
     * This is only present if the type is {@link Type#TOKEN_CREATION}
     */
    private final TokenCreationDetails tokenCreationDetails;

    public static Details newAccountTransaction(com.concordium.grpc.v2.AccountTransactionDetails accountTransaction) {
        return Details.builder().type(Type.ACCOUNT_TRANSACTION).accountTransactionDetails(AccountTransactionDetails.from(accountTransaction)).build();
    }

    public static Details newAcountCreation(com.concordium.grpc.v2.AccountCreationDetails accountCreation) {
        return Details.builder().type(Type.ACCOUNT_TRANSACTION).accountCreationDetails(AccountCreationDetails.from(accountCreation)).build();
    }

    public static Details newChainUpdate(UpdateDetails update) {
        return Details.builder().type(Type.CHAIN_UPDATE).chainUpdateDetails(ChainUpdateDetails.from(update)).build();
    }

    public static Details newTokenCreation(TokenCreationDetails tokenCreationDetails) {
        return Details.builder().type(Type.TOKEN_CREATION).tokenCreationDetails(tokenCreationDetails).build();
    }
}
