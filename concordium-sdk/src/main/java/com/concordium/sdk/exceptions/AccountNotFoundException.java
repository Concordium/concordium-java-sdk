package com.concordium.sdk.exceptions;

import com.concordium.sdk.requests.AccountQuery;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class AccountNotFoundException extends Exception {
    @Getter
    private final AccountQuery accountQuery;
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link AccountNotFoundException} from a {@link AccountAddress} and a {@link Hash}.
     * This happens when the account could not be found for the given block.
     * <p>
     * Use {@link AccountNotFoundException#from(AccountQuery, Hash)} to instantiate.
     *
     * @param accountQuery The request which could not find an account.
     * @param blockHash    The block hash
     */
    private AccountNotFoundException(AccountQuery accountQuery, Hash blockHash) {
        super("Account " + accountQuery.toString() + ") not found for block " + blockHash.asHex());
        this.accountQuery = accountQuery;
        this.blockHash = blockHash;
    }

    public static AccountNotFoundException from(AccountQuery accountQuery, Hash blockHash) {
        return new AccountNotFoundException(accountQuery, blockHash);
    }
}
