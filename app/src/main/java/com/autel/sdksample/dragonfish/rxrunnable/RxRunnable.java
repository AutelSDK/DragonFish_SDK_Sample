package com.autel.sdksample.dragonfish.rxrunnable;




import com.autel.common.error.AutelError;
import com.autel.common.error.RxException;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by A16343 on 2016/8/9.
 */
public abstract class RxRunnable<T> extends DisposableObserver<T> {
    private Disposable disposable;
    protected RequestConfig requestConfig;
    private int retryCount = 0;

    RxRunnable() {
    }


    public void execute() {
        cancel();
        disposable = this;
        generateObservable()
                .subscribeOn(provideProducerOn())
                .observeOn(provideCustomerOn())
                .subscribe(this);
    }

    public void execute(int totalRetryCount) {
        cancel();
        retryCount = 0;
        disposable = this;
        generateObservable()
                .retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    retryCount++;
                    return retryCount <= totalRetryCount ? Observable.timer(1000, TimeUnit.MILLISECONDS) : Observable.error(throwable);
                }))
                .subscribeOn(provideProducerOn())
                .observeOn(provideCustomerOn())
                .subscribe(this);
    }

    protected abstract Scheduler provideProducerOn();

    protected abstract Scheduler provideCustomerOn();

    protected abstract Observable<T> generateObservable();

    private void cancel() {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof RxException) {
            onFail(((RxException) e).getError());
        }
    }

    @Override
    public void onComplete() {

    }

    protected void onFail(AutelError error) {

    }
}
