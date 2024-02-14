package com.concordium.sdk.cis2.events;

import com.concordium.sdk.types.AbstractAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An event that is emitted from a CIS2 compliant contract when the operator has been updated
 * for a particular address.
 * https://proposals.concordium.software/CIS/cis-2.html#updateoperatorevent
 */
@Getter
@EqualsAndHashCode
@ToString
public class UpdateOperatorEvent implements Cis2Event {

    /**
     * Whether an operator was added or removed.
     */
    private final boolean isOperator;

    /**
     * Invoker of the updateOperatorOf
     */
    private final AbstractAddress owner;

    /**
     * The operator that is either added or removed.
     */
    private final AbstractAddress operator;

    public UpdateOperatorEvent(boolean isOperator, AbstractAddress owner, AbstractAddress operator) {
        this.isOperator = isOperator;
        this.owner = owner;
        this.operator = operator;
    }

    @Override
    public Type getType() {
        return Type.UPDATE_OPERATOR_OF;
    }
}
