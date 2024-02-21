package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.concordium.sdk.crypto.wallet.FileHelpers;
import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;

public class RequestStatementTest {
 
    @Test
    public void testCanIdentitySatisfyStatement() throws Exception {
        IdentityObject identityObject = FileHelpers.getIdentityObject();

        List<RequestStatement> statements = ProofTest.loadRequest("accountRequest.json").getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        assertTrue(requestStatement1.canBeProvedBy(identityObject));
    }

    @Test
    public void testGetsUnsatisfiedStatement() throws Exception {
        IdentityObject identityObject = FileHelpers.getIdentityObject();

        List<RequestStatement> statements = ProofTest.loadRequest("accountRequestUnder18.json").getCredentialStatements();
        RequestStatement requestStatement1 = statements.get(0);
        List<AtomicStatement> unsatisfied = requestStatement1.getUnsatisfiedStatements(identityObject);
        assertEquals(1, unsatisfied.size());
        assertEquals("dob", unsatisfied.get(0).getAttributeTag());
        assertEquals(requestStatement1.getStatement().get(0), unsatisfied.get(0));
    }
}
