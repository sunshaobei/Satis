package com.satis.sliver.recyclerview

import android.R
import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.satis.sliver.recyclerview.wrapper.LoadMoreWrapper

/**
 * 万能分割线
 */
class DividerItemDecoration : ItemDecoration {
    private var mPaint: Paint? = null

    //取名mDivider似乎更恰当
    private var drawable: Drawable? = null

    //分割线高度，默认为1px
    var size = 2

    var color = Color.TRANSPARENT
        set(value) {
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint!!.color = value
            //填满颜色
            mPaint!!.style = Paint.Style.FILL
            field = value
        }

    //列表的方向
    var orientation = RecyclerView.HORIZONTAL
        get() = getmOrientation()

    /**
     *  设置分割线间距 仅 VERTICAL_LIST 生效
     */
    //分割线左间距
    var paddingLeft = 0

    //分割线右间距
    var paddingRight = 0


    /**
     *  设置分割线间距 仅 HORIZONTAL_LIST 生效
     */
    //分割线上间距
    var paddingTop = 0

    //分割线下间距
    var paddingBottom = 0

    private lateinit var mRecyclerView: RecyclerView


    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context     上下文
     * @param orientation 列表方向
     */
    constructor(recyclerView: RecyclerView) {
        this.mRecyclerView = recyclerView
    }

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context     上下文
     * @param orientation 列表方向
     */
    constructor(context: Context, recyclerView: RecyclerView, orientation: Int) {
        this.orientation = orientation
        mRecyclerView = recyclerView
        //获取xml配置的参数
        val a = context.obtainStyledAttributes(ATTRS)
        //typedArray.getDrawable(attr)这句是说我们可以通过我们的资源获得资源，使用我们的资源名attr去获得资源id
        //看不懂就用自己写一个分割线的图片吧，方法：ContextCompat.getDrawable(context, drawableId);
        drawable = a.getDrawable(0)
        //官方的解释是：回收TypedArray，以便后面重用。在调用这个函数后，你就不能再使用这个TypedArray。
        //在TypedArray后调用recycle主要是为了缓存。
        a.recycle()
    }

