package com.concordium.sdk.transactions.tokens;

import com.concordium.grpc.v2.plt.TokenAmount;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.cbor.CBORConstants;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * An amount for a protocol-level token (PLT) operation.
 * It is very important for decimals to match the actual value of the token.
 *
 * @see <a href="https://github.com/Concordium/concordium-update-proposals/blob/main/source/CIS/cis-7.rst#token-amount">CBOR Schema</a>
 */
@Getter
@EqualsAndHashCode
@ToString
@JsonSerialize(
        using = TokenOperationAmount.CborSerializer.class
        // And deserialization works via the @JsonCreator constructor.
)
public class TokenOperationAmount {

    /**
     * The integer amount.
     * For example, "1.5" for a token with 6 decimals is "1500000".
     */
    private final UInt64 value;

    /**
     * The number of token decimals.
     * It is very important for this value to match the actual value of the token.
     */
    private final int decimals;

    public TokenOperationAmount(UInt64 value,
                                int decimals) {
        this.value = value;

        if (decimals < 0) {
            throw new IllegalArgumentException("The number of decimals can't be negative");
        } else if (decimals > 255) {
            throw new IllegalArgumentException("The number of decimals can't exceed 255");
        }

        this.decimals = decimals;
    }

    public TokenOperationAmount(BigDecimal decimalValue,
                                int decimals) {
        this(
                new UInt64(
                        decimalValue
                                .movePointRight(decimals)
                                .toBigInteger()
                ),
                decimals
        );
    }

    /**
     * A constructor for CBOR parser.
     *
     * @param cborParserDecimalValue parsed decimal value
     *                               which has the scale the serializer wrote (amount decimals).
     */
    @JsonCreator
    public TokenOperationAmount(BigDecimal cborParserDecimalValue) {
        this(cborParserDecimalValue, cborParserDecimalValue.scale());
    }

    @SuppressWarnings("unused")
    public TokenOperationAmount(TokenAmount tokenAmount) {
        this(
                UInt64.from(tokenAmount.getValue()),
                tokenAmount.getDecimals()
        );
    }

    static class CborSerializer extends StdSerializer<TokenOperationAmount> {

        protected CborSerializer() {
            super(TokenOperationAmount.class);
        }

        @Override
        public void serialize(TokenOperationAmount tokenOperationAmount,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;

            // Write the amount as CBOR "decfrac" (decimal fraction),
            // with the exponent matching the token decimals.
            // When parsed, it becomes BigDecimal with scale matching the decimals.
            //
            // For token module with 6 decimals,
            // "1.5" must be encoded as 4([-6, 1500000]) and not as 4([-1, 15])
            // even though the latter is shorter.
            cborGenerator.writeTag(CBORConstants.TAG_DECIMAL_FRACTION);
            cborGenerator.writeObject(new Object[]{
                    -tokenOperationAmount.decimals,
                    tokenOperationAmount.value
            });
        }
    }
}
