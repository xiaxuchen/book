package org.originit;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

public class RxjavaTests {

    @Test
    public void testCreate() {
        Observable.fromArray(1,2,3,4).subscribe(o -> {
            System.out.println(o);
        });
    }
}
