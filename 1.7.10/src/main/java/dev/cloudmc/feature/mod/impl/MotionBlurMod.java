/*
 * Copyright (c) 2022 Moulberry
 * CREATIVE COMMONS PUBLIC LICENSE
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class MotionBlurMod extends Mod {

    private Framebuffer blurBufferMain = null;
    private Framebuffer blurBufferInto = null;

    public MotionBlurMod() {
        super("MotionBlur", "Adds a motionblur effect to the screen, also seen in cameras.");

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
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(3553, 10241, filter);
        GL11.glTexParameteri(3553, 10240, filter);
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(7);
        tessellator.addVertexWithUV(x, (y + height), 0.0D, uMin, vMax);
        tessellator.addVertexWithUV((x + width), (y + height), 0.0D, uMax, vMax);
        tessellator.addVertexWithUV((x + width), y, 0.0D, uMax, vMin);
        tessellator.addVertexWithUV(x, y, 0.0D, uMin, vMin);
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

                    GL11.glMatrixMode(5889);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, width, height, 0, 2000, 4000);
                    GL11.glMatrixMode(5888);
                    GL11.glLoadIdentity();
                    GL11.glTranslated(0, 0, -2000);
                    blurBufferMain = checkFramebufferSizes(blurBufferMain, width, height);
                    blurBufferInto = checkFramebufferSizes(blurBufferInto, width, height);
                    blurBufferInto.framebufferClear();
                    blurBufferInto.bindFramebuffer(true);
                    GL14.glBlendFuncSeparate(770, 771, 0, 1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_FOG);
                    GL11.glDisable(GL11.GL_BLEND);
                    Cloud.INSTANCE.mc.getFramebuffer().bindFramebufferTexture();
                    GL11.glColor4f(1, 1, 1, 1);
                    drawTexturedRectNoBlend(0.0F, 0.0F, (float) width, (float) height, 0.0F, 1.0F, 0.0F, 1.0F, 9728);
                    GL11.glEnable(GL11.GL_BLEND);
                    blurBufferMain.bindFramebufferTexture();
                    GL11.glColor4f(1, 1, 1, getAmount() / 10 - 0.1f);
                    drawTexturedRectNoBlend(0, 0, (float) width, (float) height, 0, 1, 1, 0, 9728);
                    Cloud.INSTANCE.mc.getFramebuffer().bindFramebuffer(true);
                    blurBufferInto.bindFramebufferTexture();
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL14.glBlendFuncSeparate(770, 771, 1, 771);
                    drawTexturedRectNoBlend(0.0F, 0.0F, (float) width, (float) height, 0.0F, 1.0F, 0.0F, 1.0F, 9728);
                    Framebuffer tempBuff = this.blurBufferMain;
                    blurBufferMain = this.blurBufferInto;
                    blurBufferInto = tempBuff;
                }
            }
        }
    }

    private float getAmount(){
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Amount").getCurrentNumber();
    }
}
