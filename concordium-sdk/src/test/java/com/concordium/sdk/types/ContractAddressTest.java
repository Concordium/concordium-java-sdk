package com.concordium.sdk.types;

import lombok.val;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class ContractAddressTest {

    @Test
    public void testSerializeDeserializeContractAddress() {
        val contractAddress = ContractAddress.from(1, 0);
        assertEquals(contractAddress, ContractAddress.from(ByteBuffer.wrap(contractAddress.getBytes())));
    }
}
