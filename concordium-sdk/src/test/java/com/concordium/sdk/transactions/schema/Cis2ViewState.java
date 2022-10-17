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
public class Cis2ViewState {

    @JsonProperty("state")
    @Singular("stateItem")
    private final List<Cis2StateItem> state;

    @JsonProperty("all_tokens")
    @Singular("token")
    private final List<Cis2ContractTokenId> allTokens;

    @JsonProperty("metadata")
    @Singular("metadataItem")
    private final List<Cis2MetadataItem> metadata;
}
