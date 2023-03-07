package com.concordium.sdk.transactions;

import com.concordium.sdk.exceptions.ED25519Exception;

/**
 * The actual signing operation
 */
public interface Signer {
    /**
     * An implementer must sign the message
     *
     * @param message the data to sign
     * @return the signed data i.e., the signature
     * @throws ED25519Exception if the signing process went wrong
     */
    byte[] sign(byte[] message);
}
