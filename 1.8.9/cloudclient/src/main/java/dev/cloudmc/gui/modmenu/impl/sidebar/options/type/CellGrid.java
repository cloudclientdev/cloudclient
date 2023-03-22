/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.render.Helper2D;

public class CellGrid extends Options {

    public CellGrid(Option option, Panel panel, int y) {
        super(option, panel, y);
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
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                Helper2D.drawRectangle(
                        panel.getX() + panel.getW() - 140 + row * 11,
                        panel.getY() + panel.getH() + getY() + 5 + col * 11,
                        11, 11,
                        option.getCells()[row][col] ?
                                Style.getReverseColor(80).getRGB() :
                                MathHelper.withinBox(
                                        panel.getX() + panel.getW() - 140 + row * 11,
                                        panel.getY() + panel.getH() + getY() + 5 + col * 11,
                                        11, 11, mouseX, mouseY
                                ) ?
                                        0x00ffffff :
                                        Style.getColor(50).getRGB()
                );
            }
        }
    }

    /**
     * Toggles the state of a cell if it is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (MathHelper.withinBox(
                        panel.getX() + panel.getW() - 140 + row * 11,
                        panel.getY() + panel.getH() + getY() + 5 + col * 11,
                        11, 11, mouseX, mouseY
                )) {
                    option.getCells()[row][col] = !option.getCells()[row][col];
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}