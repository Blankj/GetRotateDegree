package com.blankj.getrotatedegree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/11/8
 *     desc  :
 * </pre>
 */
public class RotateImageView extends ImageView {

    // 点击在view中所在坐标
    private float x, y;
    // view的中心点坐标
    private float ox, oy;
    // view的宽、高
    private int w, h;
    private Matrix matrix;
    // ImageView的资源图片
    Bitmap mBitmap;
    // 旋转过的角度
    public  float   totalDegree = 0;
    // 是否初始化过
    private boolean isInit      = false;

    // 旋转角度改变的监听器
    private onRotationChangeListener mListener;

    public void setOnRotationChangeListener(onRotationChangeListener listener) {
        mListener = listener;
    }

    public RotateImageView(Context context) {
        this(context, null);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInit) return;
        int measureWidth = getWidth();
        int measureHeight = getHeight();
        Log.d("blankj", "onMeasure: w: " + measureWidth + ", h: " + measureHeight);
        // 如果没有初始化过，就初始化
        if (measureWidth != 0 && measureHeight != 0 && !isInit) {
            isInit = true;
            mBitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
            w = measureWidth;
            h = measureHeight;
            ox = w >> 1;
            oy = h >> 1;
            matrix = new Matrix();
            // 获取适配于ImageView的bitmap
            mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("blankj", "onDraw");
        canvas.save();
        canvas.drawBitmap(mBitmap, matrix, null);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX = event.getX();
                float nowY = event.getY();

                // 计算三边的平方
                float ab2 = (x - nowX) * (x - nowX) + (y - nowY) * (y - nowY);
                float oa2 = (x - ox) * (x - ox) + (y - oy) * (y - oy);
                float ob2 = (nowX - ox) * (nowX - ox) + (nowY - oy) * (nowY - oy);

                // 根据两向量的叉乘来判断顺逆时针
                boolean isClockwise = ((x - ox) * (nowY - oy) - (y - oy) * (nowX - ox)) > 0;

                // 根据余弦定理计算旋转角的余弦值
                double cosDegree = (oa2 + ob2 - ab2) / (2 * Math.sqrt(oa2) * Math.sqrt(ob2));

                // 异常处理，因为算出来会有误差绝对值可能会超过一，所以需要处理一下
                if (cosDegree > 1) {
                    cosDegree = 1;
                } else if (cosDegree < -1) {
                    cosDegree = -1;
                }

                // 计算弧度
                double radian = Math.acos(cosDegree);

                // 计算旋转过的角度，顺时针为正，逆时针为负
                float degree = (float) (isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));

                // 累加角度
                totalDegree += degree;
                matrix.setRotate(totalDegree, ox, oy);

                // 更新触摸点
                x = nowX;
                y = nowY;

                // 回调把角度抛出
                if (mListener != null) {
                    mListener.getRotation((int) totalDegree);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 如果图片需要复原原来角度，调用下方代码
//                matrix.setRotate(0, mBitmap.getWidth() >> 1, mBitmap.getHeight() >> 1);
//                invalidate();
                break;
        }
        return true;
    }

    public interface onRotationChangeListener {
        void getRotation(int degree);
    }
}
