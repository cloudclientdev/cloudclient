/*
 * Copyright (c) 2022 Moulberry
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class MotionblurMod extends Mod {

    private Framebuffer blurBufferMain = null;
    private Framebuffer blurBufferInto = null;

    public MotionblurMod() {
        super("Motionblur", "Adds a motionblur effect to the screen, also seen in cameras.");

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Amount", this, 10, 6));
    }

    private static Framebuffer checkFramebufferSizes(Framebuffer framebuffer, int width, int height) {
        if (framebuffer == null || framebuffer.framebufferWidth != width || framebuffer.framebufferHeight != height) {
            if (framebuffer == null) {
                framebuffer = new Framebuffer(width, height, true);
            }
            else {
                framebuffer.createBindFramebuffer(width, height);
            }

            framebuffer.setFramebufferFilter(9728);
        }

        return framebuffer;
    }

    public static void drawTexturedRectNoBlend(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableTexture2D();
        GL11.glTexParameteri(3553, 10241, filter);
        GL11.glTexParameteri(3553, 10240, filter);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, (y + height), 0.0D).tex(uMin, vMax).endVertex();
        worldrenderer.pos((x + width), (y + height), 0.0D).tex(uMax, vMax).endVertex();
        worldrenderer.pos((x + width), y, 0.0D).tex(uMax, vMin).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(uMin, vMin).endVertex();
        tessellator.draw();
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (Cloud.INSTANCE.mc.currentScreen == null) {
            if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
                if (OpenGlHelper.isFramebufferEnabled()) {
                    int width = Cloud.INSTANCE.mc.getFramebuffer().framebufferWidth;
                    int height = Cloud.INSTANCE.mc.getFramebuffer().framebufferHeight;

                    GlStateManager.matrixMode(5889);
                    GlStateManager.loadIdentity();
                    GlStateManager.ortho(0, width, height, 0, 2000, 4000);
                    GlStateManager.matrixMode(5888);
                    GlStateManager.loadIdentity();
                    GlStateManager.translate(0, 0, -2000);
                    blurBufferMain = checkFramebufferSizes(blurBufferMain, width, height);
                    blurBufferInto = checkFramebufferSizes(blurBufferInto, width, height);
                    blurBufferInto.framebufferClear();
                    blurBufferInto.bindFramebuffer(true);
                    OpenGlHelper.glBlendFunc(770, 771, 0, 1);
                    GlStateManager.disableLighting();
                    GlStateManager.disableFog();
                    GlStateManager.disableBlend();
                    Cloud.INSTANCE.mc.getFramebuffer().bindFramebufferTexture();
                    GlStateManager.color(1, 1, 1, 1);
                    drawTexturedRectNoBlend(0.0F, 0.0F, (float) width, (float) height, 0.0F, 1.0F, 0.0F, 1.0F, 9728);
                    GlStateManager.enableBlend();
                    blurBufferMain.bindFramebufferTexture();
                    GlStateManager.color(1, 1, 1, getAmount() / 10 - 0.1f);
                    drawTexturedRectNoBlend(0, 0, (float) width, (float) height, 0, 1, 1, 0, 9728);
                    Cloud.INSTANCE.mc.getFramebuffer().bindFramebuffer(true);
                    blurBufferInto.bindFramebufferTexture();
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.enableBlend();
                    OpenGlHelper.glBlendFunc(770, 771, 1, 771);
                    drawTexturedRectNoBlend(0.0F, 0.0F, (float) width, (float) height, 0.0F, 1.0F, 0.0F, 1.0F, 9728);
                    Framebuffer tempBuff = this.blurBufferMain;
                    blurBufferMain = this.blurBufferInto;
                    blurBufferInto = tempBuff;
                }
            }
        }
    }

    private float getAmount() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Amount").getCurrentNumber();
    }
}
