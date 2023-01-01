/*
 * Copyright (c) 2022 LaVache-FR
 *
 * GPL-3.0 license
 */

package dev.cloudmc.helpers.animation;

public class Animate {

    private float value, min, max, speed, time;
    private boolean reversed;
    private Easing ease;

    public Animate() {
        this.ease = Easing.LINEAR;
        this.value = 0;
        this.min = 0;
        this.max = 1;
        this.speed = 50;
        this.reversed = false;
    }

    public void reset() {
        time = min;
    }

    public Animate update() {
        if (reversed) {
            if (time > min) time -= (DeltaTime.getDeltaTime() * .001F * speed);
        }
        else {
            if (time < max) time += (DeltaTime.getDeltaTime() * .001F * speed);
        }
        time = clamp(time, min, max);
        float easeVal = getEase().ease(time, min, max, max);
        this.value = Math.min(easeVal, max);
        return this;
    }

    private float clamp(float num, float min, float max) {
        return num < min ? min : (Math.min(num, max));
    }

    public boolean hasFinished() {
        if (reversed) {
            return value == getMin();
        }
        else {
            return value == getMax();
        }
    }

    public int getValueI() {
        return (int) value;
    }

    public float getValueF(){
        return value;
    }

    public Animate setValue(float value) {
        this.value = value;
        return this;
    }

    public float getMin() {
        return min;
    }

    public Animate setMin(float min) {
        this.min = min;
        return this;
    }

    public float getMax() {
        return max;
    }

    public Animate setMax(float max) {
        this.max = max;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public Animate setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Animate setReversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    public Easing getEase() {
        return ease;
    }

    public Animate setEase(Easing ease) {
        this.ease = ease;
        return this;
    }
}
