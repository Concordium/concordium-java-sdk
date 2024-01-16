package com.concordium.sdk.crypto.wallet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NetworkTest {

    @Test
    public void testMainnetNetworkValue() {
        Network mainnet = Network.MAINNET;

        String mainnetValue = mainnet.getValue();

        assertEquals("Mainnet", mainnetValue);
    }

    @Test
    public void testTestnetNetworkValue() {
        Network testnet = Network.TESTNET;

        String testnetValue = testnet.getValue();

        assertEquals("Testnet", testnetValue);
    }
}
