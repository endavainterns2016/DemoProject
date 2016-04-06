package endava.com.demoproject.asyncLoader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class DemoLoader<T> extends AsyncTaskLoader<T> {
    final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
    T mData;

    public DemoLoader(final Context context) {
        super(context);
    }

    @Override
    public void deliverResult(final T data) {
        if (isReset()) {
            if (data != null) {
                onReleaseResources(data);
            }
        }
        T oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }


        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {

        super.onStartLoading();
        if (mData != null) {
            deliverResult(mData);
        }
        boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

        if (takeContentChanged() || mData == null || configChange) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(T data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    protected void onReleaseResources(T data) {
    }
}
