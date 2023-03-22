package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;

public class ScoreboardMod extends Mod {

    public ScoreboardMod() {
        super(
                "Scoreboard",
                "Adds Tweaks to the Scoreboard",
                Type.Tweaks
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Background", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Remove Red Numbers", this, false));
    }
}
