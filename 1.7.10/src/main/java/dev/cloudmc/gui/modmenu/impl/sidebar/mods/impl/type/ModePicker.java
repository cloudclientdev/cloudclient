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
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

public class ModePicker extends Settings {

    Animate animateSelect = new Animate();
    private int longestString;

    public ModePicker(Setting setting, Button button, int y) {
        super(setting, button, y);
        setSettingHeight(setting.getOptions().length * 15 + 5);
    }

    /**
     * Renders the ModePicker Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {

        animateSelect
                .setEase(Easing.CUBIC_OUT)
                .setMin(0)
                .setMax(setting.getOptions().length * 15 + 2)
                .setSpeed(200)
                .setReversed(false)
                .update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                ClientStyle.getColor().getRGB()
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                setting.getCurrentMode(),
                button.getPanel().getX() + button.getPanel().getW() - 20 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(setting.getCurrentMode()),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 9,
                ClientStyle.getColor().getRGB()
        );

        if (isOpen()) {
            if (!animateSelect.hasFinished()) {
                Helper2D.startScissor(
                        button.getPanel().getX(),
                        button.getPanel().getY() + button.getPanel().getH() + getY() + 23,
                        button.getPanel().getW(),
                        setting.getOptions().length * 15 + 2
                );
            }
            Helper2D.drawRoundedRectangle(
                    button.getPanel().getX() + button.getPanel().getW() - 30 - longestString,
                    button.getPanel().getY() + button.getPanel().getH() + getY() - setting.getOptions().length * 15 + animateSelect.getValueI() + 20,
                    longestString + 10, setting.getOptions().length * 15 + 2,
                    2, ClientStyle.getBackgroundColor(50).getRGB(),
                    ClientStyle.isRoundedCorners() ? 0 : -1
            );

            int offset = 0;
            for (int i = 0; i < setting.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        button.getPanel().getX() + button.getPanel().getW() - 30 - longestString,
                        button.getPanel().getY() + button.getPanel().getH() + getY() + offset * 15 + 25,
                        longestString + 10, 15, mouseX, mouseY
                );

                Cloud.INSTANCE.fontHelper.size20.drawString(
                        setting.getOptions()[i],
                        button.getPanel().getX() + button.getPanel().getW() - 24 - longestString,
                        button.getPanel().getY() + button.getPanel().getH() + getY() + offset * 15 - setting.getOptions().length * 15 + animateSelect.getValueI() + 25,
                        ClientStyle.getColor().getRGB()
                );

                if (hovered) {
                    Helper2D.drawRoundedRectangle(
                            button.getPanel().getX() + button.getPanel().getW() - 30 - longestString,
                            button.getPanel().getY() + button.getPanel().getH() + getY() + offset * 15 - setting.getOptions().length * 15 + animateSelect.getValueI() + 20,
                            longestString + 10, 17, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                            ClientStyle.isRoundedCorners() ? 0 : -1
                    );
                }
                if (longestString < Cloud.INSTANCE.fontHelper.size20.getStringWidth(setting.getOptions()[i])) {
                    longestString = Cloud.INSTANCE.fontHelper.size20.getStringWidth(setting.getOptions()[i]);
                }
                offset++;
            }
            if (!animateSelect.hasFinished()) {
                Helper2D.endScissor();
            }
        }
    }

    /**
     * Opens and closes the setting
     * Loops through every Mode in the Setting and checks if it is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                button.getPanel().getX() + 15,
                button.getPanel().getY() + button.getPanel().getH() + getY() - 2,
                button.getPanel().getW() - 30, 20, mouseX, mouseY)
        ) {
            if (mouseButton == 0) {
                if (isOpen()) {
                    setH(25);
                    setOpen(false);
                }
                else {
                    animateSelect.reset();
                    setH(getSettingHeight() + 25);
                    setOpen(true);
                }
            }
        }
        if (isOpen()) {
            int offset = 0;
            for (int i = 0; i < setting.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        button.getPanel().getX() + button.getPanel().getW() - 46 - longestString / 2,
                        button.getPanel().getY() + button.getPanel().getH() + getY() + offset * 15 + 24,
                        longestString + 10, 15, mouseX, mouseY
                );

                if (hovered) {
                    setting.setCurrentMode(setting.getOptions()[i]);
                }
                offset++;
            }
        }
    }
}
