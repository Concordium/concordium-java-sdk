package com.concordium.sdk.responses.cryptographicparameters;

import com.concordium.sdk.crypto.bulletproof.BulletproofGenerators;
import com.concordium.sdk.crypto.pedersencommitment.PedersenCommitmentKey;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * A Set of Cryptographic parameters that are particular to the chain and
 * shared by everybody who interacts with the chain.
 */
@Data
@Builder
@Jacksonized
public final class CryptographicParameters {

    /**
     * The version of the cryptographic parameters.
     */
    private final int version;

    /**
     * The bulletproof generators
     */
    private final BulletproofGenerators bulletproofGenerators;

    /**
     * The on chain commitment key
     */
    private final PedersenCommitmentKey onChainCommitmentKey;

    /**
     * The genesis string.
     */
    private final String genesisString;
}
