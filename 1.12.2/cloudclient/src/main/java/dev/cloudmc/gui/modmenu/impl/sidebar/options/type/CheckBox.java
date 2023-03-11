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
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import dev.cloudmc.helpers.render.Helper2D;

public class CheckBox extends Options {

    private final Animate animCheckBox = new Animate();

    public CheckBox(Option option, Panel panel, int y) {
        super(option, panel, y);
        animCheckBox.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(10).setSpeed(100);
    }

    /**
     * Renders the Checkbox Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {
        boolean roundedCorners = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        animCheckBox.setReversed(!option.isCheckToggled()).update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 40,
                panel.getY() + panel.getH() + getY() + 2,
                20, 20, 2,
                Style.getColor(option.isCheckToggled() ? 80 : 50).getRGB(),
                roundedCorners ? 0 : -1
        );

        Helper2D.drawPicture(
                panel.getX() + panel.getW() - 30 - animCheckBox.getValueI(),
                panel.getY() + panel.getH() + 12 + getY() - animCheckBox.getValueI(),
                animCheckBox.getValueI() * 2,
                animCheckBox.getValueI() * 2,
                color,
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
                    20, 20, mouseX, mouseY)
            ) {
                option.setCheckToggled(!option.isCheckToggled());
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
