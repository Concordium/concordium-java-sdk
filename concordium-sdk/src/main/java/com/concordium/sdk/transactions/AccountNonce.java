package com.concordium.sdk.transactions;

import com.concordium.sdk.serializing.JsonMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountNonce {
    private int nonce;
    private boolean allFinal;

    @SneakyThrows
    public static AccountNonce fromJson(String accountInfoJsonString) {
        return JsonMapper.MAPPER.readValue(accountInfoJsonString, AccountNonce.class);
    }
}
