package com.concordium.sdk.v2;

import com.google.common.base.Function;
import io.grpc.stub.StreamObserver;

class Utils {
    static <T1, T2> StreamObserver<T1> to(
            StreamObserver<T2> clientStreamObserver,
            Function<T1, T2> grpcToClientMap) {
        StreamObserver<T1> grpcStreamObserver = new StreamObserver<T1>() {
            @Override
            public void onNext(T1 grpcObject) {
                clientStreamObserver.onNext(grpcToClientMap.apply(grpcObject));
            }

            @Override
            public void onError(Throwable throwable) {
                clientStreamObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                clientStreamObserver.onCompleted();
            }
        };

        return grpcStreamObserver;
    }
}
