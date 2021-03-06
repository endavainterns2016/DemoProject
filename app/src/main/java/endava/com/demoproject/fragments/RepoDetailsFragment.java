package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.asyncLoader.RepoLoadingTask;
import endava.com.demoproject.constants.LoaderConstants;
import endava.com.demoproject.formatter.DateFormats;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.RepoListDetailPresenter;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.view.RepoDetailsView;

public class RepoDetailsFragment extends Fragment implements RepoDetailsView, LoaderManager.LoaderCallbacks<Repo> {

    public static final String ID_TAG = "ID";
    private RepoListDetailPresenter repoListDetailPresenter;
    private Integer id;
    private MainActivity mActivity;
    private TextView idTextView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView homeUrlTextView;
    private TextView defaultBranchTextView;
    private TextView codeLanguageTextView;
    private TextView sizeTextView;
    private TextView lastPushTextView;
    private TextView openIssuesTextView;
    private ProgressDialog dialog;
    private View view;
    private Snackbar snackbar;

    public static RepoDetailsFragment getInstance(Integer id) {
        RepoDetailsFragment repoDetailsFragment = new RepoDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        repoDetailsFragment.setArguments(args);
        return repoDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().restartLoader(LoaderConstants.REPO_LOADING_TASK_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repo_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id = getArguments().getInt(ID_TAG);
        repoListDetailPresenter = new RepoListDetailPresenter(this, DbHelper.getInstance(), ServiceFactory.getInstance());
        repoListDetailPresenter.attachView(this);
        repoListDetailPresenter.initView();
    }

    @Override
    public void initView() {
        Toolbar mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_details);
        idTextView = (TextView) view.findViewById(R.id.id);
        nameTextView = (TextView) view.findViewById(R.id.name);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        homeUrlTextView = (TextView) view.findViewById(R.id.homeUrl);
        defaultBranchTextView = (TextView) view.findViewById(R.id.defaultBranch);
        codeLanguageTextView = (TextView) view.findViewById(R.id.codeLanguage);
        sizeTextView = (TextView) view.findViewById(R.id.size);
        lastPushTextView = (TextView) view.findViewById(R.id.lastPush);
        openIssuesTextView = (TextView) view.findViewById(R.id.openIssues);
    }

    @Override
    public void populateView(Repo repo) {
        idTextView.setText(String.valueOf(repo.getGitId()));
        nameTextView.setText(repo.getName());
        if (!repo.getDescription().equals("")) {
            descriptionTextView.setText(repo.getDescription());
        }
        homeUrlTextView.setText(repo.getHomeUrl());
        defaultBranchTextView.setText(repo.getDefaultBranch());
        if (repo.getCodeLanguage() != null) {
            codeLanguageTextView.setText(repo.getCodeLanguage());
        } else {
            codeLanguageTextView.setText(R.string.unknown_language);
        }
        sizeTextView.setText(String.format(getString(R.string.repo_size), (repo.getSize() / 1024)));
        lastPushTextView.setText(DateFormats.formatISO(repo.getLastPush()));
        openIssuesTextView.setText(String.valueOf(repo.getOpenIssues()));
    }


    @Override
    public void showProgress() {
        dialog = new ProgressDialog(mActivity);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void networkError() {
        snackbar = Snackbar
                .make(view, getString(R.string.repo_update_failed), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), new ClickHandler());
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (snackbar != null)
            snackbar.dismiss();
        repoListDetailPresenter.onDestroy();
    }

    @Override
    public Loader<Repo> onCreateLoader(int Loaderid, Bundle args) {
        showProgress();
        return new RepoLoadingTask(mActivity, id);
    }

    @Override
    public void onLoadFinished(Loader<Repo> loader, Repo data) {
        hideProgress();
        repoListDetailPresenter.setRepo(data);
        repoListDetailPresenter.populateView(data);
        repoListDetailPresenter.updateRepo();
    }

    @Override
    public void onLoaderReset(Loader<Repo> loader) {

    }

    public class ClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            repoListDetailPresenter.updateRepo();
        }
    }
}
