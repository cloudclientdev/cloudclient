/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;

public class CellGrid extends Options {

    public CellGrid(Option options, Panel panel, int y) {
        super(options, panel, y);
        setH(135);
    }

    /**
     * Renders the CellGrid Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + 6 + getY(),
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
                    panel.getX() + panel.getW() - 140 + x,
                    panel.getY() + panel.getH() + getY() + y - 5,
                    11, 11,
            option.getCells()[i] ? ClientStyle.getReverseBackgroundColor(80).getRGB() : MathHelper.withinBox(
                    panel.getX() + panel.getW() - 140 + x,
                    panel.getY() + panel.getH() + getY() + y - 5,
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
                    panel.getX() + panel.getW() - 140 + x,
                    panel.getY() + panel.getH() + getY() + y - 5,
                    11, 11, mouseX, mouseY
            )){
                option.getCells()[i] = !option.getCells()[i];
            }
            x += 11;
        }
    }
}
