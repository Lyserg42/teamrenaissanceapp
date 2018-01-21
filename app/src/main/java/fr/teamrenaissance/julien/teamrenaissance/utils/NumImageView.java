package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class NumImageView extends AppCompatImageView {

    private int num = 0;
    private float radius;
    private float textSize;
    private int paddingRight;
    private int paddingTop;

    public NumImageView(Context context) {
        super(context);
    }

    public NumImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num > 0) {
            //size
            radius = getWidth() / 9;
            textSize = num < 10 ? radius + 5 : radius;
            paddingRight = getPaddingRight();
            paddingTop = getPaddingTop();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            //colorAccent
            paint.setColor(Color.parseColor("#c8e8ff"));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() - radius - paddingRight/2, radius + paddingTop/2, radius, paint);
            //white
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize);
            canvas.drawText("" + (num < 99 ? num : 99),
                    num < 10 ? getWidth() - radius - textSize / 4 - paddingRight/2
                            : getWidth() - radius - textSize / 2 - paddingRight/2,
                    radius + textSize / 3 + paddingTop/2, paint);
        }
    }
}
