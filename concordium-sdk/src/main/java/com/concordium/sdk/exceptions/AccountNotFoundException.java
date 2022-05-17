package com.concordium.sdk.exceptions;

import com.concordium.sdk.AccountRequest;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class AccountNotFoundException extends Exception {
    @Getter
    private final AccountRequest accountRequest;
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link AccountNotFoundException} from a {@link AccountAddress} and a {@link Hash}.
     * This happens when the account could not be found for the given block.
     *
     * Use {@link AccountNotFoundException#from(AccountRequest, Hash)} to instantiate.
     *
     * @param accountRequest The request which could not find an account.
     * @param blockHash The block hash
     */
    private AccountNotFoundException(AccountRequest accountRequest, Hash blockHash) {
        super("Account " + accountRequest.toString() + ") not found for block " + blockHash.asHex());
        this.accountRequest = accountRequest;
        this.blockHash = blockHash;
    }

    public static AccountNotFoundException from(AccountRequest accountRequest, Hash blockHash) {
        return new AccountNotFoundException(accountRequest, blockHash);
    }
}
