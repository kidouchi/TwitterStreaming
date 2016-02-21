package kidouchi.twitterstreaming;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.LinkedList;

public class TweetViewActivity extends AppCompatActivity {

    private String mQuery;
    private long previousTweetId = 0L;
    private LinkedList<Tweet> mQueue;
    private SearchTimeline mSearchTimeline;
    private Handler mHandler;
    private int mInterval = 10000;

    private TextView mHashtagTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_view);

        mQuery = getIntent().getStringExtra("query");

        mHashtagTextView = (TextView) findViewById(R.id.hashtag_title_tweet_view);
        mHashtagTextView.setText("Tweet to " + mQuery);

        mSearchTimeline = new SearchTimeline.Builder()
                .query(mQuery)
                .build();

        mQueue = new LinkedList<Tweet>();
        mHandler = new Handler();
    }


    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            // Fill up queue with tweets
            Toast.makeText(TweetViewActivity.this, "Queue Size: " + mQueue.size(),
                    Toast.LENGTH_LONG).show();
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
                        e.printStackTrace();
                    }
                });
                mHandler.postDelayed(mRun, 1000);
            } else {
                // Show tweets in view
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                Tweet tweet = mQueue.remove();
                // Update the most previous tweet seen
                previousTweetId = tweet.getId();
                // Update tweet fragment view
                TweetViewFragment tweetViewFragment =
                        TweetViewFragment.newInstance(tweet.getId());
                ft.replace(R.id.tweet_view_fragment, tweetViewFragment);
                ft.commit();
                mHandler.postDelayed(mRun, mInterval);
            }
        }
    };

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
