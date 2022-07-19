package com.concordium.sdk.transactions;

abstract class AbstractTransaction implements Transaction {

    abstract BlockItem getBlockItem();

    @Override
    public byte[] getSerializedPayload() {
        return getBlockItem().getVersionedBytes();
    }

    @Override
    public Hash getHash() {
        return getBlockItem().getHash();
    }

    @Override
    public int getNetworkId() {
        return DEFAULT_NETWORK_ID;
    }
}
