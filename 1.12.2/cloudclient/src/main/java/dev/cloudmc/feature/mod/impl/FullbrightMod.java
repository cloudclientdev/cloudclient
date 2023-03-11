/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullbrightMod extends Mod {

    private float oldGamma;

    public FullbrightMod() {
        super(
                "Fullbright",
                "Changes the Gamma of the game to a given value.",
                Type.Visual
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Brightness", this, 100, 10));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        oldGamma = Cloud.INSTANCE.mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Cloud.INSTANCE.mc.gameSettings.gammaSetting = oldGamma;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        Cloud.INSTANCE.mc.gameSettings.gammaSetting =
                Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Brightness").getCurrentNumber();
    }
}
