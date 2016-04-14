package endava.com.demoproject.presenter;

import endava.com.demoproject.view.MvpView;

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void initView();

    void detachView();
}
