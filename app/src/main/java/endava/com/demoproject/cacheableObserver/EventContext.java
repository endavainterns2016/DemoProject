package endava.com.demoproject.cacheableObserver;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class EventContext implements Parcelable {
    private final String eventKey;
    private final String context;

    public EventContext(String eventKey, String context) {
        if (TextUtils.isEmpty(eventKey)) {
            throw new NullPointerException("Provide a non null and non empty event key.");
        }
        this.eventKey = eventKey;
        this.context = context;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventKey);
        dest.writeString(this.context);
    }

    protected EventContext(Parcel in) {
        this.eventKey = in.readString();
        this.context = in.readString();
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getContext() {
        return context;
    }

    public static final Parcelable.Creator<EventContext> CREATOR = new Parcelable.Creator<EventContext>() {
        public EventContext createFromParcel(Parcel source) {
            return new EventContext(source);
        }

        public EventContext[] newArray(int size) {
            return new EventContext[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventContext that = (EventContext) o;

        if (eventKey != null ? !eventKey.equals(that.eventKey) : that.eventKey != null) {
            return false;
        }
        return !(context != null ? !context.equals(that.context) : that.context != null);

    }

    @Override
    public int hashCode() {
        int result = eventKey != null ? eventKey.hashCode() : 0;
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }

    public boolean isOfActiveContext(String context) {
        return this.context == null || this.context.equals(context);
    }
}
