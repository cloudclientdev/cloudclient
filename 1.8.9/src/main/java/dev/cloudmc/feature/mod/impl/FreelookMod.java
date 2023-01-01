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

public class FreelookMod extends Mod {

    public static boolean holding = false;
    public static boolean cameraToggled = false;
    public static float cameraYaw;
    public static float cameraPitch;

    public FreelookMod() {
        super(
                "Freelook",
                "Allows you to see a 360 view around your Player."
        );
        setOptionalKey(Keyboard.KEY_R);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        cameraToggled = Keyboard.isKeyDown(getOptionalKey());
        if (cameraToggled && !holding) {
            holding = true;
            cameraPitch = Cloud.INSTANCE.mc.thePlayer.rotationPitch;
            cameraYaw = Cloud.INSTANCE.mc.thePlayer.rotationYaw;
            Cloud.INSTANCE.mc.gameSettings.thirdPersonView = 1;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!cameraToggled && holding) {
            holding = false;
            Cloud.INSTANCE.mc.gameSettings.thirdPersonView = 0;
        }

        if (cameraToggled && Cloud.INSTANCE.mc.gameSettings.thirdPersonView != 1) {
            cameraToggled = false;
        }
    }
}
