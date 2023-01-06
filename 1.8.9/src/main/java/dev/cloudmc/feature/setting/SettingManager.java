/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.setting;

import dev.cloudmc.feature.mod.Mod;

import java.util.ArrayList;

public class
SettingManager {

    public ArrayList<Setting> settingList;

    public SettingManager() {
        settingList = new ArrayList<>();
    }

    /**
     * Adds a setting to the settings List
     * @param setting The setting to be added
     */

    public void addSetting(Setting setting) {
        settingList.add(setting);
    }

    /**
     * @return Returns an Arraylist of all settings
     */

    public ArrayList<Setting> getSettingList() {
        return settingList;
    }

    /**
     * Returns a list of all settings from a given mod
     * @param mod The mod
     * @return The Arraylist of settings
     */

    public ArrayList<Setting> getSettingsByMod(Mod mod) {
        ArrayList<Setting> settingList = new ArrayList<>();
        for (Setting setting : this.settingList) {
            if (setting.getMod().equals(mod)) {
                settingList.add(setting);
            }
        }
        return settingList;
    }

    /**
     * Returns a setting with a given mod name and setting name
     *
     * @param modName The mod name
     * @param setName The setting name
     * @return The setting
     */

    public Setting getSettingByModAndName(String modName, String setName) {
        for (Setting setting : settingList) {
            if (setting.getMod().getName().equalsIgnoreCase(modName) &&
                    setting.getName().equalsIgnoreCase(setName)) {
                return setting;
            }
        }
        return null;
    }
}
