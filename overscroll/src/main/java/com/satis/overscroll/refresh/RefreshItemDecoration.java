package com.satis.overscroll.refresh;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.satis.overscroll.R;

public class RefreshItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint = new Paint();

    public RefreshItemDecoration(){
        mPaint.setColor(Color.RED);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(20);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        ViewGroup parent1 = (ViewGroup)parent.getParent();
        parent1.setClipChildren(false);
        super.onDrawOver(c, parent, state);
        c.drawText("刷新",100,100,mPaint);
    }
}
