package com.concordium.sdk.crypto.wallet.web3Id;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
public class GivenContext {

    /**
     * The label identifying the context information.
     *
     * @see GivenContext#CONTEXT_STRING_LABEL
     * @see GivenContext#RESOURCE_ID_LABEL
     * @see GivenContext#BLOCK_HASH_LABEL
     * @see GivenContext#PAYMENT_HASH_LABEL
     * @see GivenContext#CONNECTION_ID_LABEL
     * @see GivenContext#NONCE_LABEL
     */
    private final String label;

    /**
     * The actual given information.
     */
    private final String context;

    public GivenContext(String label, String context) {
        this.label = label;
        this.context = context;
    }

    public static final String CONTEXT_STRING_LABEL = "ContextString";
    public static final String RESOURCE_ID_LABEL = "ResourceID";
    public static final String BLOCK_HASH_LABEL = "BlockHash";
    public static final String PAYMENT_HASH_LABEL = "PaymentHash";
    public static final String CONNECTION_ID_LABEL = "ConnectionID";
    public static final String NONCE_LABEL = "Nonce";
}
