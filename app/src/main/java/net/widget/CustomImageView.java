package net.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


import net.basicmodel.BokehEffectActivity;
import net.utils.Share;


@SuppressLint("AppCompatCustomView")
public class CustomImageView extends ImageView {

    private static final String TAG = "ZoomableImageView";

    private Bitmap imgBitmap = null;

    private int containerWidth;
    private int containerHeight;

    Paint background;

    //Matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    PointF start = new PointF();

    float currentScale;
    float curX;
    float curY;

    //We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    //For animating stuff
    float targetX;
    float targetY;
    float targetScale;
    float targetScaleX;
    float targetScaleY;
    float scaleChange;
    float targetRatio;
    float transitionalRatio;

    float easing = 0.2f;
    boolean isAnimating = false;

    float scaleDampingFactor = 0.5f;

    //For pinch and zoom
    float oldDist = 1f;
    PointF mid = new PointF();

    private Handler mHandler = new Handler();

    float minScale;
    float maxScale = 8.0f;

    float wpRadius = 25.0f;
    float wpInnerRadius = 20.0f;

    float screenDensity;
    Context context;
    private GestureDetector gestureDetector;

    public static final int DEFAULT_SCALE_FIT_INSIDE = 0;
    public static final int DEFAULT_SCALE_ORIGINAL = 1;

    private int defaultScale;

    public int getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(int defaultScale) {
        this.defaultScale = defaultScale;
    }

    public CustomImageView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        screenDensity = context.getResources().getDisplayMetrics().density;

        initPaints();
        gestureDetector = new GestureDetector(new MyGestureDetector());
        this.context = context;
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        screenDensity = context.getResources().getDisplayMetrics().density;
        initPaints();
        gestureDetector = new GestureDetector(new MyGestureDetector());

