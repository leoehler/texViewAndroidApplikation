package de.lingen.hsosna.texview.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;

/**
 * ZoomLinearLayout
 */
public class ZoomLinearLayout extends LinearLayout
        implements ScaleGestureDetector.OnScaleGestureListener {

    /**
     *
     */
    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 2.5f;
    private Mode mode = Mode.NONE;

    private float scale           = 1.0f;
    private float lastScaleFactor = 0f;
    private float startX = 0f;
    private float startY = 0f;
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;


    public ZoomLinearLayout (Context context) {
        super(context);
        init(context);
    }


    public ZoomLinearLayout (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public ZoomLinearLayout (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    /**
     * Es wird eine ScaleGestureDetector erstellt
     *
     * @param context
     */
    @SuppressLint ("ClickableViewAccessibility")
    public void init (Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new OnTouchListener() {
            /**
             *
             * @param view
             * @param motionEvent
             * @return true, wenn
             */
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    // DOWN
                    case MotionEvent.ACTION_DOWN:
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            startY = motionEvent.getY() - prevDy;
                        }
                        break;
                    // MOVE
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
                        break;
                    // POINTER DOWN
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    // POINTER UP
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    // UP
                    case MotionEvent.ACTION_UP:
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        break;
                }

                scaleDetector.onTouchEvent(motionEvent);
                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth()  - (child().getWidth()  / scale)) / 2 * scale;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
                    dx = Math.min(Math.max(dx, - maxDx), maxDx);
                    dy = Math.min(Math.max(dy, - maxDy), maxDy);
                    applyScaleAndTranslation();
                }

                return true;
            }
        });
    }


    /**
     *
     * @param scaleDetector
     * @return
     */
    @Override
    public boolean onScaleBegin (ScaleGestureDetector scaleDetector) {
        return true;
    }


    /**
     *
     * @param scaleDetector
     * @return
     */
    @Override
    public boolean onScale (ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }


    /**
     *
     * @param scaleDetector
     */
    @Override
    public void onScaleEnd (ScaleGestureDetector scaleDetector) {
    }


    /**
     *
     */
    private void applyScaleAndTranslation () {
        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);
    }


    private View child () {
        return getChildAt(0);
    }
}