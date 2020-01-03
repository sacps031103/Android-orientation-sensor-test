

package com.example.orientation_sensor_test.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.orientation_sensor_test.R;

public class CompassView extends View {
    private float mDirection;
    private Drawable compass;
    Bitmap back;

    public CompassView(Context context) {
        super(context);
        mDirection = 0.0f;
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        back = BitmapFactory.decodeResource(getResources(), R.mipmap.compass_cn);
        mDirection = 0.0f;
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDirection = 0.0f;
        compass = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (compass == null) {
            Resources res = getResources();
            compass = ResourcesCompat.getDrawable(res,  R.mipmap.compass_cn , null);
            compass.setBounds(0, 0, getWidth(), getHeight());
        }

        canvas.save();
        canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);
        compass.draw(canvas);
        canvas.restore();
    }

    public void updateDirection(float direction) {
        mDirection = direction;
        invalidate();
    }
}
