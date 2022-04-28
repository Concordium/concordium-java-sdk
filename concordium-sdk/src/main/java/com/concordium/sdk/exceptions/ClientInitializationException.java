package com.concordium.sdk.exceptions;

import lombok.Getter;

import java.io.IOException;

/**
 * Wrapper for checked exceptions that can occur when creating a {@link com.concordium.sdk.Client}.
 */
public class ClientInitializationException extends Exception {
    /**
     * The root cause
     */
    @Getter
    private final Exception inner;

     private ClientInitializationException(Exception inner) {
        super("The Client could not be constructed. " + inner.getMessage());
        this.inner = inner;
    }

    public static ClientInitializationException from(IOException e) {
        return new ClientInitializationException(e);
    }
}
