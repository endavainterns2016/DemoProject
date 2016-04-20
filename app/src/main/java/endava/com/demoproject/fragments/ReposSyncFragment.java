package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.ReposSyncAdapter;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.ReposSyncPresenter;
import endava.com.demoproject.view.ReposSyncView;

public class ReposSyncFragment extends Fragment implements ReposSyncAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, ReposSyncView {

    private ReposSyncPresenter reposSyncPresenter;
    private ReposSyncAdapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private View view;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repos_sync, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        reposSyncPresenter = new ReposSyncPresenter();
        reposSyncPresenter.attachView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void initView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        ((MainActivity) getActivity()).getActivityToolbar().setTitle(R.string.toolbar_repos_sync);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mAdapter = new ReposSyncAdapter(reposList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        snackBarOnClickListener = new SnackBarOnClickListener();
    }

    @Override
    public void showError() {
        Snackbar snackbar = Snackbar
                .make(view, getString(R.string.repoList_network_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), snackBarOnClickListener);
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reposSyncPresenter.detachView();
        reposSyncPresenter = null;
    }


    @Override
    public void onRefresh() {
        reposSyncPresenter.onRefresh();
    }

    //RepoListView
    @Override
    public void populateList(List<Repo> newReposList) {
        reposList.clear();
        for (int i = 0; i < 50; i++) {
            reposList.addAll(newReposList);
        }
            mAdapter.notifyDataSetChanged();
    }
    @Override
    public void showProgress() {
        progressDialog.show();
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("recycleViewSync", "clicked on" + position);
        RepoCommitsFragment repoCommitsFragment = RepoCommitsFragment.getInstance(reposList.get(position).getDbId());
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.root_activity_layout, repoCommitsFragment)
                .addToBackStack(null).commit();
    }


    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            reposSyncPresenter.onRefresh();
        }
    }
}
