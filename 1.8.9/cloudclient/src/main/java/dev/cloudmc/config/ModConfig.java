/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import dev.cloudmc.feature.setting.Setting;

import java.util.ArrayList;

public class ModConfig {

    private String name;
    private boolean toggled;
    private ArrayList<Setting> settings;
    private int[] positions;
    private float size;

    public ModConfig(String name, boolean toggled, ArrayList<Setting> settings, int[] positions, float size){
        this.name = name;
        this.toggled = toggled;
        this.settings = settings;
        this.positions = positions;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<Setting> settings) {
        this.settings = settings;
    }

    public int[] getPositions() {
        return positions;
    }

    public void setPositions(int[] positions) {
        this.positions = positions;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
