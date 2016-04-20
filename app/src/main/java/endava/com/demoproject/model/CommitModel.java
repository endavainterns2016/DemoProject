package endava.com.demoproject.model;

/**
 * Created by lbuzmacov on 20-04-16.
 */
public class CommitModel {

    private String sha;
    private Commit commit;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
