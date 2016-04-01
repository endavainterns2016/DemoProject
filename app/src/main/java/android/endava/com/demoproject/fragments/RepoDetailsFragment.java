package android.endava.com.demoproject.fragments;


import android.content.Context;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.activities.MainActivity;
import android.endava.com.demoproject.model.Repo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RepoDetailsFragment extends Fragment {

    private MainActivity mActivity;
    private TextView idTextView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView homeUrlTextView;
    private TextView defaultBranchTextView;
    private TextView codeLanguageTextView;
    private TextView sizeTextView;
    private TextView lastPushTextView;
    private TextView openIssuesTextView;
    private Repo mRepo;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_details);
        idTextView = (TextView) view.findViewById(R.id.id);
        nameTextView = (TextView) view.findViewById(R.id.name);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        homeUrlTextView = (TextView) view.findViewById(R.id.homeUrl);
        defaultBranchTextView = (TextView) view.findViewById(R.id.defaultBranch);
        codeLanguageTextView = (TextView) view.findViewById(R.id.codeLanguage);
        sizeTextView = (TextView) view.findViewById(R.id.size);
        lastPushTextView = (TextView) view.findViewById(R.id.lastPush);
        openIssuesTextView = (TextView) view.findViewById(R.id.openIssues);

        idTextView.setText(String.format("%d", mRepo.getId()));
        nameTextView.setText(mRepo.getName());
        descriptionTextView.setText(mRepo.getDescription());
        homeUrlTextView.setText(mRepo.getHomeUrl());
        defaultBranchTextView.setText(mRepo.getDefaultBranch());
        if(mRepo.getCodeLanguage() != null) {
            codeLanguageTextView.setText(mRepo.getCodeLanguage());
        }else {
            codeLanguageTextView.setText(R.string.unknown_language);
        }
        sizeTextView.setText(String.format(getString(R.string.repo_size), (mRepo.getSize() / 1024)));
        lastPushTextView.setText(mRepo.getLastPush());
        openIssuesTextView.setText(String.format("%d", mRepo.getOpenIssues()));
    }

    public void setRepo(Repo mRepo) {
        this.mRepo = mRepo;
    }
}
