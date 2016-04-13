package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.RepoListDetailPresenter;
import endava.com.demoproject.view.RepoDetailsView;

public class RepoDetailsFragment extends Fragment implements RepoDetailsView, LoaderManager.LoaderCallbacks<Repo> {

    public static final String ID_TAG = "ID";
    private RepoListDetailPresenter repoListDetailPresenter;
    private Integer id;
    private Repo repoFromDb;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.id = getArguments().getInt(ID_TAG);
        initView(view);
        repoListDetailPresenter = new RepoListDetailPresenter(this, id);
        repoListDetailPresenter.attachView(this);
    }

    private void initView(View view) {
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
        openIssuesTextView.setText(String.format("%d", repo.getOpenIssues()));
    }

    @Override
    public void populateViewFromDB() {
        populateView(repoFromDb);
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
    public void onDestroyView() {
        super.onDestroyView();
        repoListDetailPresenter.onDestroy();
    }

    @Override
    public Loader<Repo> onCreateLoader(int Loaderid, Bundle args) {
        showProgress();
        return new RepoLoadingTask(mActivity, id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().restartLoader(LoaderConstants.REPO_LOADING_TASK_ID, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Repo> loader, Repo data) {
        hideProgress();
        repoFromDb = data;
        repoListDetailPresenter.updateRepo(repoFromDb);
        populateViewFromDB();
    }

    @Override
    public void onLoaderReset(Loader<Repo> loader) {

    }
}
