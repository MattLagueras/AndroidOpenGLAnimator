package com.example.matt.opengl2danimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Matt on 6/27/2016.
 */
public class AnimationRenderer implements GLSurfaceView.Renderer {

    // matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // View width and height
    float mScreenWidth;
    float mScreenHeight;

    Texture tex;
    Texture andTex;
    Animator[] animators;
    Sprite[] sprites;

    Context mContext;
    long mLastTime;
    int mProgram;

    public AnimationRenderer(Context c) {
        mContext = c;
        mLastTime = -1;
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

        // Update our example

        // Render our example
        render(elapsedInSeconds);

        mLastTime = now;

    }

    private void setUpTestTexture() {
        tex = new Texture(mContext,R.drawable.textureatlas);
        andTex = new Texture(mContext,R.drawable.android);

        TextureRegion[] regions = new TextureRegion[5];

        regions[0] = new TextureRegion(tex,0,0,tex.getWidth() / 2,tex.getHeight() / 2);
        regions[1] = new TextureRegion(tex,tex.getWidth() / 2,0,tex.getWidth() / 2,tex.getHeight() / 2);
        regions[2] = new TextureRegion(tex,0,tex.getHeight() / 2,tex.getWidth() / 2,tex.getHeight() / 2);
        regions[3] = new TextureRegion(tex,tex.getWidth() / 2,tex.getHeight() / 2,tex.getWidth() / 2,tex.getHeight() / 2);
        regions[4] = new TextureRegion(andTex,0,0,andTex.getWidth(),andTex.getHeight());

        animators = new Animator[3];

        for(int i = 0; i < animators.length; i++) {
            animators[i] = new Animator(regions, 1f - (i/3f));
            animators[i].setPlayMode(Animator.PlayMode.LOOP);
        }

        sprites = new Sprite[3];

        sprites[0] = new Sprite((int)mScreenWidth / 2, (int)mScreenHeight / 2, 512, 512);
        sprites[1] = new Sprite((int)mScreenWidth / 4, (int)mScreenHeight / 4, 256, 256);
        sprites[2] = new Sprite((int)900, (int)900, 128, 128);

    }


    private void render(float deltaTime) {

        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for(int i = 0; i < animators.length; i++) {
            animators[i].update(deltaTime);
        }

        for(int i = 0; i < sprites.length; i++) {
            sprites[i].setRegion(animators[i].getCurrentKeyframe());
            sprites[i].draw(mtrxProjectionAndView);
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

        setUpTestTexture();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

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
