/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.Helper2D;

public class Button {

    private final String text;
    private int x, y;
    private final int w, h;
    private final int color, colorPressed;

    public Button(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = 150;
        this.h = 20;
        this.color = 0x20ffffff;
        this.colorPressed = 0x30ffffff;
    }

    /**
     * Renders the button with the position, width and height taken from the constructor
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderButton(int mouseX, int mouseY, int x, int y) {
        this.x = x;
        this.y = y;

        Helper2D.drawRoundedRectangle(x, y, w, h, 2,
                MathHelper.withinBox(x, y, w, h, mouseX, mouseY) ? colorPressed : color,
                ClientStyle.isRoundedCorners() ? 0 : -1
        );

        Cloud.INSTANCE.fontHelper.size20.drawString(
                text,
                x + w / 2 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(text) / 2,
                y + h / 2 - 4,
                -1
        );
    }

    public boolean isPressed(int mouseX, int mouseY){
        return MathHelper.withinBox(getX(), getY(), getW(), getH(), mouseX, mouseY);
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
