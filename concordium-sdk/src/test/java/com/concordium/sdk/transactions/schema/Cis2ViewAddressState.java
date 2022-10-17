package com.concordium.sdk.transactions.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Getter
@EqualsAndHashCode
public class Cis2ViewAddressState {
    @JsonProperty("owned_tokens")
    @Singular
    private final List<Cis2ContractTokenId> ownedTokens;
    @Singular
    private final List<Cis2Address> operators;
}
