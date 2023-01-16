/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import dev.cloudmc.feature.setting.Setting;

import java.util.ArrayList;

public class Config {

    private String name;
    private boolean toggled;
    private ArrayList<Setting> settings;
    private int[] positions;

    public Config(String name, boolean toggled, ArrayList<Setting> settings, int[] positions){
        this.name = name;
        this.toggled = toggled;
        this.settings = settings;
        this.positions = positions;
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
}
