package kidouchi.twitterstreaming.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by iuy407 on 4/13/16.
 */
public class AsyncInstagramSignIn extends AsyncTask<Void, Void, Void> {

    private Context context;

    public AsyncInstagramSignIn(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
//            URL url = new URL(InstagramApi.TOKEN_URL);
            URL url = new URL(InstagramApi.tokenURLString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
//            httpsURLConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            SharedPreferences sprefs = context.getSharedPreferences("kidouchi", Context.MODE_PRIVATE);
            String token = sprefs.getString("requestToken", "");
            outputStreamWriter.write("client_id=" + InstagramApi.CLIENT_ID +
                    "&client_secret=" + InstagramApi.CLIENT_SECRET +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + InstagramApi.CALLBACK_URL +
                    "&code=" + token);
            outputStreamWriter.flush();
            String response = getStringFromInputStream(httpsURLConnection.getInputStream());
            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
            String accessTokenString = jsonObject.getString("access_token"); //Here is your ACCESS TOKEN
            String id = jsonObject.getJSONObject("user").getString("id");
            String username = jsonObject.getJSONObject("user").getString("username"); //This is how you can get the user info. You can explore the JSON sent by Instagram as well to know what info you got in a response
            sprefs.edit().putString("accessToken", accessTokenString);
            sprefs.edit().putString("username", username);
            sprefs.edit().putString("userId", id);
            sprefs.edit().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // convert InputStream to String
    private String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
