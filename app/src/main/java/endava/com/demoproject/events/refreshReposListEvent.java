package endava.com.demoproject.events;

import android.os.Parcel;

import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;

public class refreshReposListEvent implements Event {
    @Override
    public Object getData() {
        return null;
    }

    @Override
    public EventContext getEventKey() {
        return new EventContext("refreshList", null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
