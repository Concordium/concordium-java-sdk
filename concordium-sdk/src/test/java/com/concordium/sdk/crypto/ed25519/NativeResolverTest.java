package com.concordium.sdk.crypto.ed25519;

import org.junit.Test;

public class NativeResolverTest {

    @Test
    public void testCanLoadNativeLib (){
        NativeResolver.loadLib();
    }
}
