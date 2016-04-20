package endava.com.demoproject.events;

import android.annotation.SuppressLint;
import android.os.Parcel;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.helpers.ResourcesHelper;

@SuppressLint("ParcelCreator")
public class RefreshRepoListEvent implements Event {
    @Override
    public Object getData() {
        return null;
    }

    @Override
    public EventContext getEventKey() {
        return new EventContext(ResourcesHelper.getInstance().provideResources().getString(R.string.refreshList), null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
