package com.satis.example.overscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sick on 2016/8/8.
 * 自定义可滚动组件的弹性容器，仿IOS回弹效果
 */
public class RVScrollLayout extends LinearLayout {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 容器中的组件
     */
    private View convertView;
    /**
     * 如果容器中的组件为RecyclerView
     */
    private RecyclerView recyclerView;
    /**
     * 滚动结束
     */
    private int mStart;
    /**
     * 滚动结束
     */
    private int mEnd;
    /**
     * 上一次滑动的坐标
     */
    private int mLastY;
    /**
     * 滚动辅助类
     */
    private Scroller mScroller;
 
    public RVScrollLayout(Context context) {
        this(context, null);
    }
 
    public RVScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    public RVScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
 
    }
 
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new RuntimeException(RVScrollLayout.class.getSimpleName() + "只能有一个子控件");
        }
        convertView = getChildAt(0);
       //TODO 可以拓展ListView等可滑动的组件
        if (convertView instanceof RecyclerView) {
            recyclerView = (RecyclerView) convertView;
        }
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            View view = getChildAt(0);
            view.layout(left, top, right, bottom);
        }
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
 
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();  //终止动画
                }
                scrollTo(0, (int) ((mLastY - y) * 0.4));
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                /**
                 * 回弹动画，第一二个参数为开始的x，y
                 * 第三个和第四个参数为滚动的距离（注意方向问题）
                 * 第五个参数是回弹时间
                 */
                mScroller.startScroll(0, mEnd, 0, -dScrollY, 1000);
                break;
        }
        postInvalidate();
        return true;
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
 
        int y = (int) ev.getY();
        Log.d(TAG, "相对于组件滑过的距离==getY()：" + y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 下面两个判断来自于 BGARefreshLayout 框架中的判断，github 上搜索 BGARefreshLayout
                 */
                if (convertView instanceof RecyclerView) {
                    if (y - mLastY > 0) {
                        if (Util.isRecyclerViewToTop(recyclerView)) {
                            Log.d(TAG, "滑倒顶部时时间拦截成功");
                            return true;
                        }
                    }
 
                    if (y - mLastY < 0) {
                        if (Util.isRecyclerViewToBottom(recyclerView)) {
                            Log.d(TAG, "滑倒底部时时间拦截成功");
                            return true;
                        }
                    }
                }
                break;
        }
 
        return false;
    }
 
 
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}

