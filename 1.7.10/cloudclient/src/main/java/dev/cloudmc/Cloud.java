/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import dev.cloudmc.config.ConfigLoader;
import dev.cloudmc.config.ConfigSaver;
import dev.cloudmc.feature.mod.ModManager;
import dev.cloudmc.feature.option.OptionManager;
import dev.cloudmc.feature.setting.SettingManager;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.helpers.CpsHelper;
import dev.cloudmc.helpers.MessageHelper;
import dev.cloudmc.helpers.font.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

import java.io.IOException;

@Mod(
        modid = Cloud.modID,
        name = Cloud.modName,
        version = Cloud.modVersion,
        acceptedMinecraftVersions = "[1.7.10]"
)
public class Cloud {

    @Mod.Instance()
    public static Cloud INSTANCE;

    public static final String modID = "cloudmc";
    public static final String modName = "Cloud";
    public static final String modVersion = "1.4.1 [1.7.10]";

    public Minecraft mc = Minecraft.getMinecraft();

    public ModManager modManager;
    public SettingManager settingManager;
    public HudEditor hudEditor;
    public OptionManager optionManager;
    public FontHelper fontHelper;
    public CpsHelper cpsHelper;
    public MessageHelper messageHelper;

    /**
     * Initializes the client
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        Display.setTitle(Cloud.modName + " Client " + Cloud.modVersion);
        registerEvents(
                cpsHelper = new CpsHelper(),
                settingManager = new SettingManager(),
                modManager = new ModManager(),
                optionManager = new OptionManager(),
                hudEditor = new HudEditor(),
                fontHelper = new FontHelper(),
                messageHelper = new MessageHelper()
        );

        try {
            if (!ConfigSaver.configExists()) {
                ConfigSaver.saveConfig();
            }
            ConfigLoader.loadConfig();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        fontHelper.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConfigSaver.saveConfig();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private void registerEvents(Object... events) {
        for (Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
            FMLCommonHandler.instance().bus().register(event);
        }
    }
}