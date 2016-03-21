package android.endava.com.demoproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SplashFragment extends Fragment {
    private boolean shouldShowSplash = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container,
                false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (shouldShowSplash) {
            doInit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected void doInit() {
        shouldShowSplash = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startNextActivity();
            }
        }, 1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void startNextActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
