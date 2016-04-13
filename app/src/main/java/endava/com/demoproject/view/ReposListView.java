package endava.com.demoproject.view;

import java.util.List;

import endava.com.demoproject.model.Repo;

public interface ReposListView extends MvpView {

    void populateList(List<Repo> newReposList);

    void handleOnRequestFailure();

    void handleError();

    void showProgress();

    void hideProgress();

}
