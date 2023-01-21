/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import com.google.gson.Gson;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.helpers.DirHelper;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
                switch (configArray.getConfig().get(i).getSettings().get(j).getMode()) {
                    case "CheckBox":
                        boolean toggled = configArray.getConfig().get(i).getSettings().get(j).isCheckToggled();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setCheckToggled(toggled);
                        break;
                    case "Slider":
                        float amount = configArray.getConfig().get(i).getSettings().get(j).getCurrentNumber();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setCurrentNumber(amount);
                        break;
                    case "ModePicker":
                        String mode = configArray.getConfig().get(i).getSettings().get(j).getCurrentMode();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setCurrentMode(mode);
                        break;
                    case "ColorPicker":
                        Color color = configArray.getConfig().get(i).getSettings().get(j).getColor();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setColor(color);
                        break;
                    case "CellGrid":
                        boolean[] cells = configArray.getConfig().get(i).getSettings().get(j).getCells();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setCells(cells);
                        break;
                    case "Keybinding":
                        int key = configArray.getConfig().get(i).getSettings().get(j).getKey();
                        Cloud.INSTANCE.settingManager.getSettingsByMod(mod).get(j).setKey(key);
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
            switch (configArray.getOptions().get(i).getMode()) {
                case "CheckBox":
                    boolean toggled = configArray.getOptions().get(i).isCheckToggled();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setCheckToggled(toggled);
                    break;
                case "Slider":
                    float amount = configArray.getOptions().get(i).getCurrentNumber();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setCurrentNumber(amount);
                    break;
                case "ModePicker":
                    String mode = configArray.getOptions().get(i).getCurrentMode();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setCurrentMode(mode);
                    break;
                case "ColorPicker":
                    Color color = configArray.getOptions().get(i).getColor();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setColor(color);
                    break;
                case "CellGrid":
                    boolean[] cells = configArray.getOptions().get(i).getCells();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setCells(cells);
                    break;
                case "Keybinding":
                    int key = configArray.getOptions().get(i).getKey();
                    Cloud.INSTANCE.optionManager.getOptions().get(i).setKey(key);
                    break;
            }
        }

        ClientStyle.setDarkMode(configArray.isDarkMode());
        ClientStyle.setSnapping(configArray.isSnapping());
    }
}
