package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.RepoCommitsAdapter;
import endava.com.demoproject.model.CommitModel;
import endava.com.demoproject.presenter.RepoCommitsPresenter;
import endava.com.demoproject.view.RepoCommitsView;

public class RepoCommitsFragment extends Fragment implements RepoCommitsView {

    public static final String ID_TAG = "ID";
    private RepoCommitsAdapter mAdapter;
    private ArrayList<CommitModel> commitsList = new ArrayList<>();
    private View view;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RepoCommitsPresenter repoCommitsPresenter;

    public static RepoCommitsFragment getInstance(Integer id) {
        RepoCommitsFragment repoCommitsFragment = new RepoCommitsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        repoCommitsFragment.setArguments(args);
        return repoCommitsFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repoCommitsPresenter.detachView();
        repoCommitsPresenter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repos_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        repoCommitsPresenter = new RepoCommitsPresenter();
        repoCommitsPresenter.attachView(this);
    }

    @Override
    public int getRepoID() {
        return getArguments().getInt(ID_TAG);
    }

    @Override
    public void populateList(List<CommitModel> newCommitsList) {
        commitsList.clear();
        commitsList.addAll(newCommitsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setToolbarTitle(String title){
        ((MainActivity) getActivity()).getActivityToolbar().setTitle(String.format(getString(R.string.commits_list), title));
    }

    @Override
    public void initView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RepoCommitsAdapter(commitsList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showError() {
        Snackbar snackbar = Snackbar
                .make(view, getString(R.string.repoList_network_error), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
}
