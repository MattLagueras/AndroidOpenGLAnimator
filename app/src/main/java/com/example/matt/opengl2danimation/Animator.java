package com.example.matt.opengl2danimation;

import java.util.ArrayList;

/**
 * Created by Matt on 6/29/2016.
 */
public class Animator
{
    public enum PlayMode
    {
        NORMAL,
        LOOP
    }

    private enum AnimationEventMessage
    {
        STARTED,
        FINISHED
    }

    private class AnimationEvent implements Runnable
    {
        private AnimationEventMessage message;

        public AnimationEvent(AnimationEventMessage message) {
            this.message = message;
        }

        @Override
        public void run() {

            if(message.equals(AnimationEventMessage.STARTED)){
                for(AnimatonListener listener : listeners){
                    listener.onAnimationStart();
                }
            }
            else {
                for(AnimatonListener listener : listeners){
                    listener.onAnimationComplete();
                }
            }

        }
    }

    private TextureRegion []keyframes;
    private PlayMode playMode = PlayMode.NORMAL;
    private ArrayList<AnimatonListener> listeners;
    private float frameDuration;
    private int currentFrame;
    private float stateTime;

    public Animator(TextureRegion []keyframes, float frameDuration)
    {
        this.keyframes = keyframes;
        this.frameDuration = frameDuration;
        this.listeners = new ArrayList<>();
        this.currentFrame = 0;
        this.stateTime = 0;
    }

    public void setPlayMode(PlayMode mode) {
        this.playMode = mode;
    }

    public void addAnimationListener(AnimatonListener listener) {
        listeners.add(listener);
    }

    public void update(float deltaTime) {

        if(stateTime == 0 && listeners.size() > 0) {
            Thread t = new Thread(new AnimationEvent(AnimationEventMessage.STARTED));
            t.start();
        }

        stateTime += deltaTime;

        if(keyframes.length == 1){
            currentFrame = 0;
        }

        int frame = (int)(stateTime / frameDuration);

        if(playMode.equals(PlayMode.NORMAL)) {
            currentFrame = Math.min(keyframes.length - 1, frame);
        }
        else if(playMode.equals(PlayMode.LOOP)) {
            currentFrame = frame % keyframes.length;
        }

        if(stateTime >= (frameDuration * keyframes.length) && listeners.size() > 0) {
            Thread t = new Thread(new AnimationEvent(AnimationEventMessage.FINISHED));
            t.start();
        }
    }

    public TextureRegion getCurrentKeyframe() {
        return keyframes[currentFrame];
    }





}
