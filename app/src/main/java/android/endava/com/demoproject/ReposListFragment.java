package android.endava.com.demoproject;


import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.Repo;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReposListFragment extends Fragment {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Repo> reposList = new ArrayList<>();
    private DataBaseHelper dbHelper;
    private User user;
    private Callback<List<Repo>> reposCallBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = HelperFactory.getHelper();
        try {
            if (!dbHelper.getUserDAO().getAllUsers().isEmpty())
                user = dbHelper.getUserDAO().getAllUsers().get(0);
        } catch (SQLException e) {
            Log.e("SQLException ",e.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repos_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReposAdapter(reposList);
        mRecyclerView.setAdapter(mAdapter);
        mToolbar = (Toolbar) view.findViewById(R.id.fragment_repos_toolbar);
        mToolbar.setTitle(R.string.toolbar_repos_list);

        reposCallBack = new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if (response.body() != null) {
                    reposList.clear();
                    reposList.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.get_token_error),
                        Toast.LENGTH_LONG).show();
            }
        };
        handleReposRequest();
    }


    private void handleReposRequest() {
        ServiceFactory.getInstance().getReposList("Basic " + user.getHashedCredentials()).enqueue(reposCallBack);
    }
}
