/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.type.*;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Panel {

    private final ArrayList<Button> buttonList = new ArrayList<>();
    private final ArrayList<Options> optionsList = new ArrayList<>();

    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;
    private boolean anyButtonOpen;
    private int selected = 0;

    public Panel() {
        ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc);
        this.x = sr.getScaledWidth() / 2 - 250;
        this.y = sr.getScaledHeight() / 2 - 150;
        this.w = 500;
        this.h = 30;
        this.offsetX = 0;
        this.offsetY = 0;
        this.dragging = false;

        int addButtonX = 0;
        int addButtonY = 0;
        int buttonCounter = 0;
        for (Mod mod : Cloud.INSTANCE.modManager.getMods()) {
            Button button = new Button(mod, this, addButtonX, addButtonY);
            buttonList.add(button);
            buttonCounter++;
            addButtonX += button.getW() + 3;
            if (buttonCounter % 4 == 0) {
                addButtonX = 0;
                addButtonY += button.getH() + 3;
            }
        }

        int addOptionY = 10;

        for(Option option : Cloud.INSTANCE.optionManager.getOptions()){
            switch (option.getMode()){
                case "CheckBox":
                    CheckBox checkBox = new CheckBox(option, this, addOptionY);
                    optionsList.add(checkBox);
                    addOptionY += checkBox.getH();
                    break;
                case "Slider":
                    Slider slider = new Slider(option, this, addOptionY);
                    optionsList.add(slider);
                    addOptionY += slider.getH();
                    break;
                case "ModePicker":
                    ModePicker modePicker = new ModePicker(option, this, addOptionY);
                    optionsList.add(modePicker);
                    addOptionY += modePicker.getH();
                    break;
                case "ColorPicker":
                    ColorPicker colorPicker = new ColorPicker(option, this, addOptionY);
                    optionsList.add(colorPicker);
                    addOptionY += colorPicker.getH();
                    break;
                case "CellGrid":
                    CellGrid cellGrid = new CellGrid(option, this, addOptionY);
                    optionsList.add(cellGrid);
                    addOptionY += cellGrid.getH();
                    break;
                case "Keybinding":
                    Keybinding keybinding = new Keybinding(option, this, addOptionY);
                    optionsList.add(keybinding);
                    addOptionY += keybinding.getH();
                    break;
            }
        }
    }

    /**
     * Renders the panel background, the sidebar and all the buttons
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderPanel(int mouseX, int mouseY) {
        Helper2D.drawRoundedRectangle(x, y, w, h, 2, ClientStyle.getBackgroundColor(70).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 1 : -1);
        Helper2D.drawRoundedRectangle(x, y + 30, w, h + 270, 2, ClientStyle.getBackgroundColor(50).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 2 : -1);
        Helper2D.drawRoundedRectangle(x + w - 25, y + 5, 20, 20, 2, MathHelper.withinBox(x + w - 25, y + 5, 20, 20, mouseX, mouseY) ? ClientStyle.getBackgroundColor(70).getRGB() : ClientStyle.getBackgroundColor(50).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1);
        Helper2D.drawPicture(x + w - 25, y + 5, 20, 20, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(), "icon/cross.png");
        Helper2D.drawPicture(x + 2, y - 1, 35, 35, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(), "cloudlogo.png");
        Cloud.INSTANCE.fontHelper.size40.drawString(Cloud.modName, x + 37, y + 6, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB());

        /*
        Buttons are only drawn if the Sidebar is on the mods tab
         */

        if (selected == 0) {
            Helper2D.startScissor(x, y + 30, w, h + 270);
            for (Button button : buttonList) {
                button.renderButton(mouseX, mouseY);
            }
            Helper2D.endScissor();

            int buttonFirstY = buttonList.get(0).getY();
            int buttonLastY = buttonList.get(buttonList.size() - 1).getY();
            int buttonLastH = buttonList.get(buttonList.size() - 1).getH();

            int scroll = Mouse.getDWheel();
            if (MathHelper.withinBox(x, y + 30, w, h + 270, mouseX, mouseY)) {
                for (Button button : buttonList) {
                    if(scroll > 0){
                        if(buttonFirstY < 0) {
                            button.setY(button.getY() + 13);
                        }
                    }
                    else if(scroll < 0){
                        if((buttonLastY + buttonLastH) > 300){
                            button.setY(button.getY() - 13);
                        }
                    }
                }
            }
        }
        else {
            Helper2D.startScissor(x, y + 30, w, h + 270);
            for(Options option : optionsList){
                option.renderOption(mouseX, mouseY);
            }
            Helper2D.endScissor();

            int optionFirstY = optionsList.get(0).getY();
            int optionLastY = optionsList.get(optionsList.size() - 1).getY();
            int optionLastH = optionsList.get(optionsList.size() - 1).getH();

            int scroll = Mouse.getDWheel();
            if(MathHelper.withinBox(x, y + 30, w, h + 270, mouseX, mouseY)){
                for(Options option : optionsList){
                    if(scroll > 0){
                        if(optionFirstY < 10) {
                            option.setY(option.getY() + 13);
                        }
                    }
                    else if(scroll < 0){
                        if((optionLastY + optionLastH) > 300){
                            option.setY(option.getY() - 13);
                        }
                    }
                }
            }

            for (int i = 0; i < optionsList.size(); i++) {
                Options currentOptions = optionsList.get(i);
                if (currentOptions.isOpen() && !currentOptions.isUpdated()) {
                    currentOptions.setUpdated(true);
                    for (int j = i + 1; j < optionsList.size(); j++) {
                        Options settings = optionsList.get(j);
                        settings.setY(settings.getY() + currentOptions.getOptionHeight());
                    }
                }
                if (!currentOptions.isOpen() && currentOptions.isUpdated()) {
                    currentOptions.setUpdated(false);
                    for (int j = i + 1; j < optionsList.size(); j++) {
                        Options options = optionsList.get(j);
                        options.setY(options.getY() - currentOptions.getOptionHeight());
                    }
                }
            }
        }

        /*
        Draws the sidebar with the mods and settings tab
         */

        Helper2D.drawRoundedRectangle(x - 50, y, 40, h + 300, 2, ClientStyle.getBackgroundColor(50).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1);
        Helper2D.drawRoundedRectangle(x - 50, y + selected * 40, 40, 40, 2, ClientStyle.getBackgroundColor(50).getRGB(), Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1);

        String[] buttons = {"Mods", "Settings"};
        for (int i = 0; i < buttons.length; i++) {
            Cloud.INSTANCE.fontHelper.size15.drawString(buttons[i], x - 30 - Cloud.INSTANCE.fontHelper.size15.getStringWidth(buttons[i]) / 2, y + 30 + i * 40, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB());
            Helper2D.drawPicture(x - 40, y + 5 + i * 40, 20, 20, Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB(), "icon/button/sidebar/icon" + i + ".png");
        }
    }

    /**
     * Closes the modmenu and opens the editor if the close button is pressed
     * Sets the "selected" variable to whatever tab is pressed in the sidebar
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(x + w - 25, y + 5, 20, 20, mouseX, mouseY)) {
            Cloud.INSTANCE.mc.displayGuiScreen(Cloud.INSTANCE.hudEditor);
        }

        if(selected == 0){
            for (Button button : buttonList) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        else {
            for(Options option : optionsList){
                option.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        String[] buttons = {"Mods", "Settings"};
        for (int i = 0; i < buttons.length; i++) {
            if (MathHelper.withinBox(x - 50, y + i * 40, 40, 40, mouseX, mouseY)) {
                selected = i;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(selected == 0){
            for (Button button : buttonList) {
                button.mouseReleased(mouseX, mouseY, state);
            }
        }
        else {
            for(Options option : optionsList){
                option.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    public void initGui(){
        ScaledResolution sr = new ScaledResolution(Cloud.INSTANCE.mc);
        setX(sr.getScaledWidth() / 2 - 250);
    }

    /**
     * Updates the position of the panel
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void updatePosition(int mouseX, int mouseY) {
        if (isDragging()) {
            setX(mouseX - offsetX);
            setY(mouseY - offsetY);
        }
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

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isAnyButtonOpen() {
        return anyButtonOpen;
    }

    public void setAnyButtonOpen(boolean anyButtonOpen) {
        this.anyButtonOpen = anyButtonOpen;
    }
}
