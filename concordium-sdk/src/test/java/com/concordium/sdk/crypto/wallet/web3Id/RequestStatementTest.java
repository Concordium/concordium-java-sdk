package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.wallet.FileHelpers;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.AcceptableRequest.NotAcceptableException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute.CredentialAttributeType;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.QualifiedRequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RangeStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.StatementType;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.concordium.sdk.types.ContractAddress;

public class RequestStatementTest {

    static public UnqualifiedRequest loadUnqualifiedRequest(String fileName) throws Exception {
        return JsonMapper.INSTANCE.readValue(
                FileHelpers.readFile("./src/test/testresources/wallet/web3Id/" + fileName, Charset.forName("UTF-8")),
                UnqualifiedRequest.class);
    }

    @Test
    public void testCanIdentitySatisfyStatement() throws Exception {
        IdentityObject identityObject = FileHelpers.getIdentityObject();

        List<QualifiedRequestStatement> statements = ProofTest.loadRequest("accountRequest.json")
                .getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        assertTrue(requestStatement1.canBeProvedBy(identityObject));
    }

    @Test
    public void testGetsUnsatisfiedStatement() throws Exception {
        IdentityObject identityObject = FileHelpers.getIdentityObject();

        List<QualifiedRequestStatement> statements = ProofTest.loadRequest("accountRequestUnder18.json")
                .getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        List<AtomicStatement> unsatisfied = requestStatement1.getUnsatisfiedStatements(identityObject);
        assertEquals(1, unsatisfied.size());
        assertEquals("dob", unsatisfied.get(0).getAttributeTag());
        assertEquals(requestStatement1.getStatement().get(0), unsatisfied.get(0));
    }

    @Test
    public void testIsAcceptableRequest() throws Exception {
        QualifiedRequest request = ProofTest.loadRequest("accountRequest.json");
        AcceptableRequest.acceptableRequest(request);
    }

    @Test
    public void testIsAcceptableAtomicStatement() throws NotAcceptableException {
        AtomicStatement statement = RangeStatement.builder().attributeTag("dob")
                .lower(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20140112").build())
                .upper(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20150112").build())
                .build();

        AcceptableRequest.acceptableAtomicStatement(statement, Collections.singletonList("dob"), Collections.emptyList(), new AttributeCheck() {
            @Override
            public void checkAttribute(String tag, CredentialAttribute value) {}});
    }

    @Test(expected = AcceptableRequest.NotAcceptableException.class)
    public void testIllegalTagStatementThrowsNotAcceptable() throws NotAcceptableException {
        AtomicStatement statement = RangeStatement.builder().attributeTag("dob")
                .lower(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20140112").build())
                .upper(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20150112").build())
                .build();

        AcceptableRequest.acceptableAtomicStatement(statement, Collections.emptyList(), Collections.emptyList(), new AttributeCheck() {
            @Override
            public void checkAttribute(String tag, CredentialAttribute value) {}});
    }

    @Test(expected = AcceptableRequest.NotAcceptableException.class)
    public void testAttributeCheckThrowingErrorReturnsNotAcceptable() throws NotAcceptableException {
        AtomicStatement statement = RangeStatement.builder().attributeTag("dob")
                .lower(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20140112").build())
                .upper(CredentialAttribute.builder().type(CredentialAttributeType.STRING).value("20150112").build())
                .build();

        AcceptableRequest.acceptableAtomicStatement(statement, Collections.emptyList(), Collections.emptyList(), new AttributeCheck() {
            @Override
            public void checkAttribute(String tag, CredentialAttribute value) throws Exception {
                throw new Exception("Not okay");
            }});
    }
    @Test
    public void testCanQualifyStatement() throws Exception {
        // Arrange
        UnqualifiedRequest request = loadUnqualifiedRequest("unqualifiedRequest.json");

        Network network = Network.MAINNET;
        CredentialRegistrationId credId = CredentialRegistrationId.from(
                "8a3a87f3f38a7a507d1e85dc02a92b8bcaa859f5cf56accb3c1bc7c40e1789b4933875a38dd4c0646ca3e940a02c42d8");
        ContractAddress contractAddress = ContractAddress.from(1232, 3);
        ED25519PublicKey publicKey = ED25519PublicKey

                .from("16afdb3cb3568b5ad8f9a0fa3c741b065642de8c53e58f7920bf449e63ff2bf9");

        // Act
        QualifiedRequest qualifiedRequest = request.qualify(statement -> {
            if (statement.getIdQualifier().getType().equals(StatementType.Credential)) {
                return statement.qualify(credId, network);
            } else {
                return statement.qualify(contractAddress, publicKey, network);
            }
        });

        // Assert
        QualifiedRequestStatement qualifiedAccountStatement = qualifiedRequest.getCredentialStatements().get(0);
        assertEquals(
                "did:ccd:mainnet:cred:8a3a87f3f38a7a507d1e85dc02a92b8bcaa859f5cf56accb3c1bc7c40e1789b4933875a38dd4c0646ca3e940a02c42d8",
                qualifiedAccountStatement.getId().toString());
        assertEquals(request.getCredentialStatements().get(0).getStatement(), qualifiedAccountStatement.getStatement());
        QualifiedRequestStatement qualifiedWeb3IdStatement = qualifiedRequest.getCredentialStatements().get(1);
        assertEquals(
                "did:ccd:mainnet:sci:1232:3/credentialEntry/16afdb3cb3568b5ad8f9a0fa3c741b065642de8c53e58f7920bf449e63ff2bf9",
                qualifiedWeb3IdStatement.getId().toString());
        assertEquals(request.getCredentialStatements().get(1).getStatement(), qualifiedWeb3IdStatement.getStatement());
    }
}
