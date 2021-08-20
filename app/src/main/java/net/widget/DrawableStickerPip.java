package net.widget;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class DrawableStickerPip extends StickerPip {


    protected static final String TAG = "StickerPip";

    private Drawable mDrawable;
    private Rect mRealBounds;

    public DrawableStickerPip(Drawable drawable) {
        mDrawable = drawable;
        mMatrix = new Matrix ();
        mRealBounds = new Rect (0, 0, getWidth(), getHeight());
    }
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(mMatrix);
        mDrawable.setBounds(mRealBounds);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        try {
            Log.e("TAG","stiker width  : "+mDrawable.getIntrinsicWidth());
            return mDrawable.getIntrinsicWidth();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getHeight() {
        try {
            Log.e("TAG","stiker height  : "+mDrawable.getIntrinsicHeight());
            return mDrawable.getIntrinsicHeight();
        } catch (NullPointerException e) {

        }
        return 0;
    }

    @Override
    public void release() {
        super.release();
        if (mDrawable != null) {
            mDrawable = null;
        }
    }
}
