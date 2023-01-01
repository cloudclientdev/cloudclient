/*
 * Copyright (c) 2022 DupliCAT
 * GNU General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.type;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.setting.Setting;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.Button;
import dev.cloudmc.gui.modmenu.impl.sidebar.mods.impl.Settings;
import dev.cloudmc.helpers.MathHelper;
import dev.cloudmc.helpers.Helper2D;
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

        Cloud.INSTANCE.fontHelper.size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + 6 + getY(),
                ClientStyle.getColor().getRGB()
        );
        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 90,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                70, 20,
                2,
                setting.isCheckToggled() ?
                        ClientStyle.getBackgroundColor(80).getRGB() :
                        ClientStyle.getBackgroundColor(50).getRGB(),
                ClientStyle.isRoundedCorners() ? 0 : -1
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                active ? "?" : Keyboard.getKeyName(button.getMod().getOptionalKey()),
                button.getPanel().getX() + button.getPanel().getW() - 55 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(active ? "?" : Keyboard.getKeyName(button.getMod().getOptionalKey())) / 2,
                button.getPanel().getY() + button.getPanel().getH() + 8 + getY(),
                ClientStyle.getColor().getRGB()
        );
    }

    /**
     * Changes the active variable if the background of the keybinding is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        active = MathHelper.withinBox(
                button.getPanel().getX() + button.getPanel().getW() - 140,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                120, 21, mouseX, mouseY
        );
    }

    /**
     * Checks and sets the button which is pressed if it is active
     */

    @SubscribeEvent
    public void onKey(TickEvent.ClientTickEvent e) {
        int key = Keyboard.getEventKey();
        if (active) {
            button.getMod().setOptionalKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}
