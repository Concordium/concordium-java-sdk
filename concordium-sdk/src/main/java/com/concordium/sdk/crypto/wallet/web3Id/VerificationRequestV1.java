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
 * <p>
 * To satisfy this request, call
 * {@link VerifiablePresentationV1#getVerifiablePresentation(VerificationRequestV1, List, List, CryptographicParameters) VerifiablePresentationV1.getVerifiablePresentation()}
 */
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class VerificationRequestV1 {
    private final String type = TYPE;

    /**
     * The context information for this request
     * describing both the information that is already known (given) and
     * the information that needs to be provided by the presenter (requested).
     */
    private final UnfilledContextInformation context;

    /**
     * The specific credential claims that are requested to be proven.
     * <p>
     * Currently, the only known claims are {@link IdentityClaims}.
     */
    @Singular("addSubjectClaims")
    private final List<SubjectClaims> subjectClaims;

    /**
     * Hash of the blockchain transaction anchoring this request.
     *
     * @see VerificationRequestAnchor
     */
    private final Hash transactionRef;

    public static final String TYPE = "ConcordiumVerificationRequestV1";
}
