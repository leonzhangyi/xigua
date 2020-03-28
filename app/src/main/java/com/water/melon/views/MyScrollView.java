package com.water.melon.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MyScrollView extends ScrollView {
    private List<OnScrollListener> onScrollListeners;

    //滑动开始、结束状态回调
    private List<OnScrollStateListener> mOnScrollStateListeners;

    /** 当前滑动到y坐标 */
    private int mCurrentY;

    /** 滑动到底部监听器 */
    private OnScrollToBottomListener mOnScrollToBottom;

    /** 滑动到底部监听器 */
    public interface OnScrollToBottomListener {
        void onScrollBottom();
    }

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    private long DELAY_TIME = 100;    //延迟判断滑动是否结束
    private long lastScrollTime = -1;  //上次滑动时间

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            // 此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollListeners != null) {
                for (OnScrollListener listner : onScrollListeners) {
                    listner.onScroll(scrollY);
                }
            }
        }

        ;
    };

    //通过延时时间判断onScroll函数是否执行
    private Runnable scrollTask = new Runnable() {
        @Override public void run() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastScrollTime > DELAY_TIME) {
                lastScrollTime = -1;
                onScrollStop();
            } else {
                //滑动中
                handler.postDelayed(this, DELAY_TIME);
            }
        }
    };

    //开始滑动
    private void onScrollStart() {
        if (mOnScrollStateListeners == null) {
            return;
        }

        for (OnScrollStateListener listener:mOnScrollStateListeners) {
            listener.onScrollStart();
        }
    }

    //停止滑动
    private void onScrollStop() {
        if (mOnScrollStateListeners == null) {
            return;
        }

        for (OnScrollStateListener listener:mOnScrollStateListeners) {
            listener.onScrollStop();
        }
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滑动状态监听
     */
    public void setOnScrollStateListener(OnScrollStateListener listener) {
        if (mOnScrollStateListeners == null) {
            mOnScrollStateListeners = new ArrayList<>();
        }

        if (listener != null) {
            mOnScrollStateListeners.add(listener);
        }
    }

    public void removeScrollStateListener(OnScrollStateListener listener) {
        if (listener != null && mOnScrollStateListeners != null) {
            mOnScrollStateListeners.remove(listener);
        }
    }

    /**
     * 设置滚动接口
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListeners == null) {
            onScrollListeners = new ArrayList<>();
        }
        onScrollListeners.add(onScrollListener);
    }

    public void removeAllScrollListener() {
        if (onScrollListeners != null) {
            onScrollListeners.clear();
        }
    }

    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        mOnScrollToBottom = listener;
    }

    private float xDistance, yDistance, lastX, lastY;

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListeners != null) {
            for (OnScrollListener listner : onScrollListeners) {
                listner.onScroll(lastScrollY = this.getScrollY());
                listner.onTouchEvent(ev);
            }
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnScrollStateListener {
        void onScrollStart();  //开始滑动

        void onScrollStop();   //停止滑动
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {

        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);

        /**
         * 回调方法， 返回MyScrollView滑动到Y方向上的位置
         */
        public void onScrollPosition(int position);

        public void onTouchEvent(MotionEvent ev);

    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (onScrollListeners != null) {
            for (OnScrollListener listner : onScrollListeners) {
                int scrollY = getScrollY();
                listner.onScrollPosition(scrollY);
            }
        }

        if (lastScrollTime == -1) {
            onScrollStart();
            postDelayed(scrollTask, DELAY_TIME);
        }
        lastScrollTime = System.currentTimeMillis();  //更新scrollView的滑动时间

        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //下滑
        if (scrollY > mCurrentY && scrollY > 0 && clampedY) {
            if (null != mOnScrollToBottom) {
                mOnScrollToBottom.onScrollBottom();
            }
        }
        mCurrentY = scrollY;
    }
}
