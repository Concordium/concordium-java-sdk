package com.concordium.sdk.cis2;

public enum Cis2Error {
    // A provided token id is not part of the contract.
    INVALID_TOKEN_ID,
    // An address balance contains insufficient amount of
    // tokens to complete some transfer of a token.
    INSUFFICIENT_FUNDS,
    // Sender is unauthorized to call the function.
    // (Note that authorization is not mandated by the CIS2 specification,
    // but may be introduced on top of the standard specification)
    UNAUTHORIZED
}
