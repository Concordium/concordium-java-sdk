package com.concordium.sdk.transactions;

import com.concordium.sdk.responses.transactionstatus.ContractAddress;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class UpdateContractPayloadTest {

    @Test
    public void testCreatePayload() {
        byte[] emptyArray = new byte[0];
        val payload = UpdateContractPayload.from(
                0,
                ContractAddress.from(81, 0),
                "CIS2-NFT",
                "mint",
                emptyArray);
        assertNotNull(payload);
    }
}
