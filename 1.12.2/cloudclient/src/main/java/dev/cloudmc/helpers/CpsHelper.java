/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.helpers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class CpsHelper {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    @SubscribeEvent
    public void onClick(MouseEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return; // don't register cps in GUIs
        long time = System.currentTimeMillis();

        if (!event.isButtonstate()) return;

        if (event.getButton() == 0) leftClicks.add(time);
        else if (event.getButton() == 1) rightClicks.add(time);

        removeOldClicks(time);
    }

    public int getCPS(int mouseButton) {
        removeOldClicks(System.currentTimeMillis());
        return mouseButton == 0 ? leftClicks.size() : rightClicks.size();
    }

    public void removeOldClicks(long currentTime) {
        leftClicks.removeIf(e -> e + 1000 < currentTime);
        rightClicks.removeIf(e -> e + 1000 < currentTime);
    }
}
