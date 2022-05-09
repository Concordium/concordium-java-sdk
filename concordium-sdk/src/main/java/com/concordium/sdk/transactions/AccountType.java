package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.AbstractAccount;
import com.concordium.sdk.responses.transactionstatus.TransactionResultEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AccountType {
    @JsonProperty("AddressAccount")
    ADDRESS_ACCOUNT("AddressAccount"),
    @JsonProperty("AddressContract")
    ADDRESS_CONTRACT("AddressContract");

    private final String identifier;
    AccountType(String identifier) {
        this.identifier = identifier;
    }

    public static AccountType from(String val) {
        for (AccountType value : AccountType.values()) {
            if(value.identifier.equals(val)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported AccountType: " + val);
    }

    // Convenience methods for doing 'safe' casting.
    public <T> T convert(AbstractAccount account) {
        if (this != account.getType()) {
            throw new IllegalArgumentException("Unexpected conversion. Expected " + this + " but received " + account.getType());
        }
        return (T) account;
    }
}
