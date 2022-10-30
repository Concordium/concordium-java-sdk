package com.concordium.sdk.crypto.bakertransactions;

import lombok.SneakyThrows;
import org.junit.Test;

public class BakerKeysTest {
    @SneakyThrows
    @Test
    public void shouldCreatePayloadTest() {
        BakerKeys.createBakerKeys();
    }
}
