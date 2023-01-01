/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiBlurMod extends Mod {

    public GuiBlurMod() {
        super(
                "GuiBlur",
                "Adds a blur effect to opened GUIs."
        );
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (!(e.gui instanceof GuiChat)) {
            Cloud.INSTANCE.mc.entityRenderer.loadShader(
                    new ResourceLocation("shaders/post/blur.json"));
        }
        if (e.gui == null) {
            if (Cloud.INSTANCE.mc.entityRenderer.getShaderGroup() != null) {
                Cloud.INSTANCE.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
}
