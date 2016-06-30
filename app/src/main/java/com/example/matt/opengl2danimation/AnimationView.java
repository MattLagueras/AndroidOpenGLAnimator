package com.example.matt.opengl2danimation;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Matt on 6/27/2016.
 */
public class AnimationView extends GLSurfaceView {

    private final AnimationRenderer mRenderer;

    public AnimationView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new AnimationRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mRenderer.onResume();
    }

}
