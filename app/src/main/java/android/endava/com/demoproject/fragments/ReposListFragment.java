package android.endava.com.demoproject.fragments;


import android.content.Context;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.ReposAdapter;
import android.endava.com.demoproject.activities.MainActivity;
import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.model.Repo;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private ClientDataBaseHelper dbHelper;
    private User user;
    private Callback<List<Repo>> reposCallBack;
    private Toolbar mToolbar;
    private MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repos_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_list);
        dbHelper = ClientDataBaseHelper.getInstance();
        user = dbHelper.getUser();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReposAdapter(reposList);
        mRecyclerView.setAdapter(mAdapter);

        reposCallBack = new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.body() != null) {
                    reposList.clear();
                    reposList.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mActivity, getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(mActivity, getString(R.string.get_token_error),
                        Toast.LENGTH_LONG).show();
            }
        };

        if (null != user) {
            handleReposRequest();
        } else {
            Toast.makeText(mActivity, "User is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleReposRequest() {
        ServiceFactory.getInstance().getReposList("Basic " + user.getHashedCredentials()).enqueue(reposCallBack);
    }
}
