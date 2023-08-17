package com.concordium.sdk.smartcontract;

import com.concordium.sdk.exceptions.CryptoJniException;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.Schema;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test ensures correct serialization of {@link com.concordium.sdk.transactions.SchemaParameter}.<p>
 * Specifically custom serialization of {@link com.concordium.sdk.types.AbstractAddress} is tested.<p>
 * Uses {@link MintParams} to model 'MintParams' in the <a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-nft/src/lib.rs">cis2-nft example</a>
 */
public class SerializeParameterTest {

    /**
     * Schema for a cis2-nft contract
     */
    private static final Schema SCHEMA;
    /**
     * entrypoint for method taking {@link MintParams} as parameter.
     */
    private static final ReceiveName RECEIVE_NAME = ReceiveName.from("cis2_nft", "mint");
    private static final AccountAddress ACCOUNT_ADDRESS = AccountAddress.from("3XSLuJcXg6xEua6iBPnWacc3iWh93yEDMCqX8FbE3RDSbEnT9P");
    private static final ContractAddress CONTRACT_ADDRESS = ContractAddress.from(1, 0);
    private static final List<String> TOKENS = new ArrayList<>();
    /**
     * Incorrectly formatted tokens.
     */
    private static final List<String> INCORRECT_TOKENS = new ArrayList<>();

    /**
     * Populate TOKENS and initialize SCHEMA
     */
    static {
        TOKENS.add("21");
        TOKENS.add("22");
        TOKENS.add("23");
        INCORRECT_TOKENS.add("2");
        INCORRECT_TOKENS.add("321");
        try {
            SCHEMA = Schema.from(Files.readAllBytes(Paths.get("./src/test/java/com/concordium/sdk/smartcontract/cis2-nft.schema.bin")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void shouldSerializeWithAccountAddress() {
        val mintParamsWithAccountAddress = new MintParams(SCHEMA, RECEIVE_NAME, ACCOUNT_ADDRESS, TOKENS);
        try {
            mintParamsWithAccountAddress.initialize();
        } catch (Exception e) {
            fail();
        }
    }

    @SneakyThrows
    @Test
    public void shouldSerializeWithContractAddress() {
        val mintParamsWithContractAddress = new MintParams(SCHEMA, RECEIVE_NAME, CONTRACT_ADDRESS, TOKENS);
        try {
            mintParamsWithContractAddress.initialize();
        } catch (Exception ex) {
            fail();
        }
    }

    @SneakyThrows
    @Test
    public void shouldThrowExcpetionOnInvalidParameter() {
        IncorrectParams incorrectParamsClass = new IncorrectParams(SCHEMA, RECEIVE_NAME);
        MintParams mintParamsWrongTokens = new MintParams(SCHEMA, RECEIVE_NAME, ACCOUNT_ADDRESS, INCORRECT_TOKENS);
        int failed = 0;
        try {
            incorrectParamsClass.initialize();
        } catch (CryptoJniException ex) {
            assertNotNull(ex.getErrorType());
            assertNotNull(ex.getErrorMessage());
            failed++;
        }
        try {
            mintParamsWrongTokens.initialize();
        } catch (CryptoJniException ex) {
            assertNotNull(ex.getErrorType());
            assertNotNull(ex.getErrorMessage());
            failed++;
        }
        // Makes sure an exception is thrown in both cases;
        assertEquals(failed, 2);
    }


}
