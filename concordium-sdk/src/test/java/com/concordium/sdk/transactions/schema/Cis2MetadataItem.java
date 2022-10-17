package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Tuple;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Cis2MetadataItem extends Tuple {
    @JsonCreator
    @Builder
    public Cis2MetadataItem(
            @JsonProperty("left") Cis2ContractTokenId tokenId,
            @JsonProperty("right") Cis2TokenMetadata tokenMetadata) {
        super(tokenId, tokenMetadata);
    }
}
