package com.dean.android.framework.convenient.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * 动画相关工具类
 * <p>
 * Created by dean on 2017/4/15.
 */
public class AnimatorUtils {

    /**
     * 执行在指定时间内从上到下展开view的动画
     *
     * @param view
     * @param viewHeight
     * @param duration
     * @param interpolator
     * @param animatorListener
     */
    public static synchronized void viewUnfoldFromTop2Bottom(final View view, int viewHeight, int duration, Interpolator interpolator,
                                                             final Animator.AnimatorListener animatorListener) {
        if (view == null || view.getHeight() == viewHeight)
            return;

        ValueAnimator heightAnimator = ValueAnimator.ofInt(0, viewHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = Integer.valueOf(animation.getAnimatedValue() + "");
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        });
        heightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);

                if (animatorListener != null)
                    animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationRepeat(animation);
            }
        });
        if (interpolator != null)
            heightAnimator.setInterpolator(interpolator);
        heightAnimator.setDuration(duration);
        heightAnimator.start();
    }

    /**
     * 执行在指定时间内从下到上收起view的动画
     *
     * @param view
     * @param duration
     * @param interpolator
     * @param animatorListener
     */
    public static synchronized void viewPackUpFromBottom2Top(final View view, int duration, Interpolator interpolator, final Animator.AnimatorListener animatorListener) {
        if (view == null || view.getHeight() == 0)
            return;

        ValueAnimator heightAnimator = ValueAnimator.ofInt(view.getHeight(), 0);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = Integer.valueOf(animation.getAnimatedValue() + "");
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        });
        heightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);

                if (animatorListener != null)
                    animatorListener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationRepeat(animation);
            }
        });
        if (interpolator != null)
            heightAnimator.setInterpolator(interpolator);
        heightAnimator.setDuration(duration);
        heightAnimator.start();
    }

}
