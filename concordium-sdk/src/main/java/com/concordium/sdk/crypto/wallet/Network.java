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

    static public Network fromLowerCase(String value) {
        switch (value) {
            case "mainnet":
                return MAINNET;
            case "testnet":
                return TESTNET;
            default:
                throw new IllegalArgumentException("An unsupported network was provided");
        }
    }
}