    /**
     * 自定义分割线
     *
     * @param context     上下文
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context?, recyclerView: RecyclerView, orientation: Int, drawableId: Int) {
        this.orientation = orientation
        mRecyclerView = recyclerView
        //旧的getDrawable方法弃用了，这个是新的
        drawable = ContextCompat.getDrawable(context!!, drawableId)
        size = drawable!!.intrinsicHeight
    }

    /**
     * 自定义分割线
     *
     * @param context       上下文
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(
        context: Context?, recyclerView: RecyclerView, orientation: Int,
        dividerHeight: Int, dividerColor: Int
    ) {
        mRecyclerView = recyclerView
        this.orientation = orientation
        size = dividerHeight
//        Log.e("mDividerHeight", "$size===================")
        //抗锯齿画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        //填满颜色
        mPaint!!.style = Paint.Style.FILL
    }


    private fun getmOrientation(): Int {
        val layoutManager = mRecyclerView.layoutManager
        return if (layoutManager is GridLayoutManager || layoutManager is StaggeredGridLayoutManager) {
            BOTH_SET
        } else {
            (layoutManager as LinearLayoutManager).orientation
        }
    }


    /**
     * 绘制分割线之后,需要留出一个外边框,就是说item之间的间距要换一下
     *
     * @param outRect outRect.set(0, 0, 0, 0);的四个参数理解成margin就好了
     * @param view    视图
     * @param parent  父级view
     * @param state
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //下面super...代码其实调用的就是那个过时的getItemOffsets,也就是说这个方法体内容也可以通通移到那个过时的getItemOffsets中
        super.getItemOffsets(outRect, view, parent, state)
        //获取layoutParams参数
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        //ItemView数量
        var layoutPosition = layoutParams.viewLayoutPosition
        val adapter = parent.adapter
        var childCount = adapter!!.itemCount
        if (checkSpecialPosition(parent,layoutPosition)){
            outRect.set(0, 0, 0, 0)
            return
        }
        when (orientation) {
            BOTH_SET -> {
                //获取Layout的相关参数
                val spanCount = getSpanCount(parent)
                if (isLastRaw(parent, layoutPosition, spanCount, childCount)) {
                    // 如果是最后一行，则不需要绘制底部
                    if (isLastColum(parent, layoutPosition, spanCount, childCount)) {
                        outRect.set(0, 0, 0, 0)
                    } else {
                        outRect[0, 0, size] = 0
                    }
                } else if (isLastColum(parent, layoutPosition, spanCount, childCount)) {
                    // 如果是最后一列，则不需要绘制右边
                    outRect.set(0, 0, 0, size)
                } else {
                    outRect.set(0, 0, size, size)
                }
            }
            RecyclerView.HORIZONTAL -> {
                childCount -= 1
                //水平布局右侧留Margin,如果是最后一列,就不要留Margin了
//                outRect[0, 0, if (itemPosition != childCount) size else 0] = 0
                outRect.set(0, 0, if (layoutPosition != childCount) size else 0, 0)
            }
            RecyclerView.VERTICAL -> {
                childCount -= 1
                //垂直布局底部留边，最后一行不留
//                outRect[0, 0, 0] = if (itemPosition != childCount) size else 0
                val bottom = if (layoutPosition != childCount) size else 0
                outRect.set(0, 0, 0, bottom)
            }
        }
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (orientation == RecyclerView.HORIZONTAL) {
            drawHorizontal(c, parent)
        } else if (orientation == RecyclerView.VERTICAL) {
            drawVertical(c, parent)
        } else {
//            drawVertical(c, parent)
//            drawHorizontal(c, parent)
            drawGrid(c, parent)
        }
    }

    private fun drawGrid(canvas: Canvas, parent: RecyclerView) {
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            //获取Layout的相关参数
            val spanCount = getSpanCount(parent)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            //item底部的Y轴坐标+margin值
            val layoutPosition = layoutParams.viewLayoutPosition
            val adapter = parent.adapter
            var itemCount = adapter!!.itemCount
            if (checkSpecialPosition(parent,layoutPosition)){
                continue
            }
            if (mPaint != null) {
                //绘制底部

                if (!isLastRaw(parent, layoutPosition, spanCount, itemCount)) {
                    canvas.drawRect(
                        child.x - layoutParams.leftMargin,
                        (child.bottom + layoutParams.bottomMargin).toFloat(),
                        (child.right + layoutParams.rightMargin).toFloat(),
                        (child.bottom + layoutParams.bottomMargin + size).toFloat(),
                        mPaint!!
                    )
                }

                if (!isLastColum(parent, layoutPosition, spanCount, itemCount)) {
                    //绘制右边
                    canvas.drawRect(
                        (child.right + layoutParams.rightMargin).toFloat(),
                        child.y - layoutParams.topMargin,
                        (child.right + layoutParams.rightMargin + size).toFloat(),
                        (child.bottom + layoutParams.bottomMargin + if (isLastRaw(
                                parent,
                                layoutPosition,
                                spanCount,
                                itemCount
                            )
                        ) 0 else size).toFloat(),
                        mPaint!!
                    )
                }
            }
        }
    }

    /**
     * 绘制横向 item 分割线
     *
     * @param canvas 画布
     * @param parent 父容器
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val x = parent.paddingLeft + (if (orientation == RecyclerView.VERTICAL) paddingLeft else 0)
        val width =
            parent.measuredWidth - parent.paddingRight - (if (orientation == RecyclerView.VERTICAL) paddingRight else 0)
        //getChildCount()(ViewGroup.getChildCount) 返回的是显示层面上的“所包含的子 View 个数”。
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val layoutPosition = layoutParams.viewLayoutPosition
            if (checkSpecialPosition(parent,layoutPosition)){
                continue
            }
            if (orientation == RecyclerView.VERTICAL) {
                if (layoutPosition == parent.adapter!!.itemCount - 1) {
                    continue
                }
            }
            //item底部的Y轴坐标+margin值
            val y = child.bottom + layoutParams.bottomMargin
            val height = y + size
//            Log.e("height", "$height===================")
            if (drawable != null) {
                //setBounds(x,y,width,height); x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点
                // width:组件的长度 height:组件的高度
                drawable!!.setBounds(x, y, width, height)
                drawable!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                    x.toFloat(),
                    y.toFloat(),
                    width.toFloat(),
                    height.toFloat(),
                    mPaint!!
                )
            }
        }
    }

    /**
     * 绘制纵向 item 分割线
     *
     * @param canvas
     * @param parent
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top =
            parent.paddingTop + (if (orientation == RecyclerView.HORIZONTAL) paddingTop else 0)
        val bottom =
            parent.measuredHeight - parent.paddingBottom - (if (orientation == RecyclerView.HORIZONTAL) paddingBottom else 0)
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val layoutPosition = layoutParams.viewLayoutPosition
            if (checkSpecialPosition(parent,layoutPosition)){
                continue
            }
            if (orientation == RecyclerView.HORIZONTAL) {
                if (layoutPosition == parent.adapter!!.itemCount - 1) {
                    return
                }
            }
            val left = child.right + layoutParams.rightMargin
            val right = left + size
            if (drawable != null) {
                drawable!!.setBounds(left, top, right, bottom)
                drawable!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint!!
                )
            }
        }
    }

    private fun checkSpecialPosition(parent: RecyclerView, adapterPosition:Int):Boolean{
        val adapter = parent.adapter
        var childCount = adapter!!.itemCount
        if (adapter is LoadMoreWrapper) {
            val headersCount = adapter.headersCount
            var footersCount = adapter.footersCount
            if (adapterPosition < headersCount) {
                return true
            }
            if (footersCount > 0 || adapter.loadMoreEnable()) {
                if (adapter.loadMoreEnable()){
                    footersCount +=1
                }
                if (adapterPosition >= childCount - footersCount-1) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                .spanCount
        }
        return spanCount
    }

    private fun isLastColum(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var itemCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val orientation = layoutManager
                .orientation
            if (orientation == RecyclerView.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) return true
            } else {
                val i = itemCount % spanCount
                itemCount -= if (i == 0) spanCount else i
                if (pos >= itemCount) return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            if (orientation == RecyclerView.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) return true
            } else {
                val i = itemCount % spanCount
                itemCount -= if (i == 0) spanCount else i
                if (pos >= itemCount) return true
            }
        }
        return false
    }

    private fun isLastRaw(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var itemCount = childCount
        val orientation: Int
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            orientation = layoutManager
                .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                val i = itemCount % spanCount
                itemCount -= if (i == 0) spanCount else i
                if (pos >= itemCount) return true
            } else { // StaggeredGridLayoutManager 横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            orientation = layoutManager
                .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                val i = itemCount % spanCount
                itemCount -= if (i == 0) spanCount else i
                if (pos >= itemCount) return true
            } else { // StaggeredGridLayoutManager 横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) return true
            }
        }
        return false
    }


    companion object {
        //系统自带的参数
        private val ATTRS = intArrayOf(R.attr.listDivider)

        //水平+垂直
        const val BOTH_SET = 2
    }
}