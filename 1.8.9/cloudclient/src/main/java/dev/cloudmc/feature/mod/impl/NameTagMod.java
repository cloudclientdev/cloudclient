package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;

import java.awt.*;

public class NameTagMod extends Mod {

    public NameTagMod() {
        super(
                "NameTag",
                "Adds tweaks to NameTags.",
                Type.Tweaks
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("NameTag in 3rd Person", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Opacity", this, 255, 64));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Size", this, 3, 1));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Y Position", this, 5, 2.5f));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Disable Player NameTags", this, false));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }
}
