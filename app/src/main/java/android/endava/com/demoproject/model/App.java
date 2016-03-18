package android.endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;

public class App {
    private String name;

    private String url;

    @SerializedName("client_id")
    private String clientId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
