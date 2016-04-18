package kidouchi.twitterstreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;

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

    /**
     * @return The profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * @param profilePicture The profile_picture
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}