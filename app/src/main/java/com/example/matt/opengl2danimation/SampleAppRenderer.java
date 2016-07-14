package com.example.matt.opengl2danimation;


import android.content.Context;

/**
 * An example implementation of AppRenderer
 * We simply render an animated sprite that covers the entire view
 */

public class SampleAppRenderer implements AppRenderer {

    Context context;
    float frameduration;
    int x;
    int y;
    int width;
    int height;

    Texture atlas;
    TextureRegion[] regions;
    Animator animator;
    Sprite image;

    public SampleAppRenderer(Context c, float frameduration) {
        this.context = c;
        this.frameduration = frameduration;
    }

    public void init() {

        atlas = new Texture(context,R.drawable.textureatlas);

        regions = new TextureRegion[4];

        regions[0] = new TextureRegion(atlas,0,0,atlas.getWidth() / 2,atlas.getHeight() / 2);
        regions[1] = new TextureRegion(atlas,atlas.getWidth() / 2,0,atlas.getWidth() / 2,atlas.getHeight() / 2);
        regions[2] = new TextureRegion(atlas,0,atlas.getHeight() / 2,atlas.getWidth() / 2,atlas.getHeight() / 2);
        regions[3] = new TextureRegion(atlas,atlas.getWidth() / 2,atlas.getHeight() / 2,atlas.getWidth() / 2,atlas.getHeight() / 2);

        animator = new Animator(regions,frameduration);
        animator.setPlayMode(Animator.PlayMode.LOOP);
        image = new Sprite(x,y,width,height);
    }

    @Override
    public void onViewWidthChanged(int width, int height) {
        x = width / 2;
        y = height / 2;
        this.width = width;
        this.height = height;
        init();
    }

    @Override
    public void deallocResources() {
        atlas.dealloc();
    }


    @Override
    public void render(float deltaTime, float[] viewAndProjection) {

        animator.update(deltaTime);
        image.setRegion(animator.getCurrentKeyframe());
        image.draw(viewAndProjection);
    }

}
