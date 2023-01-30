/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.IntBuffer;

public class ColorHelper {

    /**
     * Sets the color using a hex value using GlStateManager
     *
     * @param color The given hex value
     */

    public static void color(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        GlStateManager.color(red, green, blue, alpha);
    }

    /**
     * Returns a Color from a hex value
     *
     * @param color The given hex value
     * @return The Color value
     */

    public static Color hexToRgb(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Returns a hex color value from the color at an X and Y position on the screen
     *
     * @param x The X Coordinate
     * @param y The Y Coordinate
     * @return The Color value
     */

    public static int getColorAtPixel(float x, float y) {
        ScaledResolution scaledResolution = new ScaledResolution(Cloud.INSTANCE.mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        int[] ints = new int[1];
        GL11.glReadPixels(
                (int) ((x / scaleFactor) * 9),
                (int) (((scaledResolution.getScaledHeight() - y) / scaleFactor) * 9),
                1, 1, 32993, 33639, intbuffer
        );
        intbuffer.get(ints);
        return ints[0];
    }
}
