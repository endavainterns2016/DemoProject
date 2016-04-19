package endava.com.demoproject.presenter;

import endava.com.demoproject.view.MvpView;

public interface Presenter<V extends MvpView> {

    public void attachView(V mvpView);

    public void detachView();

    public void onResume();

    public void onPause();
}
