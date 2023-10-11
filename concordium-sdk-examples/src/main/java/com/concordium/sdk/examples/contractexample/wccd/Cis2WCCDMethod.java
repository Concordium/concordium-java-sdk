package com.concordium.sdk.examples.contractexample.wccd;

/**
 * Represents the different possible methods able to be run by {@link Cis2WCCD}
 */
public enum Cis2WCCDMethod {
    INIT,
    WRAP,
    UNWRAP,
    UPDATE_ADMIN,
    SET_PAUSED,
    SET_METADATA_URL,
    TRANSFER,
    UPDATE_OPERATOR,
    BALANCE_OF,
    OPERATOR_OF,
    TOKEN_METADATA,
    SUPPORTS,
    SET_IMPLEMENTORS,
    UPGRADE
}
