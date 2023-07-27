package com.concordium.sdk.responses.chainparameters;

public interface Authorizations {

    AuthorizationsType getType();

    enum AuthorizationsType {
        V1,V2
    }
}
