/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.config;

import com.google.gson.Gson;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.mod.Mod;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.Style;
import dev.cloudmc.helpers.OSHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigSaver {

    /**
     * Creates and saves a configuration in .minecraft/cloud/config.json
     */

    public static void saveConfig() throws IOException {
        createDir();

        FileWriter writer = new FileWriter(OSHelper.getCloudDirectory() + "config.json");

        Config config = new Config();

        for (Mod mod : Cloud.INSTANCE.modManager.getMods()) {
            ModConfig modConfig = new ModConfig(
                    mod.getName(),
                    mod.isToggled(),
                    Cloud.INSTANCE.settingManager.getSettingsByMod(mod),
                    Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null ?
                            new int[] { Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).getX(), Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).getY() } :
                            new int[] { 0, 0 },
                    Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()) != null ?
                            Cloud.INSTANCE.hudEditor.getHudMod(mod.getName()).getSize() : 1
            );
            config.addConfig(modConfig);
        }

        for(Option option : Cloud.INSTANCE.optionManager.getOptions()){
            config.addConfigOption(option);
        }

        config.setDarkMode(Style.isDarkMode());
        config.setSnapping(Style.isSnapping());

        String json = new Gson().toJson(config);
        writer.write(json);
        writer.close();
    }

    /**
     * Creates the .minecraft/cloud directory if it cannot be found
     */

    private static void createDir() {
        File file = new File(OSHelper.getCloudDirectory());
        if (!file.exists()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        createFile();
    }

    /**
     * Creates the .minecraft/cloud/config.json file if it cannot be found
     */

    private static void createFile() {
        File file = new File(OSHelper.getCloudDirectory() + "config.json");
        if (!file.exists()) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if the config .minecraft/cloud/config.json file exists
     *
     * @return Config can be found
     */

    public static boolean configExists() {
        File file = new File(OSHelper.getCloudDirectory() + "config.json");
        return file.exists();
    }
}