/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;

import java.awt.*;

public class KeystrokesMod extends Mod {

    public KeystrokesMod() {
        super(
                "Keystrokes",
                "Shows your Keystrokes on the HUD."
        );

        String[] mode = {"Modern", "Legacy"};
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Background", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Clicks", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("CPS", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Font Color", this, new Color(255, 255, 255)));
    }
}
