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

public class Slider extends Options {

    private boolean drag;

    public Slider(Option option, Panel panel, int y) {
        super(option, panel, y);
    }

    private final int sliderWidth = 150;

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */
    
    @Override
    public void renderOption(int mouseX, int mouseY) {

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                String.valueOf(MathHelper.round(option.getCurrentNumber(), 1)),
                panel.getX() + panel.getW() - 175 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(String.valueOf(MathHelper.round(option.getCurrentNumber(), 1))),
                panel.getY() + panel.getH() + getY() + 9,
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - sliderWidth - 20,
                panel.getY() + panel.getH() + getY() + 10,
                150, 5, 2, ClientStyle.getBackgroundColor(50).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - sliderWidth - 20 +
                        (int) (option.getCurrentNumber() * 150 / option.getMaxNumber() - 3),
                panel.getY() + panel.getH() + getY() + 5,
                7, 16, 1, ClientStyle.getBackgroundColor(80).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        if (drag) {
            option.setCurrentNumber(
                    (mouseX - (panel.getX() + panel.getW() - sliderWidth - 20))
                            * (option.getMaxNumber() / sliderWidth)
            );
            if (option.getCurrentNumber() < 0) {
                option.setCurrentNumber(0);
            }
            if (option.getCurrentNumber() > option.getMaxNumber()) {
                option.setCurrentNumber(option.getMaxNumber());
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
                panel.getX() + panel.getW() - 170,
                panel.getY() + panel.getH() + getY() + 5,
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
