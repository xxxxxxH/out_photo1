package net.widget;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import net.utils.StickerUtils;
import net.utils.StickerUtilsPip;


public abstract class StickerPip {
    protected static final String TAG = "StickerPip";

    protected Matrix mMatrix;
    protected boolean mIsFlipped;
    private float[] mMatrixValues = new float[9];

    private String tag;

    public boolean isFlipped() {
        return mIsFlipped;
    }

    public void setFlipped(boolean flipped) {
        mIsFlipped = flipped;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix.set(matrix);
    }

    public abstract void draw(Canvas canvas);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void setDrawable(Drawable drawable);

    public abstract Drawable getDrawable();

    public float[] getBoundPoints() {
        if (!mIsFlipped) {
            return new float[]{
                    0f, 0f,
                    getWidth(), 0f,
                    0f, getHeight(),
                    getWidth(), getHeight()
            };
        } else {
            return new float[]{
                    getWidth(), 0f,
                    0f, 0f,
                    getWidth(), getHeight(),
                    0f, getHeight()
            };
        }
    }

    public float[] getMappedBoundPoints() {
        float[] dst = new float[8];
        mMatrix.mapPoints(dst, getBoundPoints());
        return dst;
    }

    public float[] getMappedPoints(float[] src) {
        float[] dst = new float[src.length];
        mMatrix.mapPoints(dst, src);
        return dst;
    }


    public RectF getBound() {
        return new RectF (0, 0, getWidth(), getHeight());
    }

    public RectF getMappedBound() {
        RectF dst = new RectF ();
        mMatrix.mapRect(dst, getBound());
        return dst;
    }

    public PointF getCenterPoint() {
        return new PointF (getWidth() / 2, getHeight() / 2);
    }

    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        float[] dst = getMappedPoints(new float[]{
                pointF.x,
                pointF.y
        });
        return new PointF (dst[0], dst[1]);
    }

    public float getCurrentScale() {
        return getMatrixScale(mMatrix);
    }

    public float getCurrentHeight() {
        return getMatrixScale(mMatrix) * getHeight();
    }

    public float getCurrentWidth() {
        return getMatrixScale(mMatrix) * getWidth();
    }

    /**
     * This method calculates scale value for given Matrix object.
     */
    private float getMatrixScale(@NonNull Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2)
                + Math.pow(getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
    }

    /**
     * @return - current image rotation angle.
     */
    public float getCurrentAngle() {
        return getMatrixAngle(mMatrix);
    }

    /**
     * This method calculates rotation angle for given Matrix object.
     */
    private float getMatrixAngle(@NonNull Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
    }

    private float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0, to = 9) int valueIndex) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[valueIndex];
    }

    public boolean contains(float x, float y) {
        Matrix tempMatrix = new Matrix ();
        tempMatrix.setRotate(-getCurrentAngle());
        float[] unrotatedWrapperCorner = new float[8];
        float[] unrotatedPoint = new float[2];
        tempMatrix.mapPoints(unrotatedWrapperCorner, getMappedBoundPoints());
        tempMatrix.mapPoints(unrotatedPoint, new float[]{x, y});
        return StickerUtilsPip.trapToRect(unrotatedWrapperCorner).contains(unrotatedPoint[0], unrotatedPoint[1]);
    }

    public void release() {
    }

}
