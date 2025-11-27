package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.bls.BLSSecretKey;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.blocksummary.updates.queues.IdentityProviderInfo;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

import java.util.List;
import java.util.Map;

public class VerifiablePresentationV1 {
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Satisfies given verification request by creating a verifiable presentation (proof) for it.
     * <p>
     * Before calling this method, you must determine which identities or accounts to use
     * for each requested {@link VerificationRequestV1#getSubjectClaims() subject claim}
     * and collect all the {@link VerificationRequestV1#getContext() requested context information}.
     *
     * @param request                the request to create a presentation (proof) for
     * @param qualifiedClaims        request claims qualified with identities or accounts,
     *                               corresponds to {@link VerificationRequestV1#getSubjectClaims()}
     * @param filledRequestedContext provided requested context information,
     *                               corresponds to {@link UnfilledContextInformation#getRequested()}
     *                               from the {@link VerificationRequestV1#getContext() request context}
     * @param globalContext          chain cryptographic parameters, stored in the wallet or fetched from a node
     * @return verifiable presentation JSON (ConcordiumVerifiablePresentationV1)
     * @see com.concordium.sdk.ClientV2#getCryptographicParameters(BlockQuery) Fetch global context (chain cryptographic parameters)
     * @see IdentityClaim#qualify(Network, IdentityProviderInfo, Map, IdentityObject, BLSSecretKey, BLSSecretKey, String) Qualify for a claim with an identity
     * @see IdentityClaim#qualify(Network, UInt32, CredentialRegistrationId, Map, Map) Qualify for a claim with an account
     */
    public static String getVerifiablePresentation(VerificationRequestV1 request,
                                                   List<QualifiedSubjectClaim> qualifiedClaims,
                                                   List<GivenContext> filledRequestedContext,
                                                   CryptographicParameters globalContext) {
        val input = new PresentationV1Input(request, qualifiedClaims, filledRequestedContext, globalContext);
        return getVerifiablePresentation(input);
    }

    static String getVerifiablePresentation(PresentationV1Input input) {
        StringResult result;
        try {
            String jsonStr = CryptoJniNative.createPresentation(JsonMapper.INSTANCE.writeValueAsString(input));
            result = JsonMapper.INSTANCE.readValue(jsonStr, StringResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.isSuccess()) {
            throw CryptoJniException.from(result.getErr());
        }

        return result.getResult();
    }
}
