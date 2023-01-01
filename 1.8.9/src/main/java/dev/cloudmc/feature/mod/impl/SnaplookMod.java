/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    void onKey(InputEvent.KeyInputEvent event) {
        if (Cloud.INSTANCE.mc.thePlayer != null) {
            cameraToggled = Keyboard.isKeyDown(this.getOptionalKey());
            if (cameraToggled && !holding) {
                holding = true;
                Cloud.INSTANCE.mc.gameSettings.thirdPersonView = 1;
            }
        }
    }

    @SubscribeEvent
    void onTick(TickEvent.ClientTickEvent event) {
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
