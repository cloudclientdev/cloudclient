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
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.helpers.DirHelper;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;

public class ConfigLoader {

    /**
     * Loads the config from .cloud/cloudconfig.json and sets:
     * Mod states
     * Mod Position states
     * Setting states
     */

    public static void loadConfig() throws FileNotFoundException {
        FileReader reader = new FileReader(DirHelper.getDirectory() + ".cloud" + File.separator + "cloudconfig.json");

        ConfigArray configArray = new Gson().fromJson(reader, ConfigArray.class);

        for (int i = 0; i < configArray.getConfig().size(); i++) {
            Mod mod = Cloud.INSTANCE.modManager.getMods().get(i);
            mod.setToggled(configArray.getConfig().get(i).isToggled());
            for (int j = 0; j < configArray.getConfig().get(i).getSettings().size(); j++) {
                Setting configSetting = configArray.getConfig().get(i).getSettings().get(j);
                Setting clientSetting = Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j);
                switch (configArray.getConfig().get(i).getSettings().get(j).getMode()) {
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
                        boolean[] cells = configSetting.getCells();
                        clientSetting.setCells(cells);
                        break;
                    case "Keybinding":
                        int key = configSetting.getKey();
                        clientSetting.setKey(key);
                        break;
                }
            }

            if (Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null) {
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setX(configArray.getConfig().get(i).getPositions()[0]);
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setY(configArray.getConfig().get(i).getPositions()[1]);
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setSize(configArray.getConfig().get(i).getSize());
            }
        }

        for(int i = 0; i < configArray.getOptions().size(); i++){
            Option configOption = configArray.getOptions().get(i);
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
                    boolean[] cells = configOption.getCells();
                    clientOption.setCells(cells);
                    break;
                case "Keybinding":
                    int key = configOption.getKey();
                    clientOption.setKey(key);
                    break;
            }
        }

        ClientStyle.setDarkMode(configArray.isDarkMode());
        ClientStyle.setSnapping(configArray.isSnapping());
    }
}
