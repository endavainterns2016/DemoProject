package android.endava.com.demoproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.RepoViewHolder> {
    private ArrayList<String> mDataset;

    public ReposAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ReposAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.repoName.setText(mDataset.get(position));
        holder.repoDescription.setText(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {

        public TextView repoName, repoDescription;
        public RepoViewHolder(View view) {
            super(view);
            repoName = (TextView) view.findViewById(R.id.repo_name);
            repoDescription = (TextView) view.findViewById(R.id.repo_descrition);
        }
    }
}