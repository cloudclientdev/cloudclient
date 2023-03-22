/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.hud.ScrollHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin extends Gui {

    private static final ScrollHelper scrollHelper = new ScrollHelper(0, 0, 35, 300);

    @Shadow public int height;

    @Inject(
            method = "drawHoveringText(Ljava/util/List;IILnet/minecraft/client/gui/FontRenderer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawGradientRect(IIIIII)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ), locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            remap = false
    )
    public void drawHoveringTextStart(List textLines, int x, int y, FontRenderer font, CallbackInfo ci, int k, Iterator iterator, int j2, int k2, int i1, int j1) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, getTooltipPosition(k2, i1, height), 0);
    }

    @Inject(
            method = "drawHoveringText(Ljava/util/List;IILnet/minecraft/client/gui/FontRenderer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            remap = false
    )
    public void drawHoveringTextStop(List textLines, int x, int y, FontRenderer font, CallbackInfo ci) {
        GL11.glPopMatrix();
    }

    private static float getTooltipPosition(int tooltipY, int tooltipHeight, int screenHeight) {
        boolean shouldScroll = tooltipY < 0;
        scrollHelper.update();

        float scroll;
        float newToolTipHeight = tooltipHeight + 20;
        if (shouldScroll && Cloud.INSTANCE.modManager.getMod("ScrollTooltips").isToggled()) {
            scrollHelper.updateScroll();
            scrollHelper.setHeight(newToolTipHeight);
            scrollHelper.setMaxScroll(screenHeight);
            scroll = scrollHelper.getCalculatedScroll() + (newToolTipHeight - screenHeight);
        } else {
            scroll = 0;
        }

        return scroll;
    }

    @Redirect(
            method = "drawWorldBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawGradientRect(IIIIII)V"
            )
    )
    public void drawWorldBackground(GuiScreen instance, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (Cloud.INSTANCE.settingManager.getSettingByModAndName("Gui Tweaks", "Darken Background").isCheckToggled() &&
                Cloud.INSTANCE.modManager.getMod("Gui Tweaks").isToggled()) {
            this.drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }
}