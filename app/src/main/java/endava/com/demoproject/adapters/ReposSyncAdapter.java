package endava.com.demoproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import endava.com.demoproject.R;
import endava.com.demoproject.formatter.DateFormats;
import endava.com.demoproject.model.Repo;

public class ReposSyncAdapter extends RecyclerView.Adapter<ReposSyncAdapter.RepoViewHolder> {
    private ArrayList<Repo> reposList;
    private static OnItemClickListener listener;

    public ReposSyncAdapter(ArrayList<Repo> reposList) {
        this.reposList = reposList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ReposSyncAdapter.listener = listener;
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        public TextView repoName, repoDescription;

        public RepoViewHolder(final View view) {
            super(view);
            repoName = (TextView) view.findViewById(R.id.repo_name);
            repoDescription = (TextView) view.findViewById(R.id.repo_descrition);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(view, getLayoutPosition());
                }
            });
        }
    }
}