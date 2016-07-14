package com.example.matt.opengl2danimation;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private AnimationView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rootlayout);

        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(300, 300);

        glSurfaceView = new AnimationView(this);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glSurfaceView.setZOrderOnTop(true);
        glSurfaceView.initGL(new SampleAppRenderer(this,0.7f));
        layout.addView(glSurfaceView, glParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
