package com.concordium.sdk;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.Hash;
import com.google.protobuf.ByteString;

import java.nio.charset.StandardCharsets;

/**
 * The account request to use when querying accounts on chain.
 * See {@link Client#getAccountInfo(AccountRequest, Hash)}
 */
public final class AccountRequest {

    private final Type type;
    private final AccountAddress address;
    private final AccountIndex index;

    private AccountRequest(Type type, AccountAddress address, AccountIndex index) {
        this.type = type;
        this.address = address;
        this.index = index;
    }

    ByteString getByteString() {
        switch (type) {
            case ADDRESS:
                return ByteString.copyFrom(address.getEncodedBytes());
            case INDEX:
                return ByteString.copyFrom(Long.toString(index.getIndex().getValue()).getBytes(StandardCharsets.UTF_8));
            default:
                throw new IllegalStateException("Invalid AccountRequest Type " + type);
        }
    }

    /**
     * Create an {@link AccountRequest} given the provided {@link AccountAddress}
     *
     * @param address The account address
     * @return the AccountRequest
     */
    public static AccountRequest from(AccountAddress address) {
        return new AccountRequest(Type.ADDRESS, address, null);
    }

    /**
     * Create an {@link AccountRequest} given the provided {@link AccountIndex}
     *
     * @param index the account index, baker id, delegator id etc.
     * @return the AccountRequest
     */
    public static AccountRequest from(AccountIndex index) {
        return new AccountRequest(Type.INDEX, null, index);
    }

    @Override
    public String toString() {
        switch (type) {
            case ADDRESS:
                return address.encoded();
            case INDEX:
                return index.toString();
            default:
                throw new IllegalStateException("Invalid AccountRequest Type " + type);
        }
    }

    private enum Type {
        ADDRESS, INDEX;
    }
}
