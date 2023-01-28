/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import dev.cloudmc.Cloud;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

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
            drawRectangle(x, y, w, h, color);
        } else if (index == 0) {
            drawRectangle(x + radius, y + radius, w - radius * 2, h - radius * 2, color);
            drawRectangle(x + radius, y, w - radius * 2, radius, color);
            drawRectangle(x + w - radius, y + radius, radius, h - radius * 2, color);
            drawRectangle(x + radius, y + h - radius, w - radius * 2, radius, color);
            drawRectangle(x, y + radius, radius, h - radius * 2, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        } else if (index == 1) {
            drawRectangle(x + radius, y, w - radius * 2, radius, color);
            drawRectangle(x, y + radius, w, h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
        } else if (index == 2) {
            drawRectangle(x, y, w, h - radius, color);
            drawRectangle(x + radius, y + h - radius, w - radius * 2, radius, color);
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
            GlStateManager.color(1, 1, 1);
        } else {
            color(color);
        }
        ResourceLocation resourceLocation = new ResourceLocation(Cloud.modID, location);
        Cloud.INSTANCE.mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, w, h, w, h);
        GlStateManager.disableBlend();
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

    public static void drawGradientRectangle(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f1 = (float) (startColor >> 24 & 255) / 255.0F;
        float f2 = (float) (startColor >> 16 & 255) / 255.0F;
        float f3 = (float) (startColor >> 8 & 255) / 255.0F;
        float f4 = (float) (startColor & 255) / 255.0F;
        float f5 = (float) (endColor >> 24 & 255) / 255.0F;
        float f6 = (float) (endColor >> 16 & 255) / 255.0F;
        float f7 = (float) (endColor >> 8 & 255) / 255.0F;
        float f8 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(right, top, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(left, top, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(left, bottom, 0f).color(f6, f7, f8, f5).endVertex();
        worldRenderer.pos(right, bottom, 0f).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
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

    public static void drawHorizontalGradientRectangle(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f1 = (float) (startColor >> 24 & 255) / 255.0F;
        float f2 = (float) (startColor >> 16 & 255) / 255.0F;
        float f3 = (float) (startColor >> 8 & 255) / 255.0F;
        float f4 = (float) (startColor & 255) / 255.0F;
        float f5 = (float) (endColor >> 24 & 255) / 255.0F;
        float f6 = (float) (endColor >> 16 & 255) / 255.0F;
        float f7 = (float) (endColor >> 8 & 255) / 255.0F;
        float f8 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(left, top, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(left, bottom, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(right, bottom, 0f).color(f6, f7, f8, f5).endVertex();
        worldRenderer.pos(right, top, 0f).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
        worldrenderer.pos(x, y, 0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
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
            glVertex2f(
                    (float) (r * Math.cos(Math.PI * var / 180) + x),
                    (float) (r * Math.sin(Math.PI * var / 180) + y)
            );
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
        GlStateManager.color(red, green, blue, alpha);
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
        ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc);
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