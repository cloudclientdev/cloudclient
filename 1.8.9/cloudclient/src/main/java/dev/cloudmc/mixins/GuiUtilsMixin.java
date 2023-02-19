/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.mixins;

import dev.cloudmc.Cloud;
import dev.cloudmc.helpers.hud.ScrollHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

@Mixin(GuiUtils.class)
public abstract class GuiUtilsMixin {

    private static final ScrollHelper scrollHelper = new ScrollHelper(0, 0);

    @Inject(
            method = "drawHoveringText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/client/config/GuiUtils;drawGradientRect(IIIIIII)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ), locals = LocalCapture.CAPTURE_FAILEXCEPTION,
            remap = false
    )
    private static void drawHoveringTextStart(
            List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, CallbackInfo ci,
            int tooltipTextWidth, boolean needsWrap, int titleLinesCount, int tooltipX, int tooltipY, int tooltipHeight, int zLevel, int backgroundColor
    ) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, getTooltipPosition(tooltipY, tooltipHeight, screenHeight), 0);
    }

    @Inject(
            method = "drawHoveringText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;enableLighting()V",
                    shift = At.Shift.BEFORE
            ),
            remap = false
    )
    private static void drawHoveringTextStop(
            List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font, CallbackInfo ci
    ) {
        GlStateManager.popMatrix();
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
}