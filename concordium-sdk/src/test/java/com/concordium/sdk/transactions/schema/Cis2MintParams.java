package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Cis2MintParams {
    private final Cis2Address owner;
    private final Map<Cis2MetadataItem> tokens;
}
