/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type.*;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.animation.Animate;
import dev.cloudmc.helpers.animation.Easing;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Button {

    private final ArrayList<Settings> settingsList;

    Animate animateButton = new Animate();
    Animate animatePanel = new Animate();

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

        settingsList = new ArrayList<>();
        if (Cloud.INSTANCE.settingManager.getSettingsByMod(mod) != null) {
            int addY = 40;

            if (mod.getOptionalKey() != Keyboard.KEY_NONE) {
                Settings settingsKeybinding = new Keybinding(new Setting("Keybinding"), this, addY);
                settingsList.add(settingsKeybinding);
                addY += settingsKeybinding.getH();
            }

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
                }
            }
        }
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

        animateButton
                .setMin(0)
                .setMax(15)
                .setSpeed(50)
                .setReversed(!mod.isToggled())
                .setEase(mod.isToggled() ? Easing.CUBIC_OUT : Easing.CUBIC_IN)
                .update();

        /*
        Renders the button
         */

        if (!getPanel().isAnyButtonOpen()) {
            Helper2D.drawRoundedRectangle(panel.getX() + 5 + x, panel.getY() + panel.getH() + 5 + y, w, h, 2, ClientStyle.getBackgroundColor(40).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
            Helper2D.drawRoundedRectangle(panel.getX() + 5 + x, panel.getY() + panel.getH() + y + 60, w, 25, 2, ClientStyle.getBackgroundColor(50).getRGB(), ClientStyle.isRoundedCorners() ? 2 : -1);
            Cloud.INSTANCE.fontHelper.size20.drawString(mod.getName(), panel.getX() + 10 + x, panel.getY() + panel.getH() + y + 67, ClientStyle.getColor().getRGB());
            Helper2D.drawRoundedRectangle(panel.getX() + 13 + x + 77, panel.getY() + panel.getH() + y + h - 16, 30, 15, 2, ClientStyle.getBackgroundColor(50).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
            Helper2D.drawRoundedRectangle(animateButton.hasFinished() ? mod.isToggled() ? panel.getX() + 13 + 15 + x + 77 : panel.getX() + 13 + x + 77 : panel.getX() + 13 + animateButton.getValueI() + x + 77, panel.getY() + panel.getH() + y + h - 16, 15, 15, 2, ClientStyle.getBackgroundColor(70).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
            Helper2D.drawPicture(panel.getX() + getX() + getW() / 2 - 12, panel.getY() + panel.getH() + getY() + 15, 35, 35, ClientStyle.getColor().getRGB(), "icon/button/button/" + mod.getName() + ".png");
        }

        if (open) {
            animatePanel
                    .setEase(Easing.CUBIC_OUT)
                    .setMin(0).setMax(300)
                    .setSpeed(1000)
                    .setReversed(false)
                    .update();

            Helper2D.drawRoundedRectangle(panel.getX() + 5, panel.getY() + panel.getH() + 5 + 300 - animatePanel.getValueI(), panel.getW() - 10, panel.getH(), 2, ClientStyle.getBackgroundColor(80).getRGB(), ClientStyle.isRoundedCorners() ? 1 : -1);
            Helper2D.drawRectangle(panel.getX() + 5, panel.getY() + panel.getH() + 35 + 300 - animatePanel.getValueI(), panel.getW() - 10, 265, ClientStyle.getBackgroundColor(40).getRGB());
            Helper2D.drawRoundedRectangle(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10 + 300 - animatePanel.getValueI(), 20, 20, 2, MathHelper.withinBox(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10, 20, 20, mouseX, mouseY) ? ClientStyle.getBackgroundColor(70).getRGB() : ClientStyle.getBackgroundColor(50).getRGB(), ClientStyle.isRoundedCorners() ? 0 : -1);
            Helper2D.drawPicture(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10 + 300 - animatePanel.getValueI(), 20, 20, ClientStyle.getColor().getRGB(), "icon/cross.png");
            Cloud.INSTANCE.fontHelper.size30.drawString(mod.getName(), panel.getX() + 5 + 7, panel.getY() + panel.getH() + 5 + 8 + 300 - animatePanel.getValueI(), ClientStyle.getColor().getRGB());
            Cloud.INSTANCE.fontHelper.size20.drawString(mod.getDescription(), panel.getX() + 20 + Cloud.INSTANCE.fontHelper.size30.getStringWidth(mod.getName()), panel.getY() + panel.getH() + 316 - animatePanel.getValueI(), ClientStyle.getColor().getRGB());

            /*
            Updates settings if they change their height
             */

            for (int i = 0; i < settingsList.size(); i++) {
                Settings currentSettings = settingsList.get(i);
                if (currentSettings.isOpen() && !currentSettings.isUpdated()) {
                    currentSettings.setUpdated(true);
                    for (int j = i + 1; j < settingsList.size(); j++) {
                        Settings settings = settingsList.get(j);
                        settings.setY(settings.getY() + currentSettings.getSettingHeight());
                    }
                }
                if (!currentSettings.isOpen() && currentSettings.isUpdated()) {
                    currentSettings.setUpdated(false);
                    for (int j = i + 1; j < settingsList.size(); j++) {
                        Settings settings = settingsList.get(j);
                        settings.setY(settings.getY() - currentSettings.getSettingHeight());
                    }
                }
            }

            /*
            Renders the settings
             */

            Helper2D.startScissor(panel.getX() + 5, panel.getY() + panel.getH() + 35 + 300 - animatePanel.getValueI(), panel.getW() - 10, animatePanel.getValueI() - 35);
            for (Settings settings : settingsList) {
                settings.renderSetting(mouseX, mouseY);
            }
            Helper2D.endScissor();

            /*
            Makes the settings scrollable
             */

            if(settingsList.size() != 0) {
                int settingFirstY = settingsList.get(0).getY();
                int settingLastY = settingsList.get(settingsList.size() - 1).getY();
                int settingLastH = settingsList.get(settingsList.size() - 1).getH();

                if (MathHelper.withinBox(panel.getX() + 5, panel.getY() + panel.getH() + 35, panel.getW() - 10, 260, mouseX, mouseY)) {
                    int scroll = Mouse.getDWheel();
                    for (Settings setting : settingsList) {
                        if (scroll > 0) {
                            if (settingFirstY < 40) {
                                setting.setY(setting.getY() + 13);
                            }
                        }
                        else if (scroll < 0) {
                            if ((settingLastY + settingLastH) > 300) {
                                setting.setY(setting.getY() - 13);
                            }
                        }
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
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(panel.getX() + panel.getW() - 30, panel.getY() + panel.getH() + 10, 20, 20, mouseX, mouseY)) {
            panel.setAnyButtonOpen(false);
            open = false;
        }
        else {
            if (MathHelper.withinBox(panel.getX() + 5 + x, panel.getY() + panel.getH() + 5 + y, w, h, mouseX, mouseY)) {
                if (!panel.isAnyButtonOpen() && !open) {
                    if (mouseButton == 0) {
                        mod.toggle();
                    }
                    else if (mouseButton == 1) {
                        animatePanel.reset();
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
