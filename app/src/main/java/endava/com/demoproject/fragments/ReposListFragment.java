package endava.com.demoproject.fragments;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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
import endava.com.demoproject.adapters.ReposAdapter;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.services.RefreshReposListService;
import endava.com.demoproject.view.ReposListView;

public class ReposListFragment extends Fragment implements ReposAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, ReposListView {

    private ReposListPresenter reposListPresenter;
    private ReposAdapter mAdapter;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        reposListPresenter = new ReposListPresenter();
        reposListPresenter.attachView(this);

        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        mActivity.getActivityToolbar().setTitle(R.string.toolbar_repos_list);
        activityBottomBar = mActivity.getBottomBar();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mAdapter = new ReposAdapter(reposList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        snackBarOnClickListener = new SnackBarOnClickListener();

        reposListPresenter.populateView();

        startRefreshService();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reposListPresenter.getAutoSyncEnabled()) {
            mActivity.stopService(new Intent(mActivity, RefreshReposListService.class));
        }
        reposListPresenter.detachView();
        reposListPresenter = null;
    }

    public void startRefreshService() {
        if (reposListPresenter.getAutoSyncEnabled()) {
            if (!isMyServiceRunning(RefreshReposListService.class)) {
                Intent intent = new Intent(mActivity, RefreshReposListService.class);
                intent.putExtra("autoSyncInterval", reposListPresenter.getAutoSyncInterval());
                mActivity.startService(intent);
            }
        }
    }

    public void pushNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mActivity)
                        .setSmallIcon(R.drawable.ic_error_white_24dp)
                        .setContentTitle(getString(R.string.notification_error_title))
                        .setContentText(getString(R.string.notification_error_text));
        Intent notifIntent = new Intent(mActivity, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mActivity, 1, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    // click on RecycleView row
    @Override
    public void onItemClick(View view, int position) {
        Log.d("recycleView", "clicked on" + position);
        RepoDetailsFragment mRepoDetailsFragment = new RepoDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("repoID", reposList.get(position).getDbId());
        mRepoDetailsFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.root_activity_layout, mRepoDetailsFragment)
                .addToBackStack(null).commit();
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
        } else {
            pushNotification();
        }
    }

    @Override
    public void handleError() {
        if (!appIsMinimized) {
            finishRefreshing();
            hideProgress();
            Snackbar snackbar = Snackbar
                    .make(activityBottomBar, getString(R.string.network_error), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            pushNotification();
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
        progressDialog.dismiss();
    }


    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRefreshLayout.setRefreshing(true);
            reposListPresenter.populateView();
        }
    }
}
