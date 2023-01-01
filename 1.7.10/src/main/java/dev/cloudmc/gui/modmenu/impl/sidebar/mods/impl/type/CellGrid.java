/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.Helper2D;

public class CellGrid extends Settings {

    public CellGrid(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    /**
     * Renders the CellGrid Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + 6 + getY(),
                ClientStyle.getColor().getRGB()
        );

        int x = 0;
        int y = 0;
        for(int i = 0; i < 121; i++) {
            if(x % 121 == 0){
                y += 11;
                x = 0;
            }
            Helper2D.drawRectangle(
                    button.getPanel().getX() + button.getPanel().getW() - 140 + x,
                    button.getPanel().getY() + button.getPanel().getH() + getY() + y - 5,
                    11, 11,
                    setting.getCells()[i] ? ClientStyle.getReverseBackgroundColor(80).getRGB() : MathHelper.withinBox(
                            button.getPanel().getX() + button.getPanel().getW() - 140 + x,
                            button.getPanel().getY() + button.getPanel().getH() + getY() + y - 5,
                            11, 11, mouseX, mouseY
                    ) ? 0x00ffffff : ClientStyle.getBackgroundColor(50).getRGB());

            x += 11;
        }
    }

    /**
     * Toggles the state of a cell if it is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int x = 0;
        int y = 0;
        for(int i = 0; i < 121; i++) {
            if(x % 121 == 0){
                y += 11;
                x = 0;
            }
            if(MathHelper.withinBox(
                    button.getPanel().getX() + button.getPanel().getW() - 140 + x,
                    button.getPanel().getY() + button.getPanel().getH() + getY() + y - 5,
                    11, 11, mouseX, mouseY
            )){
                setting.getCells()[i] = !setting.getCells()[i];
            }
            x += 11;
        }
    }
}
