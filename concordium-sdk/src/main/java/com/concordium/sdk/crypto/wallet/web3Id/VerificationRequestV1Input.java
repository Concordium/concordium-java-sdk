package com.concordium.sdk.crypto.wallet.web3Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
class VerificationRequestV1Input {
    private final UnfilledContextInformation context;
    private final List<SubjectClaims> subjectClaims;
    private final Object publicInfo;
}
