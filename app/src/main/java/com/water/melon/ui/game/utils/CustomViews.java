package com.water.melon.ui.game.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.water.melon.utils.ScreenUtils;

@SuppressLint("AppCompatCustomView")
public class CustomViews extends ImageView {
    private int maxWidth;
    private int maxHeight;
    private int viewWidth;
    private int viewHeight;
    private float downx;
    private float downy;

    private OnItemClickListener listener;

    private float mOriginalX, mOriginalY;//手指按下时的初始位置

    public CustomViews(Context context) {
        super(context, null);
    }

    public CustomViews(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //屏幕得宽度
        maxWidth = ScreenUtils.getScreenWidth(getContext());
        //屏幕得高度
        maxHeight = ScreenUtils.getScreenHeight(getContext());
        /**
         *  控件的宽高
         */
        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clearAnimation();
                downx = event.getX();
                downy = event.getY();

                mOriginalX = event.getRawX();
                mOriginalY = event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX() - downx;
                float moveY = event.getRawY() - downy;
                moveX = moveX < 0 ? 0 : (moveX + viewWidth > maxWidth) ? (maxWidth - viewWidth) : moveX;
                moveY = moveY < 0 ? 0 : (moveY + viewHeight) > maxHeight ? (maxHeight - viewHeight) : moveY;
                this.setY(moveY);
                this.setX(moveX);
                return true;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getRawX() - mOriginalX) <
                        ScreenUtils.px2dip(getContext(), 5) &&
                        Math.abs(event.getRawY() - mOriginalY) <
                                ScreenUtils.px2dip(getContext(), 5)) {
                    if (listener != null) {
                        listener.onItemClick(0);
                    }
                } else {
                    //做吸附效果
                    float centerX = getX() + viewWidth / 2;
                    if (centerX > maxWidth / 2) {
                        //靠右吸附
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(maxWidth - viewWidth - getX())
                                .start();
                    } else {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .x(0)
                                .start();
                    }
                }

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
