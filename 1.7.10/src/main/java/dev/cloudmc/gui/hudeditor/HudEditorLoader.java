/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.hudeditor;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import dev.cloudmc.Cloud;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class HudEditorLoader {

    public HudEditorLoader(){
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Sets the GUI Screen to the HUD Editor if R_SHIFT is pressed
     */

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            Cloud.INSTANCE.mc.displayGuiScreen(Cloud.INSTANCE.hudEditor);
        }
    }
}
