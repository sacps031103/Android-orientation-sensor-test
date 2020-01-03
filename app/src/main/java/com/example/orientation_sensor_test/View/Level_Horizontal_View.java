package com.example.orientation_sensor_test.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Level_Horizontal_View extends View {
    public boolean Test_paint=true;
    private Paint paint = new Paint();
    private Paint line = new Paint();
    private Paint Pass_paint = new Paint();
    private int circleRadius;
    private int circleRadiusTwo;
    private static final int CIRCLE_RADIUS_DP = 10;
    public int MAX_ANGLE = 30;
    public float yAngle = 0;
    public Level_Horizontal_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        line.setColor(Color.GRAY);// 设置灰色
        line.setStyle(Paint.Style.FILL);//设置填满
        line.setStrokeWidth((float) 15.0);             //设置线宽
        paint.setColor(Color.RED);// 设置灰色
        paint.setStyle(Paint.Style.FILL);//设置填满
        circleRadius = (int)(CIRCLE_RADIUS_DP * getResources().getDisplayMetrics().density);
        circleRadiusTwo= (int)(CIRCLE_RADIUS_DP*1.5 * getResources().getDisplayMetrics().density);
        Pass_paint.setColor(Color.GREEN);
        Pass_paint.setStyle(Paint.Style.FILL);//设置填满
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight()/2, line);
        int y = canvas.getHeight() / 2;
        // 如果与Y轴的倾斜角还在最大角度之内
        if (Math.abs(yAngle) <= MAX_ANGLE) {
            // 根据与Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
            int deltaY = (int) (canvas.getHeight() / 2 * yAngle * -1 / MAX_ANGLE);
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
        canvas.drawCircle(y, canvas.getHeight() / 2, circleRadius, paint);//劃出觸控點
        if (Test_paint) {
            canvas.drawCircle(0, canvas.getHeight() / 2, circleRadiusTwo, Pass_paint);//劃出觸控點
        } else {
            canvas.drawCircle(canvas.getHeight() , canvas.getHeight() / 2, circleRadiusTwo, Pass_paint);//劃出觸控點
        }
        invalidate();//重畫
    }
}
