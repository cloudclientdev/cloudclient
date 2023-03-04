/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.impl.crosshair.CrosshairMod;
import dev.cloudmc.feature.mod.impl.crosshair.LayoutManager;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.ResolutionHelper;
import dev.cloudmc.helpers.render.Helper2D;

import java.util.Arrays;

public class CellGrid extends Settings {

    public CellGrid(Setting setting, Button button, int y) {
        super(setting, button, y);
        setH(160);
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
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 140,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                121, 121, 2, Style.getColor(50).getRGB(), 0
        );

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                int x = button.getPanel().getX() + button.getPanel().getW() - 140 + col * 11;
                int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + row * 11;
                Helper2D.drawRectangle(x, y, 11, 11,
                        setting.getCells()[row][col] ?
                                MathHelper.withinBox(x, y, 11, 11, mouseX, mouseY) ? 0x70ffffff : 0x50ffffff :
                                MathHelper.withinBox(x, y, 11, 11, mouseX, mouseY) ? 0x20ffffff : 0x00ffffff

                );
            }
        }

        int index = 0;
        int counter = 0;
        for (boolean[][] layout : CrosshairMod.layoutManager.getLayoutList()) {
            int x = button.getPanel().getX() + button.getPanel().getW() - 185 - counter * 42;
            int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + index * 42;
            Helper2D.drawRoundedRectangle(x, y, 37, 37, 2,
                    Style.getColor(MathHelper.withinBox(x, y, 37, 37, mouseX, mouseY) ? 60 : 40).getRGB(), 0);

            for (int row = 0; row < 11; row++) {
                for (int col = 0; col < 11; col++) {
                    Helper2D.drawRectangle(
                            button.getPanel().getX() + button.getPanel().getW() - 178 - counter * 42 + col * 2,
                            button.getPanel().getY() + button.getPanel().getH() + getY() + 13 + index * 42 + row * 2,
                            2, 2, layout[row][col] ? 0xffffffff : 0x00ffffff
                    );
                }
            }

            index++;
            if (index % 3 == 0) {
                counter++;
                index = 0;
            }
        }

        String text = "Erase all";
        int width = Cloud.INSTANCE.fontHelper.size20.getStringWidth(text);
        int x = button.getPanel().getX() + button.getPanel().getW() - 20 - width;
        int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 135;

        Cloud.INSTANCE.fontHelper.size20.drawString(text, x - 3, y + 6, -1);
        Helper2D.drawRoundedRectangle(x - 9, y, width + 10, 20, 2, Style.getColor(
                MathHelper.withinBox(x - 9, y, width + 10, 20, mouseX, mouseY) ? 50 : 30
        ).getRGB(), 0);
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
                        button.getPanel().getX() + button.getPanel().getW() - 140 + col * 11,
                        button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + row * 11,
                        11, 11, mouseX, mouseY
                )) {
                    setting.getCells()[row][col] = !setting.getCells()[row][col];
                }
            }
        }

        int index = 0;
        int counter = 0;
        for (boolean[][] layout : CrosshairMod.layoutManager.getLayoutList()) {
            int x = button.getPanel().getX() + button.getPanel().getW() - 190 - counter * 42;
            int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + index * 42;
            if (MathHelper.withinBox(x, y, 37, 37, mouseX, mouseY)) {
                setting.setCells(layout);
            }

            index++;
            if (index % 3 == 0) {
                counter++;
                index = 0;
            }
        }

        String text = "Erase all";
        int width = Cloud.INSTANCE.fontHelper.size20.getStringWidth(text);
        int x = button.getPanel().getX() + button.getPanel().getW() - 20 - width;
        int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 135;

        if (MathHelper.withinBox(x - 9, y, width + 10, 20, mouseX, mouseY)) {
            setting.setCells(new boolean[11][11]);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}