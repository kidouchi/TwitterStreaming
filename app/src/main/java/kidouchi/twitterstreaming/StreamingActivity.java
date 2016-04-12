package kidouchi.twitterstreaming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import io.fabric.sdk.android.Fabric;

public class StreamingActivity extends Activity {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3N6v5WACB2ve8oUYi50ZpFzKt";
    private static final String TWITTER_SECRET = "xDV0VX1SpnglmD1i7MTsB4r9LVOrwOiiXGH8dBEGgdBrLBlW52";

    private Handler mHandler;
    private TweetTimelineListAdapter mAdapter;
    private int mInterval = 10000;

    private String mQuery;

    private TextView mHashtagTitle;
    private ImageView mTwitterLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_streaming);

        mHashtagTitle = (TextView) findViewById(R.id.hashtag_title);
        mTwitterLogo = (ImageView) findViewById(R.id.twitter_logo_stream);

        // TODO: Check query from right activities
        mQuery = getIntent().getStringExtra("query");
        mHashtagTitle.setText("Tweet out to " + mQuery);

        // Animation the twitter bird
        final Animation birdDance = AnimationUtils.loadAnimation(this, R.anim.bird_dancing);
        Log.d("I'M HERE", birdDance.hasStarted() + "");
        birdDance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                birdDance.setAnimationListener(this);
                mTwitterLogo.startAnimation(birdDance);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTwitterLogo.startAnimation(birdDance);

        // Go to individual view of tweets
        View view = findViewById(android.R.id.content).getRootView();
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(StreamingActivity.this, "Left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StreamingActivity.this, TweetViewActivity.class);
                intent.putExtra("query", mQuery);
                startActivity(intent);
            }
        });

        final SearchTimeline timeline = new SearchTimeline.Builder()
                .maxItemsPerRequest(10)
                .languageCode("en")
                .query(mQuery)
                .build();

        /** TWITTER TIMELINE **/
        mAdapter = new TweetTimelineListAdapter.Builder(this)
                .setOnActionCallback(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {

                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(StreamingActivity.this,
                                "Failure to connect to Twitter", Toast.LENGTH_LONG).show();
                    }
                })
                .setTimeline(timeline)
                .build();

        ListView listView = (ListView) findViewById(R.id.list_stream);
        listView.setAdapter(mAdapter);

        mHandler = new Handler();
    }

    Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            mAdapter.refresh(new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                    Log.d("DEBUG", result.response.toString());
                }

                @Override
                public void failure(TwitterException exception) {
                    // Toast or some other action
                    Toast.makeText(StreamingActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            mHandler.postDelayed(mRefresh, mInterval);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mRefresh.run();
    }

    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mRefresh);
        finishAfterTransition();
        super.onStop();
    }
}
