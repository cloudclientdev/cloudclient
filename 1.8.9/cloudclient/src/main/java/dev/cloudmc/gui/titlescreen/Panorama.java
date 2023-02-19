/*
 * Copyright (c) 2022 Mojang &
 * slightly edited by DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.titlescreen;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class Panorama extends GuiScreen {

    public static int panoramaTimer = 0;

    private final DynamicTexture viewportTexture = new DynamicTexture(256, 256);

    /**
     * An array of all the paths to the panorama pictures.
     */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{
            new ResourceLocation(Cloud.modID, "panorama/panorama_0.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_1.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_2.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_3.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_4.png"),
            new ResourceLocation(Cloud.modID, "panorama/panorama_5.png")
    };

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        panoramaTimer++;
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(float drawPanorama3) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) panoramaTimer + drawPanorama3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) panoramaTimer + drawPanorama3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                switch (k) {
                    case 1:
                        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 2:
                        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 3:
                        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                        break;
                    case 4:
                        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                    case 5:
                        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                        break;
                }

                mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    /**
     * Rotates and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox() {
        ResourceLocation backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
        mc.getTextureManager().bindTexture(backgroundTexture);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = width;
            int l = height;
            float f1 = (float) (j - i / 2) / 256.0F;
            worldrenderer.pos(k, l, zLevel).tex((0.0F + f1), 1.0).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(k, 0.0, zLevel).tex((1.0F + f1), 1.0).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0, 0.0, zLevel).tex((1.0F + f1), 0.0).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0, l, zLevel).tex((0.0F + f1), 0.0).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(float partialTicks) {
        mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        drawPanorama(partialTicks);
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        rotateAndBlurSkybox();
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        float f = width > height ? 120.0F / (float) width : 120.0F / (float) height;
        float f1 = (float) height * f / 256.0F;
        float f2 = (float) width * f / 256.0F;
        int i = width;
        int j = height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, j, zLevel).tex((0.5F - f1), (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, j, zLevel).tex((0.5F - f1), (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, 0.0, zLevel).tex((0.5F + f1), (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0, 0.0, zLevel).tex((0.5F + f1), (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        renderSkybox(partialTicks);
        GlStateManager.enableAlpha();
        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
    }
}
