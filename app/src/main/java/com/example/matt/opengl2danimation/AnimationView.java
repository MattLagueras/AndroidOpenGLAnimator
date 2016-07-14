package com.example.matt.opengl2danimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Matt on 6/27/2016.
 */
public class AnimationView extends GLSurfaceView {

    private int color;

    private GLRenderer mRenderer;


    public AnimationView(Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray array = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.AnimationView);

        color = array.getColor(R.styleable.AnimationView_clearcolor,Color.TRANSPARENT);
    }

    public AnimationView(Context context) {
        super(context);

        color = Color.TRANSPARENT;
    }

    public void initGL(AppRenderer renderer) {

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(getContext(),renderer,color);
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
