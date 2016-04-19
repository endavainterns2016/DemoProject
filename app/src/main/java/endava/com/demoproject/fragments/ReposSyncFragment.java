package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.ReposSyncAdapter;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.view.ReposListView;

public class ReposSyncFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ReposListView {

    private ReposListPresenter reposListPresenter;
    private ReposSyncAdapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private View view;
    private MainActivity mActivity;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private BottomBar activityBottomBar;

    private boolean appIsMinimized = false;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        appIsMinimized = true;
        Log.d("lifecycle", "pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycle", "resume");
        appIsMinimized = false;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repos_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        reposListPresenter = new ReposListPresenter();
        reposListPresenter.attachView(this);
        reposListPresenter.initView();
        reposListPresenter.populateView();
    }


    @Override
    public void initView() {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        mActivity.getActivityToolbar().setTitle(R.string.toolbar_repos_sync);
        activityBottomBar = mActivity.getBottomBar();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mAdapter = new ReposSyncAdapter(reposList);
        mRecyclerView.setAdapter(mAdapter);
        snackBarOnClickListener = new SnackBarOnClickListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reposListPresenter.detachView();
        reposListPresenter = null;
    }


    @Override
    public void onRefresh() {
        reposListPresenter.populateView();
    }

    private void finishRefreshing() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    //RepoListView
    @Override
    public void populateList(List<Repo> newReposList) {
        reposList.clear();
        for (int i = 0; i < 50; i++) {
            reposList.addAll(newReposList);
        }

        if (!appIsMinimized) {
            Log.d("refreshService", "reposCallBack onresponse not minimized");
            finishRefreshing();
            mAdapter.notifyDataSetChanged();
            hideProgress();
        }
    }

    @Override
    public void handleOnRequestFailure() {
        Log.d("refreshService", "handleOnRequestFailure 1");
        if (!appIsMinimized) {
            Log.d("refreshService", "handleOnRequestFailure 2");
            finishRefreshing();
            hideProgress();
            Snackbar snackbar = Snackbar
                    .make(activityBottomBar, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.try_again), snackBarOnClickListener);
            snackbar.show();
        }
    }

    @Override
    public void handleError() {
        if (!appIsMinimized) {
            finishRefreshing();
            hideProgress();
            Snackbar snackbar = Snackbar
                    .make(activityBottomBar, getString(R.string.connection_error_message), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void showProgress() {
        if (!appIsMinimized) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (!appIsMinimized) {
            progressDialog.dismiss();
        }
    }


    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRefreshLayout.setRefreshing(true);
            reposListPresenter.populateView();
        }
    }
}
