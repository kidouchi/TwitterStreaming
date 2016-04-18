package kidouchi.twitterstreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class From {

    @SerializedName("username")
    @Expose
    private String username;

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

}