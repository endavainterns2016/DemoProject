package endava.com.demoproject.events;

import android.os.Parcel;

import javax.inject.Inject;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.helpers.ResourcesHelper;

public class ConnectionErrorEvent implements Event {
    private ResourcesHelper resourcesHelper;

    public ConnectionErrorEvent(ResourcesHelper resourcesHelper) {
        this.resourcesHelper = resourcesHelper;
    }
    @Override
    public Object getData() {
        return null;
    }

    @Override
    public EventContext getEventKey() {
        return new EventContext(resourcesHelper.provideResources().getString(R.string.connection_error_tag),null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
