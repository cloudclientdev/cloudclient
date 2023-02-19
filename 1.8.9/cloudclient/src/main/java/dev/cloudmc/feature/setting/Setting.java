/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.setting;

import dev.cloudmc.feature.mod.Mod;

import java.awt.*;

public class Setting {

    private String name;
    private Mod mod;

    private String mode;

    private String[] options;
    private String currentMode;
    private int modeIndex;

    private Color color;
    private Color sideColor;
    private float sideSlider;
    private float[] mainSlider;

    private boolean checkToggled;

    private float maxNumber, currentNumber;

    private boolean[] cells;
    private int key;

    /**
     * A setting which can be toggled on and off
     */

    public Setting(String name, Mod mod, boolean checkToggled) {
        this.mode = "CheckBox";
        this.name = name;
        this.mod = mod;

        this.checkToggled = checkToggled;
    }

    /**
     * A setting with a slider which can go from 0 to a given number
     */

    public Setting(String name, Mod mod, float maxNumber, float currentNumber) {
        this.mode = "Slider";
        this.name = name;
        this.mod = mod;

        this.maxNumber = maxNumber;
        this.currentNumber = currentNumber;
    }

    /**
     * A setting which allows you to select a String from an Array of Strings also called Modes
     */

    public Setting(String name, Mod mod, String currentMode, int modeIndex, String[] options) {
        this.mode = "ModePicker";
        this.name = name;
        this.mod = mod;

        this.currentMode = currentMode;
        this.modeIndex = modeIndex;
        this.options = options;
    }

    /**
     * A setting which allows you to choose a specific color
     */

    public Setting(String name, Mod mod, Color color, Color sideColor, float sideSlider, float[] mainSlider) {
        this.mode = "ColorPicker";
        this.name = name;
        this.mod = mod;

        this.color = color;
        this.sideColor = sideColor;
        this.sideSlider = sideSlider;
        this.mainSlider = mainSlider;
    }

    /**
     * A setting which allows you to "draw" on an 11 x 11 grid, used for the crosshair mod
     */

    public Setting(String name, Mod mod, boolean[] cells) {
        this.mode = "CellGrid";
        this.name = name;
        this.mod = mod;

        this.cells = cells;
    }

    public Setting(String name, Mod mod, int key){
        this.mode = "Keybinding";
        this.name = name;
        this.mod = mod;

        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mod getMod() {
        return mod;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isCheckToggled() {
        return checkToggled;
    }

    public void setCheckToggled(boolean checkToggled) {
        this.checkToggled = checkToggled;
    }

    public float getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(float maxNumber) {
        this.maxNumber = maxNumber;
    }

    public float getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(float currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void setModeIndex(int modeIndex) {
        this.modeIndex = modeIndex;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getSideColor() {
        return sideColor;
    }

    public void setSideColor(Color sideColor) {
        this.sideColor = sideColor;
    }

    public float getSideSlider() {
        return sideSlider;
    }

    public void setSideSlider(float sideSlider) {
        this.sideSlider = sideSlider;
    }

    public float[] getMainSlider() {
        return mainSlider;
    }

    public void setMainSlider(float[] mainSlider) {
        this.mainSlider = mainSlider;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public boolean[] getCells() {
        return cells;
    }

    public void setCells(boolean[] cells) {
        this.cells = cells;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
