package com.concordium.sdk.responses.transactionevent.updatepayloads;

import com.concordium.grpc.v2.MintRate;

/**
 * Various helper methods for parsing {@link com.concordium.grpc.v2.UpdatePayload}.
 */
interface ParsingHelper {

    /**
     * Calculates mantissa*10^(-exponent).
     */
     static double toMintRate (MintRate mintRate) {
        return mintRate.getMantissa()*Math.pow(10, -1 * mintRate.getExponent());
    }
}
