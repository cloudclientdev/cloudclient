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

public class CheckBox extends Settings {

    Animate animateCheckBox = new Animate();

    public CheckBox(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    /**
     * Renders the Checkbox Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {

        animateCheckBox
                .setEase(Easing.CUBIC_OUT)
                .setMin(0)
                .setMax(10)
                .setSpeed(100)
                .setReversed(!setting.isCheckToggled())
                .setEase(setting.isCheckToggled() ? Easing.CUBIC_IN : Easing.CUBIC_OUT)
                .update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                ClientStyle.getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 40,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 2,
                20,
                20,
                2,
                setting.isCheckToggled() ? ClientStyle.getBackgroundColor(80).getRGB() : ClientStyle.getBackgroundColor(50).getRGB(),
                ClientStyle.isRoundedCorners() ? 0 : -1
        );

        Helper2D.drawPicture(
                animateCheckBox.hasFinished() ?
                        setting.isCheckToggled() ?
                                button.getPanel().getX() + button.getPanel().getW() - 40
                                : button.getPanel().getX() + button.getPanel().getW() - 30
                        : button.getPanel().getX() + button.getPanel().getW() - 30 - animateCheckBox.getValueI(),
                animateCheckBox.hasFinished() ?
                        setting.isCheckToggled() ?
                                button.getPanel().getY() + button.getPanel().getH() + 2 + getY()
                                : button.getPanel().getY() + button.getPanel().getH() + 12 + getY()
                        : button.getPanel().getY() + button.getPanel().getH() + 12 + getY() - animateCheckBox.getValueI(),
                animateCheckBox.hasFinished() ?
                        setting.isCheckToggled() ?
                                20 : 0 : animateCheckBox.getValueI() * 2, animateCheckBox.hasFinished() ?
                        setting.isCheckToggled()
                                ? 20
                                : 0
                        : animateCheckBox.getValueI() * 2,
                ClientStyle.getColor().getRGB(),
                "icon/check.png"
        );
    }

    /**
     * Toggles the state of the setting if it is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0){
            if (MathHelper.withinBox(
                    button.getPanel().getX() + button.getPanel().getW() - 40,
                    button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                    20,
                    20,
                    mouseX,
                    mouseY)
            ) {
                setting.setCheckToggled(!setting.isCheckToggled());
            }
        }
    }
}
