/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc;

import dev.cloudmc.config.ConfigLoader;
import dev.cloudmc.config.ConfigSaver;
import dev.cloudmc.feature.mod.ModManager;
import dev.cloudmc.feature.mod.impl.AnimationMod;
import dev.cloudmc.feature.option.OptionManager;
import dev.cloudmc.feature.setting.SettingManager;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.helpers.CpsHelper;
import dev.cloudmc.helpers.font.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

import java.io.IOException;

@Mod(
        modid = Cloud.modID,
        name = Cloud.modName,
        version = Cloud.modVersion,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class Cloud {

    @Mod.Instance()
    public static Cloud INSTANCE;

    public static final String modID = "cloudmc";
    public static final String modName = "Cloud";
    public static final String modVersion = "1.3.3 [1.8.9]";

    public Minecraft mc = Minecraft.getMinecraft();

    public ModManager modManager;
    public SettingManager settingManager;
    public HudEditor hudEditor;
    public OptionManager optionManager;
    public FontHelper fontHelper;
    public CpsHelper cpsHelper;

    /**
     * Initializes the client
     */
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register((cpsHelper = new CpsHelper()));
        MinecraftForge.EVENT_BUS.register((settingManager = new SettingManager()));
        MinecraftForge.EVENT_BUS.register((modManager = new ModManager()));
        MinecraftForge.EVENT_BUS.register((optionManager = new OptionManager()));
        MinecraftForge.EVENT_BUS.register((hudEditor = new HudEditor()));
        MinecraftForge.EVENT_BUS.register((fontHelper = new FontHelper()));
        Display.setTitle(Cloud.modName + " Client " + Cloud.modVersion);

        if (!ConfigSaver.configExists()) {
            ConfigSaver.saveConfig();
        }
        try {
            ConfigLoader.loadConfig();
        }
        catch (Exception ignored) {}

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConfigSaver.saveConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}