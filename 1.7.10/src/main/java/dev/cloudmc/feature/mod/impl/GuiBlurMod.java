/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;

import java.io.IOException;

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
            try {
                Cloud.INSTANCE.mc.entityRenderer.theShaderGroup =
                        new ShaderGroup(
                                Cloud.INSTANCE.mc.getTextureManager(),
                                Cloud.INSTANCE.mc.getResourceManager(),
                                Cloud.INSTANCE.mc.getFramebuffer(),
                                new ResourceLocation("shaders/post/blur.json")
                        );
                Cloud.INSTANCE.mc.entityRenderer.theShaderGroup
                        .createBindFramebuffers(Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
        if (e.gui == null) {
            if (Cloud.INSTANCE.mc.entityRenderer.getShaderGroup() != null) {
                Cloud.INSTANCE.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
}
