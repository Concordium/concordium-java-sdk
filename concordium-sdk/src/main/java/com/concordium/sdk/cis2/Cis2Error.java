package com.concordium.sdk.cis2;

import lombok.Getter;

/**
 * Errors which are part of the CIS2 specification specified <a href="https://proposals.concordium.software/CIS/cis-2.html#rejection-errors">here</a>
 */
public enum Cis2Error {
    // A provided token id is not part of the contract.
    INVALID_TOKEN_ID(-42000001),
    // An address balance contains insufficient amount of
    // tokens to complete some transfer of a token.
    INSUFFICIENT_FUNDS(-42000002),
    // Sender is unauthorized to call the function.
    // (Note that authorization is not mandated by the CIS2 specification,
    // but may be introduced on top of the standard specification)
    UNAUTHORIZED(-42000003);

    @Getter
    private final int errorCode;
    Cis2Error(int errorCode) {
        this.errorCode = errorCode;
    }
}
