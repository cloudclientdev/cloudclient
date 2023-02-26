/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;

public class NickHiderMod extends Mod {

    public NickHiderMod() {
        super(
                "NickHider",
                "Hides your nickname in game by replacing it with \"You\".",
                Type.Visual
        );
    }

    public static String replaceNickname(String nick) {
        if(Cloud.INSTANCE != null) {
            if (Cloud.INSTANCE.modManager != null) {
                if (Cloud.INSTANCE.modManager.getMod("NickHider").isToggled()) {
                    return nick.replace(Cloud.INSTANCE.mc.getSession().getUsername(), "You");
                }
            }
        }
        return nick;
    }
}
