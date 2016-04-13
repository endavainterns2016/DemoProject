package endava.com.demoproject.view;


import endava.com.demoproject.model.Repo;

public interface RepoDetailsView extends MvpView{

    void populateView(Repo repo);

    void showProgress();

    void hideProgress();

}
