/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraftforge.common.MinecraftForge;

public abstract class HudMod {

    private String name;
    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;
    private float size;

    public HudMod(String name, int x, int y) {
        MinecraftForge.EVENT_BUS.register(this);
        this.name = name;
        this.x = x;
        this.y = y;
        this.size = 1;
    }

    public void renderMod(int mouseX, int mouseY) {
        if (withinMod(mouseX, mouseY) || isDragging()) {
            Helper2D.drawOutlinedRectangle(getX() - 3, getY() - 3, getW() + 6, getH() + 6, 1, -1);
            Cloud.INSTANCE.fontHelper.size20.drawString("Size: " + MathHelper.round(getSize(), 1),
                    getX() + getW() / 2f - Cloud.INSTANCE.fontHelper.size20.getStringWidth("Size: " + MathHelper.round(getSize(), 1)) / 2f,
                    getY() + getH() + 10, -1
            );
        }
    }

    public boolean withinMod(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, (int) (w * size), (int) (h * size), mouseX, mouseY);
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (isDragging()) {
            setX(mouseX - offsetX);
            setY(mouseY - offsetY);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
