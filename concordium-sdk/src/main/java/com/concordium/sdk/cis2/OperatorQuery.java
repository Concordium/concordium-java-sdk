package com.concordium.sdk.cis2;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An object used for querying the operator of a specific {@link AbstractAddress}
 * See <a href="https://proposals.concordium.software/CIS/cis-2.html#balanceof">here</a> for the specification.
 */
@Getter
@ToString
@EqualsAndHashCode
public class OperatorQuery {
    /**
     * Potential operator
     */
    private final AbstractAddress owner;

    /**
     * The address which {@link OperatorQuery#owner} operates.
     */
    private final AbstractAddress address;

    public OperatorQuery(AbstractAddress owner, AbstractAddress address) {
        this.owner = owner;
        this.address = address;
    }
}
