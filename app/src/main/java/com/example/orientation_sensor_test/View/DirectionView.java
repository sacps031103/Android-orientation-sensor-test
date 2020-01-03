package com.example.orientation_sensor_test.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

public class DirectionView extends View {
    private Paint paint = new Paint();
    private Paint line = new Paint();
    private int circleRadius;
    private static final int CIRCLE_RADIUS_DP = 10;
    public int MAX_ANGLE = 30;
    // 获取与Y轴的夹角
    public float yAngle = 0;
    // 获取与Z轴的夹角
    public float zAngle = 0;
    public DirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        line.setColor(Color.GRAY);// 设置灰色
        line.setStyle(Paint.Style.FILL);//设置填满
        line.setStrokeWidth((float) 15.0);             //设置线宽
        paint.setColor(Color.RED);// 设置灰色
        paint.setStyle(Paint.Style.FILL);//设置填满
        circleRadius = (int)(CIRCLE_RADIUS_DP * getResources().getDisplayMetrics().density);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight()/2, line);
        canvas.drawLine(canvas.getWidth()/2, 0, canvas.getWidth()/2, canvas.getHeight(), line);
        // 气泡位于中间时（水平仪完全水平），气泡的X、Y座标
        int x = canvas.getWidth()/2;
        int y = canvas.getHeight()/2;
        // 如果与Z轴的倾斜角还在最大角度之内
        if (Math.abs(zAngle) <= MAX_ANGLE) {
            // 根据与Z轴的倾斜角度计算X座标的变化值（倾斜角度越大，X座标变化越大）
            int deltaX = (int) (canvas.getWidth()/2 * zAngle / MAX_ANGLE);
            x += deltaX;
        }
        // 如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
        else if (zAngle > MAX_ANGLE) {
            x = canvas.getWidth();
        }
        // 如果与Z轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            x =0;
        }
        // 如果与Y轴的倾斜角还在最大角度之内
        if (Math.abs(yAngle) <= MAX_ANGLE) {
            // 根据与Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
            int deltaY = (int) (canvas.getHeight()/2* yAngle*-1 / MAX_ANGLE);
            y += deltaY;
        }
        // 如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
        else if (yAngle > MAX_ANGLE) {
            y = 0;
        }
        // 如果与Y轴的倾斜角已经小于负的MAX_ANGLE，气泡应到最右边
        else {
            y = canvas.getHeight();
        }
        Log.v(TAG, "\n");
        Log.v(TAG, "X "+x+" Y "+y);
        canvas.drawCircle(y, x, circleRadius ,paint);//劃出觸控點
        Paint p = new Paint();//这个是画矩形的画笔，方便大家理解这个圆弧
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.RED);
        p.setStrokeWidth((float) 10.0);
        RectF oval = new RectF( 0, 0, getWidth(), getHeight());
        canvas.drawRect(oval, p);//画矩形
        invalidate();//重畫
    }
}
