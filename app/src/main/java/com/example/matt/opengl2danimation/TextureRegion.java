package com.example.matt.opengl2danimation;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Matt on 6/28/2016.
 */
public class TextureRegion
{
    private Texture texture;

    private float uvs[];
    private FloatBuffer uvBuffer;

    public TextureRegion(Texture t, int rX, int rY, int rW, int rH)
    {
        texture = t;
        setUV(rX, rY, rW, rH);
    }

    public void setAsActiveTexture() {
        texture.setAsActiveTexture();
    }

    public FloatBuffer getUvBuffer() {
        return uvBuffer;
    }

    public void setUV(int rX, int rY, int rW, int rH) {

        float inverseWidth = 1.0f / texture.getWidth();
        float inverseHeight = 1.0f / texture.getHeight();

        float u1 = rX * inverseWidth;
        float v1 = rY * inverseHeight;
        float u2 = (rX + rW) * inverseWidth;
        float v2 = (rY + rH) * inverseHeight;

        uvs = new float[]{
                u1, v2, // TL
                u1, v1, // BL
                u2, v1, // BR
                u2, v2  // TR
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }




}
