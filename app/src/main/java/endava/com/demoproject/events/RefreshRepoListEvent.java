package endava.com.demoproject.events;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Parcel;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;

@SuppressLint("ParcelCreator")
public class RefreshRepoListEvent implements Event {
    private Resources resources;

    public RefreshRepoListEvent(Resources resources){
        this.resources = resources;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public EventContext getEventKey() {
        return new EventContext(resources.getString(R.string.refreshList), null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
