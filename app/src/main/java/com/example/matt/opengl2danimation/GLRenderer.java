package com.example.matt.opengl2danimation;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Matt on 6/27/2016.
 */
public class GLRenderer implements GLSurfaceView.Renderer {

    // matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    private AppRenderer renderer;

    // View width and height
    float mViewWidth;
    float mViewHeight;

    Context mContext;
    long mLastTime;
    int mProgram;
    int clearcolor;

    public float[] getMtrxProjectionAndView() {return mtrxProjectionAndView;}

    public GLRenderer(Context c, AppRenderer r, int color) {
        mContext = c;
        mLastTime = -1;
        renderer = r;
        clearcolor = color;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = -1;
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        if(mLastTime == -1){mLastTime = System.currentTimeMillis();}

        long now = System.currentTimeMillis();

        float elapsedInSeconds = (now - mLastTime) / 1000f;

        render(elapsedInSeconds);

        mLastTime = now;

    }

    private void render(float deltaTime) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if(renderer != null) {
            renderer.render(deltaTime,getMtrxProjectionAndView());
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mViewWidth = width;
        mViewHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mViewWidth, (int) mViewHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mViewWidth, 0.0f, mViewHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

        if(renderer != null) {
            renderer.onViewWidthChanged((int)mViewWidth, (int)mViewHeight);
        }


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the clear color to black

        float red = 0.0f;
        float green = 0.0f;
        float blue = 0.0f;
        float alpha = 0.0f;

        GLES20.glClearColor(red,green,blue,alpha);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Create the shaders, solid color
        int vertexShader = TextureShader.loadShader(GLES20.GL_VERTEX_SHADER,
                TextureShader.vs_SolidColor);
        int fragmentShader = TextureShader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                TextureShader.fs_SolidColor);

        TextureShader.sp_SolidColor = GLES20.glCreateProgram();
        GLES20.glAttachShader(TextureShader.sp_SolidColor, vertexShader);
        GLES20.glAttachShader(TextureShader.sp_SolidColor, fragmentShader);
        GLES20.glLinkProgram(TextureShader.sp_SolidColor);

        // Create the shaders, images
        vertexShader = TextureShader.loadShader(GLES20.GL_VERTEX_SHADER,
                TextureShader.vs_Image);
        fragmentShader = TextureShader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                TextureShader.fs_Image);

        TextureShader.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(TextureShader.sp_Image, vertexShader);
        GLES20.glAttachShader(TextureShader.sp_Image, fragmentShader);
        GLES20.glLinkProgram(TextureShader.sp_Image);

        // Set our shader programm
        GLES20.glUseProgram(TextureShader.sp_Image);
    }

}
