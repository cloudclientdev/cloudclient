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
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;

public class ModePicker extends Options {

    private final Animate animateSelect = new Animate();
    private int longestString;
    private boolean fontChanger = false;

    public ModePicker(Option option, Panel panel, int y) {
        super(option, panel, y);
        setOptionHeight(option.getOptions().length * 15 + 5);
        animateSelect.setEase(Easing.CUBIC_OUT).setMin(0).setMax(option.getOptions().length * 15 + 2).setReversed(false);
        if(option.getName().equalsIgnoreCase("font changer")){
            fontChanger = true;
        }
    }

    /**
     * Renders the ModePicker Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {
        boolean roundedCorners = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        animateSelect.setSpeed(getOptionHeight() * 2).update();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                option.getCurrentMode(),
                panel.getX() + panel.getW() - 20 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(option.getCurrentMode()),
                panel.getY() + panel.getH() + getY() + 9,
                color
        );

        if (isOpen()) {
            if (!animateSelect.hasFinished()) {
                GLHelper.startScissor(
                        panel.getX(),
                        panel.getY() + panel.getH() + getY() + 23,
                        panel.getW(),
                        option.getOptions().length * 15 + 2
                );
            }
            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - 30 - longestString,
                    panel.getY() + panel.getH() + getY() - option.getOptions().length * 15 + animateSelect.getValueI() + 20,
                    longestString + 10, option.getOptions().length * 15 + 2,
                    2, Style.getColor(50).getRGB(),
                    roundedCorners ? 0 : -1
            );

            int offset = 0;
            for (int i = 0; i < option.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        panel.getX() + panel.getW() - 30 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 + 25,
                        longestString + 10, 15, mouseX, mouseY
                );

                Cloud.INSTANCE.fontHelper.size20.drawString(
                        option.getOptions()[i],
                        panel.getX() + panel.getW() - 24 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 - option.getOptions().length * 15 + animateSelect.getValueI() + 25,
                        color
                );

                if (hovered) {
                    Helper2D.drawRoundedRectangle(
                            panel.getX() + panel.getW() - 30 - longestString,
                            panel.getY() + panel.getH() + getY() + offset * 15 - option.getOptions().length * 15 + animateSelect.getValueI() + 20,
                            longestString + 10, 17, 2, Style.getColor(50).getRGB(),
                            roundedCorners ? 0 : -1
                    );
                }
                if (longestString < Cloud.INSTANCE.fontHelper.size20.getStringWidth(option.getOptions()[i])) {
                    longestString = Cloud.INSTANCE.fontHelper.size20.getStringWidth(option.getOptions()[i]);
                }
                offset++;
            }
            if (!animateSelect.hasFinished()) {
                GLHelper.endScissor();
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
                panel.getX() + 15,
                panel.getY() + panel.getH() + getY() - 2,
                panel.getW() - 30, 20, mouseX, mouseY)
        ) {
            if (mouseButton == 0) {
                if (isOpen()) {
                    setH(25);
                    setOpen(false);
                } else {
                    animateSelect.reset();
                    setH(getOptionHeight() + 25);
                    setOpen(true);
                }
            }
        }
        if (isOpen()) {
            int offset = 0;
            for (int i = 0; i < option.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        panel.getX() + panel.getW() - 30 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 + 25,
                        longestString + 10, 15, mouseX, mouseY
                );

                if (hovered) {
                    option.setCurrentMode(option.getOptions()[i]);
                    if(fontChanger){
                        Cloud.INSTANCE.fontHelper.setFont(option.getCurrentMode());
                        Cloud.INSTANCE.fontHelper.init();
                    }
                }
                offset++;
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
