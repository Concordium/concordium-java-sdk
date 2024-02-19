package com.concordium.sdk.crypto.wallet.web3Id;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.concordium.sdk.Range;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.AtomicStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.MembershipStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.NonMembershipStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RangeStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.RevealStatement;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.responses.cryptographicparameters.CryptographicParameters;
import com.concordium.sdk.serializing.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class ProofTest {

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static public Request loadRequest(String fileName) throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/web3Id/" + fileName, Charset.forName("UTF-8")),
                Request.class
        );
    }

    public List<CommitmentInput> loadCommitmentInputs(String fileName) throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/web3Id/" + fileName, Charset.forName("UTF-8")),
                new TypeReference<List<CommitmentInput>>(){}
        );
    }

    private CryptographicParameters getCryptographicParameters() throws Exception {
        return JsonMapper.INSTANCE.readValue(
                readFile("./src/test/testresources/wallet/global.json", Charset.forName("UTF-8")),
                CryptographicParameters.class);
    }

    @Test
    public void testCanParseRequest() throws Exception {
        Request request = loadRequest("accountRequest.json");

        assertEquals("beefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeefbeef", request.getChallenge());
        
        RequestStatement credentialStatement = request.getCredentialStatements().get(0);
        assertEquals("did:ccd:testnet:cred:a88a8214fc7a7f11aeda54661b76a1fd7c67e15278b83a85ec92cb799ef0abaa3b7c61a7e90ea6bb108fa2ca1a3ba217", credentialStatement.getId());

        Iterator<AtomicStatement> iter = credentialStatement.getStatement().iterator();

        AtomicStatement atomicStatement1 = iter.next();
        assertThat(atomicStatement1, instanceOf(RevealStatement.class));
        RevealStatement revealStatement = ((RevealStatement) atomicStatement1);
        
        assertEquals("firstName", revealStatement.getAttributeTag());

        AtomicStatement atomicStatement2 = iter.next();
        assertThat(atomicStatement2, instanceOf(MembershipStatement.class));
        MembershipStatement membershipStatement = ((MembershipStatement) atomicStatement2);

        assertEquals("nationality", membershipStatement.getAttributeTag());
        membershipStatement.getSet().forEach(a -> System.out.println(a.getValue()));
        System.out.println(membershipStatement.getSet().size());
        assert(membershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("DK")));
        assert(membershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("FR")));
        assert(membershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("GB")));

        AtomicStatement atomicStatement3 = iter.next();
        assertThat(atomicStatement3, instanceOf(NonMembershipStatement.class));
        NonMembershipStatement nonMembershipStatement = ((NonMembershipStatement) atomicStatement3);

        assertEquals("countryOfResidence", nonMembershipStatement.getAttributeTag());
        assert(nonMembershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("DE")));
        assert(nonMembershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("FR")));
        assert(nonMembershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("GB")));

        AtomicStatement atomicStatement4 = iter.next();
        assertThat(atomicStatement4, instanceOf(RangeStatement.class));
        RangeStatement rStatement = ((RangeStatement) atomicStatement4);
        
        assertEquals("dob", rStatement.getAttributeTag());
        assertEquals("18000101", rStatement.getLower().getValue());
        assertEquals("20060215", rStatement.getUpper().getValue());
    }

    @Test
    public void testCanParseCommitments() throws Exception {
        List<CommitmentInput> commitmentInputs = loadCommitmentInputs("accountCommitments.json");

        assertTrue(commitmentInputs.size() > 0);

        CommitmentInput commitmentInput = commitmentInputs.get(0);
        assertThat(commitmentInput, instanceOf(AccountCommitmentInput.class));
        AccountCommitmentInput acInput = (AccountCommitmentInput) commitmentInput;

        assertEquals("19700101", acInput.getValues().get(AttributeType.DOB));
        assertEquals("DK", acInput.getValues().get(AttributeType.COUNTRY_OF_RESIDENCE));
        assertEquals("6879bdc6b5798f7bbeb0f47b6abd9a5c5b84bf594b63e64af5a5df0f239299e9", acInput.getRandomness().get(AttributeType.DOB));
        assertEquals(0, acInput.getIssuer().getValue());
    }

    @Test
    public void testCanCreateProofForAccountCredential() throws Exception {
        Web3IdProofInput input = Web3IdProofInput.builder()
            .request(loadRequest("accountRequest.json"))
            .commitmentInputs(loadCommitmentInputs("accountCommitments.json"))
            .globalContext(getCryptographicParameters())
            .build();
        String proof = Web3IdProof.getWeb3IdProof(input);
        assertNotNull(proof);
    }

    @Test
    public void testCanParseWeb3IdRequest() throws Exception {
        Request request = loadRequest("web3IdRequest.json");

        assertEquals("5d50c6e18aca83e991af81e7d7e760f5dc753db7966dc19e9aa42f25aef1696b", request.getChallenge());
        
        RequestStatement credentialStatement = request.getCredentialStatements().get(0);
        assertEquals("did:ccd:testnet:sci:6105:0/credentialEntry/31163ba14e30b834f1e97b9544d86df94883fd4f2c77e1d1fac0b6189c9e7996", credentialStatement.getId());

        List<String> type = credentialStatement.getType();
        assert(type.stream().anyMatch(item -> item.equals("VerifiableCredential")));
        assert(type.stream().anyMatch(item -> item.equals("ConcordiumVerifiableCredential")));
        assert(type.stream().anyMatch(item -> item.equals("UniversityDegreeCredential"))); 

        Iterator<AtomicStatement> iter = credentialStatement.getStatement().iterator();

        AtomicStatement atomicStatement1 = iter.next();
        assertThat(atomicStatement1, instanceOf(RangeStatement.class));
        RangeStatement rangeStatement = ((RangeStatement) atomicStatement1);

        assertEquals("graduationDate", rangeStatement.getAttributeTag());
        assertEquals("2023-01-29T00:00:00.000Z", rangeStatement.getLower().getValue());
        assertEquals("2023-10-29T00:00:00.000Z", rangeStatement.getUpper().getValue());

        AtomicStatement atomicStatement2 = iter.next();
        assertThat(atomicStatement2, instanceOf(MembershipStatement.class));
        MembershipStatement membershipStatement = ((MembershipStatement) atomicStatement2);

        assertEquals("degreeType", membershipStatement.getAttributeTag());
        assert(membershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("BachelorDegree")));
        assert(membershipStatement.getSet().stream().anyMatch(a -> a.getValue().equals("MasterDegree")));        
    }

    @Test
    public void testCanCreateProofForWeb3IdCredential() throws Exception {
        Web3IdProofInput input = Web3IdProofInput.builder()
            .request(loadRequest("web3IdRequest.json"))
            .commitmentInputs(loadCommitmentInputs("web3IdCommitments.json"))
            .globalContext(getCryptographicParameters())
            .build();
        String proof = Web3IdProof.getWeb3IdProof(input);
        assertNotNull(proof);
    }

}
