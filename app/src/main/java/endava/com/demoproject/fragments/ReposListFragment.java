package endava.com.demoproject.fragments;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.ReposAdapter;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.services.RefreshReposListService;
import endava.com.demoproject.view.ReposListView;

public class ReposListFragment extends Fragment implements ReposAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, ReposListView {

    private ReposListPresenter reposListPresenter;
    private ReposAdapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private User user;
    private View view;

    private MainActivity mActivity;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private BroadcastReceiver mBroadcastReceiver;
    private BroadcastReceiver networkBroadcastReceiver;
    private SharedPreferences mSharedPreferences;
    private boolean enableAutoSync;
    private int autoSyncInterval;
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
    }

    @Override
    public void onResume() {
        super.onResume();
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

        mActivity.getActivityToolbar().setTitle(R.string.toolbar_repos_list);
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

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        enableAutoSync = mSharedPreferences.getBoolean("enable_auto_sync", false);
        autoSyncInterval = ((mSharedPreferences.getInt("auto_sync_number_picker_key", 1)) * 60 * 1000);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
            }
        };

        mActivity.registerReceiver(networkBroadcastReceiver, new IntentFilter("refreshReposListOnConnectionRestore"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(networkBroadcastReceiver);
        if (enableAutoSync) {
            mActivity.unregisterReceiver(mBroadcastReceiver);
            mActivity.stopService(new Intent(mActivity, RefreshReposListService.class));
        }
        reposListPresenter.detachView();
    }

    public void startRefreshService() {
        if (enableAutoSync) {
            mActivity.registerReceiver(mBroadcastReceiver, new IntentFilter("refreshReposList"));
            if (!isMyServiceRunning(RefreshReposListService.class)) {
                Intent intent = new Intent(mActivity, RefreshReposListService.class);
                intent.putExtra("autoSyncInterval", autoSyncInterval);
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
        RepoDetailsFragment mRepoDetailsFragment = RepoDetailsFragment.getInstance(reposList.get(position).getDbId());
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
                    .make(view, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
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
                    .make(view, getString(R.string.network_error), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            pushNotification();
        }
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.progress_dialog_loading));
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
        progressDialog = null;
    }


    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRefreshLayout.setRefreshing(true);
            reposListPresenter.populateView();
        }
    }
}
