package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.AmountFraction;
import com.concordium.sdk.responses.blocksummary.updates.Fraction;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
public class PartsPerHundredThousand {
    private final Fraction value;

    private final BigInteger HUNDRED_THOUSAND = new BigInteger("100000");

    @JsonCreator
    PartsPerHundredThousand(BigInteger value) {
        this.value = new Fraction(HUNDRED_THOUSAND, value);
    }

    /**
     * Parses {@link AmountFraction} to {@link PartsPerHundredThousand}.
     * @param amountFraction {@link AmountFraction} returned by the GRPC V2 API.
     * @return parsed {@link PartsPerHundredThousand}.
     */
    public static PartsPerHundredThousand parse(AmountFraction amountFraction) {
        return new PartsPerHundredThousand(BigInteger.valueOf(amountFraction.getPartsPerHundredThousand()));
    }

    public static PartsPerHundredThousand from(BigInteger value) {
        return new PartsPerHundredThousand(value);
    }
}
