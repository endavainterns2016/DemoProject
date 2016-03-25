package android.endava.com.demoproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.endava.com.demoproject.activities.LoginActivity;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.activities.SplashActivity;
import android.endava.com.demoproject.cacheableObserver.Event;
import android.endava.com.demoproject.cacheableObserver.EventContext;
import android.endava.com.demoproject.cacheableObserver.Observer;
import android.endava.com.demoproject.cacheableObserver.SplashRotationEvent;
import android.endava.com.demoproject.cacheableObserver.Subject;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class SplashFragment extends Fragment implements Observer {
    Subject subject = Subject.newInstance();
    private boolean shouldShowSplash = true;
    private SplashActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SplashActivity) {
            mActivity = (SplashActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container,
                false);
    }

    @Override
    public void onPause() {
        super.onPause();
        subject.unregisterObservers(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        subject.registerObserver(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (shouldShowSplash) {
            doInit();
        }
    }

    protected void doInit() {
        shouldShowSplash = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                SplashRotationEvent splashRotationEvent = new SplashRotationEvent();
                subject.onNewEvent(splashRotationEvent);
            }
        }, 1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void startNextActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void onEvent(Event e) {
        startNextActivity();
    }

    @Override
    public List<EventContext> getObserverKeys() {
        EventContext eventContext = new EventContext("rotation", null);
        List<EventContext> list = new ArrayList<>();
        list.add(eventContext);
        return list;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        return false;
    }
}
