package com.concordium.sdk.v2.types;

import lombok.*;

/**
 * Information on a single anonymity revoker help by the identity provider.
 */
@ToString(includeFieldNames = false, exclude = {"publicKey"})
@Builder(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ArInfo {

    /**
     * Unique Identifier of the anonymity revoker on the chain.
     * This defines their evaluation point for secret sharing, and thus it cannot be 0
     */
    @Getter
    private final Identity identity;

    /**
     * Off-Chain Description of the anonymity revoker.
     */
    @Getter
    private final Description description;

    /**
     * Elgamal encryption key of the anonymity revoker.
     */
    @Getter
    private final PublicKey publicKey;
}

@ToString(includeFieldNames = false)
@Builder(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class Identity {
    @Getter
    private final int value;
}

@ToString
@Builder(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class Description {

    /**
     * Name of anonymity revoker or identity provider.
     */
    @Getter
    private final String name;

    /**
     * A link to more information about the anonymity revoker or identity provider.
     */
    @Getter
    private final String url;

    /**
     * A free form description of the revoker or provider.
     */
    @Getter
    private final String description;
}

class PublicKey extends ByteArrayWrapper {
    PublicKey(byte[] bytes) {
        super(bytes);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

