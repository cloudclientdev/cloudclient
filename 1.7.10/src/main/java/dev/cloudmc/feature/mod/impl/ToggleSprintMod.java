/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */
package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ToggleSprintMod extends Mod {

    private static boolean toggled = false;

    public ToggleSprintMod() {
        super(
                "ToggleSprint",
                "Allows you to toggle the Sprint button instead of holding it."
        );
        setOptionalKey(Keyboard.KEY_LCONTROL);

        String[] mode = {"Modern", "Legacy"};
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Background", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Font Color", this, new Color(255, 255, 255)));
    }

    public static boolean isSprinting() {
        return toggled;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if (toggled) {
            KeyBinding.setKeyBindState(Cloud.INSTANCE.mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if(Keyboard.isKeyDown(getOptionalKey())){
            toggled = !toggled;
        }
    }
}
