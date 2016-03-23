package android.endava.com.demoproject.cacheableObserver;

import android.os.Parcelable;

public interface Event<T> extends Parcelable {
    /**
     * Returns data object
     *
     * @return instance of T
     */
    T getData();

    /**
     * Returns {@link String} that represents event key which used for identify {@link Event}
     *
     * @return {@link String}
     */
    EventContext getEventKey();

}
