package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.ByteList;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@EqualsAndHashCode
@Getter
public class Cis2ContractTokenId extends ByteList {

    @JsonCreator
    Cis2ContractTokenId(final UInt32 value) {
        super(value.getBytes());
    }

    Cis2ContractTokenId(final String hexEncodedBytes) throws DecoderException {
        super(hexEncodedBytes);
    }

    public static Cis2ContractTokenId from(final UInt32 value) {
        try {
            return new Cis2ContractTokenId(Hex.encodeHexString(value.getBytes()));
        } catch (DecoderException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Cis2ContractTokenId from(String hexEncodedBytes) {
        try {
            return new Cis2ContractTokenId(hexEncodedBytes);
        } catch (DecoderException e) {
            throw new RuntimeException("Could not construct ContractTokenId from HexInput", e);
        }
    }
}
