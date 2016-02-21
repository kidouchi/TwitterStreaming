package kidouchi.twitterstreaming;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by iuy407 on 12/9/15.
 */
public class CustomTweetTimelineListAdapter extends TweetTimelineListAdapter {

    public CustomTweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
        super(context, timeline);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
