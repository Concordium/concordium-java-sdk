package com.concordium.sdk.examples;

import com.concordium.sdk.ClientV2;
import com.concordium.sdk.Connection;
import com.concordium.sdk.TLSConfig;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.*;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RevealStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.did.IdentityProviderRequestIdentifier;
import com.concordium.sdk.requests.BlockQuery;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.UInt32;
import com.google.common.collect.ImmutableSet;
import picocli.CommandLine;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * An example of creating a verifiable presentation in a wallet.
 */
@CommandLine.Command(name = "CreateVerifiablePresentation", mixinStandardHelpOptions = true)
public class CreateVerifiablePresentation implements Callable<Integer> {

    @CommandLine.Option(
            names = {"--endpoint"},
            description = "GRPC interface of the node.",
            defaultValue = "https://grpc.testnet.concordium.com:20000")
    private String endpoint;

    @Override
    public Integer call() throws Exception {

        URL endpointUrl = new URL(this.endpoint);
        Connection connection = Connection.newBuilder()
                .host(endpointUrl.getHost())
                .port(endpointUrl.getPort())
                .useTLS(TLSConfig.auto())
                .build();
        ClientV2 client = ClientV2.from(connection);

        // This is a verification request created by a connected site.
        // It can be deserialized from JSON with JsonMapper.INSTANCE,
        // but here we build it ourselves
        VerificationRequestV1 request = VerificationRequestV1.builder()
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
                                        .add(IdentityClaims.ACCOUNT_CREDENTIAL_SOURCE)
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

        System.out.println("Request is created");

        // This is a Verification Request Anchor (VRA) for the request.
        // It is the content of a RegisterData transaction which is fetched
        // by the wallet using transactionRef hash of the request.
        // It can be deserialized from CBOR HEX from the dataRegistered transaction details
        // with CborMapper.INSTANCE, but here we create it ourselves.
        VerificationRequestAnchor anchor = VerificationRequestAnchor.builder()
                .version(VerificationRequestAnchor.V1)
                .hash(VerificationRequestV1.getAnchorHash(request.getContext(), request.getSubjectClaims()))
                .build();

        System.out.println("Anchor is created");

        // The request should be verified against the anchor.
        if (!request.verifyAnchor(anchor)) {
            throw new IllegalStateException("Anchor is not valid");
        }

        System.out.println("Anchor is valid");

        // The request has claims which must be proven.
        // At the moment, the only claims type is IdentityClaims.
        IdentityClaims identityClaims = (IdentityClaims) request.getSubjectClaims().get(0);

        // The wallet must present the claims to the user.
        // The user can pick identity or account which satisfy all the statements from the claims.
        // Different claims can be proven with different credentials.
        // Here we prove the claims with an account credential.
        IdentityClaimsAccountProofInput proofInput;

        // To create a proof input, the following data is required:

        // Wallet network.
        Network network = Network.TESTNET;
        // Index of the identity provider that issued the account's identity.
        UInt32 ipIdentity = UInt32.from(0);
        // Credential registration ID stored in the wallet after account creation or recovery.
        CredentialRegistrationId credId = CredentialRegistrationId.from("9052365696509601fe89043cdee14656388eb29bd7b2751a8199e2d7a2cfcadffe7c47f565c28d0a7524e3af2bb7bf7f");
        // Account identity attribute values stored in the wallet after account creation or recovery.
        // Here we fill it manually, and the only attribute we need is the first name.
        Map<AttributeType, String> attributeValues = new HashMap<>();
        attributeValues.put(AttributeType.FIRST_NAME, "John");
        // Account attribute randomness vales stored in the wallet after account creation or recovery.
        // Here we fill it manually, and the only attribute we need is the first name.
        Map<AttributeType, String> attributeRandomness = new HashMap<>();
        attributeRandomness.put(AttributeType.FIRST_NAME, "552b0ffed699894f25c8f2732acf1aba7b7871e76cfd995b08ccc8b3863233e5");

        proofInput = identityClaims
                .getAccountProofInput(network, ipIdentity, credId, attributeValues, attributeRandomness);

        System.out.println("Proof input is created");

        // To create a presentation for the given verification request,
        // not only proofs but also a requested context information is required.
        List<GivenContext> providedContext = new ArrayList<>();
        for (String requestedContextInfoLabel : request.getContext().getRequested()) {
            switch (requestedContextInfoLabel) {
                // If a block hash is requested,
                // the wallet puts the hash of the block
                // in which the anchor transaction was found finalized.
                // Since we didn't actually fetch the transaction, we mock the hash.
                case GivenContext.BLOCK_HASH_LABEL:
                    providedContext.add(GivenContext.builder()
                            .label(requestedContextInfoLabel)
                            .context("00000000839a8e6886ab5951d76f411475428afc90947ee320161bbf18eb6048")
                            .build()
                    );
                    break;
                // If a resource ID is requested,
                // the wallet puts the URL of the connected site.
                case GivenContext.RESOURCE_ID_LABEL:
                    providedContext.add(GivenContext.builder()
                            .label(requestedContextInfoLabel)
                            .context("https://connectedsite.online")
                            .build()
                    );
                    break;
                // At the moment, the wallet doesn't need to support any other context information.
                default:
                    throw new IllegalStateException("Can't provide " + requestedContextInfoLabel);
            }
        }

        System.out.println("Requested context info is collected");

        // The wallet must also fetch the network cryptographic parameters,
        // which is also called global context.
        CryptographicParameters globalContext = client.getCryptographicParameters(BlockQuery.BEST);

        // Everything is ready to create the presentation.
        // The result must be sent back to the connected site.
        String verifiablePresentationJson = VerifiablePresentationV1.getVerifiablePresentation(
                request,
                Collections.singletonList(proofInput),
                providedContext,
                globalContext
        );

        System.out.println("The presentation:");
        System.out.println(verifiablePresentationJson);

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CreateVerifiablePresentation()).execute(args);
        System.exit(exitCode);
    }
}
