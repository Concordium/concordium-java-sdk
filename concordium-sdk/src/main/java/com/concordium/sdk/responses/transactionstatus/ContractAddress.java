package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.AccountType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class ContractAddress extends AbstractAddress {

    @JsonProperty("subindex")
    private final int subIndex;

    @JsonProperty("index")
    private final int index;

    @JsonCreator
    public ContractAddress(@JsonProperty("subindex") int subIndex,
                           @JsonProperty("index") int index) {
        super(AccountType.ADDRESS_CONTRACT);
        this.subIndex = subIndex;
        this.index = index;
    }

    public String toJson() {
        try {
            return JsonMapper.INSTANCE.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize Contract Address");
        }
    }
}
