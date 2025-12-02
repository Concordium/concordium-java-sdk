package com.concordium.sdk.crypto.wallet.web3Id;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Context information for a verification request.
 * Contains both the information that is already known (given) and
 * the information that needs to be provided by the presenter (requested).
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class UnfilledContextInformation {
    /**
     * Context information that is already known.
     */
    @Singular("givenContext")
    private final List<GivenContext> given;

    /**
     * Labels of the context information that needs to be provided by the presenter.
     *
     * @see GivenContext#BLOCK_HASH_LABEL
     * @see GivenContext#RESOURCE_ID_LABEL
     */
    @Singular("requestedContext")
    private final List<String> requested;

    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "ConcordiumUnfilledContextInformationV1";
}
