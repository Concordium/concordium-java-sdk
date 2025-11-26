package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@Getter
class VerifiablePresentationInputV1 {
    private final JsonNode request;
    private final CryptographicParameters global;
    private final List<CommitmentInput> inputs;

    VerifiablePresentationInputV1(VerificationRequestV1 request,
                                  List<QualifiedSubjectClaim> qualifiedClaims,
                                  List<GivenContext> filledRequestedContext,
                                  CryptographicParameters globalContext) {
        val context = JsonMapper.INSTANCE.createObjectNode();
        context.put("type", "ConcordiumContextInformationV1");
        context.putPOJO("given", request.getContext().getGiven());
        context.putPOJO("requested", filledRequestedContext);

        val requestJson = JsonMapper.INSTANCE.createObjectNode();
        requestJson.put("type", "ConcordiumVerifiablePresentationRequestV1");
        requestJson.putPOJO("subjectClaims", qualifiedClaims);
        requestJson.set("context", context);

        this.request = requestJson;
        this.global = globalContext;
        this.inputs = qualifiedClaims.stream()
                .map(QualifiedSubjectClaim::getCommitmentInput)
                .collect(Collectors.toList());
    }
}
