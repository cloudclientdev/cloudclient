/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen.buttons;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.render.Helper2D;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

import java.awt.*;

public class TextButton {

    private final Animate animate = new Animate();

    private final String text;
    private int x, y;
    private final int w, h;

    public TextButton(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = 150;
        this.h = 20;
        animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
    }

    /**
     * Renders the button with the position, width and height taken from the constructor
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderButton(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;

        animate.update().setReversed(!isHovered(mouseX, mouseY));

        Helper2D.drawRoundedRectangle(x, y, w, h, 2,
                new Color(255, 255, 255, animate.getValueI() + 30).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        Cloud.INSTANCE.fontHelper.size20.drawString(
                text,
                x + w / 2f - Cloud.INSTANCE.fontHelper.size20.getStringWidth(text) / 2f,
                y + h / 2f - 4,
                -1
        );
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }
}
