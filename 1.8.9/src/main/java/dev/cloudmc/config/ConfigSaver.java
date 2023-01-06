/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import com.google.gson.Gson;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.helpers.DirHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigSaver {

    /**
     * Saves a configuration of:
     * - Toggled Mods;
     * - Position of Mods;
     * - Settings of Mods;
     * in .cloud/cloudconfig.json
     */

    public static void saveConfig() throws IOException {
        createDir();

        FileWriter writer = new FileWriter(DirHelper.getDirectory() + ".cloud" + File.separator + "cloudconfig.json");

        ConfigArray configArray = new ConfigArray();

        for (Mod mod : Cloud.INSTANCE.modManager.getMods()) {
            Config config = new Config(
                    mod.getName(),
                    mod.isToggled(),
                    Cloud.INSTANCE.settingManager.getSettingsByMod(mod),
                    Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null ?
                            new int[] { Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).getX(), Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).getY() } :
                            new int[] { 0, 0 }
            );
            configArray.addConfig(config);
        }

        for(Option option : Cloud.INSTANCE.optionManager.getOptions()){
            configArray.addConfigOption(option);
        }

        configArray.setDarkMode(ClientStyle.isDarkMode());

        String json = new Gson().toJson(configArray);
        writer.write(json);
        writer.close();
    }

    /**
     * Creates the .cloud directory if it cannot be found
     */

    private static void createDir() throws IOException {
        File file = new File(DirHelper.getDirectory() + ".cloud");
        if (!file.exists()) {
            Files.createDirectory(file.toPath());
        }
        createFile();
    }

    /**
     * Creates the .cloud/cloudconfig.json file if it cannot be found
     */

    private static void createFile() throws IOException {
        File file = new File(DirHelper.getDirectory() + ".cloud" + File.separator + "cloudconfig.json");
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
    }

    /**
     * Checks if the config / .cloud/cloudconfig.json file exists
     *
     * @return Config can be found
     */

    public static boolean configExists() {
        File file = new File(DirHelper.getDirectory() + ".cloud" + File.separator + "cloudconfig.json");
        return file.exists();
    }
}