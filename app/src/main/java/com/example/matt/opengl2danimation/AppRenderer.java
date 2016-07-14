package com.example.matt.opengl2danimation;

/**
 * This is the class you will have to extend to actually implement the rendering on an animation
 * a sample one has been provided to show how this can be done
 */

public interface AppRenderer {

    public void render(float deltaTime, float[] viewAndProjection);
    public void onViewWidthChanged(int width, int height);
    public void deallocResources();
}
