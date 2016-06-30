package com.example.matt.opengl2danimation;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Matt on 6/29/2016.
 */
public class Sprite
{
    private TextureRegion region;

    private int width;
    private int height;

    private int x;
    private int y;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private float vertices[];
    private short indices[];

    public Sprite(TextureRegion region, int x, int y, int width, int height)
    {
        this(x,y,width,height);
        this.region = region;
    }

    public Sprite(int x, int y, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        setupTriangle();
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public void draw(float []combinedMat)
    {
        region.setAsActiveTexture();

        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(TextureShader.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(TextureShader.sp_Image,
                "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, region.getUvBuffer());

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(TextureShader.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, combinedMat, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(TextureShader.sp_Image,
                "s_texture");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void setupTriangle() {
        // We have to create the vertices of our triangle.
        vertices = new float[]
                {       x - width/2, y - height/2, 0.0f, // TL
                        x - width/2, y + height/2, 0.0f, // BL
                        x + width/2, y + height/2, 0.0f,  // BR
                        x + width/2, y - height/2, 0.0f,  // TR
                };

        indices = new short[] {0, 1, 2, 0, 2, 3}; // The order of vertexrendering.

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);


    }
}

