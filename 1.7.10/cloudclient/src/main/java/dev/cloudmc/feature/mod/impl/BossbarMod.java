package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;
import dev.cloudmc.feature.setting.Setting;

public class BossbarMod extends Mod {
    public BossbarMod() {
        super(
                "Bossbar",
                "Adds tweaks to the Bossbar",
                Type.Tweaks
        );
    }
}
