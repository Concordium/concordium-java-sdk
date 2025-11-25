package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.transactions.Hash;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * A verification request that specifies what credentials and proofs
 * are being requested from a credential holder.
 * This class encapsulates the request context, the specific credential subject claims needed,
 * and a reference to the blockchain transaction (its hash) that anchors the request.
 * <br><br>
 * To satisfy this request, call
 * {@link VerifiablePresentationV1#getVerifiablePresentation(VerificationRequestV1, List, List, CryptographicParameters)}
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class VerificationRequestV1 {
    /**
     * The context information for this request
     * describing both the information that is already known (given) and
     * the information that needs to be provided by the presenter (requested).
     */
    private final UnfilledContextInformation context;

    /**
     * The specific credential subject claims that are requested to be proven.
     */
    @Singular
    private final List<SubjectClaim> subjectClaims;

    /**
     * Hash of the blockchain transaction anchoring this request.
     *
     * @see VerificationRequestAnchor
     */
    private final Hash transactionRef;

    public String getType() {
        return TYPE;
    }

    public static final String TYPE = "ConcordiumVerificationRequestV1";
}
