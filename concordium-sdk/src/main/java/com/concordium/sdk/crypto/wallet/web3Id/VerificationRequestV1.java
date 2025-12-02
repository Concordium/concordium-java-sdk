package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * A verification request that specifies what credentials and proofs
 * are being requested from a credential holder.
 * This class encapsulates the request context, the specific credential subject claims needed,
 * and a reference to the blockchain transaction (its hash) that anchors the request.
 * <p>
 * To satisfy this request, call
 * {@link VerifiablePresentationV1#getVerifiablePresentation(VerificationRequestV1, List, List, CryptographicParameters) VerifiablePresentationV1.getVerifiablePresentation()}.
 * <p>
 * ⚠️ Verifiable presentation created for this request can be used to reveal the <b>whole</b> related identity via the
 * <a href="https://docs.concordium.com/en/mainnet/docs/protocol/identity-disclosure-processes.html">identity disclosure process</a>
 * even though there may never be any on-chain activity associated with this identity.
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
     * @see com.concordium.sdk.ClientV2#getBlockItemStatus(Hash) Fetch a transaction by hash
     */
    private final Hash transactionRef;

    public boolean verifyAnchor(VerificationRequestAnchor anchor) {
        return anchor.getHash().equals(getAnchorHash(context, subjectClaims));
    }

    public static Hash getAnchorHash(UnfilledContextInformation context,
                                     List<SubjectClaims> subjectClaims) {
        StringResult result;
        try {
            NativeResolver.loadLib();
            val input = new VerificationRequestV1Input(context, subjectClaims, null);
            String jsonStr = CryptoJniNative.computeAnchorHash(JsonMapper.INSTANCE.writeValueAsString(input));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return Hash.from(result.getResult().replace("\"", ""));
    }

    public static final String TYPE = "ConcordiumVerificationRequestV1";
}
