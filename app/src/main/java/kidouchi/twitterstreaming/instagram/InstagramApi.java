package kidouchi.twitterstreaming.instagram;

/**
 * Created by iuy407 on 4/12/16.
 */
public class InstagramApi {

    public static final String CLIENT_ID = "a004f7bdc8a048259fbf53469cbaf4b8";
    public static final String CLIENT_SECRET = "377669199dc743d2a8bb216d84ce5d62";
    public static final String CALLBACK_URL = "http://socialstreaming/callback";

    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";

    public static final String API_URL = "https://api.instagram.com/v1/";

    private String requestToken = "";


    public static String authURLString = AUTH_URL + "?client_id=" + CLIENT_ID +
            "&redirect_uri=" + CALLBACK_URL +
            "&response_type=code&display=touch&scope=basic";

    public static String tokenURLString = TOKEN_URL + "?client_id=" +
            CLIENT_ID + "&client_secret=" + CLIENT_SECRET +
            "&redirect_uri=" + CALLBACK_URL + "&grant_type=authorization_code";


    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String token) {
        this.requestToken = token;
    }

}
