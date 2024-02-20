package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import org.apache.commons.codec.binary.Hex;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.ContractAddress;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
@Jacksonized
public class UnqualifiedRequestStatement extends RequestStatement {
    private IdQualifier idQualifier;

    public QualifiedRequestStatement qualify(CredentialRegistrationId credId, Network network) {
        if (!idQualifier.getType().equals(IdQualifier.QualifierType.Credential)) {
            throw new IllegalArgumentException("Only an account statement may only be qualified using a credentialId");
        }
        String did = "did:ccd:" + network.getValue().toLowerCase() + ":cred:" + credId.getEncoded();
        return QualifiedRequestStatement.builder().id(did).statement(getStatement()).build();
    }

    public QualifiedRequestStatement qualify(ContractAddress contract, ED25519PublicKey publicKey, Network network) {
        if (!idQualifier.getType().equals(IdQualifier.QualifierType.SmartContract)) {
            throw new IllegalArgumentException(
                    "Only an web3Id statement may only be qualified using a Contract Address");
        }
        if (!((Web3IdIssuerQualifier) idQualifier).getIssuers().contains(contract)) {
            throw new IllegalArgumentException("The Contract Address must be one specified in the qualifier");
        }

        String did = "did:ccd:" +
                network.getValue().toLowerCase() +
                ":sci:" +
                contract.getIndex() +
                ":" +
                contract.getSubIndex() +
                "/credentialEntry/" +
                Hex.encodeHexString(publicKey.getRawBytes());
        return QualifiedRequestStatement.builder().id(did).statement(getStatement()).build();
    }
}
