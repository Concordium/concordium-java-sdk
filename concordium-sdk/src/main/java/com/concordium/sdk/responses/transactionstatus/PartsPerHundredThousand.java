package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AmountFraction;
import com.concordium.sdk.responses.Fraction;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@EqualsAndHashCode
@Getter
@ToString
public class PartsPerHundredThousand {
    private final Fraction value;

    private final BigInteger HUNDRED_THOUSAND = new BigInteger("100000");

    @JsonCreator
    PartsPerHundredThousand(BigInteger value) {
        this.value = new Fraction(HUNDRED_THOUSAND, value);
    }

    public static PartsPerHundredThousand from(long x) {
        return new PartsPerHundredThousand(BigInteger.valueOf(x));
    }

    /**
     * Get the parts per hundred thousand as a floating point value.
     *
     * @return the value as a floating point value.
     */
    public double asDouble() {
        return getValue().asDouble();
    }
}
