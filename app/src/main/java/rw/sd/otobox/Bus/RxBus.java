package rw.sd.otobox.Bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by miller on 12/7/17.
 */

public class RxBus {
    public RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }
}
