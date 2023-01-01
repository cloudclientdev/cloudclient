/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor.impl;

import cpw.mods.fml.common.FMLCommonHandler;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.Helper2D;
import net.minecraftforge.common.MinecraftForge;

public class HudMod {

    private String name;
    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;

    public HudMod(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public void renderMod(int mouseX, int mouseY) {
        if (withinMod(mouseX, mouseY) || isDragging()) {
            Helper2D.drawOutlinedRectangle(getX() - 3, getY() - 3, getW() + 6, getH() + 6, 1, -1);
        }
    }

    public boolean withinMod(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
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
}
