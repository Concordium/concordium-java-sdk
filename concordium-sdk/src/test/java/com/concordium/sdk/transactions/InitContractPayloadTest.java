package com.concordium.sdk.transactions;

import org.junit.Test;


public class InitContractPayloadTest {

    @Test
    public void testCreatePayload() {
        byte[] emptyArray = new byte[0];
        Hash moduleRef = Hash.from("37eeb3e92025c97eaf40b66891770fcd22d926a91caeb1135c7ce7a1ba977c07");
        InitContractPayload.from(0, moduleRef.getBytes(), "CIS2-NFT", emptyArray);
    }

}
