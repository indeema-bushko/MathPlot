package com.indeema.plot;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import static android.content.ContentValues.TAG;

/**
 * Created by Kostiantyn Bushko on 2/13/17.
 */

public class PlotView extends GLSurfaceView {

    private PlotRenderer mPlotRenderer;
    private OpenGLRenderer mOpenGLRenderer;
    private OrthoRenderer mOrthoRenderer;

    public PlotView(Context context) {
        super(context);
        initialisation();
    }

    private void initialisation() {
        setEGLContextClientVersion(2);

//        mPlotRenderer = new PlotRenderer(getContext());
//        setRenderer(mPlotRenderer);

//        mOpenGLRenderer = new OpenGLRenderer(getContext());
//        setRenderer(mOpenGLRenderer);

        mOrthoRenderer = new OrthoRenderer(getContext());
        setRenderer(mOrthoRenderer);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "On Touch Event : ACTION_DOWN -> " + " x = " +  event.getX() + ", y = " + event.getY());
                if (event.getX() < getWidth() / 2) {
                    mOrthoRenderer.incrementEyeZ(1);
                } else {
                    mOrthoRenderer.decrementEyeZ(1);
                }
                break;
            }
            default: break;
        }
        return true;
    }
}
