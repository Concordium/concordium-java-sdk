package com.concordium.sdk.transactions.schema;

import java.util.Optional;

public interface ContractFunction {

    /**
     * Gets Function Parameters
     *
     * @return {@link Optional#empty()} is there are no parameters to the function
     * Or {@link Optional} of {@link SchemaType}.
     */
    Optional<SchemaType> getParameter();

    /**
     * Gets Function Return Type.
     *
     * @return {@link Optional#empty()} is function specifies no returns
     * Or {@link Optional} of {@link SchemaType}.
     */
    Optional<SchemaType> getReturnValue();
}
