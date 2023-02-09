package dev.cloudmc.helpers.hud;

import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import org.lwjgl.input.Mouse;

public class ScrollHelper {

    private float scrollStep = 0;
    private boolean direction = true;
    private Animate animate = new Animate();
    private float minScroll, maxScroll, height;
    private float calculatedScroll;

    public ScrollHelper(int minScroll, int maxScroll) {
        this.minScroll = minScroll;
        this.maxScroll = maxScroll;
        this.height = 0;
        animate.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(35).setSpeed(300);
    }

    public void update() {
        animate.update();
    }

    public void updateScroll() {
        int scroll = Mouse.getDWheel();
        calculatedScroll = direction ?
                scrollStep * 35 + animate.getValueI() - 35 :
                scrollStep * 35 - animate.getValueI() + 35;

        if (scroll > 0) {
            if (scrollStep * 35 < minScroll) {
                scrollStep++;
                direction = true;
                animate.reset();
            }
        } else if (scroll < 0) {
            if ((scrollStep * 35 + height + 10) > maxScroll) {
                scrollStep--;
                direction = false;
                animate.reset();
            }
        }
    }

    public float getScrollStep() {
        return scrollStep;
    }

    public void setScrollStep(float scrollStep) {
        this.scrollStep = scrollStep;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public float getMinScroll() {
        return minScroll;
    }

    public void setMinScroll(float minScroll) {
        this.minScroll = minScroll;
    }

    public float getMaxScroll() {
        return maxScroll;
    }

    public void setMaxScroll(float maxScroll) {
        this.maxScroll = maxScroll;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getCalculatedScroll() {
        return calculatedScroll;
    }

    public void setCalculatedScroll(float calculatedScroll) {
        this.calculatedScroll = calculatedScroll;
    }
}
