package com.concordium.sdk.transactions;

import org.junit.Test;


public class UpdateContractPayloadTest {

    @Test
    public void testCreatePayload() {
        byte[] emptyArray = new byte[0];
        UpdateContractPayload.from(
                0,
                ContractAddress.from(81, 0),
                "CIS2-NFT",
                "mint",
                emptyArray);
    }
}
