package kidouchi.twitterstreaming;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private Button mGoButton;
    private ImageView mWhiteCircle;
    private EditText mQuerySearch;
    private ImageView mTwitterLogo;

    private Transition.TransitionListener mEnterTransitionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoButton = (Button) findViewById(R.id.start_query_button);
        mWhiteCircle = (ImageView) findViewById(R.id.shared_white_circle);
        mQuerySearch = (EditText) findViewById(R.id.query_search);
        mTwitterLogo = (ImageView) findViewById(R.id.twitter_logo_main);

        mGoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final MotionEvent mEvent = event;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (v.getId() == R.id.start_query_button) {
                        mEnterTransitionListener = new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                whiteOut((int)mEvent.getRawX(), (int)mEvent.getRawY());
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        };

                        getWindow().getEnterTransition().addListener(mEnterTransitionListener);

                        String query = mQuerySearch.getText().toString();
                        if (query.length() > 0) {
                            Intent intent = new Intent(MainActivity.this, StreamingActivity.class);
                            intent.putExtra("query", query);
                            circleDisappear(); // Make button disappear right as
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(
                                            MainActivity.this, mTwitterLogo, "twitter_logo");
                            startActivity(intent, options.toBundle());
                        }
                    }
                }
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressWarnings("deprecated")
    public void whiteOut(int cx, int cy) {
        View rootView = findViewById(android.R.id.content).getRootView();
//        int cx = mWhiteCircle.getWidth() / 2;
//        int cy = mWhiteCircle.getHeight() / 2;
        float finalRadius = (float) Math.hypot(rootView.getWidth(), rootView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0, finalRadius);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());

        // Finish the activity after transition
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
                finishAfterTransition();
            }
        });

        // Change bg color to white (accounting for getColor in version SDK 23)
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.M) {
            rootView.setBackgroundColor(getColor(android.R.color.white));
        } else {
            rootView.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        anim.start();
    }

    public void enterReveal(View myView) {
        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    public void circleDisappear() {
        // Get center of circle view
        int cx = mGoButton.getMeasuredWidth() / 2;
        int cy = mGoButton.getMeasuredHeight() / 2;

        // Get initial radius
        int initRadius = mGoButton.getWidth() / 2;

        Animator anim = ViewAnimationUtils
                .createCircularReveal(mGoButton, cx, cy, initRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mWhiteCircle.setVisibility(View.VISIBLE);
                mGoButton.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mWhiteCircle.setVisibility(View.INVISIBLE);
        mGoButton.setVisibility(View.VISIBLE);
    }
}
