package endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;

public class Avatar {
    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
