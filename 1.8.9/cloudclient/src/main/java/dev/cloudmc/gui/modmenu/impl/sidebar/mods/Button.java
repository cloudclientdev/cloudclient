/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type.*;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.hud.ScrollHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;

import java.util.ArrayList;
import java.util.Set;

public class Button {

    private final ArrayList<Settings> settingsList = new ArrayList<>();

    private final Animate animButton = new Animate();
    private final Animate animPanel = new Animate();

    private final ScrollHelper scrollHelper = new ScrollHelper(0, 210, 35, 300);

    private Panel panel;
    private Mod mod;
    private int x, y, w, h;
    private boolean open;

    public Button(Mod mod, Panel panel, int x, int y) {
        this.mod = mod;
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.w = 120;
        this.h = 80;

        if (Cloud.INSTANCE.settingManager.getSettingsByMod(mod) != null) {
            int addY = 70;
            for (Setting setting : Cloud.INSTANCE.settingManager.getSettingsByMod(mod)) {
                switch (setting.getMode()) {
                    case "CheckBox":
                        CheckBox settingsCheckBox = new CheckBox(setting, this, addY);
                        settingsList.add(settingsCheckBox);
                        addY += settingsCheckBox.getH();
                        break;
                    case "Slider":
                        Slider settingsSlider = new Slider(setting, this, addY);
                        settingsList.add(settingsSlider);
                        addY += settingsSlider.getH();
                        break;
                    case "ModePicker":
                        ModePicker settingsModePicker = new ModePicker(setting, this, addY);
                        settingsList.add(settingsModePicker);
                        addY += settingsModePicker.getH();
                        break;
                    case "ColorPicker":
                        ColorPicker settingsColorPicker = new ColorPicker(setting, this, addY);
                        settingsList.add(settingsColorPicker);
                        addY += settingsColorPicker.getH();
                        break;
                    case "CellGrid":
                        CellGrid settingsCellGrid = new CellGrid(setting, this, addY);
                        settingsList.add(settingsCellGrid);
                        addY += settingsCellGrid.getH();
                        break;
                    case "TextBox":
                        TextBox settingTextBox = new TextBox(setting, this, addY);
                        settingsList.add(settingTextBox);
                        addY += settingTextBox.getH();
                        break;
                    case "Keybinding":
                        Keybinding settingKeybinding = new Keybinding(setting, this, addY);
                        settingsList.add(settingKeybinding);
                        addY += settingKeybinding.getH();
                        break;
                }
            }
        }

        animPanel.setEase(Easing.CUBIC_OUT).setMin(0).setMax(270).setSpeed(1000).setReversed(false);
        animButton.setMin(0).setMax(15).setSpeed(50);
    }

    /**
     * Renders the button and the setting background
     * If no button is open, render the background and the toggle button
     * If a button is open render the background panel for the settings
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderButton(int mouseX, int mouseY) {
        boolean roundedCorners = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        animButton.setReversed(!mod.isToggled()).setEase(
                mod.isToggled() ?
                        Easing.CUBIC_OUT :
                        Easing.CUBIC_IN
        ).update();
        scrollHelper.update();

        /*
        Renders the button
         */

