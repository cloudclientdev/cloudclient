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
import org.lwjgl.input.Keyboard;

public class SnaplookMod extends Mod {

    private static boolean holding = false;
    private boolean cameraToggled = false;

    public SnaplookMod() {
        super(
                "Snaplook",
                "Allows you to see you in 3rd person, by only holding a button."
        );
        setOptionalKey(Keyboard.KEY_F);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Cloud.INSTANCE.mc.thePlayer != null) {
            cameraToggled = Keyboard.isKeyDown(this.getOptionalKey());
            if (cameraToggled && !holding) {
                holding = true;
                Cloud.INSTANCE.mc.gameSettings.thirdPersonView = 1;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Cloud.INSTANCE.mc.thePlayer != null) {
            if (!cameraToggled && holding) {
                holding = false;
                Cloud.INSTANCE.mc.gameSettings.thirdPersonView = 0;
            }

            if (cameraToggled && Cloud.INSTANCE.mc.gameSettings.thirdPersonView != 1) {
                cameraToggled = false;
            }
        }
    }
}
