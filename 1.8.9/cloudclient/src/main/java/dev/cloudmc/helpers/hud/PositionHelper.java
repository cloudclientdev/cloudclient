package dev.cloudmc.helpers.hud;

import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

public class PositionHelper {

    private float difference;
    private boolean direction = false;
    private Animate animate = new Animate();
    private float pre;
    private float post;

    public PositionHelper(int speed) {
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setSpeed(speed);
    }

    public void pre(float pos) {
        pre = pos;
    }

    public void post(float pos){
        post = pos;
    }

    public void update(){
        if(post != pre) {
            difference = post - pre;
            if(difference > 0) {
                direction = false;
                animate.setMax(difference);
            } else if (difference < 0) {
                direction = true;
                animate.setMax(-difference);
            }
            animate.reset();
        }

        animate.update();
    }

    public float getDifference() {
        return difference;
    }

    public boolean isDirection() {
        return direction;
    }

    public int getValue() {
        return animate.getValueI();
    }
}