        if (!getPanel().isAnyButtonOpen()) {
            Helper2D.drawRoundedRectangle(panel.getX() + 5 + x, panel.getY() + panel.getH() + 35 + y, w, h, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
            Helper2D.drawRoundedRectangle(panel.getX() + 5 + x, panel.getY() + panel.getH() + y + 90, w, 25, 2, Style.getColor(50).getRGB(), roundedCorners ? 2 : -1);

            Cloud.INSTANCE.fontHelper.size20.drawString(mod.getName(), panel.getX() + 10 + x, panel.getY() + panel.getH() + y + 97, color);

            Helper2D.drawRoundedRectangle(panel.getX() + 13 + x + 77, panel.getY() + panel.getH() + y + h + 14, 30, 15, 2, Style.getColor(50).getRGB(), roundedCorners ? 0 : -1);
            Helper2D.drawRoundedRectangle(
                    animButton.hasFinished() ?
                            mod.isToggled() ?
                                    panel.getX() + 13 + 15 + x + 77 :
                                    panel.getX() + 13 + x + 77 :
                            panel.getX() + 13 + animButton.getValueI() + x + 77,
                    panel.getY() + panel.getH() + y + h + 14, 15, 15, 2, Style.getColor(70).getRGB(), roundedCorners ? 0 : -1);

            Helper2D.drawPicture(panel.getX() + getX() + getW() / 2 - 12, panel.getY() + panel.getH() + getY() + 45, 35, 35, color, "icon/button/button/" + mod.getName() + ".png");
        }

        if (open) {
            animPanel.update();

            Helper2D.drawRoundedRectangle(panel.getX() + 5, panel.getY() + panel.getH() + 5 + 300 - animPanel.getValueI(), panel.getW() - 10, panel.getH(), 2, Style.getColor(80).getRGB(), roundedCorners ? 1 : -1);
            Helper2D.drawRectangle(panel.getX() + 5, panel.getY() + panel.getH() + 35 + 300 - animPanel.getValueI(), panel.getW() - 10, 235, Style.getColor(40).getRGB());

            boolean hovered = MathHelper.withinBox(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10, 20, 20, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10 + 300 - animPanel.getValueI(), 20, 20, 2, Style.getColor(hovered ? 70 : 50).getRGB(), roundedCorners ? 0 : -1);
            Helper2D.drawPicture(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10 + 300 - animPanel.getValueI(), 20, 20, color, "icon/cross.png");

            Cloud.INSTANCE.fontHelper.size30.drawString(mod.getName(), panel.getX() + 5 + 7, panel.getY() + panel.getH() + 5 + 8 + 300 - animPanel.getValueI(), color);
            Cloud.INSTANCE.fontHelper.size20.drawString(mod.getDescription(), panel.getX() + 20 + Cloud.INSTANCE.fontHelper.size30.getStringWidth(mod.getName()), panel.getY() + panel.getH() + 316 - animPanel.getValueI(), color);

            /*
            Renders the settings
             */


            GLHelper.startScissor(
                    panel.getX() + 5,
                    panel.getY() + panel.getH() + 65,
                    panel.getW() - 10,
                    235
            );
            for (Settings settings : settingsList) {
                settings.renderSetting(mouseX, mouseY);
            }
            GLHelper.endScissor();

            /*
            Makes the settings scrollable
             */

            if (settingsList.size() != 0) {
                if (MathHelper.withinBox(
                        panel.getX() + 5,
                        panel.getY() + panel.getH() + 35,
                        panel.getW() - 10,
                        260,
                        mouseX, mouseY)
                ) {
                    int height = 0;
                    for (Settings settings : settingsList) {
                        height += settings.getH();
                    }
                    scrollHelper.setHeight(height);
                    scrollHelper.updateScroll();

                    height = 0;
                    for (Settings settings : settingsList) {
                        float position = height;
                        position += scrollHelper.getCalculatedScroll() + 270 - animPanel.getValueI() + 70;
                        settings.setY((int) position);
                        height += settings.getH();
                    }
                }
            }
        }
    }

    /**
     * Checks if close button is pressed and removes the settings panel
     * Toggles a mod if it is pressed with the left mouse button
     * Enables the settings panel if a mod is pressed with the right mouse button
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(panel.getX(), panel.getY() + panel.getH() + 30, panel.getW(), 270, mouseX, mouseY)) {
            if (MathHelper.withinBox(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 40, 20, 20, mouseX, mouseY)) {
                panel.setAnyButtonOpen(false);
                open = false;
            } else if (MathHelper.withinBox(panel.getX() + 5 + x, panel.getY() + panel.getH() + 35 + y, w, h, mouseX, mouseY)) {
                if (!panel.isAnyButtonOpen() && !open) {
                    if (mouseButton == 0) {
                        mod.toggle();
                    } else if (mouseButton == 1) {
                        animPanel.reset();
                        getPanel().setAnyButtonOpen(true);
                        open = true;
                    }
                }
            }
        }

        if (open && mouseButton == 0) {
            for (Settings settings : settingsList) {
                settings.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Settings settings : settingsList) {
            settings.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Settings setting : settingsList) {
            setting.keyTyped(typedChar, keyCode);
        }
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public Mod getMod() {
        return mod;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}
