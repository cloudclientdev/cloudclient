/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;

public class AnimationMod extends Mod {

    public AnimationMod() {
        super(
                "Animation",
                "1.7 Animations in 1.8."
        );

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Block Animation", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Eat/Drink Animation", this, true));
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Bow Animation", this, true));

    }
}