        defaultScale = CustomImageView.DEFAULT_SCALE_FIT_INSIDE;
    }

    private void initPaints() {
        background = new Paint();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //Reset the width and height. Will draw bitmap and change
        containerWidth = width;
        containerHeight = height;

        if (imgBitmap != null) {
            int imgHeight = imgBitmap.getHeight();
            int imgWidth = imgBitmap.getWidth();

            float scale;
            int initX = 0;
            int initY = 0;

            if (defaultScale == CustomImageView.DEFAULT_SCALE_FIT_INSIDE) {
                if (imgWidth > containerWidth) {
                    scale = (float) containerWidth / imgWidth;
                    float newHeight = imgHeight * scale;
                    initY = (containerHeight - (int) newHeight) / 2;

                    matrix.setScale(scale, scale);
                    matrix.postTranslate(0, initY);
                } else {
                    scale = (float) containerHeight / imgHeight;
                    float newWidth = imgWidth * scale;
                    initX = (containerWidth - (int) newWidth) / 2;

                    matrix.setScale(scale, scale);
                    matrix.postTranslate(initX, 0);
                }

                curX = initX;
                curY = initY;

                currentScale = scale;
                minScale = scale;
            } else {
                if (imgWidth > containerWidth) {
                    initY = (containerHeight - (int) imgHeight) / 2;
                    matrix.postTranslate(0, initY);
                } else {
                    initX = (containerWidth - (int) imgWidth) / 2;
                    matrix.postTranslate(initX, 0);
                }

                curX = initX;
                curY = initY;

                currentScale = 1.0f;
                minScale = 1.0f;
            }


            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (imgBitmap != null && canvas != null) {
            canvas.drawBitmap(imgBitmap, matrix, background);
        }
    }

    //Checks and sets the target image x and y co-ordinates if out of bounds
    private void checkImageConstraints() {

        Log.e("zoom", "checkImageConstraints");
        if (imgBitmap == null) {
            return;
        }

        float[] mvals = new float[9];
        matrix.getValues(mvals);

        currentScale = mvals[0];

        /*if (currentScale < minScale) {
            Log.e("zoom", "less");
            float deltaScale = minScale / currentScale;
            float px = containerWidth / 2;
            float py = containerHeight / 2;
            matrix.postScale(deltaScale, deltaScale, px, py);
            invalidate();
        }*/

        matrix.getValues(mvals);
        currentScale = mvals[0];
        curX = mvals[2];
        curY = mvals[5];

        int rangeLimitX = containerWidth - (int) (imgBitmap.getWidth() * currentScale);
        int rangeLimitY = containerHeight - (int) (imgBitmap.getHeight() * currentScale);


        boolean toMoveX = false;
        boolean toMoveY = false;

        if (rangeLimitX < 0) {
            if (curX > 0) {
                targetX = 0;
//                toMoveX = true;
            } else if (curX < rangeLimitX) {
                targetX = rangeLimitX;
//                toMoveX = true;
            }
        } else {
            targetX = rangeLimitX / 2;
//            toMoveX = true;
        }

        if (rangeLimitY < 0) {
            if (curY > 0) {
                targetY = 0;
//                toMoveY = true;
            } else if (curY < rangeLimitY) {
                targetY = rangeLimitY;
//                toMoveY = true;
            }
        } else {
            targetY = rangeLimitY / 2;
//            toMoveY = true;
        }

        if (toMoveX == true || toMoveY == true) {

            Log.e("zoom", "checkImageConstraints if");
            if (toMoveY == false) {
                targetY = curY;
            }
            if (toMoveX == false) {
                targetX = curX;
            }

            //Disable touch event actions
            isAnimating = true;
            //Initialize timer
            mHandler.removeCallbacks(mUpdateImagePositionTask);
            mHandler.postDelayed(mUpdateImagePositionTask, 100);
        }
    }


    @SuppressLint("LongLogTag")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float oldX = 0, newX = 0, sens = 5;
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }

        if (isAnimating == true) {
            return true;
        }

        //Handle touch events here
        float[] mvals = new float[9];
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("Image", "ACTION_DOWN");

                Log.e("ACTION_DOWN Share.isStickerAvail", Share.isStickerAvail + "");
                if (Share.isStickerAvail) {
                    Log.e("ACTION_DOWN Share.isStickerTouch", Share.isStickerTouch + "");

                    if (Share.isStickerTouch || !Share.isStickerTouch) {
                        Share.isStickerTouch = false;
                        BokehEffectActivity.stickerView.setLocked(true);
                    }
                }

                if (isAnimating == false) {

                    savedMatrix.set(matrix);
                    oldX = event.getX();
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("Image", "ACTION_POINTER_DOWN");

                /*if (Share.isStickerAvail) {
                    if (Share.isStickerTouch) {
                        Share.isStickerTouch = false;
                        BokehEffectActivity.stickerView.setLocked(true);
                    }
                }*/

                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("Image", "ACTION_UP");

                if (Share.isStickerAvail) {

                    Log.e("ACTION_UP Share.isStickerTouch", Share.isStickerTouch + "");
                    if (!Share.isStickerTouch) {
                        Share.isStickerTouch = true;
                        BokehEffectActivity.stickerView.setLocked(false);
                    }
                }

                newX = event.getX();
                if (Math.abs(oldX - newX) < sens) {
//                    Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                    return true;
                }
                oldX = 0;
                newX = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("Image", "ACTION_POINTER_UP");

                /*if (Share.isStickerAvail) {
                    if (!Share.isStickerTouch) {
                        Share.isStickerTouch = true;
                        BokehEffectActivity.stickerView.setLocked(false);
                    }
                }*/

                mode = NONE;

                matrix.getValues(mvals);
                curX = mvals[2];
                curY = mvals[5];
                currentScale = mvals[0];

                if (isAnimating == false) {
                    checkImageConstraints();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("Image", "ACTION_MOVE");

                if (Share.isStickerAvail) {
                    if (!Share.isStickerTouch || Share.isStickerTouch) {
                        Log.e("image move", "sticker lock");
                        Share.isStickerTouch = false;
                        BokehEffectActivity.stickerView.setLocked(true);
                    }
                }

                if (mode == DRAG && isAnimating == false) {
                    matrix.set(savedMatrix);
                    float diffX = event.getX() - start.x;
                    float diffY = event.getY() - start.y;

                    matrix.postTranslate(diffX, diffY);

                    matrix.getValues(mvals);
                    curX = mvals[2];
                    curY = mvals[5];
                    currentScale = mvals[0];
                } else if (mode == ZOOM && isAnimating == false) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.getValues(mvals);
                        currentScale = mvals[0];

                        if (currentScale * scale <= minScale) {
                            Log.e("zoom", "if");
//                            matrix.postScale(minScale / currentScale, minScale / currentScale, mid.x, mid.y);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        } else if (currentScale * scale >= maxScale) {
                            Log.e("zoom", "else if");
//                            matrix.postScale(maxScale / currentScale, maxScale / currentScale, mid.x, mid.y);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        } else {
                            Log.e("zoom", "else");
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }


                        matrix.getValues(mvals);
                        curX = mvals[2];
                        curY = mvals[5];
                        currentScale = mvals[0];
                    }
                }

                break;
        }

        //Calculate the transformations and then invalidate
        invalidate();
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void setImageBitmap(Bitmap b) {
        if (b != null) {
            imgBitmap = b;

            containerWidth = getWidth();
            containerHeight = getHeight();

            int imgHeight = imgBitmap.getHeight();
            int imgWidth = imgBitmap.getWidth();

            float scale;
            int initX = 0;
            int initY = 0;

            matrix.reset();

            if (defaultScale == CustomImageView.DEFAULT_SCALE_FIT_INSIDE) {
                if (imgWidth > containerWidth) {
                    scale = (float) containerWidth / imgWidth;
                    float newHeight = imgHeight * scale;
                    initY = (containerHeight - (int) newHeight) / 2;

                    matrix.setScale(scale, scale);
                    matrix.postTranslate(0, initY);
                } else {
                    scale = (float) containerHeight / imgHeight;
                    float newWidth = imgWidth * scale;
                    initX = (containerWidth - (int) newWidth) / 2;

                    matrix.setScale(scale, scale);
                    matrix.postTranslate(initX, 0);
                }

                curX = initX;
                curY = initY;

                currentScale = scale;
                minScale = scale;
            } else {
                if (imgWidth > containerWidth) {
                    initX = 0;
                    if (imgHeight > containerHeight) {
                        initY = 0;
                    } else {
                        initY = (containerHeight - (int) imgHeight) / 2;
                    }

                    matrix.postTranslate(0, initY);
                } else {
                    initX = (containerWidth - (int) imgWidth) / 2;
                    if (imgHeight > containerHeight) {
                        initY = 0;
                    } else {
                        initY = (containerHeight - (int) imgHeight) / 2;
                    }
                    matrix.postTranslate(initX, 0);
                }

                curX = initX;
                curY = initY;

                currentScale = 1.0f;
                minScale = 1.0f;
            }

            invalidate();
        } else {
            Log.d(TAG, "bitmap is null");
        }
    }

    public Bitmap getImageBitmap() {
        return imgBitmap;
    }


    private Runnable mUpdateImagePositionTask = new Runnable() {
        public void run() {
            float[] mvals;

            if (Math.abs(targetX - curX) < 5 && Math.abs(targetY - curY) < 5) {
                Log.e("zoom", "update pos if");
                isAnimating = false;
                mHandler.removeCallbacks(mUpdateImagePositionTask);

                mvals = new float[9];
                matrix.getValues(mvals);

                currentScale = mvals[0];
                curX = mvals[2];
                curY = mvals[5];

                //Set the image parameters and invalidate display
                float diffX = (targetX - curX);
                float diffY = (targetY - curY);

                matrix.postTranslate(diffX, diffY);
            } else {
                Log.e("zoom", "update pos else");
                isAnimating = true;
                mvals = new float[9];
                matrix.getValues(mvals);

                currentScale = mvals[0];
                curX = mvals[2];
                curY = mvals[5];

                //Set the image parameters and invalidate display
                float diffX = (targetX - curX) * 0.3f;
                float diffY = (targetY - curY) * 0.3f;

                matrix.postTranslate(diffX, diffY);
                mHandler.postDelayed(this, 25);
            }

            invalidate();
        }
    };

    private Runnable mUpdateImageScale = new Runnable() {
        public void run() {
            float transitionalRatio = targetScale / currentScale;
            float dx;
            if (Math.abs(transitionalRatio - 1) > 0.05) {
                isAnimating = true;
                if (targetScale > currentScale) {
                    dx = transitionalRatio - 1;
                    scaleChange = 1 + dx * 0.2f;

                    currentScale *= scaleChange;

                    if (currentScale > targetScale) {
                        currentScale = currentScale / scaleChange;
                        scaleChange = 1;
                    }
                } else {
                    dx = 1 - transitionalRatio;
                    scaleChange = 1 - dx * 0.5f;
                    currentScale *= scaleChange;

                    if (currentScale < targetScale) {
                        currentScale = currentScale / scaleChange;
                        scaleChange = 1;
                    }
                }


                if (scaleChange != 1) {
                    matrix.postScale(scaleChange, scaleChange, targetScaleX, targetScaleY);
                    mHandler.postDelayed(mUpdateImageScale, 15);
                    invalidate();
                } else {
                    isAnimating = false;
                    scaleChange = 1;
                    matrix.postScale(targetScale / currentScale, targetScale / currentScale, targetScaleX, targetScaleY);
                    currentScale = targetScale;
                    mHandler.removeCallbacks(mUpdateImageScale);
                    invalidate();
                    checkImageConstraints();
                }
            } else {
                isAnimating = false;
                scaleChange = 1;
                matrix.postScale(targetScale / currentScale, targetScale / currentScale, targetScaleX, targetScaleY);
                currentScale = targetScale;
                mHandler.removeCallbacks(mUpdateImageScale);
                invalidate();
                checkImageConstraints();
            }
        }
    };

    /**
     * Show an event in the LogCat view, for debugging
     */
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");

        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            if (isAnimating == true) {
                return true;
            }

            scaleChange = 1;
            isAnimating = true;
            targetScaleX = event.getX();
            targetScaleY = event.getY();

            if (Math.abs(currentScale - maxScale) > 0.1) {
                targetScale = maxScale;
            } else {
                targetScale = minScale;
            }
            targetRatio = targetScale / currentScale;
            mHandler.removeCallbacks(mUpdateImageScale);
            mHandler.post(mUpdateImageScale);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }
}
