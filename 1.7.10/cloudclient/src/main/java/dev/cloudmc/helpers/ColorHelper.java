/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;

public class ColorHelper {

    /**
     * Sets the color using an int color value using GlStateManager
     *
     * @param color The given int color value
     */

    public static void color(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    /**
     * Returns a Color from an int color value
     *
     * @param color The given int color value
     * @return The Color value
     */

    public static Color intColorToRGB(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Returns a Color from an X and Y position on the screen
     *
     * @param x The X Coordinate
     * @param y The Y Coordinate
     * @return The Color
     */

    public static Color getColorAtPixel(float x, float y) {
        ScaledResolution scaledResolution = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
        int scaleFactor = scaledResolution.getScaleFactor();
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(3);
        GL11.glReadPixels(
                (int) (x * scaleFactor),
                (int) (Cloud.INSTANCE.mc.displayHeight - y * scaleFactor),
                1, 1, GL11.GL_RGB, GL11.GL_FLOAT, floatBuffer
        );
        float red = floatBuffer.get(0);
        float green = floatBuffer.get(1);
        float blue = floatBuffer.get(2);
        return new Color(red, green, blue);
    }
}