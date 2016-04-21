package endava.com.demoproject.view;

import java.util.List;

import endava.com.demoproject.model.CommitModel;

public interface RepoCommitsView extends MvpView {

    int getRepoID();

    void populateList(List<CommitModel> newReposList);

    void setRepoName(String title);

    void initView();

    void showError();

    void showProgress();

    void hideProgress();

}
