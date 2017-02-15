package com.indeema.plot;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Kostiantyn Bushko on 2/13/17.
 */

public class PlotView extends GLSurfaceView {

    private PlotRenderer mPlotRenderer;
    private OpenGLRenderer mOpenGLRenderer;

    public PlotView(Context context) {
        super(context);
        initialisation();
    }

    private void initialisation() {
        setEGLContextClientVersion(2);
//        mPlotRenderer = new PlotRenderer(getContext());
//        setRenderer(mPlotRenderer);

        mOpenGLRenderer = new OpenGLRenderer(getContext());
        setRenderer(mOpenGLRenderer);
    }
}
