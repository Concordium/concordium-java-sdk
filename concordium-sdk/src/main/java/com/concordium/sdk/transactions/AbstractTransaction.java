package com.concordium.sdk.transactions;

import lombok.Setter;

public class AbstractTransaction implements Transaction {

    @Setter
    private BlockItem item;

    @Override
    public byte[] getBytes() {
        return item.getVersionedBytes();
    }

    @Override
    public Hash getHash() {
        return item.getHash();
    }

    @Override
    public int getNetworkId() {
        return DEFAULT_NETWORK_ID;
    }
}
