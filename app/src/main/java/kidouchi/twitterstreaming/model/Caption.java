package kidouchi.twitterstreaming.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caption {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("from")
    @Expose
    private From from;

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return The from
     */
    public From getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(From from) {
        this.from = from;
    }

}