package android.endava.com.demoproject.fragments;


import android.content.Context;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.RoundedTransformation;
import android.endava.com.demoproject.activities.MainActivity;
import android.endava.com.demoproject.constants.LoaderConstants;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ReposLikeFragment extends Fragment {

    private MainActivity mActivity;

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
        return inflater.inflate(R.layout.fragment_like, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Toolbar mToolbar = mActivity.getActivityToolbar();
        mToolbar.setTitle(R.string.toolbar_repos_like_list);
    }
}
