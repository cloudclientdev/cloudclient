/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ZoomMod extends Mod {

    private static float defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
    private static final Animate animate = new Animate();
    private static boolean zoom = false;
    private static int scrollAmount;

    public ZoomMod() {
        super(
                "Zoom",
                "Allows you to zoom into the world.",
                Type.Mechanic
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Keybinding", this, Keyboard.KEY_C));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Zoom Amount", this, 100, 30));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Smooth Zoom", this, true));

        defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
        animate.setEase(Easing.LINEAR).setSpeed(700);
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text e) {
        defaultZoom = Cloud.INSTANCE.mc.gameSettings.fovSetting;
        animate.setMin(getAmount() / 2).setMax(defaultZoom).update();

        if(zoom && Cloud.INSTANCE.mc.currentScreen == null) {
            int scroll = Mouse.getDWheel();
            if (scroll > 0) {
                if(isSmooth()) {
                    if(scrollAmount + animate.getValueI() > 2) {
                        scrollAmount -= 2;
                    }
                } else {
                    if(scrollAmount + getAmount() > 2) {
                        scrollAmount -= 2;
                    }
                }
            } else if (scroll < 0) {
                if(scrollAmount  < 0) {
                    scrollAmount += 2;
                }
            }
        } else {
            scrollAmount = 0;
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        zoom = Keyboard.isKeyDown(getKey());
        animate.setReversed(zoom);
    }

    public static float getFOV() {
        if (isSmooth()) {
            return animate.getValueI() + scrollAmount;
        }
        return zoom ? getAmount() + scrollAmount : defaultZoom;
    }

    public static boolean isZoom() {
        return zoom;
    }

    private static boolean isSmooth() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName("Zoom", "Smooth Zoom").isCheckToggled();
    }

    private static float getAmount() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName("Zoom", "Zoom Amount").getCurrentNumber() + 2;
    }

    private int getKey() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
