package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@EqualsAndHashCode
@Builder
@RequiredArgsConstructor
@Jacksonized
public class Cis2TokenMetadata {

    private final String url;
    private final String hash;
}
