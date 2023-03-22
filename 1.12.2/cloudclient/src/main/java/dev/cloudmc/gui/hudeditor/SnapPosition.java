/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor;

public class SnapPosition {

    private float sPos;
    private float pos;
    private boolean horizontal;
    private boolean snapping;

    public SnapPosition() {
        this.sPos = 0;
        this.pos = 0;
        this.horizontal = false;
        this.snapping = true;
    }

    public void setAll(float newPos, float pos, boolean horizontal) {
        this.sPos = newPos;
        this.pos = pos;
        this.horizontal = horizontal;
    }

    public float getsPos() {
        return sPos;
    }

    public void setsPos(float sPos) {
        this.sPos = sPos;
    }

    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isSnapping() {
        return snapping;
    }

    public void setSnapping(boolean snapping) {
        this.snapping = snapping;
    }
}