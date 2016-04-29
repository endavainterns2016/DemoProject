package endava.com.demoproject.fragments;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.ReposAdapter;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.services.RefreshReposListService;
import endava.com.demoproject.view.ReposListView;

public class ReposListFragment extends Fragment implements ReposAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, ReposListView {

    @Inject
    public ReposListPresenter reposListPresenter;
    private ReposAdapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private View view;
    private boolean appIsMinimized = false;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    public void onPause() {
        super.onPause();
        appIsMinimized = true;
        reposListPresenter.onPause();
        Log.d("lifecycle", "pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        appIsMinimized = false;
        reposListPresenter.onResume();
        Log.d("lifecycle", "resume");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DemoProjectApplication.getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
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
        reposListPresenter.attachView(this);
        startRefreshService();
    }


    @Override
    public void initView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        ((MainActivity) getActivity()).getActivityToolbar().setTitle(R.string.toolbar_repos_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mAdapter = new ReposAdapter(reposList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        snackBarOnClickListener = new SnackBarOnClickListener();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
            if (isMyServiceRunning(RefreshReposListService.class)) {
                getActivity().stopService(new Intent(getActivity(), RefreshReposListService.class));
            }
        }
        reposListPresenter.detachView();
        Log.d("lifecycle", "onDestroy");
        reposListPresenter = null;
    }

    public void startRefreshService() {
        if (reposListPresenter.getAutoSyncEnabled()) {
            if (!isMyServiceRunning(RefreshReposListService.class)) {
                Intent intent = new Intent(getActivity(), RefreshReposListService.class);
                intent.putExtra("autoSyncInterval", reposListPresenter.getAutoSyncInterval());
                getActivity().startService(intent);
            }
        }
    }

    public void pushNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_error_white_24dp)
                        .setContentTitle(getString(R.string.notification_error_title))
                        .setContentText(getString(R.string.notification_error_text));
        Intent notifIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 1, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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
        reposListPresenter.onRefresh();
    }

    //RepoListView
    @Override
    public void populateList(List<Repo> newReposList) {
        reposList.clear();
        for (int i = 0; i < 50; i++) {
            reposList.addAll(newReposList);
        }

        Log.d("refreshService", "reposCallBack onresponse not minimized");
        mAdapter.notifyDataSetChanged();
    }

    public void showError() {
        Snackbar snackbar = Snackbar
                .make(view, getString(R.string.repoList_network_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), snackBarOnClickListener);
        snackbar.show();

        if (appIsMinimized) {
            pushNotification();
        }
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


    public class SnackBarOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            reposListPresenter.onRefresh();
        }
    }
}
