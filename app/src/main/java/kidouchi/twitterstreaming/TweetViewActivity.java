package kidouchi.twitterstreaming;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.Hashtable;
import java.util.LinkedList;

public class TweetViewActivity extends AppCompatActivity {

    private String mQuery;
    private long previousTweetId = 0L;
    private LinkedList<Tweet> mQueue;
    private SearchTimeline mSearchTimeline;
    private Handler mHandler;
    private int mInterval = 10000;
    private Hashtable<String, Integer> adsMap = new Hashtable<String, Integer>();
    private String[] matches;

    private TextView mHashtagTextView;
    private ImageView mTwitterLogo;
    private ImageView mCapOneAds;

    private Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_view);

        // Setup animation
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        setupAdsMap();
        // TODO: Check query from right activities
        mQuery = getIntent().getStringExtra("query");

        // setup views
        mHashtagTextView = (TextView) findViewById(R.id.hashtag_title_tweet_view);
        mTwitterLogo = (ImageView) findViewById(R.id.twitter_logo_stream);
        mCapOneAds = (ImageView) findViewById(R.id.capitalone_ads);

        mHashtagTextView.setText("Tweet to " + mQuery);

        final Animation birdDance = AnimationUtils.loadAnimation(this, R.anim.bird_dancing);
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

        // Go to list view of tweet streams
        View view = findViewById(android.R.id.content).getRootView();
        view.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                Toast.makeText(TweetViewActivity.this, "Left", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TweetViewActivity.this, StreamingActivity.class);
                intent.putExtra("query", mQuery);
                startActivity(intent);
            }
        });

        mSearchTimeline = new SearchTimeline.Builder()
                .query(mQuery)
                .build();

        mQueue = new LinkedList<Tweet>();
        mHandler = new Handler();
    }

    // Setup for showing new tweets in intervals
    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            // Fill up queue with tweets
            Toast.makeText(TweetViewActivity.this, "Queue Size: " + mQueue.size(),
                    Toast.LENGTH_LONG).show();
            mCapOneAds.setVisibility(View.GONE);
            if (mQueue.size() == 0) {
                mSearchTimeline.next(previousTweetId, new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        for (Tweet tweet : result.data.items) {
                            mQueue.add(tweet);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(TweetViewActivity.this,
                                "Failure to connect to Twitter", Toast.LENGTH_LONG).show();
                    }
                });
                mHandler.postDelayed(mRun, 1000);
            } else {
                // Show tweets in view
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Tweet tweet = mQueue.remove();

                for (String s : matches) {
                    if (tweet.text.contains(s)) {
                        Log.d("MATCHED", tweet.text);
                        mCapOneAds.setVisibility(View.VISIBLE);
                        Glide.with(TweetViewActivity.this)
                                .load(adsMap.get(s))
                                .into(mCapOneAds);
                        mCapOneAds.startAnimation(fadeIn);
                        break;
                    }
                }
                // Update the most previous tweet seen
                previousTweetId = tweet.getId();
                // Update tweet fragment view
                TweetViewFragment tweetViewFragment =
                        TweetViewFragment.newInstance(tweet.getId());
                ft.replace(R.id.tweet_view_fragment, tweetViewFragment);
                ft.commit();

                // Run in intervals
                mHandler.postDelayed(mRun, mInterval);
            }
        }
    };

    // Setup keywords that map to specific AD images
    public void setupAdsMap() {
        adsMap.put("card", R.drawable.quicksilver);
        adsMap.put("quicksilver", R.drawable.quicksilver);
        adsMap.put("mortgage", R.drawable.mortgage);
        matches = new String[adsMap.keySet().size()];
        adsMap.keySet().toArray(matches);
        Log.d("MATCHES", matches.length + "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRun.run();
    }

    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mRun);
        super.onStop();
    }
}
