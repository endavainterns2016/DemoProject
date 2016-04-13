package endava.com.demoproject.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.asyncLoader.RepoLoadingTask;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.view.RepoDetailsView;

public class RepoListDetailPresenter extends BasePresenter<RepoDetailsView> implements LoaderManager.LoaderCallbacks<Repo> {
    @Inject
    Context context;
    private RepoDetailsView repoDetailsView;
    private Integer repoId;

    public RepoListDetailPresenter(RepoDetailsView repoDetailsView, Integer repoId) {
        this.repoDetailsView = repoDetailsView;
        this.repoId = repoId;
        DemoProjectApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void attachView(RepoDetailsView mvpView) {
        super.attachView(mvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
        repoDetailsView = null;
    }

    public void onDestroy() {
        detachView();
    }

    @Override
    public Loader<Repo> onCreateLoader(int id, Bundle args) {
        repoDetailsView.showProgress();
        return new RepoLoadingTask(context, repoId);
    }

    @Override
    public void onLoadFinished(Loader<Repo> loader, Repo data) {
        repoDetailsView.hideProgress();
        repoDetailsView.populateView(data);
    }

    @Override
    public void onLoaderReset(Loader<Repo> loader) {

    }
}
