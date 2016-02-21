package kidouchi.twitterstreaming;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private Button mGoButton;
    private EditText mQuerySearch;
    private ImageView mTwitterLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoButton = (Button) findViewById(R.id.start_query_button);
        mQuerySearch = (EditText) findViewById(R.id.query_search);
        mTwitterLogo = (ImageView) findViewById(R.id.twitter_logo_main);


//        Animation birdDance = AnimationUtils.loadAnimation(this, R.anim.bird_dancing);
//        AnimatorSet birdDance = new AnimatorSet();
//        birdDance.setInterpolator();
//        birdDance.playSequentially(
        ObjectAnimator birdDance = ObjectAnimator.ofInt(mTwitterLogo, "rotationX", -50, 50);
        birdDance.setDuration(500);
        birdDance.setRepeatCount(ValueAnimator.INFINITE);
        birdDance.setInterpolator(new BounceInterpolator());
//        );
        birdDance.start();

//        mTwitterLogo.setAnimation(birdDance);

        mGoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String query = mQuerySearch.getText().toString();
                        if (query.length() > 0) {
                            Intent intent = new Intent(MainActivity.this, StreamingActivity.class);
                            intent.putExtra("query", query);
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(
                                            MainActivity.this, mTwitterLogo, "twitter_logo");
                            startActivity(intent, options.toBundle());
                        }
                    }
                });
    }
}
