package com.example.matt.opengl2danimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Matt on 6/28/2016.
 */
public class Texture
{

    private Context mContext;

    private int glTexId;
    private int []texturenames;

    private int width;
    private int height;

    public Texture(Context c, int resourceID){
        this.mContext = c;
        loadImage(resourceID);
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void setAsActiveTexture() {

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexId);
    }

    private void loadImage(int resID) {

        // Generate Textures, if more needed, alter these numbers.
        texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);


        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), resID);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp, -1, 0);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, loadImage(bmp));

        width = bmp.getWidth();
        height = bmp.getHeight();

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

        glTexId = texturenames[0];
    }

    public static ByteBuffer loadImage(Bitmap bitmap) {
        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(bitmap.getHeight() * bitmap.getWidth() * 4);
        imageBuffer.order(ByteOrder.nativeOrder());
        byte buffer[] = new byte[4];
        for(int i = 0; i < bitmap.getHeight(); i++)
        {
            for(int j = 0; j < bitmap.getWidth(); j++)
            {
                int color = bitmap.getPixel(j, i);
                buffer[0] = (byte)Color.red(color);
                buffer[1] = (byte)Color.green(color);
                buffer[2] = (byte)Color.blue(color);
                buffer[3] = (byte)Color.alpha(color);
                imageBuffer.put(buffer);
            }
        }
        imageBuffer.position(0);

        return imageBuffer;
    }

    public void dealloc() {
        GLES20.glDeleteBuffers(1,texturenames,0);
    }

}
