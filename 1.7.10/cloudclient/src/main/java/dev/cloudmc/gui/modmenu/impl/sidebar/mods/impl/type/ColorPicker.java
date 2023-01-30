/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.ColorHelper;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.IntBuffer;

public class ColorPicker extends Settings {

    private boolean dragSide;
    private boolean dragMain;
    private float sideSlider;
    private float[] mainSlider;
    private boolean open;

    public ColorPicker(Setting setting, Button button, int y) {
        super(setting, button, y);
        sideSlider = setting.getSideSlider();
        mainSlider = setting.getMainSlider();
        open = false;
    }

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        if(open) {
            setH(100);
        } else {
            setH(25);
        }

        int getXW = button.getPanel().getX() + button.getPanel().getW();
        int getYH = button.getPanel().getY() + button.getPanel().getH() + getY();
        boolean rounded = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();

        if(open) {
            Helper2D.drawRoundedRectangle(getXW - 40, getYH + 25, 20, 70, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);
            Helper2D.drawRoundedRectangle(getXW - 193, getYH + 25, 150, 70, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);

            Helper2D.drawPicture(getXW - 38, getYH + 27, 16, 66, 0, "icon/hue.png");
            if (dragSide) {
                sideSlider = mouseY - (getYH + 25);
                float sliderHeight = 65;
                if (sideSlider < 0) {
                    sideSlider = 0;
                } else if (sideSlider > sliderHeight) {
                    sideSlider = sliderHeight;
                }
                setting.setSideSlider(sideSlider);
            }
            int sideSliderColor = ColorHelper.getColorAtPixel(getXW - 35, getYH + 28 + sideSlider);
            Helper2D.drawRoundedRectangle(getXW - 40, (int) (getYH + 25 + sideSlider), 20, 5, 2, -1, rounded ? 0 : -1);
            Helper2D.drawHorizontalGradientRectangle(getXW - 191, getYH + 27, 146, 66, -1, sideSliderColor);
            Helper2D.drawGradientRectangle(getXW - 191, getYH + 27, 146, 66, 0x00000000, 0xff000000);
            if (dragMain) {
                mainSlider[0] = mouseX - (getXW - 193);
                mainSlider[1] = mouseY - (getYH + 25);
                float sliderWidth = 145;
                float sliderHeight = 65;
                if (mainSlider[0] < 0) {
                    mainSlider[0] = 0;
                } else if (mainSlider[0] > sliderWidth) {
                    mainSlider[0] = sliderWidth;
                }
                if (mainSlider[1] < 0) {
                    mainSlider[1] = 0;
                } else if (mainSlider[1] > sliderHeight) {
                    mainSlider[1] = sliderHeight;
                }
                setting.setMainSlider(mainSlider);
            }
            int preMainColor = ColorHelper.getColorAtPixel(getXW - 191 + mainSlider[0], getYH + 28 + mainSlider[1]);
            Color mainColor = new Color(
                    ColorHelper.hexToRgb(preMainColor).getRed(),
                    ColorHelper.hexToRgb(preMainColor).getGreen(),
                    ColorHelper.hexToRgb(preMainColor).getBlue(),
                    255
            );
            Helper2D.drawRoundedRectangle((int) (getXW - 193 + mainSlider[0]), (int) (getYH + 25 + mainSlider[1]), 5, 5, 3, -1, 0);

            setting.setColor(mainColor);
        }

        Helper2D.drawRoundedRectangle(getXW - 40, getYH + 2, 20, 20, 2, ClientStyle.getBackgroundColor(50).getRGB(), rounded ? 0 : -1);
        Helper2D.drawRectangle(getXW - 38, getYH + 4, 16, 16, setting.getColor().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int getXW = button.getPanel().getX() + button.getPanel().getW();
        int getYH = button.getPanel().getY() + button.getPanel().getH() + getY();

        if(MathHelper.withinBox(button.getPanel().getX(), getYH, button.getPanel().getW(), 25, mouseX, mouseY)) {
            open = !open;
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
}