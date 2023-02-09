/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers.render;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class GLHelper {

    /**
     * Scissors out everything outside a rectangle using GL_SCISSOR_TEST
     *
     * @param x      Left X coordinate of the rectangle
     * @param y      Top Y coordinate of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     */

    public static void startScissor(int x, int y, int width, int height) {
        GL11.glEnable(GL_SCISSOR_TEST);
        ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
        GL11.glScissor(
                x * sr.getScaleFactor(),
                (sr.getScaledHeight() - y) * sr.getScaleFactor() - height * sr.getScaleFactor(),
                width * sr.getScaleFactor(),
                height * sr.getScaleFactor()
        );
    }

    /**
     * Stops the scissors
     */

    public static void endScissor() {
        GL11.glDisable(GL_SCISSOR_TEST);
    }

    /**
     * Scales everything with the given scale amount
     *
     * @param x X Position of the Object
     * @param y Y Position of the Object
     * @param scale The Amount the Object should be scaled
     */

    public static void startScale(int x, int y, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-x, -y, -1);
    }

    /**
     * Stops scaling
     */

    public static void endScale() {
        GL11.glPopMatrix();
    }
}
