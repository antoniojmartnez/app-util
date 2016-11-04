package com.ibs.tecnicos.seyte.app;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Animations {
    private App app;

    public Animations(App app) {
        this.app = app;
    }

    public void fadeIn (final View view, int duration) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation arg0) {

            }
            public void onAnimationRepeat(Animation arg0) {

            }

            public void onAnimationEnd(Animation arg0) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(animation);
    }

    public void fadeOut (final View view, int duration) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation arg0) {

            }
            public void onAnimationRepeat(Animation arg0) {

            }

            public void onAnimationEnd(Animation arg0) {
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);
    }
}
