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
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;

public class Slider extends Settings {

    private boolean drag;

    public Slider(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    private final int sliderWidth = 150;

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                ClientStyle.getColor().getRGB()
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1)),
                button.getPanel().getX() + button.getPanel().getW() - 175 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1))),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 9,
                ClientStyle.getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 10,
                150, 5, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                ClientStyle.isRoundedCorners() ? 0 : -1
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20 +
                        (int) (setting.getCurrentNumber() * 150 / setting.getMaxNumber() - 3),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                7, 16, 1, ClientStyle.getBackgroundColor(80).getRGB(),
                ClientStyle.isRoundedCorners() ? 0 : -1
        );

        if (drag) {
            setting.setCurrentNumber(
                    (mouseX - (button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20))
                            * (setting.getMaxNumber() / sliderWidth)
            );
            if (setting.getCurrentNumber() < 0) {
                setting.setCurrentNumber(0);
            }
            if (setting.getCurrentNumber() > setting.getMaxNumber()) {
                setting.setCurrentNumber(setting.getMaxNumber());
            }
        }
    }

    /**
     * Changes the drag variable if the slider is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                button.getPanel().getX() + button.getPanel().getW() - 170,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                150, 16, mouseX, mouseY)
        ) {
            drag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
    }
}
