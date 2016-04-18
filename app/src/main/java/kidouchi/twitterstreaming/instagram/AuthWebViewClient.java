package kidouchi.twitterstreaming.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by iuy407 on 4/12/16.
 */
public class AuthWebViewClient extends WebViewClient {

    private Context context;

    public AuthWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(InstagramApi.CALLBACK_URL)) {
            String[] parts = url.split("=");
            String requestToken = parts[1];
            Log.d("TOKEN", requestToken);
            SharedPreferences sprefs = context.getSharedPreferences("kidouchi", Context.MODE_PRIVATE);
            sprefs.edit().putString("requestToken", requestToken);
            sprefs.edit().commit();
            AsyncInstagramSignIn runner = new AsyncInstagramSignIn(context);
            runner.execute();
            return true;
        }
        return false;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onReceivedSslError(WebView view,
                                   SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        if(error.getPrimaryError()==SslError.SSL_UNTRUSTED){
            handler.proceed();
        }else{
            handler.proceed();
        }
    }


}
