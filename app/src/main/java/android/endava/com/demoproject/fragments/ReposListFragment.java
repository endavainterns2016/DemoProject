package android.endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.ReposAdapter;
import android.endava.com.demoproject.activities.MainActivity;
import android.endava.com.demoproject.asyncLoader.UserLoadingTask;
import android.endava.com.demoproject.constants.LoaderConstants;
import android.endava.com.demoproject.model.Repo;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposListFragment extends Fragment implements LoaderManager.LoaderCallbacks<User>, OnMenuTabClickListener {


    private RecyclerView.Adapter mAdapter;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private User user;
    private Callback<List<Repo>> reposCallBack;
    private MainActivity mActivity;
    private BottomBar mBottomBar;
    private SnackBarOnClickListener snackBarOnClickListener;
    private ProgressDialog progressDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repos_list, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, null, this);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Toolbar mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_list);
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) view, null, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.repos_list_fragment_bottom_bar, this);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReposAdapter(reposList);
        mRecyclerView.setAdapter(mAdapter);
        snackBarOnClickListener = new SnackBarOnClickListener();

        reposCallBack = new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.body() != null) {
                    reposList.clear();
                    for (int i = 0; i < 50; i++) {
                        reposList.addAll(response.body());
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(view, getString(R.string.network_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Snackbar snackbar = Snackbar
                        .make(view, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.try_again), snackBarOnClickListener);
                snackbar.show();
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
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
    public void onMenuTabSelected(@IdRes int menuItemId) {
        // do smth
    }

    @Override
    public void onMenuTabReSelected(@IdRes int menuItemId) {
        // do smth
    }


    public class SnackBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            handleReposRequest();
        }
    }
}
