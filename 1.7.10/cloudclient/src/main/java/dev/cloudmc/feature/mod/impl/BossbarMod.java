package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;

public class BossbarMod extends Mod {
    public BossbarMod() {
        super(
                "Bossbar",
                "Adds tweaks to the Bossbar"
        );

        String[] mode = {"Modern", "Legacy"};
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Mode", this, "Legacy", 1, mode));
    }
}
