package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.CryptoJniNative;
import com.concordium.sdk.crypto.NativeResolver;
import com.concordium.sdk.crypto.wallet.StringResult;
import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;

import java.util.List;

public class VerifiablePresentationV1 {
    // Static block to load native library.
    static {
        NativeResolver.loadLib();
    }

    /**
     * Satisfies given verification request by creating a verifiable presentation (proof) for it.
     * Before calling this method, you must determine which identities or accounts to use
     * for each requested subject claim and collect all the requested context information.
     *
     * @param request                the request to create a presentation (proof) for
     * @param commitmentInputs       which identities or accounts to use to prove each requested subject claim,
     *                               corresponds to {@link VerificationRequestV1#getSubjectClaims()}.
     *                               Only {@link IdentityCommitmentInput} and {@link AccountCommitmentInput} are allowed.
     * @param filledRequestedContext provided requested context information,
     *                               corresponds to {@link UnfilledContextInformation#getRequested()}
     *                               of {@link VerificationRequestV1#getContext()}
     * @param globalContext          chain cryptographic parameters, stored in the wallet or fetched from a node
     * @return verifiable presentation JSON (ConcordiumVerifiablePresentationV1)
     * @see com.concordium.sdk.ClientV2#getCryptographicParameters(BlockQuery)
     */
    public static String getVerifiablePresentation(VerificationRequestV1 request,
                                                   List<CommitmentInput> commitmentInputs,
                                                   List<GivenContext> filledRequestedContext,
                                                   CryptographicParameters globalContext) {
        val context = JsonMapper.INSTANCE.createObjectNode();
        context.put("type", "ConcordiumContextInformationV1");
        context.putPOJO("given", request.getContext().getGiven());
        context.putPOJO("requested", filledRequestedContext);
//
//        val subjectClaims = JsonMapper.INSTANCE.createArrayNode();
//
//        for (int claimIndex = 0; claimIndex < request.getSubjectClaims().size(); claimIndex++) {
//            val requestSubjectClaim = request.getSubjectClaims().get(claimIndex);
//            val commitmentInput = commitmentInputs.get(claimIndex);
//
//            val claimJson = JsonMapper.INSTANCE.createObjectNode();
//            val claimTypeArray = JsonMapper.INSTANCE.createArrayNode().add("ConcordiumSubjectClaimsV1");
//            claimJson.set("type", claimTypeArray);
//            claimJson.putPOJO("statement", requestSubjectClaim.getStatements());
//
//            if (commitmentInput instanceof IdentityCommitmentInput) {
//                val identityCommitmentInput = (IdentityCommitmentInput) commitmentInput;
//                claimTypeArray.add("ConcordiumIdBasedSubjectClaims");
//                claimJson.put(
//                        "issuer",
//                        "did:ccd:blabla:idp:" + identityCommitmentInput.getIpInfo().getIpIdentity().toString()
//                );
//            } else if (commitmentInput instanceof AccountCommitmentInput) {
//                val accountCommitmentInput = (AccountCommitmentInput) commitmentInput;
//                claimTypeArray.add("ConcordiumAccountBasedSubjectClaims");
//                claimJson.put(
//                        "id",
//                        "did:ccd:blabla:cred:" + accountCommitmentInput.getValues()
//                );
//            } else {
//                throw new IllegalArgumentException("Commitment input #" + claimIndex +
//                        " has unsupported type " + commitmentInput.getClass().getSimpleName());
//            }
//
//            subjectClaims.add(claimJson);
//        }

        val requestJson = JsonMapper.INSTANCE.createObjectNode();
        requestJson.put("type", "ConcordiumVerifiablePresentationRequestV1");
        requestJson.putPOJO("subjectClaims", request.getSubjectClaims());
        requestJson.set("context", context);

        val input = JsonMapper.INSTANCE.createObjectNode();
        input.putPOJO("inputs", commitmentInputs);
        input.putPOJO("global", globalContext);
        input.set("request", requestJson);

        StringResult result;
        try {
            String jsonStr = CryptoJniNative.createPresentationV1(JsonMapper.INSTANCE.writeValueAsString(input));
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
