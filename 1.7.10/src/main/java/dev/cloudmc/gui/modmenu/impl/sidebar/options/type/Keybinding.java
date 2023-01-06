/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options.type;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dev.cloudmc.Cloud;
import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.ClientStyle;
import dev.cloudmc.gui.modmenu.impl.Panel;
import dev.cloudmc.gui.modmenu.impl.sidebar.options.Options;
import dev.cloudmc.helpers.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import org.lwjgl.input.Keyboard;

public class Keybinding extends Options {

    private boolean active;

    public Keybinding(Option option, Panel panel, int y) {
        super(option, panel, y);
    }

    /**
     * Renders the current keybinding and background,
     * Keybinding Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {

        Cloud.INSTANCE.fontHelper.size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + 6 + getY(),
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
        );
        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 90,
                panel.getY() + panel.getH() + 2 + getY(),
                70, 20,
                2,
                option.isCheckToggled() ?
                        ClientStyle.getBackgroundColor(80).getRGB() :
                        ClientStyle.getBackgroundColor(50).getRGB(),
                Cloud.INSTANCE.optionManager.getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
        Cloud.INSTANCE.fontHelper.size20.drawString(
                active ? "?" : Keyboard.getKeyName(option.getKey()),
                panel.getX() + panel.getW() - 55 -
                        Cloud.INSTANCE.fontHelper.size20.getStringWidth(active ? "?" : Keyboard.getKeyName(option.getKey())) / 2,
                panel.getY() + panel.getH() + 8 + getY(),
                Cloud.INSTANCE.optionManager.getOptionByName("Color").getColor().getRGB()
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
        active = MathHelper.withinBox(panel.getX() + panel.getW() - 140, panel.getY() + panel.getH() + 2 + getY(), 120, 21, mouseX, mouseY);
    }

    /**
     * Checks and sets the button which is pressed if it is active
     */

    @SubscribeEvent
    public void onKey(TickEvent.ClientTickEvent e) {
        int key = Keyboard.getEventKey();
        if (active) {
            option.setKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}
