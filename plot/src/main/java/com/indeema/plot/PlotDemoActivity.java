package com.indeema.plot;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PlotDemoActivity extends Activity {

    PlotView mPlotView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPlotView = new PlotView(this);
        setContentView(mPlotView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlotView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlotView.onResume();
    }
}
