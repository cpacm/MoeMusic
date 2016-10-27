package com.cpacm.moemusic.ui.widgets.floatingmusicmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.cpacm.moemusic.R;

/**
 * @author: cpacm
 * @date: 2016/8/10
 * @desciption: floating music menu
 */
@CoordinatorLayout.DefaultBehavior(FloatingMusicMenu.Behavior.class)
public class FloatingMusicMenu extends ViewGroup {

    private static final int SHADOW_OFFSET = 20;

    private FloatingMusicButton floatingMusicButton;
    private AnimatorSet showAnimation;
    private AnimatorSet hideAnimation;

    private int progressWidthPercent;
    private int progressColor;
    private float progress;
    private float buttonSpace;
    private ColorStateList backgroundTint;
    private Drawable cover;
    private boolean isExpanded;
    private boolean isHided;
    private boolean isRotated;

    public FloatingMusicMenu(Context context) {
        this(context, null);
    }

    public FloatingMusicMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMenu(context, attrs);
    }

    public FloatingMusicMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMenu(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatingMusicMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMenu(context, attrs);
    }

    private void initMenu(Context context, AttributeSet attrs) {

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingMusicMenu, 0, 0);
        progressWidthPercent = attr.getInteger(R.styleable.FloatingMusicMenu_fmm_progress_percent, 3);
        progressColor = attr.getColor(R.styleable.FloatingMusicMenu_fmm_progress_color, getResources().getColor(android.R.color.holo_blue_dark));
        progress = attr.getFloat(R.styleable.FloatingMusicMenu_fmm_progress, 0);
        buttonSpace = attr.getDimension(R.styleable.FloatingMusicMenu_fmm_button_space, 4);
        buttonSpace = dp2px(buttonSpace);
        cover = attr.getDrawable(R.styleable.FloatingMusicMenu_fmm_cover);
        backgroundTint = attr.getColorStateList(R.styleable.FloatingMusicMenu_fmm_backgroundTint);
        attr.recycle();

        createRootButton(context);

        addScrollAnimation();
    }

    private void addScrollAnimation() {
        showAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
        showAnimation.play(ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f));
        showAnimation.setInterpolator(alphaExpandInterpolator);
        showAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setVisibility(VISIBLE);
            }
        });

        hideAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
        hideAnimation.play(ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f));
        hideAnimation.setInterpolator(alphaExpandInterpolator);
        hideAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(GONE);
            }
        });
    }

    private void createRootButton(Context context) {
        floatingMusicButton = new FloatingMusicButton(context);
        floatingMusicButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        floatingMusicButton.config(progressWidthPercent, progressColor, backgroundTint);
        floatingMusicButton.setProgress(progress);
        if (cover != null) {
            floatingMusicButton.setCoverDrawable(cover);
        }
    }

    public void setProgress(float progress) {
        if (floatingMusicButton != null) {
            floatingMusicButton.setProgress(progress);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(floatingMusicButton, super.generateDefaultLayoutParams());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            width = Math.max(child.getMeasuredWidth(), width);
            height += child.getMeasuredHeight();
        }
        width += SHADOW_OFFSET * 2;
        height += SHADOW_OFFSET * 2;
        height += buttonSpace * (getChildCount() - 1);
        height = adjustShootHeight(height);
        setMeasuredDimension(width, height);
    }

    private int adjustShootHeight(int height) {
        return height * 12 / 10;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerX = (r - l) / 2;
        int offsetY = b - t - SHADOW_OFFSET;

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(centerX - width / 2, offsetY - height, centerX + width / 2, offsetY);

            //排除根按钮
            if (i != getChildCount() - 1) {
                float collapsedTranslation = b - t - SHADOW_OFFSET - offsetY;
                float expandedTranslation = 0f;
                child.setTranslationY(isExpanded ? expandedTranslation : collapsedTranslation);
                child.setAlpha(isExpanded ? 1f : 0f);

                MenuLayoutParams params = (MenuLayoutParams) child.getLayoutParams();
                params.collapseDirAnim.setFloatValues(expandedTranslation, collapsedTranslation);
                params.expandDirAnim.setFloatValues(collapsedTranslation, expandedTranslation);
                params.setAnimationsTarget(child);
            }

            offsetY -= height + buttonSpace;
        }

    }

    public void addButton(FloatingActionButton button) {
        addView(button, 0);
        requestLayout();
    }

    public void removeButton(FloatingActionButton button) {
        removeView(button);
        requestLayout();
    }

    public void setMusicCover(Drawable drawable) {
        floatingMusicButton.setCoverDrawable(drawable);
        if (isRotated) {
            rotateStart();
        } else {
            rotateStop();
        }
    }

    public void setMusicCover(RotatingProgressDrawable drawable) {
        floatingMusicButton.setCoverDrawable(drawable);
        if (isRotated) {
            rotateStart();
        } else {
            rotateStop();
        }
    }

    public void setMusicCover(Bitmap bitmap) {
        floatingMusicButton.setCover(bitmap);
        if (isRotated) {
            rotateStart();
        } else {
            rotateStop();
        }
    }

    public void rotateStart() {
        isRotated = true;
        floatingMusicButton.start();
    }

    public void rotateStop() {
        isRotated = false;
        floatingMusicButton.stop();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MenuLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MenuLayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MenuLayoutParams(super.generateLayoutParams(p));
    }

    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

    private static Interpolator expandInterpolator = new OvershootInterpolator();
    private static Interpolator collapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator alphaExpandInterpolator = new DecelerateInterpolator();

    private class MenuLayoutParams extends LayoutParams {

        private ObjectAnimator expandDirAnim = new ObjectAnimator();
        private ObjectAnimator expandAlphaAnim = new ObjectAnimator();
        private ObjectAnimator collapseDirAnim = new ObjectAnimator();
        private ObjectAnimator collapseAlphaAnim = new ObjectAnimator();

        private boolean animationsSetToPlay;

        public MenuLayoutParams(LayoutParams source) {
            super(source);

            expandDirAnim.setInterpolator(expandInterpolator);
            expandAlphaAnim.setInterpolator(alphaExpandInterpolator);
            collapseDirAnim.setInterpolator(collapseInterpolator);
            collapseAlphaAnim.setInterpolator(collapseInterpolator);

            collapseAlphaAnim.setProperty(View.ALPHA);
            collapseAlphaAnim.setFloatValues(1f, 0f);

            expandAlphaAnim.setProperty(View.ALPHA);
            expandAlphaAnim.setFloatValues(0f, 1f);

            collapseDirAnim.setProperty(View.TRANSLATION_Y);
            expandDirAnim.setProperty(View.TRANSLATION_Y);
        }

        public void setAnimationsTarget(View view) {
            collapseAlphaAnim.setTarget(view);
            collapseDirAnim.setTarget(view);
            expandDirAnim.setTarget(view);
            expandAlphaAnim.setTarget(view);

            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(expandDirAnim, view);
                addLayerTypeListener(collapseDirAnim, view);

                mCollapseAnimation.play(collapseAlphaAnim);
                mCollapseAnimation.play(collapseDirAnim);
                mExpandAnimation.play(expandAlphaAnim);
                mExpandAnimation.play(expandDirAnim);
                animationsSetToPlay = true;
            }
        }

        private void addLayerTypeListener(Animator animator, final View view) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(LAYER_TYPE_NONE, null);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setLayerType(LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }

    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }

    private void collapse(boolean immediately) {
        if (isExpanded) {
            isExpanded = false;
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();
        }
    }

    public void toggle() {
        if (isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!isExpanded) {
            isExpanded = true;
            mCollapseAnimation.cancel();
            mExpandAnimation.start();
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void hide() {
        if (!isHided) {
            isHided = true;
            hideAnimation.start();
            showAnimation.cancel();
        }
    }

    public void show() {
        if (isHided) {
            isHided = false;
            showAnimation.start();
            hideAnimation.cancel();
        }
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static class Behavior extends CoordinatorLayout.Behavior<FloatingMusicMenu> {

        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingMusicMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            if (dyConsumed > 30 && child.getVisibility() == VISIBLE) {
                child.hide();
            } else if (dyConsumed < -30 && child.getVisibility() == GONE) {
                child.show();
            }
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingMusicMenu child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }
    }
}
