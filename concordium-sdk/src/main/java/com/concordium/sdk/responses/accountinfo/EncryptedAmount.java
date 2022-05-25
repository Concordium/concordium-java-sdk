package com.concordium.sdk.responses.accountinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public final class EncryptedAmount {
    private final List<String> incomingAmounts;
    private final String selfAmount;
    private final int startIndex;

    @JsonCreator
    EncryptedAmount(@JsonProperty("incomingAmounts") List<String> incomingAmounts,
                    @JsonProperty("selfAmount") String selfAmount,
                    @JsonProperty("startIndex") int startIndex) {
        this.incomingAmounts = incomingAmounts;
        this.selfAmount = selfAmount;
        this.startIndex = startIndex;
    }
}
