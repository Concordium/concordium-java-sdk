package com.concordium.sdk.examples.contractexample.cis2nft;

/**
 * Represents the different possible methods able to be run by {@link Cis2Nft}
 */
public enum Cis2NftMethod {
    INIT,
    MINT,
    TRANSFER,
    UPDATE_OPERATOR,
    OPERATOR_OF,
    BALANCE_OF,
    TOKEN_METADATA,
    SUPPORTS,
    SET_IMPLEMENTORS,
}
