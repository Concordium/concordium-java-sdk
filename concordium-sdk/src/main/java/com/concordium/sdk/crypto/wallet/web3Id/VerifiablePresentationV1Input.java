package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Getter
class VerifiablePresentationV1Input {
    private final ObjectNode request;
    private final CryptographicParameters global;
    private final List<CommitmentInput> inputs;

    VerifiablePresentationV1Input(VerificationRequestV1 request,
                                  List<SubjectClaimsProofInput> claimProofInputs,
                                  List<GivenContext> requestedContext,
                                  CryptographicParameters globalContext) {
        val context = JsonMapper.INSTANCE.createObjectNode();
        context.put("type", "ConcordiumContextInformationV1");
        context.putPOJO("given", request.getContext().getGiven());
        context.putPOJO("requested", requestedContext);

        this.request = JsonMapper.INSTANCE.createObjectNode();
        this.request.put("type", "ConcordiumVerifiablePresentationRequestV1");
        this.request.putPOJO("subjectClaims", claimProofInputs);
        this.request.set("context", context);

        this.inputs = new ArrayList<>();
        for (SubjectClaimsProofInput proofInput : claimProofInputs) {
            this.inputs.add(proofInput.getCommitmentInput());
        }

        this.global = globalContext;
    }
}
