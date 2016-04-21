package endava.com.demoproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaredrummler.fastscrollrecyclerview.FastScrollRecyclerView;

import java.util.ArrayList;

import endava.com.demoproject.R;
import endava.com.demoproject.formatter.DateFormats;
import endava.com.demoproject.model.CommitModel;

public class RepoCommitsAdapter extends RecyclerView.Adapter<RepoCommitsAdapter.RepoCommitsViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private ArrayList<CommitModel> commitsList;
    private Context context;

    public RepoCommitsAdapter(ArrayList<CommitModel> commitsList, Context context) {
        this.commitsList = commitsList;
        this.context = context;
    }


    @Override
    public RepoCommitsAdapter.RepoCommitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_repo_commit, parent, false);

        return new RepoCommitsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoCommitsViewHolder holder, int position) {
        CommitModel commit = commitsList.get(position);
        holder.commit.setText(String.format(context.getString(R.string.commit_number), commitsList.size() - position));
        holder.commit_message.setText(commit.getCommit().getMessage());
        holder.commit_date.setText(DateFormats.formatISO(commit.getCommit().getAuthor().getDate()));
        holder.commit_sha.setText(commit.getSha());
        holder.commit_author_name.setText(String.format(context.getString(R.string.name), commit.getCommit().getAuthor().getName()));
        holder.commit_author_email.setText(String.format(context.getString(R.string.email), commit.getCommit().getAuthor().getEmail()));
        holder.commit_commiter_name.setText(String.format(context.getString(R.string.name), commit.getCommit().getCommitter().getName()));
        holder.commit_commiter_email.setText(String.format(context.getString(R.string.email), commit.getCommit().getCommitter().getEmail()));
        commit = null;
    }

    @Override
    public int getItemCount() {
        return commitsList.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {

        return String.format("%d", commitsList.size() - position);
    }

    public static class RepoCommitsViewHolder extends RecyclerView.ViewHolder {

        public TextView commit, commit_message, commit_date, commit_sha, commit_author_name, commit_author_email, commit_commiter_name, commit_commiter_email;

        public RepoCommitsViewHolder(final View view) {
            super(view);
            commit = (TextView) view.findViewById(R.id.commit);
            commit_message = (TextView) view.findViewById(R.id.commit_message);
            commit_date = (TextView) view.findViewById(R.id.commit_date);
            commit_sha = (TextView) view.findViewById(R.id.commit_sha);
            commit_author_name = (TextView) view.findViewById(R.id.commit_author_name);
            commit_author_email = (TextView) view.findViewById(R.id.commit_author_email);
            commit_commiter_name = (TextView) view.findViewById(R.id.commit_commiter_name);
            commit_commiter_email = (TextView) view.findViewById(R.id.commit_commiter_email);
        }
    }
}