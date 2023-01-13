package com.concordium.sdk.examples.utils;

import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.SneakyThrows;

public class ConsoleStreamObserver<T> implements StreamObserver<T> {

    @Getter
    private boolean isComplete;

    @Override
    public void onNext(T t) {
        System.out.println(t);
    }

    @SneakyThrows
    @Override
    public void onError(Throwable throwable) {
        throw throwable;
    }

    @Override
    public void onCompleted() {
        this.isComplete = true;
    }
}
