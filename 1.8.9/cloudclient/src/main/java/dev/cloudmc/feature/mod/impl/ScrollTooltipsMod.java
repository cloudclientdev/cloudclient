/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.mod.Type;

public class ScrollTooltipsMod extends Mod {

    public ScrollTooltipsMod() {
        super(
                "ScrollTooltips",
                "Makes long tooltips which go offscreen, scrollable.",
                Type.Tweaks
        );
    }
}
