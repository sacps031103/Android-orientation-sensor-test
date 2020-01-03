package com.example.orientation_sensor_test.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Level_Vertical_View extends View {
    public boolean Test_paint=true;
    private Paint paint = new Paint();
    private Paint line = new Paint();
    private Paint Pass_paint = new Paint();
    private int circleRadius;
    private int circleRadiusTwo;
    private static final int CIRCLE_RADIUS_DP = 10;
    public int MAX_ANGLE = 30;
    public float zAngle = 0;
    public Level_Vertical_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        line.setColor(Color.GRAY);// 设置灰色
        line.setStyle(Paint.Style.FILL);//设置填满
        line.setStrokeWidth((float) 15.0);//设置线宽
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
        canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), line);
        int x = canvas.getWidth()/2;
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
        canvas.drawCircle(canvas.getWidth() / 2, x, circleRadius ,paint);//劃出觸控點
        if(Test_paint){
            canvas.drawCircle(canvas.getWidth() / 2,  canvas.getHeight(), circleRadiusTwo , Pass_paint);//劃出觸控點
        }else {
            canvas.drawCircle(canvas.getWidth() / 2,  0, circleRadiusTwo , Pass_paint);//劃出觸控點
        }
        invalidate();//重畫
    }
}
