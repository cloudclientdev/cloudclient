/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.gui.modmenu.impl.sidebar.options;

import dev.cloudmc.feature.option.Option;
import dev.cloudmc.gui.modmenu.impl.Panel;
import net.minecraftforge.common.MinecraftForge;

public abstract class Options {

    public Option option;
    public Panel panel;
    private int h, y;
    private boolean open;
    private int settingHeight;
    private boolean updated;

    public Options(Option options, Panel panel, int y) {
        MinecraftForge.EVENT_BUS.register(this);
        this.option = options;
        this.panel = panel;
        this.open = false;
        this.h = 25;
        this.y = y;
    }

    public abstract void renderOption(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public int getOptionHeight() {
        return settingHeight;
    }

    public void setOptionHeight(int settingHeight) {
        this.settingHeight = settingHeight;
    }
}
