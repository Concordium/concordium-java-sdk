package com.concordium.sdk.responses.chainparameters;

import com.concordium.grpc.v2.UpdatePublicKey;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.google.protobuf.ByteString;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.val;

import java.util.stream.Collectors;

/**
 * Authorizations for chain parameters version 1.
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
public class AuthorizationsV1 extends AuthorizationsV0 implements Authorizations {

    /**
     * Keys allowed to alter the cooldown parameters.
     */
    private final AccessStructure cooldownParameters;

    /**
     * Keys allowed to alter the time parameters.
     */
    private final AccessStructure timeParameters;

    public static AuthorizationsV1 from(com.concordium.grpc.v2.AuthorizationsV1 value) {
        val v0Authorizations = value.getV0();
        val keys = v0Authorizations.getKeysList().stream().map(UpdatePublicKey::getValue).map(ByteString::toByteArray).map(ED25519PublicKey::from).collect(Collectors.toSet());
        return AuthorizationsV1
                .builder()
                .addAnonymityRevoker(AccessStructure.from(v0Authorizations.getAddAnonymityRevoker()))
                .addIdentityProvider(AccessStructure.from(v0Authorizations.getAddIdentityProvider()))
                .emergency(AccessStructure.from(v0Authorizations.getEmergency()))
                .consensusParameters(AccessStructure.from(v0Authorizations.getParameterConsensus()))
                .mintDistribution(AccessStructure.from(v0Authorizations.getParameterMintDistribution()))
                .euroPerEnergy(AccessStructure.from(v0Authorizations.getParameterEuroPerEnergy()))
                .poolParameters(AccessStructure.from(v0Authorizations.getPoolParameters()))
                .protocol(AccessStructure.from(v0Authorizations.getProtocol()))
                .gasRewards(AccessStructure.from(v0Authorizations.getParameterGasRewards()))
                .foundationAccount(AccessStructure.from(v0Authorizations.getParameterFoundationAccount()))
                .microCCDPerEuro(AccessStructure.from(v0Authorizations.getParameterMicroCCDPerEuro()))
                .transactionFeeDistribution(AccessStructure.from(v0Authorizations.getParameterTransactionFeeDistribution()))
                .keys(keys)
                .timeParameters(AccessStructure.from(value.getParameterTime()))
                .cooldownParameters(AccessStructure.from(value.getParameterCooldown()))
                .build();
    }

    @Override
    public AuthorizationsType getType() {
        return AuthorizationsType.V2;
    }
}
