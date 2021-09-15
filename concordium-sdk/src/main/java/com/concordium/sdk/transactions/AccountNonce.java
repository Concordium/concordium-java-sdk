package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@NoArgsConstructor
@Getter
public final class AccountNonce {
    private UInt64 nonce;
    private boolean allFinal;

    private AccountNonce(UInt64 value, boolean allFinal) {
        this.nonce = value;
        this.allFinal = allFinal;
    }

    public static AccountNonce fromJson(String accountInfoJsonString) {
        try {
            return JsonMapper.INSTANCE.readValue(accountInfoJsonString, AccountNonce.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not parse AccountNonce JSON", e);
        }
    }

    /**
     * Increments the AccountNonce
     *
     * @return The new incremented AccountNonce
     */
    public AccountNonce increment() {
        return new AccountNonce(UInt64.from(this.nonce.getValue() + 1), this.allFinal);
    }

    public static AccountNonce from(long value) {
        return new AccountNonce(UInt64.from(value), true);
    }
}
