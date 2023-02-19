/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import dev.cloudmc.Cloud;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.render.Helper2D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Keybinding extends Settings {

    private boolean active;

    public Keybinding(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    /**
     * Renders the current keybinding and background,
     * Keybinding Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        boolean roundedCorners = Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled();
        int color = Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB();

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + 6 + getY(),
                color
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 90,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                70, 20, 2,
                Style.getColor(setting.isCheckToggled() ? 80 : 50).getRGB(),
                roundedCorners ? 0 : -1
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                active ? "?" : Keyboard.getKeyName(setting.getKey()),
                button.getPanel().getX() + button.getPanel().getW() - 55 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(active ? "?" : Keyboard.getKeyName(setting.getKey())) / 2f,
                button.getPanel().getY() + button.getPanel().getH() + 8 + getY(),
                color
        );
    }

    /**
     * Changes the active variable if the background of the keybinding is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(button.getPanel().getX() + button.getPanel().getW() - 140,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                120, 21, mouseX, mouseY)
        ) {
            active = !active;
            if(active) {
                setting.setKey(Keyboard.KEY_NONE);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    /**
     * Checks and sets the button which is pressed if it is active
     */

    @SubscribeEvent
    public void onKey(TickEvent.ClientTickEvent e) {
        int key = Keyboard.getEventKey();
        if (active) {
            setting.setKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}
