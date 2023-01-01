package dev.cloudmc.config;

import com.google.gson.Gson;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
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
                    case "Checkbox":
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
                }
            }

            if (Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null) {
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setX(configArray.getConfig().get(i).getPositions()[0]);
                Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).setY(configArray.getConfig().get(i).getPositions()[1]);
            }
        }
    }
}
