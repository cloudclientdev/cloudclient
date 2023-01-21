/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import dev.cloudmc.feature.option.Option;

import java.util.ArrayList;

public class ConfigArray {

    private ArrayList<Config> configList;
    private ArrayList<Option> options;
    private boolean darkMode;
    private boolean snapping;

    public ConfigArray() {
        configList = new ArrayList<>();
        options = new ArrayList<>();
        darkMode = false;
        snapping = true;
    }

    public void addConfig(Config config) {
        configList.add(config);
    }

    public void addConfigOption(Option option){
        options.add(option);
    }

    public void setDarkMode(boolean toggled){
        darkMode = toggled;
    }

    public boolean isDarkMode(){
        return darkMode;
    }

    public boolean isSnapping() {
        return snapping;
    }

    public void setSnapping(boolean snapping) {
        this.snapping = snapping;
    }

    public ArrayList<Config> getConfig() {
        return configList;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }
}
