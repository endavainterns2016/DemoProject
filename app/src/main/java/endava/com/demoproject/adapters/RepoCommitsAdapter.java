package endava.com.demoproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import endava.com.demoproject.R;
import endava.com.demoproject.model.CommitModel;

public class RepoCommitsAdapter extends RecyclerView.Adapter<RepoCommitsAdapter.RepoViewHolder> {
    private ArrayList<CommitModel> reposList;

    public RepoCommitsAdapter(ArrayList<CommitModel> reposList) {
        this.reposList = reposList;
    }


    @Override
    public RepoCommitsAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_repo_sync, parent, false);

        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
//        holder.repoName.setText(reposList.get(position).getName());
//        holder.repoDescription.setText(DateFormats.formatISO(reposList.get(position).getLastPush()));
    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        public TextView repoName, repoDescription;

        public RepoViewHolder(final View view) {
            super(view);
            repoName = (TextView) view.findViewById(R.id.repo_name);
            repoDescription = (TextView) view.findViewById(R.id.repo_descrition);
        }
    }
}