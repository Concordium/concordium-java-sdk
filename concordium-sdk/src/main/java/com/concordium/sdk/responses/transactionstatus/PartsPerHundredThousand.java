package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.types.UInt32;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class PartsPerHundredThousand {
    private final UInt32 numerator;

    private final int HUNDRED_THOUSAND = 100000;

    PartsPerHundredThousand(UInt32 value) {
        this.numerator = value;
    }

    /**
     * Create a {@link PartsPerHundredThousand} from the provided value.
     *
     * @param x numerator of the fraction
     * @return The parts per hundred thousands i.e. x / 100_000
     * <p>
     * @throws IllegalArgumentException if the provided numerator is greater than 100_000.
     */
    public static PartsPerHundredThousand from(int x) {
        if (x > 100000) throw new IllegalArgumentException("numerator must be less or equal than 100_000.");
        return new PartsPerHundredThousand(UInt32.from(x));
    }


    /**
     * Get the parts per hundred thousand as a floating point value.
     *
     * @return the value as a floating point value.
     */
    public double asDouble() {
        return (double) this.numerator.getValue() / (double) HUNDRED_THOUSAND;
    }


    /**
     * Get the bytes representing the {@link PartsPerHundredThousand}
     *
     * @return the serialized format
     */
    public byte[] getBytes() {
        return this.numerator.getBytes();
    }
}
