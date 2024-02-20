package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.concordium.sdk.crypto.wallet.ConcordiumHdWallet;
import com.concordium.sdk.crypto.wallet.FileHelpers;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.QualifiedRequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.UnqualifiedRequestStatement;
import com.concordium.sdk.serializing.JsonMapper;

public class RequestStatementTest {

    static public UnqualifiedRequest loadUnqualifiedRequest(String fileName) throws Exception {
        return JsonMapper.INSTANCE.readValue(
                FileHelpers.readFile("./src/test/testresources/wallet/web3Id/" + fileName, Charset.forName("UTF-8")),
                UnqualifiedRequest.class);
    }

    private static String TEST_SEED = "efa5e27326f8fa0902e647b52449bf335b7b605adc387015ec903f41d95080eb71361cbc7fb78721dcd4f3926a337340aa1406df83332c44c1cdcfe100603860";

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
    public void testCanQualifyStatement() throws Exception {
        UnqualifiedRequest request = loadUnqualifiedRequest("unqualifiedRequest.json");

        Network network = Network.MAINNET;
        ConcordiumHdWallet wallet = ConcordiumHdWallet.fromHex(TEST_SEED, network);
        // Parameters taken from ConcordiumHdWalletTest.testMainnetCredentialId
        QualifiedRequest qualifiedRequest = request.qualify(Arrays.asList(wallet.getCredentialId(10, 50, 5,
                "b14cbfe44a02c6b1f78711176d5f437295367aa4f2a8c2551ee10d25a03adc69d61a332a058971919dad7312e1fc94c5a8d45e64b6f917c540eee16c970c3d4b7f3caf48a7746284878e2ace21c82ea44bf84609834625be1f309988ac523fac")),
                network);
        QualifiedRequestStatement qualifiedStatement = qualifiedRequest.getCredentialStatements().get(0);
        assertEquals(
                "did:ccd:mainnet:cred:8a3a87f3f38a7a507d1e85dc02a92b8bcaa859f5cf56accb3c1bc7c40e1789b4933875a38dd4c0646ca3e940a02c42d8",
                qualifiedStatement.getId());
        assertEquals(request.getCredentialStatements().get(0).getStatement(), qualifiedStatement.getStatement());
    }
}
