/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;

public class Helper2D {

    /**
     * Draws a rounded Rectangle on the HUD using quarter circles and rectangles
     *
     * @param x      Left X coordinate of the rectangle
     * @param y      Top Y coordinate of the rectangle
     * @param w      Width of the rectangle
     * @param h      Height of the rectangle
     * @param radius The radius of the corners
     * @param color  The Color of the rectangle
     * @param index  -1: No Rounded Corners; 0: Rounded Corners on all sides; 1: Rounded Corners on the top; 2: Rounded Corners on the bottom;
     */

    public static void drawRoundedRectangle(int x, int y, int w, int h, int radius, int color, int index) {
        if (index == -1) {
            Gui.drawRect(x, y, w + x, y + h, color);
        }
        else if (index == 0) {
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x + radius, y, x + w - radius, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x + radius, y + h - radius, x + w - radius, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        }
        else if (index == 1) {
            Gui.drawRect(0, 0, 0, 0, -1);
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x + radius, y, x + w - radius, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x, y + h - radius, x + w, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
        }
        else if (index == 2) {
            Gui.drawRect(0, 0, 0, 0, -1);
            Gui.drawRect(x + radius, y + radius, x + w - radius, y + h - radius, color);
            Gui.drawRect(x, y, x + w, y + radius, color);
            Gui.drawRect(x + w - radius, y + radius, x + w, y + h - radius, color);
            Gui.drawRect(x + radius, y + h - radius, x + w - radius, y + h, color);
            Gui.drawRect(x, y + radius, x + radius, y + h - radius, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        }
    }

    /**
     * Draws a rectangle using width and height, instead of 4 coordinate positions
     *
     * @param x     Left X coordinate of the rectangle
     * @param y     Top Y coordinate of the rectangle
     * @param w     Width of the rectangle
     * @param h     Height of the rectangle
     * @param color The Color of the rectangle
     */

    public static void drawRectangle(int x, int y, int w, int h, int color) {
        Gui.drawRect(x, y, x + w, y + h, color);
    }

    /**
     * Draws a given picture
     *
     * @param x        Left X coordinate of the image
     * @param y        Top Y coordinate of the image
     * @param w        Width of the image
     * @param h        Height of the image
     * @param color    The Color of the image, if value is 0, normal color is used
     * @param location The location of the image to be loaded from
     */

    public static void drawPicture(int x, int y, int w, int h, int color, String location) {
        if (color == 0) {
            GL11.glColor4f(1, 1, 1, 1);
        }
        else {
            color(color);
        }
        ResourceLocation resourceLocation = new ResourceLocation(Cloud.modID, location);
        Cloud.INSTANCE.mc.getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable(GL_BLEND);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, w, h, w, h);
        GL11.glDisable(GL_BLEND);
    }

    /**
     * Draws an outline of a rectangle
     *
     * @param x     Left X coordinate of the rectangle outline
     * @param y     Top Y coordinate of the rectangle outline
     * @param w     Width of the rectangle outline
     * @param h     Height of the rectangle outline
     * @param t     The Width of the rectangle outline
     * @param color The Color of the rectangle outline
     */

    public static void drawOutlinedRectangle(int x, int y, int w, int h, int t, int color) {
        drawRectangle(x, y, w, t, color);
        drawRectangle(x + w - t, y, t, h, color);
        drawRectangle(x, y + h - t, w, t, color);
        drawRectangle(x, y, t, h, color);
    }

    /**
     * Draws a rectangle gradient from 4 given coordinates and 2 given colors vertically
     *
     * @param left       Left X coordinate of the rectangle
     * @param top        Top Y coordinate of the rectangle
     * @param right      Right X coordinate of the rectangle
     * @param bottom     Bottom Y coordinate of the rectangle
     * @param startColor The first color of the gradient
     * @param endColor   The second color of the gradient
     */

    public static void drawGradientRectangle(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, 0);
        tessellator.addVertex(left, top, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, 0);
        tessellator.addVertex(right, bottom, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a rectangle gradient from 4 given coordinates and 2 given colors horizontally
     *
     * @param left       Left X coordinate of the rectangle
     * @param top        Top Y coordinate of the rectangle
     * @param right      Right X coordinate of the rectangle
     * @param bottom     Bottom Y coordinate of the rectangle
     * @param startColor The first color of the gradient
     * @param endColor   The second color of the gradient
     */

    public static void drawHorizontalGradientRectangle(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(left, top, 0);
        tessellator.addVertex(left, bottom, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(right, bottom, 0);
        tessellator.addVertex(right, top, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a circle
     *
     * @param x     Left X coordinate of the circle
     * @param y     Top Y coordinate of the circle
     * @param r     The radius of the circle
     * @param h     The beginning of from where the circle should be drawn
     * @param j     The ending of from where the circle should be drawn
     * @param color The color of the circle
     */

    public static void drawCircle(float x, float y, float r, int h, int j, int color) {
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        glBegin(GL_TRIANGLE_FAN);

        color(color);

        float var;
        glVertex2f(x, y);
        for (var = h; var <= j; var++) {
            color(color);
            glVertex2f((float) (r * Math.cos(Math.PI * var / 180) + x), (float) (r * Math.sin(Math.PI * var / 180) + y));
        }

        glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL_BLEND);
    }

    /**
     * Sets the color using a hex value using GlStateManager
     *
     * @param color The given hex value
     */

    private static void color(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        GL11.glColor4f(red, green, blue, alpha);
    }

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

    public static void startScale(int x, int y, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-x, -y, -1);
    }

    public static void endScale() {
        GL11.glPopMatrix();
    }
}