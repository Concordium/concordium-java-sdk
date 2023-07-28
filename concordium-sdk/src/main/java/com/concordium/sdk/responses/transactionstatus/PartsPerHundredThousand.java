package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.Fraction;
import com.concordium.sdk.types.UInt64;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class PartsPerHundredThousand {
    private final Fraction value;

    private final int HUNDRED_THOUSAND = 100000;

    @JsonCreator
    PartsPerHundredThousand(UInt64 value) {
        this.value = new Fraction(value, UInt64.from(HUNDRED_THOUSAND));
    }

    public static PartsPerHundredThousand from(long x) {
        return new PartsPerHundredThousand(UInt64.from(x));
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
