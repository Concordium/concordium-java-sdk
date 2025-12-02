package com.concordium.sdk.crypto.wallet.web3Id;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
@AllArgsConstructor
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
    @NonNull
    private final String label;

    /**
     * The actual given information.
     */
    @NonNull
    private final String context;

    /**
     * Resource which is being accessed, for example a site URL.
     */
    public static final String RESOURCE_ID_LABEL = "ResourceID";

    /**
     * Concordium block hash. When requested, it is the hash of the block
     * in which the verification request anchor transaction is finalized.
     *
     * @see VerificationRequestAnchor
     */
    public static final String BLOCK_HASH_LABEL = "BlockHash";

    /**
     * A payment transaction hash, or other payment-related hash.
     */
    public static final String PAYMENT_HASH_LABEL = "PaymentHash";

    /**
     * Identifier of the verification session,
     * for example a WalletConnect session public key.
     */
    public static final String CONNECTION_ID_LABEL = "ConnectionID";

    /**
     * A 32 byte long cryptographic nonce, HEX-encoded.
     */
    public static final String NONCE_LABEL = "Nonce";

    /**
     * Additional context information, a string value for general purposes.
     */
    public static final String CONTEXT_STRING_LABEL = "ContextString";
}
