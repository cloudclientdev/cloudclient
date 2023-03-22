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
import dev.cloudmc.helpers.ColorHelper;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import dev.cloudmc.helpers.hud.PositionHelper;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;

import java.awt.*;

public class ColorPicker extends Options {

    private final PositionHelper sidePosHelper = new PositionHelper(75);
    private final PositionHelper mainPosHelperX = new PositionHelper(100);
    private final PositionHelper mainPosHelperY = new PositionHelper(150);
    private final Animate animate = new Animate();

    private boolean dragSide;
    private boolean dragMain;
    private boolean open;

    public ColorPicker(Option option, Panel panel, int y) {
        super(option, panel, y);
        open = false;
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setMax(70).setSpeed(200);
    }

    @Override
    public void renderOption(int mouseX, int mouseY) {
        int getXW = panel.getX() + panel.getW();
        int getYH = panel.getY() + panel.getH() + getY();
        boolean rounded = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        Cloud.INSTANCE.fontHelper.size30.drawString(option.getName(), panel.getX() + 20, getYH + 6, color);
        animate.update();

        if (open) {
            if (!animate.hasFinished()) {
                GLHelper.startScissor(getXW - 193, getYH + 25, 180, 70);
            }

            int offset = animate.getValueI() - 70;

            Helper2D.drawRoundedRectangle(getXW - 40, getYH + 25 + offset, 20, 70, 2, Style.getColor(50).getRGB(), rounded ? 0 : -1);
            Helper2D.drawRoundedRectangle(getXW - 193, getYH + 25 + offset, 150, 70, 2, Style.getColor(50).getRGB(), rounded ? 0 : -1);

            Helper2D.drawPicture(getXW - 38, getYH + 27 + offset, 16, 66, 0, "icon/hue.png");

            sidePosHelper.pre(option.getSideSlider());

            if (dragSide) {
                option.setSideSlider(mouseY - (getYH + 25));
                float sliderHeight = 65;
                if (option.getSideSlider() < 0) {
                    option.setSideSlider(0);
                } else if (option.getSideSlider() > sliderHeight) {
                    option.setSideSlider(sliderHeight);
                }
            }

            sidePosHelper.post(option.getSideSlider());
            sidePosHelper.update();

            Color sideColor = ColorHelper.getColorAtPixel(getXW - 35, getYH + 28 + option.getSideSlider() + offset);
            if (animate.hasFinished() && getYH + 28 + option.getSideSlider() + offset < (panel.getY() + panel.getH() + 300))
                option.setSideColor(sideColor);

            float sidePosY = getYH + 25 + option.getSideSlider() + offset;
            Helper2D.drawRoundedRectangle(getXW - 40, (int) (sidePosHelper.isDirection() ?
                    sidePosY - sidePosHelper.getDifference() - sidePosHelper.getValue() :
                    sidePosY - sidePosHelper.getDifference() + sidePosHelper.getValue()
            ), 20, 5, 2, -1, rounded ? 0 : -1);
            Helper2D.drawHorizontalGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, -1, option.getSideColor().getRGB());
            Helper2D.drawGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, 0x00000000, 0xff000000);

            mainPosHelperX.pre(option.getMainSlider()[0]);
            mainPosHelperY.pre(option.getMainSlider()[1]);

            if (dragMain) {
                option.getMainSlider()[0] = mouseX - (getXW - 193);
                option.getMainSlider()[1] = mouseY - (getYH + 25);
                float sliderWidth = 145;
                float sliderHeight = 65;
                if (option.getMainSlider()[0] < 0) {
                    option.getMainSlider()[0] = 0;
                } else if (option.getMainSlider()[0] > sliderWidth) {
                    option.getMainSlider()[0] = sliderWidth;
                }
                if (option.getMainSlider()[1] < 0) {
                    option.getMainSlider()[1] = 0;
                } else if (option.getMainSlider()[1] > sliderHeight) {
                    option.getMainSlider()[1] = sliderHeight;
                }
            }

            mainPosHelperX.post(option.getMainSlider()[0]);
            mainPosHelperY.post(option.getMainSlider()[1]);
            mainPosHelperX.update();
            mainPosHelperY.update();

            Color mainColor = ColorHelper.getColorAtPixel(getXW - 191 + option.getMainSlider()[0], getYH + 28 + option.getMainSlider()[1] + offset);
            if (animate.hasFinished() && getYH + 28 + option.getMainSlider()[1] + offset < (panel.getY() + panel.getH() + 300))
                option.setColor(mainColor);
            float mainPosX = getXW - 193 + option.getMainSlider()[0];
            float mainPosY = getYH + 25 + option.getMainSlider()[1] + offset;
            Helper2D.drawRoundedRectangle(
                    (int) (mainPosHelperX.isDirection() ?
                            mainPosX - mainPosHelperX.getDifference() - mainPosHelperX.getValue() :
                            mainPosX - mainPosHelperX.getDifference() + mainPosHelperX.getValue()
                    ),
                    (int) (mainPosHelperY.isDirection() ?
                            mainPosY - mainPosHelperY.getDifference() - mainPosHelperY.getValue() :
                            mainPosY - mainPosHelperY.getDifference() + mainPosHelperY.getValue()
                    ), 5, 5, 3, -1, 0
            );

            if (!animate.hasFinished()) {
                GLHelper.endScissor();
            }
        }

        String rgbText = "R" + option.getColor().getRed() + " G" + option.getColor().getGreen() + " B" + option.getColor().getBlue();
        Cloud.INSTANCE.fontHelper.size20.drawString(rgbText, getXW - 45 - Cloud.INSTANCE.fontHelper.size20.getStringWidth(rgbText), getYH + 9, -1);
        Helper2D.drawRoundedRectangle(getXW - 40, getYH + 2, 20, 20, 2, Style.getColor(50).getRGB(), rounded ? 0 : -1);
        Helper2D.drawRectangle(getXW - 38, getYH + 4, 16, 16, option.getColor().getRGB());

        setH(open ? 100 : 25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int getXW = panel.getX() + panel.getW();
        int getYH = panel.getY() + panel.getH() + getY();

        if (MathHelper.withinBox(panel.getX(), getYH, panel.getW(), 25, mouseX, mouseY)) {
            open = !open;
            animate.reset();
        } else if (MathHelper.withinBox(getXW - 40, getYH + 25, 20, 70, mouseX, mouseY)) {
            dragSide = true;
        } else if (MathHelper.withinBox(getXW - 193, getYH + 25, 150, 70, mouseX, mouseY)) {
            dragMain = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragSide = false;
        dragMain = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}