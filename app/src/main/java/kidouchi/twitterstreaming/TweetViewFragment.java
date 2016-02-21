package kidouchi.twitterstreaming;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

public class TweetViewFragment extends Fragment {
    private static final String TWEET_ID_PARAM = "tweet_param";

    private FragmentActivity listener;

    // TODO: Rename and change types of parameters
    private long mTweetId;

    public TweetViewFragment() {
        // Required empty public constructor
    }

    public static TweetViewFragment newInstance(long tweetId) {
        TweetViewFragment fragment = new TweetViewFragment();
        Bundle args = new Bundle();
        args.putLong(TWEET_ID_PARAM, tweetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTweetId = getArguments().getLong(TWEET_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//
//        View child;
//        for (int i = 0; i < parentView.getChildCount(); i++) {
//            child = parentView.getChildAt(i);
////            Log.d("CHILD VIEW", child.get);
//        }


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tweet_view, container, false);
        final ViewGroup parentView = (ViewGroup) view.getRootView();
        TweetUtils.loadTweet(mTweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(getActivity(), result.data);
                parentView.addView(tweetView);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Load Tweet failure", e);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
