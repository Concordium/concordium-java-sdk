package com.concordium.sdk.examples.contractexample.cis2nft;

import com.concordium.sdk.examples.contractexample.parameters.*;

/**
 * Represents the different possible methods able to be run by {@link Cis2Nft} representing a <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft contract</a>.
 */
public enum Cis2NftMethod {
    /**
     * The init method.
     */
    INIT,
    /**
     * The 'mint' method. Uses parameter {@link MintParams}.
     */
    MINT,
    /**
     * The 'transfer' method. Uses parameter {@link NftTransferParam}.
     */
    TRANSFER,
    /**
     * The 'updateOperator' method. Uses parameter {@link UpdateOperator}.
     */
    UPDATE_OPERATOR,
    /**
     * The 'operatorOf' of method. Uses parameter {@link OperatorOfQueryParams}.
     */
    OPERATOR_OF,
    /**
     * The 'balanceOf' method. Uses parameter {@link NftBalanceOfQueryParams}.
     */
    BALANCE_OF,
    /**
     * The 'tokenMetadata' method. Uses parameter {@link NftTokenMetaDataQueryParams}.
     */
    TOKEN_METADATA,
    /**
     * The 'supports' method. Uses parameter {@link SupportsQueryParams}.
     */
    SUPPORTS,
    /**
     * The 'setImplementors method. Uses parameter {@link SetImplementorsParams}.
     */
    SET_IMPLEMENTORS,
}
