package com.concordium.sdk.crypto.wallet;

public enum Network {
    MAINNET,
    TESTNET;

    public String getValue() {
        switch (this.ordinal()) {
            case 0: return "Mainnet";
            case 1: return "Testnet";
            default: throw new IllegalArgumentException("An unsupported network was provided");
        }
    }
}
