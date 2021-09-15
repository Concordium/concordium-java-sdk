package com.concordium.sdk.transactions;

import lombok.Getter;

@Getter
public class SignerEntry {
    private final Index credentialIndex;
    private final Index keyIndex;
    private final Signer signer;

    private SignerEntry(Index credentialIndex, Index keyIndex, Signer signer) {
        this.credentialIndex = credentialIndex;
        this.keyIndex = keyIndex;
        this.signer = signer;
    }

    public static SignerEntry from(Index credentialIndex, Index keyIndex, Signer signer) {
        return new SignerEntry(credentialIndex, keyIndex, signer);
    }
}
