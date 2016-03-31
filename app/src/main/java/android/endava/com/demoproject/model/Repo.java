package android.endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;

public class Repo {
    private long id;
    private String name;
    private String description;

    @SerializedName("html_url")
    private String homeUrl;

    @SerializedName("default_branch")
    private String defaultBranch;

    @SerializedName("language")
    private String codeLanguage;

    private float size;

    @SerializedName("pushed_at")
    private String lastPush;

    @SerializedName("open_issues")
    private int openIssues;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getLastPush() {
        return lastPush;
    }

    public void setLastPush(String lastPush) {
        this.lastPush = lastPush;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }
}
