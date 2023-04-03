package com.lripl.network;

import com.lripl.utils.Utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private int maxRetries=0;
    private int retryCount = 0;

    public RetryWithDelay(int maxRetries){
        this.maxRetries =  maxRetries;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(new Function<Throwable, Observable<?>>() {
            @Override
            public Observable<?> apply(final Throwable throwable) {

                if (++retryCount < maxRetries) {
                    // When this Observable calls onNext, the original
                    // Observable will be retried (i.e. re-subscribed).
                    long retryDelayMillis = Utils.getWaitTimeExp(retryCount);
                    return Observable.timer(retryDelayMillis,
                            TimeUnit.MILLISECONDS);
                }

                // Max retries hit. Just pass the error along.
                return Observable.error(throwable);
            }
        });
    }
}
