package com.concordium.sdk.crypto.wallet;

public enum Network {
    Mainnet,
    Testnet;

    public String getValue() {
        switch (this.ordinal()) {
            case 0: return "Mainnet";
            case 1: return "Testnet";
            default: throw new IllegalArgumentException("An unsupported network was provided");
        }
    }
}
