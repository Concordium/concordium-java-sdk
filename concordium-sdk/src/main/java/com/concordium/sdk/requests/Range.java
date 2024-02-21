package com.concordium.sdk.requests;

import lombok.*;

import java.util.Optional;

/**
 * A range parameter for queries in {@link com.concordium.sdk.ClientV2}.
 * @param <T> type of the values in the range.
 */
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@ToString
public class Range<T> {

    /**
     * Lower bound of the range. If not present indicates unbounded in the lower direction.
     */
    private final T lowerBound;
    /**
     * Upper bound of the range. If not present indicates unbounded in the upper direction.
     */
    private final T upperBound;

    /**
     * Creates a new {@link Range} unbounded in either direction.
     */
    public static <T> Range<T> newUnbounded() {
        return Range.<T>builder().build();
    }

    /**
     * Creates a new {@link Range} bounded in the lower direction.
     */
    public static <T> Range<T> newLowerBounded(T lowerBound) {
        return Range.<T>builder()
                .lowerBound(lowerBound).build();
    }

    /**
     * Creates a new {@link Range} bounded in the upper direction.
     */
    public static <T> Range<T> newUpperBounded(T upperBound) {
        return Range.<T>builder().upperBound(upperBound).build();
    }

    /**
     * Creates a new {@link Range} bounded in both directions.
     */
    public static <T> Range<T> newBounded(T lowerBound, T upperBound) {
        return Range.<T>builder()
                .lowerBound(lowerBound)
                .upperBound(upperBound)
                .build();
    }
    public Optional<T> getLowerBound() {
        return Optional.ofNullable(lowerBound);
    }

    public Optional<T> getUpperBound() {
        return Optional.ofNullable(upperBound);
    }


}
