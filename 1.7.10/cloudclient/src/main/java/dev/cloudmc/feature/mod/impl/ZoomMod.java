/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;

public class ZoomMod extends Mod {

    private static float defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
    private static final Animate animate = new Animate();
    private static boolean zoom = false;

    public ZoomMod() {
        super(
                "Zoom",
                "Allows you to zoom into the world.",
                Type.Mechanic
        );
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Keybinding", this, Keyboard.KEY_C));

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Zoom Amount", this, 120, 30));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Smooth Zoom", this, true));

        defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
        animate.setEase(Easing.LINEAR).setSpeed(700);
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text e) {
        defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
        animate.setMin(getAmount() / 2).setMax(defaultZoom).update();
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if (Keyboard.isKeyDown(getKey())) {
            zoom = true;
            animate.setReversed(true);
        } else {
            zoom = false;
            animate.setReversed(false);
        }
    }

    public static float getFOV() {
        if (isSmooth()) {
            return animate.getValueI();
        }
        return zoom ? getAmount() : defaultZoom;
    }

    private static boolean isSmooth() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName("Zoom", "Smooth Zoom").isCheckToggled();
    }

    private static float getAmount() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName("Zoom", "Zoom Amount").getCurrentNumber();
    }

    private int getKey() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
