package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;

public class TimeChangerMod extends Mod {
    public TimeChangerMod() {
        super(
                "TimeChanger",
                "Changes the time of the current World visually.",
                Type.Visual
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Offset", this, 12000, 0));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Speed", this, 50, 1));
    }
}
