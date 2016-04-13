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
import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.ReposAdapter;
import endava.com.demoproject.asyncLoader.UserLoadingTask;
import endava.com.demoproject.constants.LoaderConstants;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.services.RefreshReposListService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposListFragment extends Fragment implements LoaderManager.LoaderCallbacks<User>, ReposAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private ReposAdapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private User user;
    private Callback<List<Repo>> reposCallBack;
    private MainActivity mActivity;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private BroadcastReceiver mBroadcastReceiver;
    private BroadcastReceiver networkBroadcastReceiver;
    private SharedPreferences mSharedPreferences;
    private boolean enableAutoSync;
    private int autoSyncInterval;
    private boolean appIsMinimized;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        appIsMinimized = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        appIsMinimized = false;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repos_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        getLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, null, this);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Toolbar mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_list);
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


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onRefresh();
            }
        };

        reposCallBack = new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.body() != null) {
                    reposList.clear();
                    for (int i = 0; i < 50; i++) {
                        reposList.addAll(response.body());
                    }

                    if (!appIsMinimized) {
                        Log.d("refreshService", "reposCallBack onresponse not minimized");
                        finishRefreshing();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("refreshService", "reposCallBack onresponse minimized");
                    }
                } else {
                    if (!appIsMinimized) {
                        Snackbar snackbar = Snackbar
                                .make(view, getString(R.string.network_error), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        finishRefreshing();
                    } else {
                        pushNotification();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                if (!appIsMinimized) {
                    Snackbar snackbar = Snackbar
                            .make(view, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.try_again), snackBarOnClickListener);
                    snackbar.show();
                    finishRefreshing();
                } else {
                    pushNotification();
                }
            }
        };

        startRefreshService();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        RepoDetailsFragment mRepoDetailsFragment = new RepoDetailsFragment();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.root_activity_layout, mRepoDetailsFragment)
                .addToBackStack(null).commit();
    }

    private void handleReposRequest() {
        ServiceFactory.getInstance().getReposList("Basic " + user.getHashedCredentials()).enqueue(reposCallBack);
    }

    @Override
    public Loader<User> onCreateLoader(final int id, final Bundle args) {
        progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.progress_dialog_loading));
        return new UserLoadingTask(mActivity);
    }

    @Override
    public void onLoadFinished(final Loader<User> loader, final User result) {
        if (result != null) {
            user = result;
            handleReposRequest();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(final Loader<User> loader) {

    }

    @Override
    public void onRefresh() {
        handleReposRequest();
    }

    private void finishRefreshing() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        if (key.equals("enable_auto_sync")) {
            enableAutoSync = mSharedPreferences.getBoolean("enable_auto_sync", false);
            if (enableAutoSync) {
                mActivity.registerReceiver(mBroadcastReceiver, new IntentFilter("refreshReposList"));
                if (!isMyServiceRunning(RefreshReposListService.class)) {
                    Intent intent = new Intent(mActivity, RefreshReposListService.class);
                    intent.putExtra("autoSyncInterval", autoSyncInterval);
                    mActivity.startService(intent);
                }
            } else {
                mActivity.unregisterReceiver(mBroadcastReceiver);
                mActivity.stopService(new Intent(mActivity, RefreshReposListService.class));
            }
        }

        if (key.equals("auto_sync_number_picker_key")) {
            autoSyncInterval = ((mSharedPreferences.getInt("auto_sync_number_picker_key", 1)) * 60 * 1000);
            mActivity.stopService(new Intent(mActivity, RefreshReposListService.class));
            Intent intent = new Intent(mActivity, RefreshReposListService.class);
            intent.putExtra("autoSyncInterval", autoSyncInterval);
            mActivity.startService(intent);
        }
    }

    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRefreshLayout.setRefreshing(true);
            handleReposRequest();
        }
    }
}
