package com.concordium.sdk.transactions.schema;

import com.concordium.sdk.types.UInt;
import com.concordium.sdk.types.UInt16;
import com.concordium.sdk.types.UInt32;
import com.concordium.sdk.types.UInt64;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
class SizeLength {

    private final SchemaTypeUInt uInt = new SchemaTypeUInt();
    private final SchemaTypeUInt16 uInt16 = new SchemaTypeUInt16();
    private final SchemaTypeUInt32 uInt32 = new SchemaTypeUInt32();
    private final SchemaTypeUInt64 uInt64 = new SchemaTypeUInt64();

    private final UInt num;

    public byte[] getValueBytes(long value) {
        switch (num.getValue()) {
            case 0:
                return uInt.getSchemaBytes(UInt.from(Math.toIntExact(value)));
            case 1:
                return uInt16.getSchemaBytes(UInt16.from(Math.toIntExact(value)));
            case 2:
                return uInt32.getSchemaBytes(UInt32.from(Math.toIntExact(value)));
            case 3:
                return uInt64.getSchemaBytes(UInt64.from(value));
            default:
                throw new RuntimeException("invalid size length: " + num);
        }
    }

    static SizeLength from(final UInt num) {
        return new SizeLength(num);
    }

    static SizeLength from(final int num) {
        return new SizeLength(UInt.from(num));
    }

    public long getValue(ByteBuffer buff) {
        switch (num.getValue()) {
            case 0:
                return uInt.fromSchemaBytes(buff).getValue();
            case 1:
                return uInt16.fromSchemaBytes(buff).getValue();
            case 2:
                return uInt32.fromSchemaBytes(buff).getValue();
            case 3:
                return uInt64.fromSchemaBytes(buff).getValue();
            default:
                throw new RuntimeException("invalid size length: " + num);
        }
    }
}
