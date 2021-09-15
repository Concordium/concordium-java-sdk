package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

public final class AccountNotFoundException extends Exception {
    @Getter
    private final AccountAddress address;
    @Getter
    private final Hash blockHash;

    /**
     * Creates a new {@link AccountNotFoundException} from a {@link AccountAddress} and a {@link Hash}.
     * This happens when the account could not be found for the given block.
     *
     * Use {@link AccountNotFoundException#from(AccountAddress, Hash)} to instantiate.
     *
     * @param address The account address
     * @param blockHash The block hash
     */
    private AccountNotFoundException(AccountAddress address, Hash blockHash) {
        super("Account " + address.encoded() + ") not found for block " + blockHash.asHex());
        this.address = address;
        this.blockHash = blockHash;
    }

    public static AccountNotFoundException from(AccountAddress address, Hash blockHash) {
        return new AccountNotFoundException(address, blockHash);
    }
}
