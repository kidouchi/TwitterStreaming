package kidouchi.twitterstreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class InstagramMedia {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("caption")
    @Expose
    private Caption caption;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("location")
    @Expose
    private Object location;

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return The caption
     */
    public Caption getCaption() {
        return caption;
    }

    /**
     * @param caption The caption
     */
    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    /**
     * @return The likes
     */
    public Likes getLikes() {
        return likes;
    }

    /**
     * @param likes The likes
     */
    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return The images
     */
    public Images getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
     * @return The location
     */
    public Object getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(Object location) {
        this.location = location;
    }

}