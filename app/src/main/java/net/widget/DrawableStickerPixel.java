package net.widget;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class DrawableStickerPixel extends StickerPixel {

    private Drawable mDrawable;
    private Rect mRealBounds;

    public DrawableStickerPixel(Drawable drawable) {
        mDrawable = drawable;
        mMatrix = new Matrix ();
        mRealBounds = new Rect (0, 0, getWidth(), getHeight());
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
            return mDrawable.getIntrinsicWidth();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getHeight() {
        try
        {
            return mDrawable.getIntrinsicHeight();
        }catch(NullPointerException e)
        {

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
