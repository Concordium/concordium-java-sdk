package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;
import com.concordium.sdk.serializing.JsonMapper;


public class RequestStatementTest {

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    private IdentityObject getIdentityObject() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/id_object.json", Charset.forName("UTF-8")),
                IdentityObject.class);    
    }
    
    @Test
    public void testCanIdentitySatisfyStatement() throws Exception {
        IdentityObject identityObject = getIdentityObject();

        List<RequestStatement> statements = ProofTest.loadRequest("accountRequest.json").getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        assertTrue(requestStatement1.canBeProvedBy(identityObject));
    }

    @Test
    public void testGetsUnsatisfiedStatement() throws Exception {
        IdentityObject identityObject = getIdentityObject();

        List<RequestStatement> statements = ProofTest.loadRequest("accountRequestUnder18.json").getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        List<AtomicStatement> unsatisfied = requestStatement1.getUnsatisfiedStatements(identityObject);
        assertEquals(1, unsatisfied.size());
        assertEquals("dob", unsatisfied.get(0).getAttributeTag());
        assertEquals(requestStatement1.getStatement().get(0), unsatisfied.get(0));
    }
}
