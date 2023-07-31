package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.transactions.ConfigureBakerKeysPayload;
import com.concordium.sdk.types.AccountAddress;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BakerKeysTest {
    @SneakyThrows
    @Test
    public void shouldCreateBakerKeysTest() {
        val bakerKeys = BakerKeys.createBakerKeys();
        assertNotNull(bakerKeys.getElectionVerifyKey());
        assertNotNull(bakerKeys.getSignatureSignKey());
        assertNotNull(bakerKeys.getAggregationSignKey());
        assertNotNull(bakerKeys.getSignatureSignKey());
        assertNotNull(bakerKeys.getElectionPrivateKey());
        assertNotNull(bakerKeys.getAggregationSignKey());
    }

    @SneakyThrows
    @Test
    public void shouldConfigureEmptyBakerKeysTest() {
        BakerKeys bakerKeys = BakerKeys.createBakerKeys();
        AccountAddress sender = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");
        ConfigureBakerKeysJniInput input = ConfigureBakerKeysJniInput.builder()
                .keys(bakerKeys)
                .sender(sender)
                .build();

        ConfigureBakerKeysJniOutput output = ConfigureBakerKeys.generateConfigureBakerKeysPayload(input);
        assertNotNull(output.getElectionVerifyKey());
        assertNotNull(output.getSignatureVerifyKey());
        assertNotNull(output.getAggregationVerifyKey());
        assertNotNull(output.getProofSig());
        assertNotNull(output.getProofElection());
        assertNotNull(output.getProofAggregation());
    }

    @SneakyThrows
    @Test
    public void shouldCreateNewBakerKeysTest() {
        AccountAddress sender = AccountAddress.from("48x2Uo8xCMMxwGuSQnwbqjzKtVqK5MaUud4vG7QEUgDmYkV85e");
        val bakerKeys = ConfigureBakerKeysPayload.getNewConfigureBakerKeysPayload(sender, BakerKeys.createBakerKeys());
        assertEquals(352, bakerKeys.getBytes().length);
    }
}
