package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.formatter.DateFormats;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.RepoListDetailPresenter;
import endava.com.demoproject.view.RepoDetailsView;

public class RepoDetailsFragment extends Fragment implements RepoDetailsView {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }
    public static RepoDetailsFragment getInstance(Integer id){
        RepoDetailsFragment repoDetailsFragment = new RepoDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        repoDetailsFragment.setArguments(args);
        return repoDetailsFragment;
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
        idTextView.setText(String.valueOf(repo.getId()));
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
}
