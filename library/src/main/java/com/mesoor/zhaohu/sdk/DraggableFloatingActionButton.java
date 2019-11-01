package com.mesoor.zhaohu.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DraggableFloatingActionButton extends FloatingActionButton {
    static final String TOKEN = "com.mesoor.zhaohu.sdk.TOKEN";
    static final String FROM = "com.mesoor.zhaohu.sdk.FROM";
    static final String ENV = "com.mesoor.zhaohu.sdk.ENV";
    static final String DEBUG = "com.mesoor.zhaohu.sdk.DEBUG";

    public enum ZhaohuEnvironmentEnum {
        PROD, DEV
    }

    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private float downRawX, downRawY;
    private float dX, dY;
    private String token;
    private String from;
    private boolean debug = false;
    private ZhaohuEnvironmentEnum env = ZhaohuEnvironmentEnum.PROD;
    private Activity activity;
    private Class<? extends ZhaohuActivity> webviewActivityClass;

    public DraggableFloatingActionButton(Context context) {
        super(context);
        setup();
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void initialize(@NonNull Activity activity,
                           @NonNull String token,
                           @NonNull String from,
                           @NonNull Class<? extends ZhaohuActivity> webviewActivityClass) {
        this.activity = activity;
        this.token = token;
        this.from = from;
        this.webviewActivityClass = webviewActivityClass;
    }

    public void setDebuggingEnabled(boolean enabled) {
        this.debug = enabled;
    }

    public void setEnv(ZhaohuEnvironmentEnum env) {
        this.env = env;
    }

    private void setup() {
        this.setImageResource(R.drawable.mesoor_round_logo);
        setOnTouchListener(this::onTouch);
        setOnClickListener(this::onClick);
    }

    private boolean onTouch(View view, MotionEvent motionEvent) {
        View viewParent;
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("MovableFAB", "ACTION_DOWN");
                downRawX = motionEvent.getRawX();
                downRawY = motionEvent.getRawY();
                dX = view.getX() - downRawX;
                dY = view.getY() - downRawY;

                return true; // Consumed

            case MotionEvent.ACTION_MOVE:
                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();

                viewParent = (View) view.getParent();
                int parentWidth = viewParent.getWidth();
                int parentHeight = viewParent.getHeight();

                float newX = motionEvent.getRawX() + dX;
                newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
                newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent

                float newY = motionEvent.getRawY() + dY;
                newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
                newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent

                view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();
                return true; // Consumed

            case MotionEvent.ACTION_UP:


                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - downRawX;
                float upDY = upRawY - downRawY;

                if ((Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) && performClick())
                    return true;

                View viewParent2 = (View) view.getParent();
                float borderY, borderX;
                float oldX = view.getX(), oldY = view.getY();
                float finalX, finalY;

                borderY = Math.min(view.getY() - viewParent2.getTop(), viewParent2.getBottom() - view.getY());
                borderX = Math.min(view.getX() - viewParent2.getLeft(), viewParent2.getRight() - view.getX());

                //You can set your dp margin from dimension resources (Suggested)
                //float fab_margin= getResources().getDimension(R.dimen.fab_margin);
                float fab_margin = 15;

                //check if is nearest Y o X
                if (borderX > borderY) {
                    if ((int)view.getY() > viewParent2.getHeight() / 2) { //view near Bottom
                        finalY = viewParent2.getBottom() - view.getHeight();
                        finalY = Math.min(viewParent2.getHeight() - view.getHeight(), finalY) - fab_margin; // Don't allow the FAB past the bottom of the parent
                    } else {  //view vicina a Top
                        finalY = viewParent2.getTop();
                        finalY = Math.max(0, finalY) + fab_margin; // Don't allow the FAB past the top of the parent
                    }
                    //check if X it's over fab_margin
                    finalX = oldX;
                    if (view.getX() + viewParent2.getLeft() < fab_margin)
                        finalX = viewParent2.getLeft() + fab_margin;
                    if (viewParent2.getRight() - view.getX() - view.getWidth() < fab_margin)
                        finalX = viewParent2.getRight() - view.getWidth() - fab_margin;
                } else {  //view near Right
                    if ((int)view.getX() > viewParent2.getWidth() / 2) {
                        finalX = viewParent2.getRight() - view.getWidth();
                        finalX = Math.max(0, finalX) - fab_margin; // Don't allow the FAB past the left hand side of the parent
                    } else {  //view near Left
                        finalX = viewParent2.getLeft();
                        finalX = Math.min(viewParent2.getWidth() - view.getWidth(), finalX) + fab_margin; // Don't allow the FAB past the right hand side of the parent
                    }
                    //check if Y it's over fab_margin
                    finalY = oldY;
                    if (view.getY() + viewParent2.getTop() < fab_margin)
                        finalY = viewParent2.getTop() + fab_margin;
                    if (viewParent2.getBottom() - view.getY() - view.getHeight() < fab_margin)
                        finalY = viewParent2.getBottom() - view.getHeight() - fab_margin;
                }

                view.animate()
                        .x(finalX)
                        .y(finalY)
                        .setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();

                Log.d("MovableFAB", "ACTION_UP");
                return false;

            // A drag consumed
            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    private void onClick(View view) {
        if (this.activity == null || this.token == null || this.from == null) {
            Snackbar.make(view, "Please call the initialize method before using it.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            showWebView();
        }
    }

    private void showWebView() {
        Intent webview = new Intent(this.activity, webviewActivityClass);
        webview.putExtra(TOKEN, this.token);
        webview.putExtra(FROM, this.from);
        webview.putExtra(ENV, this.env == ZhaohuEnvironmentEnum.PROD ? "mesoor" : "nadileaf");
        webview.putExtra(DEBUG, this.debug);
        this.activity.startActivity(webview);
    }
}

