/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc.feature.mod;

import net.minecraftforge.common.MinecraftForge;

public class Mod {

    private String name;
    private String description;
    private boolean toggled;

    public Mod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        callMethod();
    }

    public void toggle() {
        toggled = !toggled;
        callMethod();
    }

    private void callMethod() {
        if (toggled) {
            onEnable();
        }
        else {
            onDisable();
        }
    }
}
