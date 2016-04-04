package android.endava.com.demoproject.adapters;

import android.endava.com.demoproject.R;
import android.endava.com.demoproject.formatter.DateFormats;
import android.endava.com.demoproject.model.Repo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReposSyncAdapter extends RecyclerView.Adapter<ReposSyncAdapter.RepoViewHolder> {
    private ArrayList<Repo> reposList;

    public ReposSyncAdapter(ArrayList<Repo> reposList) {
        this.reposList = reposList;
    }


    @Override
    public ReposSyncAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_repo_sync, parent, false);

        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.repoName.setText(reposList.get(position).getName());
        holder.repoDescription.setText(DateFormats.formatISO(reposList.get(position).getLastPush()));
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