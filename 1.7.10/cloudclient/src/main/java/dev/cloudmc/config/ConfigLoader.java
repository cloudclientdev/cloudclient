/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import com.google.gson.Gson;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.Style;
import dev.cloudmc.helpers.OSHelper;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ConfigLoader {

    /**
     * Loads the config from .minecraft/cloud/config.json
     */

    public static void loadConfig() throws FileNotFoundException {
        FileReader reader = new FileReader(OSHelper.getCloudDirectory() + "config.json");

        Config config = new Gson().fromJson(reader, Config.class);

        for (int i = 0; i < config.getConfig().size(); i++) {
            Mod mod = Cloud.INSTANCE.modManager.getMods().get(i);
            mod.setToggled(config.getConfig().get(i).isToggled());
            for (int j = 0; j < config.getConfig().get(i).getSettings().size(); j++) {
                Setting configSetting = config.getConfig().get(i).getSettings().get(j);
                Setting clientSetting = Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j);
                switch (config.getConfig().get(i).getSettings().get(j).getMode()) {
                    case "CheckBox":
                        boolean toggled = configSetting.isCheckToggled();
                        clientSetting.setCheckToggled(toggled);
                        break;
                    case "Slider":
                        float amount = configSetting.getCurrentNumber();
                        clientSetting.setCurrentNumber(amount);
                        break;
                    case "ModePicker":
                        String mode = configSetting.getCurrentMode();
                        int index = configSetting.getModeIndex();
                        clientSetting.setCurrentMode(mode);
                        clientSetting.setModeIndex(index);
                        break;
                    case "ColorPicker":
                        Color color = configSetting.getColor();
                        Color sideColor = configSetting.getSideColor();
                        float sideSlider = configSetting.getSideSlider();
                        float[] mainSlider = configSetting.getMainSlider();
                        clientSetting.setColor(color);
                        clientSetting.setSideColor(sideColor);
                        clientSetting.setSideSlider(sideSlider);
                        clientSetting.setMainSlider(mainSlider);
                        break;
                    case "CellGrid":
                        boolean[][] cells = configSetting.getCells();
                        clientSetting.setCells(cells);
                        break;
                    case "Keybinding":
                        int key = configSetting.getKey();
                        clientSetting.setKey(key);
                        break;
                }
            }

            if (Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null) {
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setX(config.getConfig().get(i).getPositions()[0]);
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setY(config.getConfig().get(i).getPositions()[1]);
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setSize(config.getConfig().get(i).getSize());
            }
        }

        for(int i = 0; i < config.getOptionsConfigList().size(); i++){
            Option configOption = config.getOptionsConfigList().get(i);
            Option clientOption = Cloud.INSTANCE.optionManager.getOptions().get(i);
            switch (configOption.getMode()) {
                case "CheckBox":
                    boolean toggled = configOption.isCheckToggled();
                    clientOption.setCheckToggled(toggled);
                    break;
                case "Slider":
                    float amount = configOption.getCurrentNumber();
                    clientOption.setCurrentNumber(amount);
                    break;
                case "ModePicker":
                    String mode = configOption.getCurrentMode();
                    int index = configOption.getModeIndex();
                    clientOption.setCurrentMode(mode);
                    clientOption.setModeIndex(index);
                    break;
                case "ColorPicker":
                    Color color = configOption.getColor();
                    Color sideColor = configOption.getSideColor();
                    float sideSlider = configOption.getSideSlider();
                    float[] mainSlider = configOption.getMainSlider();
                    clientOption.setColor(color);
                    clientOption.setSideColor(sideColor);
                    clientOption.setSideSlider(sideSlider);
                    clientOption.setMainSlider(mainSlider);
                    break;
                case "CellGrid":
                    boolean[][] cells = configOption.getCells();
                    clientOption.setCells(cells);
                    break;
                case "Keybinding":
                    int key = configOption.getKey();
                    clientOption.setKey(key);
                    break;
            }
        }

        Style.setDarkMode(config.isDarkMode());
        Style.setSnapping(config.isSnapping());
    }
}
