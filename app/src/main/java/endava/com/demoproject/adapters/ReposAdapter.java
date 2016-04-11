package endava.com.demoproject.adapters;

import endava.com.demoproject.R;
import endava.com.demoproject.model.Repo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.RepoViewHolder> {
    private ArrayList<Repo> reposList;
    private static OnItemClickListener listener;

    public ReposAdapter(ArrayList<Repo> reposList) {
        this.reposList = reposList;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ReposAdapter.listener = listener;
    }

    @Override
    public ReposAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_repo_list, parent, false);

        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.repoName.setText(reposList.get(position).getName());
        holder.repoDescription.setText(reposList.get(position).getDescription());
        holder.repoId.setText(String.valueOf(reposList.get(position).getId()));

    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        public TextView repoName, repoDescription, repoId;

        public RepoViewHolder(final View view) {
            super(view);
            repoName = (TextView) view.findViewById(R.id.repo_name);
            repoDescription = (TextView) view.findViewById(R.id.repo_descrition);
            repoId = (TextView) view.findViewById(R.id.repo_id);
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