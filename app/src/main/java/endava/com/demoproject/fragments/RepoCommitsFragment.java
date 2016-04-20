package endava.com.demoproject.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.adapters.RepoCommitsAdapter;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.CommitModel;

public class RepoCommitsFragment extends Fragment {

    public static final String ID_TAG = "ID";
    private Integer id;
    private RepoCommitsAdapter mAdapter;
    private ArrayList<CommitModel> commitsList = new ArrayList<>();
    private View view;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;

    public static RepoCommitsFragment getInstance(Integer id) {
        RepoCommitsFragment repoCommitsFragment = new RepoCommitsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TAG, id);
        repoCommitsFragment.setArguments(args);
        return repoCommitsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repos_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        id = getArguments().getInt(ID_TAG);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        ((MainActivity) getActivity()).getActivityToolbar().setTitle(String.format(getString(R.string.commits_list), DbHelper.getInstance().getRepoById(id).getName()));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.repos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RepoCommitsAdapter(commitsList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
