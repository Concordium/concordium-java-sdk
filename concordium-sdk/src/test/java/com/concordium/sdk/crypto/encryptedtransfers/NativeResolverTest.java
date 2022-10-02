package com.concordium.sdk.crypto.encryptedtransfers;

import org.junit.Test;

public class NativeResolverTest {

    @Test
    public void testCanLoadNativeLib (){
        NativeResolver.loadLib();
    }
}
