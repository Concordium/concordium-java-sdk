package com.concordium.sdk.crypto.wallet.web3Id;

import com.concordium.sdk.crypto.wallet.web3Id.Statement.RevealStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.Hash;
import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

public class VerificationRequestV1Test {

    private final VerificationRequestV1 REQUEST = VerificationRequestV1.builder()
            .context(
                    UnfilledContextInformation.builder()
                            .givenContext(
                                    new GivenContext(
                                            "Nonce",
                                            "0101010101010101010101010101010101010101010101010101010101010101"
                                    )
                            )
                            .givenContext(
                                    new GivenContext(
                                            "ConnectionID",
                                            "0102010201020102010201020102010201020102010201020102010201020102"
                                    )
                            )
                            .givenContext(
                                    new GivenContext(
                                            "ContextString",
                                            "Wine payment"
                                    )
                            )
                            .requestedContext(GivenContext.BLOCK_HASH_LABEL)
                            .requestedContext(GivenContext.RESOURCE_ID_LABEL)
                            .build()
            )
            .addSubjectClaims(
                    IdentityClaims.builder()
                            .source(ImmutableSet.<String>builder()
                                    .add(IdentityClaims.IDENTITY_CREDENTIAL_SOURCE)
                                    .build()
                            )
                            .statement(
                                    RevealStatement.builder()
                                            .attributeTag("firstName")
                                            .build()
                            )
                            .issuer(IdentityProviderRequestIdentifier.fromString("did:ccd:testnet:idp:0"))
                            .issuer(IdentityProviderRequestIdentifier.fromString("did:ccd:testnet:idp:1"))
                            .issuer(IdentityProviderRequestIdentifier.fromString("did:ccd:testnet:idp:2"))
                            .build()
            )
            .transactionRef(Hash.from("0102030401020304010203040102030401020304010203040102030401020304"))
            .build();

    @Test
    @SneakyThrows
    public void jsonSerialization() {
        val expected = "{\"type\":\"ConcordiumVerificationRequestV1\",\"context\":{\"given\":[{\"label\":\"Nonce\",\"context\":\"0101010101010101010101010101010101010101010101010101010101010101\"},{\"label\":\"ConnectionID\",\"context\":\"0102010201020102010201020102010201020102010201020102010201020102\"},{\"label\":\"ContextString\",\"context\":\"Wine payment\"}],\"requested\":[\"BlockHash\",\"ResourceID\"],\"type\":\"ConcordiumUnfilledContextInformationV1\"},\"subjectClaims\":[{\"type\":\"identity\",\"source\":[\"identityCredential\"],\"statements\":[{\"type\":\"RevealAttribute\",\"attributeTag\":\"firstName\"}],\"issuers\":[\"did:ccd:testnet:idp:0\",\"did:ccd:testnet:idp:1\",\"did:ccd:testnet:idp:2\"]}],\"transactionRef\":\"0102030401020304010203040102030401020304010203040102030401020304\"}";
        Assert.assertEquals(
                expected,
                JsonMapper.INSTANCE.writeValueAsString(REQUEST)
        );
        Assert.assertEquals(
                REQUEST,
                JsonMapper.INSTANCE.readValue(expected, VerificationRequestV1.class)
        );
    }

    @Test
    @SneakyThrows
    public void anchorVerification() {
        val anchor = VerificationRequestAnchor.builder()
                .hash(VerificationRequestV1.getAnchorHash(REQUEST.getContext(), REQUEST.getSubjectClaims()))
                .version(VerificationRequestAnchor.V1)
                .build();
        Assert.assertEquals(
                "eb2499bd1ab8f24357945bb59e05a51c4cf19631d20bf1cb1bfc2c5974083ca9",
                anchor.getHash().toString()
        );
        Assert.assertTrue(REQUEST.verifyAnchor(anchor));
    }
}
