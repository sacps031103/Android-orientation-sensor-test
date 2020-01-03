package com.example.orientation_sensor_test.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import com.example.orientation_sensor_test.R;

public class Round_Bar_View extends View {
    public int i = 0;           // 弧形角度
    public int Start=0;
    public int Stop=0;
    Paint mPaint = new Paint();  // 繪製樣式物件
    Paint mPaintP = new Paint();  // 繪製樣式物件
    private float mDirection;
    private Drawable compass;
    Bitmap back;
    public boolean Direction=true;
    public Round_Bar_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        back = BitmapFactory.decodeResource(getResources(), R.mipmap.compass_cn);
        mDirection = 0.0f;
        compass = null;
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);
        mPaintP.setAntiAlias(true);//取消锯齿
        mPaintP.setStyle(Paint.Style.FILL);//设置画圆弧的画笔的属性为描边(空心)，个人喜欢叫它描边，叫空心有点会引起歧义
        mPaintP.setStrokeWidth(3);
        mPaintP.setColor(Color.GREEN);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF oval = new RectF( 0, 0, getWidth(), getHeight());
        if(Direction) {
            // 繪製一個弧形
            canvas.drawArc(oval, 0, Stop, true, mPaint);
            canvas.drawArc(oval, 0, i, true, mPaintP);
        }else {
            canvas.drawArc(oval,-0, -Stop, true, mPaint);
            canvas.drawArc(oval, 0, i, true, mPaintP);
        }

        if (compass == null) {
            Resources res = getResources();
            compass = ResourcesCompat.getDrawable(res,  R.mipmap.aim , null);
            compass.setBounds(0, 0, getWidth(), getHeight());
        }
        canvas.save();
        canvas.rotate(i, getWidth() / 2, getHeight() / 2);
        compass.draw(canvas);
        // 重繪, 再一次執行 onDraw 程序
        canvas.restore();
        invalidate();
    }
}
