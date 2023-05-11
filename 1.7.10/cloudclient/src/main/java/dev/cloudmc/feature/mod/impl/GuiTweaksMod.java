/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;

public class GuiTweaksMod extends Mod {

    public GuiTweaksMod() {
        super(
                "Gui Tweaks",
                "Adds Tweaks to the Gui like blur and transparency.",
                Type.Tweaks
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Blur Background", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Darken Background", this, true));
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Blur Background").isCheckToggled()) {
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
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
            if (e.gui == null) {
                if (Cloud.INSTANCE.mc.entityRenderer.getShaderGroup() != null) {
                    Cloud.INSTANCE.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
        }
    }
}
