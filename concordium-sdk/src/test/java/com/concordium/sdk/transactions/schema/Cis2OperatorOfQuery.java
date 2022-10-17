package com.concordium.sdk.transactions.schema;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Cis2OperatorOfQuery {
    private final Cis2Address owner;
    private final Cis2Address address;
}
