package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.types.Nonce;
import com.concordium.sdk.types.UInt64;
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
    private Nonce nonce;
    private boolean allFinal;

    private AccountNonce(Nonce value, boolean allFinal) {
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
        return new AccountNonce(this.nonce.increment(), this.allFinal);
    }

    public static AccountNonce from(long value) {
        return new AccountNonce(Nonce.from(value), true);
    }
}
