package com.concordium.sdk.responses.blocksummary.updates.keys;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.ToString;
import lombok.val;

import java.util.*;

@ToString
public enum SigningScheme {
    ED25519;

    private static final Map<String, SigningScheme> signingSchemes = new HashMap<>();

    static {
        signingSchemes.put("ed25519", SigningScheme.ED25519);
    }

    @JsonCreator
    public static SigningScheme forValue(String schemeId) {
        val scheme = SigningScheme.signingSchemes.get(schemeId);
        if (Objects.isNull(scheme)) {
            throw new IllegalArgumentException("Unrecognized signing scheme " + schemeId);
        }
        return scheme;
    }
}
