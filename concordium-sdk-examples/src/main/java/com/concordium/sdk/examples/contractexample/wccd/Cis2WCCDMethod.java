package com.concordium.sdk.examples.contractexample.wccd;

import com.concordium.sdk.examples.contractexample.parameters.*;
import com.concordium.sdk.transactions.smartcontracts.parameters.AddressParam;
import com.concordium.sdk.types.AbstractAddress;

/**
 * Represents the different possible methods able to be run by {@link Cis2WCCD} representing a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs">cis2-wCCD contract</a>.
 */
public enum Cis2WCCDMethod {
    /**
     * The init method.
     */
    INIT,
    /**
     * The 'wrap' method. Uses parameter {@link WrapParams}.
     */
    WRAP,
    /**
     * The 'unwrap' method. Uses parameter {@link UnwrapParams}.
     */
    UNWRAP,
    /**
     * The 'updateAdmin' method. Uses parameter {@link AddressParam} to pass an {@link AbstractAddress} as a parameter.
     */
    UPDATE_ADMIN,
    /**
     * The 'setPaused' method. Uses parameter {@link SetPausedParams}.
     */
    SET_PAUSED,
    /**
     * The 'setMetadataUrl' method. Uses parameter {@link SetMetadataUrlParams}.
     */
    SET_METADATA_URL,
    /**
     * The 'transfer' method. Uses parameter {@link WCCDTransferParam}.
     */
    TRANSFER,
    /**
     * The 'updateOperator' method. Uses parameter {@link UpdateOperatorParams}.
     */
    UPDATE_OPERATOR,
    /**
     * The 'balanceOf' method. Uses parameter {@link WCCDBalanceOfQueryParams}.
     */
    BALANCE_OF,
    /**
     * The 'operatorOf' method. Uses parameter {@link OperatorOfQueryParams}.
     */
    OPERATOR_OF,
    /**
     * The 'tokenMetadata' method. Uses parameter {@link WCCDTokenMetadataQueryParams}.
     */
    TOKEN_METADATA,
    /**
     * The 'supports' method. Uses parameter {@link SupportsQueryParams}.
     */
    SUPPORTS,
    /**
     * The 'setImplementors' method. Uses parameter {@link SetImplementorsParams}.
     */
    SET_IMPLEMENTORS,
    /**
     * The 'upgrade' method. Uses parameter {@link UpgradeParams}.
     */
    UPGRADE
}
