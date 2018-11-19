package ensharp.imagincup2019.fishingphishing.UI.UIElements;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;

public class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

    WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

    @Override
    public void executeAnim(final View target, final int animType) {
        if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
            return;
        }
        if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
            return;
        }
        if(mOriginalViewHeightPool.get(target)==null){
            mOriginalViewHeightPool.put(target,target.getHeight());
        }
        final int viewHeight = mOriginalViewHeightPool.get(target);
        float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
        float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
        final ViewGroup.LayoutParams lp = target.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
        animator.setDuration(200);
        target.setVisibility(View.VISIBLE);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                    target.setVisibility(View.VISIBLE);
                } else {
                    target.setVisibility(View.GONE);
                }
                target.getLayoutParams().height = viewHeight;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                target.setLayoutParams(lp);
                target.requestLayout();
            }
        });
        animator.start();
    }
}
