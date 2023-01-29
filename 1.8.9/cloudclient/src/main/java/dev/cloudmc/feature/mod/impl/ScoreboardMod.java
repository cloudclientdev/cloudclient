package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;

import java.awt.*;

public class ScoreboardMod extends Mod {

    public ScoreboardMod() {
        super(
                "Scoreboard",
                "Adds Tweaks to the Scoreboard"
        );

        String[] mode = {"Modern", "Legacy"};
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Mode", this, "Legacy", 1, mode));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Background", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Remove Red Numbers", this, false));
    }
}
