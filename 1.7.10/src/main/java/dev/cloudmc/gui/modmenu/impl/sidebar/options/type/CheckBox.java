/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

public class CheckBox extends Options {

    Animate animateCheckBox = new Animate();

    public CheckBox(Option option, Panel panel, int y) {
        super(option, panel, y);
    }

    /**
     * Renders the Checkbox Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {

        animateCheckBox
                .setEase(Easing.CUBIC_OUT)
                .setMin(0)
                .setMax(10)
                .setSpeed(100)
                .setReversed(!option.isCheckToggled())
                .setEase(option.isCheckToggled() ? Easing.CUBIC_IN : Easing.CUBIC_OUT)
                .update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 40,
                panel.getY() + panel.getH() + getY() + 2,
                20,
                20,
                2,
                option.isCheckToggled() ? ClientStyle.getBackgroundColor(80).getRGB() : ClientStyle.getBackgroundColor(50).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        Helper2D.drawPicture(
                animateCheckBox.hasFinished() ?
                        option.isCheckToggled() ?
                                panel.getX() + panel.getW() - 40
                                : panel.getX() + panel.getW() - 30
                        : panel.getX() + panel.getW() - 30 - animateCheckBox.getValueI(),
                animateCheckBox.hasFinished() ?
                        option.isCheckToggled() ?
                                panel.getY() + panel.getH() + 2 + getY()
                                : panel.getY() + panel.getH() + 12 + getY()
                        : panel.getY() + panel.getH() + 12 + getY() - animateCheckBox.getValueI(),
                animateCheckBox.hasFinished() ?
                        option.isCheckToggled() ?
                                20 : 0 : animateCheckBox.getValueI() * 2, animateCheckBox.hasFinished() ?
                        option.isCheckToggled()
                                ? 20
                                : 0
                        : animateCheckBox.getValueI() * 2,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(),
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
                    panel.getX() + panel.getW() - 40,
                    panel.getY() + panel.getH() + 2 + getY(),
                    20,
                    20,
                    mouseX,
                    mouseY)
            ) {
                option.setCheckToggled(!option.isCheckToggled());
            }
        }
    }
}
