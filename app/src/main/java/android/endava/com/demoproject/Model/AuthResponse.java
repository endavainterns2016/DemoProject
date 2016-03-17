package android.endava.com.demoproject.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by agrebennicov on 3/16/2016.
 */
public class AuthResponse {


    @SerializedName("hashed_token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
