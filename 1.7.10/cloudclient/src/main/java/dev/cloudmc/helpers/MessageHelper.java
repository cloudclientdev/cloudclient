package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import dev.cloudmc.helpers.render.Helper2D;

public class MessageHelper {

    private final Animate animate = new Animate();
    private String message;
    private String subMessage;
    private double time;
    private int timeLength;

    public MessageHelper() {
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setMax(200).setSpeed(200);
        timeLength = 2500;
        time = 0;
    }

    public void renderMessage() {
        int messageWidth = Cloud.INSTANCE.fontHelper.size20.getStringWidth(subMessage) + 50;
        animate.update();
        animate.setMax(messageWidth);
        if (!(time > timeLength)) {
            if(message != null && subMessage != null) {
                Helper2D.drawRoundedRectangle(5 + animate.getValueI() - messageWidth, 5, messageWidth, 40, 2, 0x30ffffff, 0);
                Helper2D.drawPicture(10 + animate.getValueI() - messageWidth, 10, 30, 30, -1, "icon/warning.png");
                Cloud.INSTANCE.fontHelper.size30.drawString(message, 50 + animate.getValueI() - messageWidth, 12.5f, -1);
                Cloud.INSTANCE.fontHelper.size20.drawString(subMessage, 50 + animate.getValueI() - messageWidth, 30, -1);
                time++;
            }
        }
    }

    public void showMessage(String message, String subMessage) {
        this.message = message;
        this.subMessage = subMessage;

        animate.reset();
        time = 0;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }
}