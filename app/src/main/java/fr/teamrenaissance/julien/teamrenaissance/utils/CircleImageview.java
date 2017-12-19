package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import fr.teamrenaissance.julien.teamrenaissance.R;

public class CircleImageview extends android.support.v7.widget.AppCompatImageView {
    private Paint mPaintCircle;
    private Paint mPaintBorder;
    private Paint mPaintBackgroud;
    private BitmapShader mBitmapShader;
    private Matrix mMatrix;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mCircleBorderWidth;
    private int mCirlcleBorderColor;
    private int mCircleBackgroudColor;
    public CircleImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleHead);//将获取的属性转化为我们最先设好的属性
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CircleHead_circleBorderHeadWidth:
                    mCircleBorderWidth = (int) typedArray.getDimension(attr, 0);
                    break;
                case R.styleable.CircleHead_ringHeadColor:
                    mCirlcleBorderColor = typedArray.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CircleHead_backgroundHeadColor:
                    mCircleBackgroudColor = typedArray.getColor(attr, Color.YELLOW);
                    break;
            }
        }
        init();
    }

    private void init() {
        mMatrix = new Matrix();

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeWidth(12);
        this.setLayerType(LAYER_TYPE_SOFTWARE, mPaintCircle);
        mPaintCircle.setShadowLayer(13.0f, 5.0f, 5.0f, Color.GRAY);

        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(mCircleBorderWidth);
        mPaintBorder.setColor(mCirlcleBorderColor);

        mPaintBackgroud = new Paint();
        mPaintBackgroud.setColor(mCircleBackgroudColor);
        mPaintBackgroud.setAntiAlias(true);
        mPaintBackgroud.setStyle(Paint.Style.FILL);
    }

    private void setBitmapShader() {
        Drawable drawable = getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        int bitmapSize = Math.min(bitmap.getHeight(), bitmap.getWidth());

        scale = mWidth * 1.0f / bitmapSize;
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaintCircle.setShader(mBitmapShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getWidth();
        mHeight = getHeight();
        int mCircleSize = Math.min(mHeight, mWidth);
        mRadius = mCircleSize / 2 - mCircleBorderWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            setBitmapShader();
            canvas.drawRect(0, 0, mWidth, mHeight, mPaintBackgroud);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintCircle);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mCircleBorderWidth / 2, mPaintBorder);
        } else {
            super.onDraw(canvas);
        }

    }
}