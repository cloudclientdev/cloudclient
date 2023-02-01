/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod.impl;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.helpers.Helper2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class CrosshairMod extends Mod {

    public CrosshairMod() {
        super("Crosshair", "Makes Crosshair customizable.");

        Cloud.INSTANCE.settingManager.addSetting(new Setting("Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
        boolean[] cells = {
                false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, true, false, false, false, false, false,
                false, false, false, false, false, true, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false,
                false, false, true, true, false, true, false, true, true, false, false,
                false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, true, false, false, false, false, false,
                false, false, false, false, false, true, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false
        };
        Cloud.INSTANCE.settingManager.addSetting(new Setting("Cells", this, cells));
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e) {
        if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
            ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc, Cloud.INSTANCE.mc.displayWidth, Cloud.INSTANCE.mc.displayHeight);

            int x = 0;
            int y = 0;
            for(int i = 0; i < 121; i++) {
                if(x % 11 == 0){
                    y += 1;
                    x = 0;
                }
                if(getCells()[i] && isToggled()) {
                    Helper2D.drawRectangle(
                            sr.getScaledWidth() / 2 - 5 + x,
                            sr.getScaledHeight() / 2 - 6 + y,
                            1,
                            1,
                            color()
                    );
                }

                x += 1;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre e){
        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if (!e.isCanceled() && e.isCancelable()) {
                e.setCanceled(true);
            }
        }
    }

    private int color() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Color").getColor().getRGB();
    }

    private boolean[] getCells() {
        return Cloud.INSTANCE.settingManager.getSettingByModAndName(getName(), "Cells").getCells();
    }
}
